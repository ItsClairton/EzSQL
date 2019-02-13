package com.gitlab.pauloo27.core.sql;

/**
 * The delete statement.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class Delete extends StatementBase {

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
}
