package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

public class EzSQLiteDatabase extends EzDatabase {
    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public EzSQLiteDatabase(EzSQL sql, String name) {
        super(sql, name);
    }

    @Override
    public boolean exists() throws SQLException {
        // SQLite has no databases.
        return false;
    }
}
