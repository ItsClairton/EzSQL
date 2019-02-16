package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.SQLException;

public class InsertReturning extends Insert {

    private String returningColumns;

    /*
     * Inserts data in the database.
     *
     * @param sql
     * @param table
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     */
    public InsertReturning(EzSQL sql, PostgreSQLTable table, String columnsName, String returningColumns, Object... values) {
        super(sql, table, columnsName, values);
        this.returningColumns = returningColumns;
    }

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
