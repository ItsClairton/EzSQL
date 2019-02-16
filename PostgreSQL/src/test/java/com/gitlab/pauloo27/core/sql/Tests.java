package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tests {

    public void testWithPostgreSQL() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzPostgreSQL().withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT).withLogin("ezsql", "1234"));
    }

    @Test
    public void testInsertReturning() throws SQLException, ClassNotFoundException {
        testWithPostgreSQL();

        EzPostgreSQL sql = (EzPostgreSQL) new EzPostgreSQL()
                .withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT)
                .withLogin("ezsql", "1234")
                .withDefaultDatabase("ezsql")
                .connect();

        PostgreSQLTable table = sql.getTable("friends");

        int id;
        try (QueryResult result = table
                .insertReturning("name, age, phone",
                        "id",
                        "PSQL", 22, 999).executeReturning()) {

            ResultSet rs = result.getResultSet();
            if (rs.next()) {
                id = rs.getInt("id");
                System.out.printf("PSQL's id: %d%n", id);
                System.out.println("=======");
                Assert.assertEquals(5, id);
            } else {
                throw new NullPointerException("Returning id cannot be null");
            }
        }
        table.delete().where().equals("id", id).executeAndClose();
        System.out.println("\n\n\n");

        Table amigos = sql.createIfNotExists(Friend.class);

        amigos.truncate();

        Friend robison = new Friend("Robison", 20, 123, "Robison@Robison.Robison");
        Friend john = new Friend("John", 12, 321, "john@Robison.Robison");

        amigos.insert(robison).executeAndClose();
        amigos.insert(john).executeAndClose();

        Friend fromTable = null;
        try (QueryResult result = amigos.select()
                .where().equals("name", "John").execute()) {
            System.out.println(fromTable = result.to(Friend.class));
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Printing every row:");
            amigos.select().execute().toList(Friend.class)
                    .forEach(System.out::println);
            System.out.println();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        fromTable.phone = 222;

        amigos.update(fromTable).executeAndClose();

        amigos.delete(fromTable).executeAndClose();
    }
}
