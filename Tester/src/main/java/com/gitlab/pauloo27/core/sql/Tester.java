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
        System.out.printf("Testing %s...%n", sql.getType().name());
        // connects
        sql.registerDriver();
        sql.withDefaultDatabase("ezsql", true).connect();
        // drops the table if exists
        if (sql.getTable("friends").exists()) sql.getTable("friends").drop();
        // creates table
        EzTable table = sql.createIfNotExists(
                new EzTableBuilder("friends")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR)
                                .withLength(30)
                                .withAttributes(EzAttribute.NOT_NULL, EzAttribute.UNIQUE))
                        .withColumn(new EzColumnBuilder("age", EzDataType.INTEGER)
                                .withAttributes(EzAttribute.NOT_NULL))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.INTEGER)
                                .withAttributes(EzAttribute.UNIQUE, EzAttribute.NOT_NULL))
                        .withColumn(new EzColumnBuilder("email", EzDataType.VARCHAR)
                                .withLength(30)
                                .withDefaultValue("No e-mail")));
        // inserts data
        table.insert(new EzInsert("name, age, phone, email",
                new EzInsert.EzValue("Paulo' or 1=1", 12, 666, "sql_injection@test.com"),
                new EzInsert.EzValue("John Doe", 21, 123, "john_doe@sample.com"),
                new EzInsert.EzValue("Mark", 92, 911, "mark@sample.com"))).close();
        // inserts more data
        table.insert(new EzInsert("name, age, phone", "Doe John", 18, 321)).close();
        // inserts returning (PostgreSQL only)
        if (sql.getType() == EzSQLType.POSTGRESQL) {
            int id;
            try (EzQueryResult result = table.insertReturning(new EzInsert("name, age, phone", "PSQL", 22, 999), "id")) {
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
            table.delete(new EzDelete().where().equals("id", id));
        }
        // reads data
        reads(table, "0c1d8224e6ba56271a3694bd2882af7a657203064440ac5eb4c22f5ba306b0ac");
        System.out.println("=======");
        // updates
        table.update(new EzUpdate().set("name", "John").where().equals("name", "Doe John")).close();
        // reads again
        reads(table, "32ebd11565862dce4b4ce285003e356d0afb34bcb7297af79cd8067205b819a8");
        System.out.println("=======");
        // reads where the age is at least 18
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
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
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
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
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .where()
                .lessThan("age", 18)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            // I think it's too short to use a hash
            Assert.assertEquals("Paulo' or 1=1", String.join(", ", getNames(result.getResultSet())));
        }
        System.out.println("=======");
        // deletes
        table.delete(new EzDelete().where().equals("name", "John Doe"));
        // reads again
        reads(table, "95a5947b39c17fb00ff326642402549f4e1fb2e25db69ba9a8fb7275039a350a");
        // Now, test join:
        //testJoin(sql);
        sql.disconnect();
        System.out.printf("Test %s ended%n", sql.getType().name());
    }

    private static void reads(EzTable table, String expectedHash) throws SQLException {
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email, age")
                .orderBy("name", EzStatement.OrderByType.ASC)
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
        EzTable clients = sql.createIfNotExists(
                new EzTableBuilder("clients")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR, 64))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.VARCHAR, 64))
        );
        EzTable employees = sql.createIfNotExists(
                new EzTableBuilder("employees")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR, 64))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.VARCHAR, 64))
        );
        EzTable requests = sql.createIfNotExists(
                new EzTableBuilder("requests")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("employee", EzDataType.INTEGER))
                        .withColumn(new EzColumnBuilder("client", EzDataType.INTEGER))
                        .withColumn(new EzColumnBuilder("createIn", EzDataType.TIMESTAMP))
        );

        // TODO insert
        try (EzQueryResult result = requests.select(
                new EzSelect("requests.id, clients.name client, employees.name employee")
                        .join(
                                new EzStatement.Join("clients", "requests.client",
                                        "clients.id", EzStatement.Join.JoinType.INNER))
                        .join(new EzStatement.Join("employees", "requests.employee",
                                "employees.id", EzStatement.Join.JoinType.INNER))
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
