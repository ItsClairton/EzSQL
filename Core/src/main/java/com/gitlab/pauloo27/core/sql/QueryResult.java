package com.gitlab.pauloo27.core.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public <T> List<T> toList(Class clazz) throws SQLException, IllegalAccessException, InstantiationException {
        List list = new ArrayList();
        while (result.next()) {
            list.add(createObject(clazz.newInstance()));
        }
        return list;
    }

    public <T> T to(Class<T> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        T row = clazz.newInstance();

        if (!result.next())
            return null;

        return createObject(row);
    }

    private <T> T createObject(T object) {
        Arrays.stream(object.getClass().getDeclaredFields())
                .forEach(field -> {
                    String name = field.getName();

                    if (field.isAnnotationPresent(Name.class))
                        name = field.getAnnotation(Name.class).value();

                    try {
                        int columnIndex = result.findColumn(name);
                        field.setAccessible(true);

                        field.set(object, result.getObject(columnIndex));
                    } catch (SQLException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return object;
    }
}
