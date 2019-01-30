package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.io.File;
import java.sql.SQLException;

public class Tests {

    @Test
    public void testWithSQLite() throws SQLException, ClassNotFoundException {
        File file = new File("db.sqlite");
        Tester.testWith(new EzSQLite(file.getPath()));
        System.out.println("Deleting file...");
        file.delete();
    }
}
