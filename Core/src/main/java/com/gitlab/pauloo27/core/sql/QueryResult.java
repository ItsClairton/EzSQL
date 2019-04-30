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
     * @param sql       The EzSQL connection.
     * @param statement The statement.
     *
     * @throws SQLException Problems to execute the statement.
     */

    public QueryResult(EzSQL sql, PreparedStatement statement) throws SQLException {
        super(sql, statement);
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
     * @param clazz   The object type to build from the ResultSet.
     * @param handler The exception handler.
     * @param <T>     The object type to build from the ResultSet.
     *
     * @return The built object.
     *
     * @see #toList(Class) to builds the entire ResultSet.
     */
    @CheckReturnValue
    public <T> T to(Class<T> clazz, ExceptionHandler handler) {
        try {
            T row = clazz.newInstance();

            if (!result.next())
                return null;

            T object = createObject(row);
            result.close();
            return object;
        } catch (Exception e) {
            if (handler == null)
                e.printStackTrace();
            else
                handler.onException(e);
            return null;
        }
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
     * @see #toList(Class) to builds the entire ResultSet.
     */
    @CheckReturnValue
    public <T> T to(Class<T> clazz) {
        return to(clazz, null);
    }

    /**
     * Builds the entire ResultSet to an object list.
     *
     * @param clazz   The object type to build from the ResultSet.
     * @param handler The exception handler.
     * @param <T>     The object type to build from the ResultSet.
     *
     * @return The built object list.
     *
     * @see #to(Class) to builds the first ResultSet item.
     */
    @CheckReturnValue
    public <T> List<T> toList(Class<T> clazz, ExceptionHandler handler) {
        try {

            List<T> list = new ArrayList<>();
            while (result.next()) {
                T object = createObject(clazz.newInstance());
                list.add(object);
            }
            result.close();
            return list;
        } catch (Exception e) {
            if (handler == null)
                e.printStackTrace();
            else
                handler.onException(e);
            return null;
        }
    }

    /**
     * Builds the entire ResultSet to an object list.
     *
     * @param clazz The object type to build from the ResultSet.
     * @param <T>   The object type to build from the ResultSet.
     *
     * @return The built object list.
     *
     * @see #to(Class) to builds the first ResultSet item.
     */
    @CheckReturnValue
    public <T> List<T> toList(Class<T> clazz) {
        return toList(clazz, null);
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
                    field.setAccessible(true);

                    if (ReflectionUtils.isIgnored(field))
                        return;

                    String name = ReflectionUtils.getName(field);

                    try {
                        int columnIndex = result.findColumn(name);

                        Object value = result.getObject(columnIndex);

                        if (value != null)
                            field.set(object, sql.getSerializerByClass(field.getType()).getDeserializer().apply(field.getType(), value));
                    } catch (SQLException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return object;
    }

    public int getFirstColumnAsInt(ExceptionHandler handler) {
        try(ResultSet result = getResultSet()) {
            if (result.next())
                return result.getInt(1);
        } catch (SQLException e) {
            if (handler != null)
                handler.onException(e);
            else
                e.printStackTrace();
        }
        return 0;
    }

    public int getFirstColumnAsInt() {
        return getFirstColumnAsInt(null);
    }

    public long getFirstColumnAsLong(ExceptionHandler handler) {
        try(ResultSet result = getResultSet()) {
            if (result.next())
                return result.getLong(1);
        } catch (SQLException e) {
            if (handler != null)
                handler.onException(e);
            else
                e.printStackTrace();
        }
        return 0;
    }

    public long getFirstColumnAsLong() {
        return getFirstColumnAsLong(null);
    }

    public boolean getFirstColumnAsBoolean(ExceptionHandler handler) {
        try(ResultSet result = getResultSet()) {
            if (result.next())
                return result.getBoolean(1);
        } catch (SQLException e) {
            if (handler != null)
                handler.onException(e);
            else
                e.printStackTrace();
        }
        return false;
    }

    public boolean getFirstColumnAsBoolean() {
        return getFirstColumnAsBoolean(null);
    }


    public double getFirstColumnAsDouble(ExceptionHandler handler) {
        try(ResultSet result = getResultSet()) {
            if (result.next())
                return result.getDouble(1);
        } catch (SQLException e) {
            if (handler != null)
                handler.onException(e);
            else
                e.printStackTrace();
        }
        return 0;
    }

    public double getFirstColumnAsDouble() {
        return getFirstColumnAsDouble(null);
    }

    public <T> T getFirstColumn(ExceptionHandler handler) {
        try(ResultSet result = getResultSet()) {
            if (result.next())
                return (T) result.getObject(1);
        } catch (SQLException e) {
            if (handler != null)
                handler.onException(e);
            else
                e.printStackTrace();
        }
        return null;
    }

    public <T> T getFirstColumn() {
        return getFirstColumn(null);
    }
}
