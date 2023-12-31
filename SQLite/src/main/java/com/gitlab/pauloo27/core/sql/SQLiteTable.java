package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * The SQL Table.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class SQLiteTable extends Table {
    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */
    public SQLiteTable(EzSQL sql, String name) {
        super(sql, name);
    }

    @Override
    public void truncate() throws SQLException {
        sql.executeUnsafeStatementAndClose("DELETE FROM %s", getName());
    }

    @Override
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        // TO DO Change to EzTable#count when it's implemented
//        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = ?;", getName()).executeQuery()) {
//            if (result.next())
//                return result.getInt(1) == 1;
//        }
//        return false;

        try {
            return sql.getTable("sqlite_master")
                    .count()
                    .where().equals("type", "table")
                    .and().equals("name", this.getName())
                    .execute().getFirstColumnAsInt() == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
