package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tester {

    public static String MARIADB_HOST;
    public static int MARIADB_PORT;
    public static String MYSQL_HOST;
    public static int MYSQL_PORT;
    public static String PSQL_HOST;
    public static int PSQL_PORT;

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
        // connects
        sql.registerDriver();
        sql.withDefaultDatabase("ezsql", true).connect();
        // drops the table if exists
        if (sql.getTable("friends").exists()) sql.getTable("friends").drop();
        // creates table
        Table table = sql.createIfNotExists(
                new TableBuilder("friends")
                        .withColumn(new ColumnBuilder("id", DefaultDataTypes.PRIMARY_KEY))
                        .withColumn(new ColumnBuilder("name", DefaultDataTypes.VARCHAR)
                                .withLength(30)
                                .withAttributes(DefaultAttributes.NOT_NULL, DefaultAttributes.UNIQUE))
                        .withColumn(new ColumnBuilder("age", DefaultDataTypes.INTEGER)
                                .withAttributes(DefaultAttributes.NOT_NULL))
                        .withColumn(new ColumnBuilder("phone", DefaultDataTypes.INTEGER)
                                .withAttributes(DefaultAttributes.UNIQUE, DefaultAttributes.NOT_NULL))
                        .withColumn(new ColumnBuilder("email", DefaultDataTypes.VARCHAR)
                                .withLength(30)
                                .withDefaultValue("No e-mail")));
        // inserts data
        table.insert(new Insert("name, age, phone, email",
                "Paulo' or 1=1", 12, 666, "sql_injection@test.com",
                "John Doe", 21, 123, "john_doe@sample.com",
                "Mark", 92, 911, "mark@sample.com")).close();
        // inserts more data
        table.insert(new Insert("name, age, phone", "Doe John", 18, 321)).close();
        // reads data
        reads(table, "0c1d8224e6ba56271a3694bd2882af7a657203064440ac5eb4c22f5ba306b0ac");
        System.out.println("=======");
        // updates
        table.update(new Update().set("name", "John").where().equals("name", "Doe John")).close();
        // reads again
        reads(table, "32ebd11565862dce4b4ce285003e356d0afb34bcb7297af79cd8067205b819a8");
        System.out.println("=======");
        // reads where the age is at least 18
        try (QueryResult result = table.select(new Select("name")
                .orderBy("name", StatementBase.OrderByType.ASC)
                .where()
                .atLeast("age", 18)
                .limit(10))) {
            ResultSet rs = result.getResultSet();
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                names.add(name);
                System.out.printf("%s's age is at least 18%n", name);
            }
            // I think it's too short to use a hash
            Assert.assertEquals("John, John Doe, Mark", String.join(", ", names));
        }
        System.out.println("=======");
        // reads where the age is at least 18 and less than 60
        try (QueryResult result = table.select(new Select("name")
                .orderBy("name", StatementBase.OrderByType.ASC)
                .where()
                .atLeast("age", 18)
                .and()
                .lessThan("age", 60)
                .limit(3))) {
            // I think it's too short to use a hash
            Assert.assertEquals("John, John Doe", String.join(", ", getNames(result.getResultSet())));
        }
        System.out.println("=======");
        // reads where age is less than 18
        try (QueryResult result = table.select(new Select("name")
                .orderBy("name", StatementBase.OrderByType.ASC)
                .where()
                .lessThan("age", 18)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            // I think it's too short to use a hash
            Assert.assertEquals("Paulo' or 1=1", String.join(", ", getNames(result.getResultSet())));
        }
        System.out.println("=======");
        // deletes
        table.delete(new Delete().where().equals("name", "John Doe"));
        // reads again
        reads(table, "95a5947b39c17fb00ff326642402549f4e1fb2e25db69ba9a8fb7275039a350a");
        // Now, test join:
        //testJoin(sql);
        sql.disconnect();
//        System.out.printf("Test %s ended%n", sql.getType().name());
    }

    private static void reads(Table table, String expectedHash) throws SQLException {
        try (QueryResult result = table.select(new Select("name, phone, email, age")
                .orderBy("name", StatementBase.OrderByType.ASC)
                .limit(10))) {
            ResultSet rs = result.getResultSet();
            // String to hash
            StringBuilder sb = new StringBuilder();
            checkAndPrintResult(rs, sb);
            Assert.assertEquals(expectedHash, hash(sb.toString()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getNames(ResultSet rs) throws SQLException {
        List<String> names = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            names.add(name);
            System.out.printf("%s's age is at least 18 and less than 60%n", name);
        }
        return names;
    }

    private static void checkAndPrintResult(ResultSet rs, StringBuilder sb) throws SQLException {
        while (rs.next()) {
            String name = rs.getString("name");
            int phone = rs.getInt("phone");
            String email = rs.getString("email");
            int age = rs.getInt("age");

            // Append to String to hash
            sb.append(name);
            sb.append(phone);
            sb.append(email);
            sb.append(age);

            System.out.printf("%s -> %d | %s | %d years old%n", name, phone, email, age);
        }
    }

    public static void testJoin(EzSQL sql) throws SQLException {
        Table clients = sql.createIfNotExists(
                new TableBuilder("clients")
                        .withColumn(new ColumnBuilder("id", DefaultDataTypes.PRIMARY_KEY))
                        .withColumn(new ColumnBuilder("name", DefaultDataTypes.VARCHAR, 64))
                        .withColumn(new ColumnBuilder("phone", DefaultDataTypes.VARCHAR, 64))
        );
        Table employees = sql.createIfNotExists(
                new TableBuilder("employees")
                        .withColumn(new ColumnBuilder("id", DefaultDataTypes.PRIMARY_KEY))
                        .withColumn(new ColumnBuilder("name", DefaultDataTypes.VARCHAR, 64))
                        .withColumn(new ColumnBuilder("phone", DefaultDataTypes.VARCHAR, 64))
        );
        Table requests = sql.createIfNotExists(
                new TableBuilder("requests")
                        .withColumn(new ColumnBuilder("id", DefaultDataTypes.PRIMARY_KEY))
                        .withColumn(new ColumnBuilder("employee", DefaultDataTypes.INTEGER))
                        .withColumn(new ColumnBuilder("client", DefaultDataTypes.INTEGER))
                        .withColumn(new ColumnBuilder("createIn", DefaultDataTypes.TIMESTAMP))
        );

        // TODO insert
        try (QueryResult result = requests.select(
                new Select("requests.id, clients.name client, employees.name employee")
                        .join(
                                new StatementBase.Join("clients", "requests.client",
                                        "clients.id", StatementBase.Join.JoinType.INNER))
                        .join(new StatementBase.Join("employees", "requests.employee",
                                "employees.id", StatementBase.Join.JoinType.INNER))
                        .where().notNull("requests.client"))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%d | %s | %s%n", rs.getInt("id"), rs.getString("employee"), rs.getString("client"));
            }
        }
        clients.drop();
        employees.drop();
        requests.drop();
    }

    // TODO Use a external dependency
    public static String hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        byte[] bytes = md.digest();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
