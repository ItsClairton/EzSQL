package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tests {

    @Test
    public void testWithPostgreSQL() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzPostgreSQL().withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT).withLogin("ezsql", "1234"));
    }

    @Test
    public void testInsertReturning() throws SQLException {
        EzPostgreSQL sql = (EzPostgreSQL) new EzPostgreSQL()
                .withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT)
                .withLogin("ezsql", "1234")
                .withDefaultDatabase("ezsql")
                .connect();

        EzPostgreSQLTable table = sql.getTable("friends");

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
}
