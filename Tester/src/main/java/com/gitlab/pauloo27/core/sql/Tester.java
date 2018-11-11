package com.gitlab.pauloo27.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tester {

    public static String MARIADB_HOST;
    public static String MYSQL_HOST;
    public static String PSQL_HOST;

    static {
        MARIADB_HOST = System.getenv("MARIADB_HOST") != null ? System.getenv("MARIADB_HOST") : "localhost";
        MYSQL_HOST = System.getenv("MYSQL_HOST") != null ? System.getenv("MYSQL_HOST") : "localhost";
        PSQL_HOST = System.getenv("PSQL_HOST") != null ? System.getenv("PSQL_HOST") : "localhost";
    }

    public static void testWith(EzSQL sql) throws SQLException, ClassNotFoundException {
        System.out.printf("Testing %s...%n", sql.getType().name());
        // connects
        sql.registerDriver();
        sql.withDefaultDatabase("ezsql", true).connect();
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
        // inserts returning (postgresql only)
        if (sql.getType() == EzSQLType.POSTGRESQL) {
            try (EzQueryResult result = table.insertReturning(new EzInsert("name, age, phone", "PSQL", 22, 999), "id")) {
                ResultSet rs = result.getResultSet();
                if (rs.next()) {
                    System.out.printf("PSQL's id: %d%n", rs.getInt("id"));
                }
            }
        }
        // reads data
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email, age")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(10))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s | %d years old%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"), rs.getInt("age"));
            }
        }
        System.out.println("=======");
        // updates
        table.update(new EzUpdate().set("name", "John").where().equals("name", "Doe John")).close();
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email, age")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(10))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s | %d years old%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"), rs.getInt("age"));
            }
        }
        System.out.println("=======");
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .where()
                .atLeast("age", 18)
                .limit(10))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s's age is at least 18%n", rs.getString("name"));
            }
        }
        System.out.println("=======");
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .where()
                .atLeast("age", 18)
                .and()
                .lessThan("age", 60)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s's age is at least 18 and less than 60%n", rs.getString("name"));
            }
        }
        System.out.println("=======");
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .where()
                .lessThan("age", 18)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s's age is less than 18%n", rs.getString("name"));
            }
        }
        System.out.println("=======");
        // deletes
        table.delete(new EzDelete().where().equals("name", "John Doe"));
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email, age")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s | %d years old%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"), rs.getInt("age"));
            }
        }
        sql.disconnect();
        System.out.printf("Test %s ended%n", sql.getType().name());
    }

}
