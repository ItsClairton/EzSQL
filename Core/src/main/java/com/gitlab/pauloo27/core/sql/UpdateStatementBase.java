package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * A abstract update statement.
 *
 * @param <Statement> The current statement class.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public abstract class UpdateStatementBase<Statement extends UpdateStatementBase> extends StatementBase<Statement, UpdateResult> {
    /**
     * Builds a update statement.
     *
     * @param sql   The EzSQL connection.
     * @param table The table.
     */
    public UpdateStatementBase(EzSQL sql, Table table) {
        super(sql, table);
    }

    /**
     * Execute and close the statement.
     *
     * @throws SQLException Problems to run statement.
     */
    public void executeAndClose() throws SQLException {
        this.execute().close();
    }
}
