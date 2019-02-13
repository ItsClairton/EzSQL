package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

public class SQLiteDatabase extends Database {
    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public SQLiteDatabase(EzSQL sql, String name) {
        super(sql, name);
    }

    @Override
    public boolean exists() throws SQLException {
        // SQLite has no databases.
        return false;
    }
}
