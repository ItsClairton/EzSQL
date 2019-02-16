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
    }
}
