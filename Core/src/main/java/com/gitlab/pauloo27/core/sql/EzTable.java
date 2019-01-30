package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The SQL Table.
 *
 * @author Paulo
 * @version 3.0
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
    protected EzSQL<EzDatabase, EzTable> sql;

    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */

    public EzTable(EzSQL<EzDatabase, EzTable> sql, String name) {
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
        // TODO Change to EzTable#count when it's implemented
        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM information_schema.tables where table_name = ?;", getName()).executeQuery()) {
            if(result.next())
                return result.getInt(1) == 1;
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
        sql.getConnection().prepareStatement(String.format("TRUNCATE TABLE %s;", this.getName())).close();
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
