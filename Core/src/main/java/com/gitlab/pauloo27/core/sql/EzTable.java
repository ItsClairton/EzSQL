package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The SQL Table.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzTable {

    /**
     * The table's name.
     */
    private String name;
    /**
     * The SQL where the table is.
     */
    private EzSQL sql;

    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */

    public EzTable(EzSQL sql, String name) {
        Preconditions.checkArgument(EzSQL.checkEntryName(name), name + " is not a valid name");
        this.name = name;
        this.sql = sql;
    }

    /**
     * Checks if the table exists.
     *
     * @return If the table exists.
     * @throws SQLException Problems to execute the statement.
     */
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        if (this.sql.getType().isMySQLLike()) {
            PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SHOW TABLES LIKE '%s';", this.name));
            EzQueryResult result = new EzQueryResult(statement);
            boolean hasNext = result.getResultSet().next();
            result.close();
            return hasNext;
        } else if (this.sql.getType() == EzSQLType.POSTGRESQL) {
            PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SELECT tablename FROM pg_catalog.pg_tables WHERE tablename = '%s';", this.name));
            EzQueryResult result = new EzQueryResult(statement);
            boolean hasNext = result.getResultSet().next();
            result.close();
            return hasNext;
        } else if (this.sql.getType() == EzSQLType.SQLITE) {
            PreparedStatement statement = sql.getConnection().prepareStatement(String.format("SELECT name FROM sqlite_master WHERE type='table' AND name LIKE '%s';", this.name));
            EzQueryResult result = new EzQueryResult(statement);
            boolean hasNext = result.getResultSet().next();
            result.close();
            return hasNext;
        }
        return false;
    }

    /**
     * Truncates the table.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public void truncate() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        if (this.sql.getType() == EzSQLType.SQLITE) {
            sql.getConnection().prepareStatement(String.format("DELETE FROM %s;", this.getName())).close();
        } else {
            sql.getConnection().prepareStatement(String.format("TRUNCATE TABLE %s;", this.getName())).close();
        }
    }

    /**
     * Gets the table's name.
     *
     * @return The table's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Drops the table.
     *
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public EzUpdateResult drop() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        PreparedStatement statement = sql.getConnection().prepareStatement(String.format("DROP TABLE %s;", this.getName()));
        return new EzUpdateResult(statement);
    }

    /**
     * Inserts values into the table.
     *
     * @param insert The Insert statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public EzUpdateResult insert(EzInsert insert) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        // No EzStatement? Yeah, Insert haven't WHERE
        return new EzUpdateResult(sql.build(insert, this));
    }

    /**
     * Inserts values into the table and then close.
     *
     * @param insert The Insert statement.
     * @throws SQLException Problems to execute the statement.
     */
    public void insertAndClose(EzInsert insert) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        sql.executeAndClose(sql.build(insert, this));
    }

    /**
     * Inserts values into the table and returns one or more values.
     *
     * @param insert      The Insert statement.
     * @param columnNames The returning columns' names separated by ", ".
     * @return The returning values.
     * @throws SQLException Problems to execute the statement.
     */
    public EzQueryResult insertReturning(EzInsert insert, String columnNames) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        if (sql.getType() != EzSQLType.POSTGRESQL)
            throw new SQLException("Insert returning is only valid to PostgreSQL");
        // No EzStatement? Yeah, Insert haven't WHERE
        return new EzQueryResult(sql.build(insert, columnNames, this));
    }

    /**
     * Selects values from the table.
     *
     * @param select The select statement.
     * @return The query result.
     * @throws SQLException Problems to execute the statement.
     */
    public EzQueryResult select(EzStatement select) throws SQLException {
        // EzStatement to avoid type cast on the call.
        if (!(select instanceof EzSelect)) throw new IllegalArgumentException("The parameter should to be EzSelect");
        return this.select((EzSelect) select);
    }

    /**
     * Selects values from the table.
     *
     * @param select The select statement.
     * @return The query result.
     * @throws SQLException Problems to execute the statement.
     */

    public EzQueryResult select(EzSelect select) throws SQLException {
        return new EzQueryResult(sql.build(select, this));
    }

    /**
     * Updates the table's values.
     *
     * @param update The update statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public EzUpdateResult update(EzStatement update) throws SQLException {
        if (!(update instanceof EzUpdate)) throw new IllegalArgumentException("The parameter should be EzUpdate");
        return this.update((EzUpdate) update);
    }

    /**
     * Updates table's values.
     *
     * @param update The update statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */

    public EzUpdateResult update(EzUpdate update) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        return new EzUpdateResult(sql.build(update, this));
    }

    /**
     * Deletes table's values.
     *
     * @param delete The delete statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public EzUpdateResult delete(EzStatement delete) throws SQLException {
        // EzStatement to avoid type cast on the call.
        if (!(delete instanceof EzDelete)) throw new IllegalArgumentException("The parameter should be EzDelete");
        return this.delete((EzDelete) delete);
    }

    /**
     * Deletes table's values.
     *
     * @param delete The delete statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */

    public EzUpdateResult delete(EzDelete delete) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        return new EzUpdateResult(sql.build(delete, this));
    }

}
