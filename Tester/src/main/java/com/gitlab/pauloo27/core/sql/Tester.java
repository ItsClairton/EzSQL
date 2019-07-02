package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.gitlab.pauloo27.core.sql.Friend.FriendType;

public class Tester {

    public static String MARIADB_HOST;
    public static int MARIADB_PORT;
    public static String MYSQL_HOST;
    public static int MYSQL_PORT;
    public static String PSQL_HOST;
    public static int PSQL_PORT;
    private static Friend john;
    private static Friend mary;

    static {
        MARIADB_HOST = System.getenv("MARIADB_HOST") != null ? System.getenv("MARIADB_HOST") : "localhost";
        MARIADB_PORT = parsePort(System.getenv("MARIADB_PORT"));
        MYSQL_HOST = System.getenv("MYSQL_HOST") != null ? System.getenv("MYSQL_HOST") : "localhost";
        MYSQL_PORT = parsePort(System.getenv("MYSQL_PORT"));
        PSQL_HOST = System.getenv("PSQL_HOST") != null ? System.getenv("PSQL_HOST") : "localhost";
        PSQL_PORT = parsePort(System.getenv("PSQL_PORT"));
    }

    private static int parsePort(String port) {
        if (port == null)
            return -1;
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void testWith(EzSQL sql) throws SQLException, ClassNotFoundException {
        // Connecting...
        sql.withDefaultDatabase("ezsql")
                .withLogin("ezsql", "1234")
                .registerDriver()
                .connect();

        testTableWithBuilders(sql);

        sql.getTable("friends").truncate();

        testTableWithObject(sql);

    }

    private static void countSumAndAvg(Table friends) {
        System.out.println("Testing with COUNT");
        Assert.assertEquals(2, friends.count().execute().getFirstColumnAsInt());

        System.out.println("Testing with SUM");
        Assert.assertEquals(42, friends.sum("age").execute().getFirstColumnAsInt());

        System.out.println("Testing with AVG");
        Assert.assertEquals(21, friends.avg("age").execute().getFirstColumnAsDouble(), 0);
    }

    private static void testTableWithObject(EzSQL sql) throws SQLException {
        System.out.println("Testing with Objects");
        Table friends = sql.getTable("friends");

        if (friends.exists())
            friends.drop();

        Assert.assertFalse(friends.exists());

        friends = sql.createIfNotExists(Friend.class);

        Assert.assertTrue(friends.exists());

        long start = new Date().getTime();

        insertDataWithObject(friends);

        checkDataWithObject(friends);

        countSumAndAvg(friends);

        updateWithObject(friends);

        deleteWithObject(friends);

        printStatus(start);
    }

    private static void insertDataWithObject(Table friends) {
        john = new Friend("John Doe", 21, "321", "johndoe@example.com", FriendType.IRL);
        mary = new Friend("Mary Doe", 21, "221", "marydoe@example.com", FriendType.WEB_FRIEND);

        friends.insertAll(john, mary).executeAndClose();

        System.out.println("Testing default handler");
        // Handle Exception (should print "Duplicate Key" error)
        Friend test = friends.select().where()
                .equals("id", 1).execute()
                .to(Friend.class);

        System.out.println(test);

        friends.insert(test)
                .executeAndClose(e -> System.out.println(e.getMessage()));
    }

    private static void checkDataWithObject(Table friends) {
        john = friends.select().where().equals("id", 1).execute().to(Friend.class);
        mary = friends.select().where().equals("id", 2).execute().to(Friend.class);

        Assert.assertEquals(1, john.id);
        Assert.assertEquals("johndoe@example.com", john.email);

        Assert.assertEquals(2, mary.id);
        Assert.assertEquals("marydoe@example.com", mary.email);

        john = friends.select().where().like("email", "john%").execute().to(Friend.class);

        Assert.assertEquals(1, john.id);
        Assert.assertEquals("johndoe@example.com", john.email);

        friends.select().execute().toList(Friend.class)
                .forEach(friend ->
                        Assert.assertEquals(
                                String.format("%s@example.com", friend.username.toLowerCase().replace(" ", "")),
                                friend.email
                        ));
    }

    private static void updateWithObject(Table friends) {
        mary.phone = "222";
        friends.update(mary).executeAndClose();

        mary = friends.select().where().equals("id", 2).execute().to(Friend.class);

        Assert.assertEquals("222", mary.phone);
    }

    private static void deleteWithObject(Table friends) {
        friends.delete(mary).executeAndClose();

        Assert.assertEquals(1, friends.select().execute().toList(Friend.class).size());
    }

    private static void testTableWithBuilders(EzSQL sql) throws SQLException {
        System.out.println("Testing with Builders");
        Table friends = sql.getTable("friends");

        if (friends.exists())
            friends.drop();

        Assert.assertFalse(friends.exists());

        friends = sql.createIfNotExists(new TableBuilder("friends")
                .withColumn(new ColumnBuilder("id", DefaultDataTypes.PRIMARY_KEY))
                .withColumn(new ColumnBuilder("name", DefaultDataTypes.VARCHAR, 32, DefaultAttributes.NOT_NULL, DefaultAttributes.UNIQUE))
                .withColumn(new ColumnBuilder("phone", DefaultDataTypes.VARCHAR, 32, DefaultAttributes.UNIQUE))
                .withColumn(new ColumnBuilder("age", DefaultDataTypes.INTEGER, DefaultAttributes.NOT_NULL))
                .withColumn(new ColumnBuilder("email", DefaultDataTypes.VARCHAR, 64, DefaultAttributes.NOT_NULL, DefaultAttributes.UNIQUE)
                        .withDefaultValue("No e-mail")
                ).withColumn(new ColumnBuilder("friend_type", DefaultDataTypes.VARCHAR, 10, DefaultAttributes.NOT_NULL)
                        .withDefaultValue(Friend.FriendType.IRL.name())
                )
        );

        Assert.assertTrue(friends.exists());

        long start = new Date().getTime();

        insertDataWithBuilder(friends);

        checkDataWithBuilder(friends);

        countSumAndAvg(friends);

        updateDataAndCheckWithBuilder(friends);

        deleteDataAndCheckWithBuilder(friends);

        printStatus(start);

    }

    private static void insertDataWithBuilder(Table friends) {
        friends.insert(
                "name, age, phone, email, friend_type",
                "John Doe", 21, "321", "johndoe@example.com", Friend.FriendType.IRL.name(),
                "Mary Doe", 21, "221", "marydoe@example.com", Friend.FriendType.WEB_FRIEND.name()
        ).executeAndClose();
    }

    private static void checkDataWithBuilder(Table friends) throws SQLException {
        // select using where
        try (ResultSet result = friends.select().where().equals("name", "John Doe")
                .execute().getResultSet()) {
            if (result.next()) {
                Assert.assertEquals(1, result.getInt("id"));
                Assert.assertEquals("johndoe@example.com", result.getString("email"));
            } else {
                throw new NullPointerException("Result set is empty");
            }
        }

        // select using where like
        try (ResultSet result = friends.select().where().like("email", "john%")
                .execute().getResultSet()) {
            if (result.next()) {
                Assert.assertEquals(1, result.getInt("id"));
                Assert.assertEquals("johndoe@example.com", result.getString("email"));
            } else {
                throw new NullPointerException("Result set is empty");
            }
        }

        // select everything
        try (ResultSet result = friends.select()
                .execute().getResultSet()) {
            Assert.assertEquals(2, checkDataWithBuilder(result));
        }
    }

    private static void updateDataAndCheckWithBuilder(Table friends) throws SQLException {
        friends.update().set("phone", "222").where().equals("id", 2).executeAndClose();

        // check the updated value
        try (ResultSet result = friends.select().where().equals("id", 2)
                .execute().getResultSet()) {
            if (result.next()) {
                Assert.assertEquals("222", result.getString("phone"));
            } else {
                throw new NullPointerException("Result set is empty");
            }
        }
    }

    private static void deleteDataAndCheckWithBuilder(Table friends) throws SQLException {
        friends.delete().where().equals("id", 2).executeAndClose();

        // select everything
        try (ResultSet result = friends.select()
                .execute().getResultSet()) {
            Assert.assertEquals(1, checkDataWithBuilder(result));
        }
    }

    private static int checkDataWithBuilder(ResultSet result) throws SQLException {
        int id = 0;
        while (result.next()) {
            Assert.assertEquals(++id, result.getInt("id"));
            String name = result.getString("name");
            Assert.assertEquals(String.format("%s@example.com", name.toLowerCase().replace(" ", "")), result.getString("email"));
        }
        return id;
    }

    public static void printStatus(long start) {
        long end = new Date().getTime();

        System.out.printf("Start > %d%n", start);
        System.out.printf("End > %d%n", end);
        System.out.printf("Time > %d%n", end - start);
    }

}
