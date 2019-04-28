package com.gitlab.pauloo27.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The SQL Table.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class PostgreSQLTable extends Table {
    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */
    public PostgreSQLTable(EzSQL sql, String name) {
        super(sql, name);
    }

    /**
     * Checks if the table exists.
     *
     * @return If the table exists.
     *
     * @throws SQLException Problems to execute the statement.
     */
    @Override
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        // TODO Change to EzTable#count when it's implemented
        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE tablename = ?;", getName()).executeQuery()) {
            if (result.next())
                return result.getInt(1) == 1;
        }
        return false;
    }

    /**
     * Inserts values into the table.
     *
     * @param columnNames   The ordered columns name separated by ", ".
     * @param returnColumns The columns to return.
     * @param values        The values to insert.
     *
     * @return The insert statement.
     */
    public InsertReturning insertReturning(String columnNames, String returnColumns, Object... values) {
        return new InsertReturning(sql, this, columnNames, returnColumns, values);
    }


}
