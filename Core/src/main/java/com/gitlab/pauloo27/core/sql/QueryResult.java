package com.gitlab.pauloo27.core.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The query result.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class QueryResult extends Result {

    /**
     * The result set.
     */
    private ResultSet result;

    /**
     * Executes a query and save the result set. Use {@link #close()} to close the statement, use try-with-resources to
     * closes automatically.
     *
     * @param statement The statement.
     * @throws SQLException Problems to execute the statement.
     */

    public QueryResult(PreparedStatement statement) throws SQLException {
        super(statement);
        this.result = this.getStatement().executeQuery();
    }

    /**
     * Gets the result set.
     *
     * @return The result set.
     */
    public ResultSet getResultSet() {
        return result;
    }
}
