package com.gitlab.pauloo27.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EzSQLiteTable extends EzTable {
    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */
    public EzSQLiteTable(EzSQL sql, String name) {
        super(sql, name);
    }

    @Override
    public void truncate() throws SQLException {
        sql.executeStatementAndClose("DELETE FROM %s", getName());
    }

    @Override
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        // TODO Change to EzTable#count when it's implemented
        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = ?;", getName()).executeQuery()) {
            if (result.next())
                return result.getInt(1) == 1;
        }
        return false;
    }
}
