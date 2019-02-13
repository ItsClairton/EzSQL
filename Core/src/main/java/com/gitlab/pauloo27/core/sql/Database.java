package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The SQL Database.
 *
 * @author Paulo
 * @version 4.0
 * @since 0.1.0
 */
public class Database {

    /**
     * The database's name.
     */
    private String name;
    /**
     * The SQL where the database is.
     */
    protected EzSQL sql;

    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public Database(EzSQL sql, String name) {
        Preconditions.checkArgument(EzSQL.checkEntryName(name), name + " is not a valid name");
        this.name = name;
        this.sql = sql;
    }

    /**
     * Gets the database name.
     *
     * @return The database's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if the database exists. If the SQL is SQLite returns null, SQLite has no databases.
     *
     * @return If the database exists.
     * @throws SQLException Problems to execute the statement.
     */
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        try (PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SHOW DATABASES LIKE '%s';", this.getName()))) {
            QueryResult result = new QueryResult(statement);
            return result.getResultSet().next();
        }
    }

    /**
     * Drops the database.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public void drop() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        sql.getConnection().prepareStatement(String.format("DROP DATABASE %s;", this.getName())).execute();
    }

}
