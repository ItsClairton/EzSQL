package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.SQLException;

public class Tests {

    @Test
    public void testWithMariaDB() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzSQL(EzSQLType.MARIADB).withAddress(Tester.MARIADB_HOST).withLogin("ezsql", "1234"));
    }
}
