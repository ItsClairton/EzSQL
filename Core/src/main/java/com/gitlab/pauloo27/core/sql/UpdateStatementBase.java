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
     */
    public void executeAndClose() {
        executeAndClose(null);
    }

    /**
     * Execute and close the statement.
     *
     * @param handler The exception handler.
     */
    public void executeAndClose(ExceptionHandler handler) {
        try (Result result = this.execute(handler)) {
            if (result != null)
                result.close();
        } catch (SQLException e) {
            if (handler == null)
                e.printStackTrace();
            else
                handler.onException(e);
        }
    }

}
