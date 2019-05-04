package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * The SQL Database.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class PostgreSQLDatabase extends Database {
    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public PostgreSQLDatabase(EzSQL<PostgreSQLDatabase, PostgreSQLTable> sql, String name) {
        super(sql, name);
    }

    /**
     * Checks if the database exists. If the SQL is SQLite returns null, SQLite has no databases.
     *
     * @return If the database exists.
     *
     * @throws SQLException Problems to execute the statement.
     */
    @Override
    public boolean exists() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
            return sql.getTable("pg_catalog.pg_database")
                    .count()
                    .where().equals("datname", this.getName())
                    .executeThrowing().getFirstColumnAsInt() == 1;
    }
}
