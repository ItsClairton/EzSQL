package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.SQLException;

public class Tests {

    @Test
    public void testWithPostgreSQL() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzSQL(EzSQLType.POSTGRESQL).withAddress(Tester.PSQL_HOST, Tester.PSQL_PORT).withLogin("ezsql", "1234"));
    }
}
