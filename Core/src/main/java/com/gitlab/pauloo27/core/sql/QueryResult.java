package com.gitlab.pauloo27.core.sql;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;

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
     *
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

    /**
     * Builds an object from the ResultSet. If the ResultSet is empty, returns null. If the ResultSet contains more than
     * one value, returns only the first one.
     *
     * @param clazz The object type to build from the ResultSet.
     * @param <T>   The object type to build from the ResultSet.
     *
     * @return The built object.
     *
     * @throws IllegalAccessException Error to build the object.
     * @throws InstantiationException Error to build the object.
     * @throws SQLException           Error to process the ResultSet.
     * @see #toList(Class) to builds the entire ResultSet.
     */
    @CheckReturnValue
    public <T> T to(Class<T> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        T row = clazz.newInstance();

        if (!result.next())
            return null;

        T object = createObject(row);
        result.close();
        return object;
    }

    /**
     * Builds the entire ResultSet to an object list.
     *
     * @param clazz The object type to build from the ResultSet.
     * @param <T>   The object type to build from the ResultSet.
     *
     * @return The built object list.
     *
     * @throws IllegalAccessException Error to build the object.
     * @throws InstantiationException Error to build the object.
     * @throws SQLException           Error to process the ResultSet.
     * @see #to(Class) to builds the first ResultSet item.
     */
    @CheckReturnValue
    public <T> List<T> toList(Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<>();
        while (result.next()) {
            T object = createObject(clazz.newInstance());
            list.add(object);
        }
        result.close();
        return list;
    }

    /**
     * Creates a object from the ResultSet.
     *
     * @param object The object instance.
     * @param <T>    The object to create type.
     *
     * @return The created object.
     */
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
