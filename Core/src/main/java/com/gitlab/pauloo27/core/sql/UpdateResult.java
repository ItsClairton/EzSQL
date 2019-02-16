package com.gitlab.pauloo27.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The update result.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class UpdateResult extends Result {

    /**
     * The updated rows.
     */
    private int result;

    /**
     * Executes a update and save the updated rows. Use {@link #close()} to close the statement, use try-with-resources
     * to closes automatically.
     *
     * @param statement The statement.
     * @throws SQLException Problems to execute the statement.
     */
    public UpdateResult(PreparedStatement statement) throws SQLException {
        super(statement);
        this.result = this.getStatement().executeUpdate();
    }

    /**
     * Gets the updated rows.
     *
     * @return The updated rows.
     */
    public int getUpdatedRows() {
        return result;
    }

}
