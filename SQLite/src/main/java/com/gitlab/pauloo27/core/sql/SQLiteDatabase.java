package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * The SQL Database.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class SQLiteDatabase extends Database {
    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public SQLiteDatabase(EzSQL<SQLiteDatabase, SQLiteTable> sql, String name) {
        super(sql, name);
    }

    @Override
    public boolean exists() throws SQLException {
        // SQLite has no databases.
        return false;
    }
}
