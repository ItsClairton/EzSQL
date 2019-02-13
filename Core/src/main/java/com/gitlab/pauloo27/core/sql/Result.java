package com.gitlab.pauloo27.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The result base.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public abstract class Result implements AutoCloseable {

    /**
     * The statement to execute.
     */
    private PreparedStatement statement;

    /**
     * Create a result and close the statement.
     *
     * @param statement The statement.
     */
    public Result(PreparedStatement statement) {
        this.statement = statement;
    }

    /**
     * Gets the statement.
     *
     * @return The statement.
     */
    public PreparedStatement getStatement() {
        return statement;
    }

    /**
     * Closes the statement.
     *
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public void close() throws SQLException {
        statement.close();
    }
}
