package com.gitlab.pauloo27.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

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
     * Inserts values into the table and returns one or more values.
     *
     * @param insert      The Insert statement.
     * @param columnNames The returning columns' names separated by ", ".
     * @return The returning values.
     * @throws SQLException Problems to execute the statement.
     */
    public QueryResult insertReturning(Insert insert, String columnNames) throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        // No EzStatement? Yeah, Insert haven't WHERE
        return new QueryResult(sql.build(insert, columnNames, this));
    }


}
