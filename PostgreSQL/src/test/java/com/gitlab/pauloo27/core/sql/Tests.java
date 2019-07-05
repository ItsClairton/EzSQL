package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tests {

    public void testWithPostgreSQL() throws SQLException, ClassNotFoundException, IllegalAccessException {
        Tester.testWith(new EzPostgreSQL().withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT).withLogin("ezsql", "1234"));
    }

    @Test
    public void testInsertReturning() throws SQLException, ClassNotFoundException, IllegalAccessException {
        testWithPostgreSQL();

        EzPostgreSQL sql = (EzPostgreSQL) new EzPostgreSQL()
                .withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT)
                .withLogin("ezsql", "1234")
                .withDefaultDatabase("ezsql")
                .connect();

        PostgreSQLTable friends = sql.getTable("friends");

        int id;
        try (QueryResult result = friends.insertReturning(
                "name, age, phone",
                "id",
                "PSQL", 22, 999
        ).executeReturning()) {

            ResultSet rs = result.getResultSet();
            if (rs.next()) {
                id = rs.getInt("id");
//                Assert.assertEquals(4, id);
            } else {
                throw new NullPointerException("Returning id cannot be null");
            }
        }
        friends.delete().where().equals("id", id).executeAndClose();
        System.out.println("\n\n\n");
    }
}
