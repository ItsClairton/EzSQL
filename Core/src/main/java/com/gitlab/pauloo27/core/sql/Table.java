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
public class Table {

    /**
     * The table's name.
     */
    private String name;
    /**
     * The SQL where the table is.
     */
    protected EzSQL<Database, Table> sql;

    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */

    public Table(EzSQL<Database, Table> sql, String name) {
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
    public UpdateResult drop() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        PreparedStatement statement = sql.getConnection().prepareStatement(String.format("DROP TABLE %s;", this.getName()));
        return new UpdateResult(statement);
    }

    /**
     * Inserts values into the table.
     *
     * @param insert The Insert statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public UpdateResult insert(Insert insert) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        // No EzStatement? Yeah, Insert haven't WHERE
        return new UpdateResult(sql.build(insert, this));
    }

    /**
     * Inserts values into the table and then close.
     *
     * @param insert The Insert statement.
     * @throws SQLException Problems to execute the statement.
     */
    public void insertAndClose(Insert insert) throws SQLException {
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
    public QueryResult select(StatementBase select) throws SQLException {
        // EzStatement to avoid type cast on the call.
        if (!(select instanceof Select)) throw new IllegalArgumentException("The parameter should to be EzSelect");
        return this.select((Select) select);
    }

    /**
     * Selects values from the table.
     *
     * @param select The select statement.
     * @return The query result.
     * @throws SQLException Problems to execute the statement.
     */

    public QueryResult select(Select select) throws SQLException {
        return new QueryResult(sql.build(select, this));
    }

    /**
     * Updates the table's values.
     *
     * @param update The update statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public UpdateResult update(StatementBase update) throws SQLException {
        if (!(update instanceof Update)) throw new IllegalArgumentException("The parameter should be EzUpdate");
        return this.update((Update) update);
    }

    /**
     * Updates table's values.
     *
     * @param update The update statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */

    public UpdateResult update(Update update) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        return new UpdateResult(sql.build(update, this));
    }

    /**
     * Deletes table's values.
     *
     * @param delete The delete statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */
    public UpdateResult delete(StatementBase delete) throws SQLException {
        // EzStatement to avoid type cast on the call.
        if (!(delete instanceof Delete)) throw new IllegalArgumentException("The parameter should be EzDelete");
        return this.delete((Delete) delete);
    }

    /**
     * Deletes table's values.
     *
     * @param delete The delete statement.
     * @return The update result.
     * @throws SQLException Problems to execute the statement.
     */

    public UpdateResult delete(Delete delete) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        return new UpdateResult(sql.build(delete, this));
    }

}
