package com.gitlab.pauloo27.core.sql.samples;

import com.gitlab.pauloo27.core.sql.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleSQL {

    public static void main(String[] args) throws SQLException {
        test(new EzSQL(EzSQLType.SQLITE).asSQLite("Database.sqlite"));
    }

    private static void test(EzSQL sql) throws SQLException {
        // connect: Connects to `testing` and creates if it doesn't exist
        sql.withDefaultDatabase("testing", true).connect();
        // If the table `friends` exists, drops it
        if (sql.getTable("friends").exists()) sql.getTable("friends").drop();
        /* create table `friends` with 4 columns:
            id: INTEGER PRIMARY KEY
            name: VARCHAR(30) NOT NULL UNIQUE
            phone: INTEGER NOT NULL UNIQUE
            email: VARCHAR(30) DEFAULT 'No e-mail'
         */
        EzTable table = sql.createIfNotExists(
                new EzTableBuilder("friends")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR)
                                .withLength(30)
                                .withAttributes(EzAttribute.NOT_NULL, EzAttribute.UNIQUE))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.INTEGER)
                                .withAttributes(EzAttribute.NOT_NULL, EzAttribute.UNIQUE))
                        .withColumn(new EzColumnBuilder("email", EzDataType.VARCHAR)
                                .withLength(30)
                                .withDefaultValue("No e-mail")));
        /* inserts data in the columns `name`, `phone` and `email`, respectively:
            ''or 1=1' 666 sql_injection@test.com
            'People0' 30213 people@sample.com
           SQL Injection will not work
         */
        table.insert(new EzInsert("name", "phone", "email",
                new EzInsert.EzValue("' or 1=1", 666, "sql_injection@test.com"),
                new EzInsert.EzValue("People0", 30213, "people@sample.com"))).close();
        /* inserts more data (using less verbose code), but without the email:
            'People1' 8321
         */
        table.insert(new EzInsert("name", "phone", "People1", 8321)).close();
        // reads data of the columns name, phone and email, respectively
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"));
            }
        }
        System.out.println("=======");
        // updates data of the column name to 'People01' where name is 'People1'
        table.update(new EzUpdate().set("name", "People01").where().equals("name", "People1")).close();
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"));
            }
        }
        System.out.println("=======");
        // deletes the data where name is 'People0'
        table.delete(new EzDelete().where().equals("name", "People0"));
        // reads again
        try (EzQueryResult result = table.select(new EzSelect("name, phone, email")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%s -> %d | %s%n", rs.getString("name"), rs.getInt("phone"), rs.getString("email"));
            }
        }
        // truncates the table
        table.truncate().close();
        // disconnects
        sql.disconnect();
    }
}
