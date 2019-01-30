package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.SQLException;

public class Tests {

    @Test
    public void testWithMariaDB() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzMariaDB().withAddress(Tester.MARIADB_HOST, Tester.MARIADB_PORT).withLogin("ezsql", "1234"));
    }
}
