package com.gitlab.pauloo27.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLDatabase extends Database {
    /**
     * Gets a database of the SQL.
     *
     * @param sql  The SQL.
     * @param name The database's name.
     */
    public PostgreSQLDatabase(EzSQL sql, String name) {
        super(sql, name);
    }

    @Override
    public boolean exists() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        // TODO Change to EzTable#count when it's implemented
        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM pg_catalog.pg_database WHERE datname = '?';", getName()).executeQuery()) {
            if (result.next())
                return result.getInt(1) == 1;
        }
        return false;
    }
}
