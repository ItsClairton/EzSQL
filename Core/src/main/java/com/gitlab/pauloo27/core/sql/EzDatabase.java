package com.gitlab.pauloo27.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The SQL Database.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzDatabase {

    /**
     * The database's name.
     */
    private String name;
    /**
     * The SQL where the database is.
     */
    private EzSQL sql;

    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public EzDatabase(EzSQL sql, String name) {
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
    public Boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        if (this.sql.getType().isMySQLLike()) {
            PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SHOW DATABASES LIKE '%s';", this.getName()));
            EzQueryResult result = new EzQueryResult(statement);
            boolean hasNext = result.getResultSet().next();
            result.close();
            return hasNext;
        } else if (this.sql.getType() == EzSQLType.POSTGRESQL) {
            PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SELECT datname FROM pg_catalog.pg_database WHERE datname = '%s';", this.getName()));
            EzQueryResult result = new EzQueryResult(statement);
            boolean hasNext = result.getResultSet().next();
            result.close();
            return hasNext;
        }
        return null;
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
