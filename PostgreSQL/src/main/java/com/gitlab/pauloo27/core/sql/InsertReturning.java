package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.SQLException;

/**
 * The insert statement.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class InsertReturning extends Insert {

    /**
     * The columns to return.
     */
    private String returningColumns;

    /**
     * Builds a insert returning statement.
     *
     * @param sql              The EzSQL connection.
     * @param table            The table.
     * @param columnsName      The ordered columns name separated by ", ".
     * @param returningColumns The columns to return.
     * @param values           The values to insert.
     */
    public InsertReturning(EzSQL sql, PostgreSQLTable table, String columnsName, String returningColumns, Object... values) {
        super(sql, table, columnsName, values);
        this.returningColumns = returningColumns;
    }

    /**
     * Executes the statement and returning the columns.
     *
     * @return The columns to return.
     */
    public QueryResult executeReturning() {
        Preconditions.checkState(sql.isConnected(), new SQLException("Not connected."));
        try {
            return new QueryResult(((EzPostgreSQL) sql).build(this, returningColumns, (PostgreSQLTable) table));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
