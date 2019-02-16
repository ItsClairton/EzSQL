package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * The delete statement.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class Delete extends UpdateStatementBase<Delete> {
    /**
     * Builds a delete statement.
     *
     * @param sql   The EzSQL connection.
     * @param table The table.
     */
    public Delete(EzSQL sql, Table table) {
        super(sql, table);
    }

    /**
     * Converts the statement to SQL.
     *
     * @return The class converted to SQL.
     * @deprecated Use {{@link EzSQL#build(Delete, Table)}} instead.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public String toString() {
        return String.format("DELETE FROM ${table} %s;", super.toString());
    }

    @Override
    protected UpdateResult getResultType() throws SQLException {
        return new UpdateResult(sql.build(this, table));
    }
}
