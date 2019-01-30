package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class Tests {

    @Test
    public void testWithMySQL() throws SQLException, ClassNotFoundException {
        Tester.testWith(new EzMySQL().withAddress(Tester.MYSQL_HOST, Tester.MYSQL_PORT).withLogin("ezsql", "1234"));
    }

}
