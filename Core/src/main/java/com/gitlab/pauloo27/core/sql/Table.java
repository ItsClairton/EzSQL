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
            if (result.next())
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
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     * @return The insert statement.
     */
    public Insert insert(String columnsName, Object... values) {
        return new Insert(sql, this, columnsName, values);
    }

    /**
     * Selects values from the table.
     *
     * @param columnsName The columns to select name.
     * @return The select statement.
     */
    public Select select(String columnsName) {
        return new Select(sql, this, columnsName);
    }

    /**
     * Updates table's values.
     *
     * @return The update statement.
     */
    public Update update() {
        return new Update(sql, this);
    }

    /**
     * Deletes table's values.
     *
     * @return The delete statement.
     */
    public Delete delete() {
        return new Delete(sql, this);
    }

}
