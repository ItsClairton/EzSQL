package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The SQL Table.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class Table {

    /**
     * The SQL where the table is.
     */
    protected EzSQL<Database, Table> sql;
    /**
     * The table's name.
     */
    private String name;

    /**
     * Gets a table from the SQL.
     *
     * @param sql  The SQL.
     * @param name The table's name.
     */

    public Table(EzSQL<Database, Table> sql, String name) {
        Preconditions.checkArgument(EzSQL.checkEntryName(name), name + " is not a valid name");
        this.name = name;
        this.sql = sql;
    }

    /**
     * Checks if the table exists.
     *
     * @return If the table exists.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public boolean exists() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        // TODO Change to Table#count when it's implemented
        try (ResultSet result = sql.prepareStatement("SELECT COUNT(*) FROM information_schema.tables where table_name = ?;", getName()).executeQuery()) {
            if (result.next())
                return result.getInt(1) == 1;
        }
        return false;
    }

    /**
     * Truncates the table and closes the statement.
     *
     * @throws SQLException Problems to execute the statement.
     * @see #truncateReturningUpdatedLines() to get the updated lines.
     */
    public void truncate() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        sql.executeAndClose(sql.prepareStatement(String.format("TRUNCATE TABLE %s;", this.getName())));
    }

    /**
     * Truncates the table and returns the updated lines.
     *
     * @return The updated lines.
     *
     * @throws SQLException Problems to execute the statement.
     */
    @CheckReturnValue
    public int truncateReturningUpdatedLines() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        return sql.prepareStatement(String.format("TRUNCATE TABLE %s;", this.getName())).executeUpdate();
    }

    /**
     * Gets the table's name.
     *
     * @return The table's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Drops the table and closes the statement.
     *
     * @throws SQLException Problems to execute the statement.
     * @see #dropReturningUpdatedLines() to get the updated lines.
     */
    public void drop() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        sql.executeAndClose(sql.prepareStatement(String.format("DROP TABLE %s;", this.getName())));
    }

    /**
     * Drops the table and returns the updated lines.
     *
     * @return The updated lines.
     *
     * @throws SQLException Problems to execute the statement.
     */
    @CheckReturnValue
    public int dropReturningUpdatedLines() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        return sql.prepareStatement(String.format("DROP TABLE %s;", this.getName())).executeUpdate();
    }

    /**
     * Inserts values into the table.
     *
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     *
     * @return The insert statement.
     */
    public Insert insert(String columnsName, Object... values) {
        return new Insert(sql, this, columnsName, values);
    }

    /**
     * Inserts values into the table using an object.
     *
     * @param <T>    The object type to insert.
     * @param object The object to insert.
     *
     * @return The insert statement.
     */
    @CheckReturnValue
    public <T> Insert insert(T object) {
        Class<T> clazz = (Class<T>) object.getClass();
        StringBuilder sb = new StringBuilder();
        List<Object> values = new ArrayList<>();

        prepareObjectToInsert(object, clazz, sb, values);

        return new Insert(sql, this, sb.toString(), values.toArray());
    }

    /**
     * Prepares the object to be inserted.
     *
     * @param clazz   The object class
     * @param object  The object to the inserted.
     * @param columns The columns name.
     * @param values  The values list.
     * @param <T>     The object type to insert.
     */
    private <T> void prepareObjectToInsert(T object, Class<T> clazz, StringBuilder columns, List<Object> values) {
        // only append the columns name if the StringBuilder is empty
        // if it's not empty, the columns are already in the StringBuilder
        boolean appendColumn = columns.length() == 0;

        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            if (ReflectionUtils.isIgnored(field))
                return;

            if (ReflectionUtils.isId(field))
                return;

            try {
                String name = ReflectionUtils.getName(field);
                Object value = field.get(object);

                if (value == null)
                    return;

                if (columns.length() != 0 && appendColumn)
                    columns.append(", ");


                if (appendColumn)
                    columns.append(name);

                values.add(sql.getSerializerByClass(clazz).getSerializer().apply(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Inserts multiples objects in the same query using objects.
     *
     * @param objects The objects to insert.
     * @param <T>     The object type to insert.
     *
     * @return The insert statement.
     */
    @SafeVarargs
    @CheckReturnValue
    public final <T> Insert insertAll(T... objects) {
        StringBuilder sb = new StringBuilder();
        List<Object> values = new ArrayList<>();
        Arrays.stream(objects).forEach(object -> {
            Class<T> clazz = (Class<T>) object.getClass();
            prepareObjectToInsert(object, clazz, sb, values);
        });

        return new Insert(sql, this, sb.toString(), values.toArray());
    }

    /**
     * Selects the values from the table.
     *
     * @param columnsName The columns to select name.
     *
     * @return The select statement.
     */
    @CheckReturnValue
    public Select select(String columnsName) {
        return new Select(sql, this, columnsName);
    }

    /**
     * Selects the values from the table. Is the same that use {@code #select("*")}.
     *
     * @return The select statement.
     *
     * @see #select(String) to specify the columns to select.
     */
    @CheckReturnValue
    public Select select() {
        return this.select("*");
    }

    /**
     * Updates the table's values.
     *
     * @return The update statement.
     */
    @CheckReturnValue
    public Update update() {
        return new Update(sql, this);
    }

    /**
     * Updates the table's values using an object.
     *
     * @param <T>    The object type to update.
     * @param object The object to update.
     *
     * @return The update statement.
     */
    @CheckReturnValue
    public <T> Update update(T object) {
        Class<T> clazz = (Class<T>) object.getClass();

        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        Preconditions.checkNotNull(idField);

        idField.setAccessible(true);

        try {
            String idColumn = idField.getName();
            int id = idField.getInt(object);
            Preconditions.checkState(id != 0, "Invalid id (0).");

            Preconditions.checkNotNull(idField);

            Update update = new Update(sql, this).where().equals(idColumn, id);

            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !field.getName().equals(idColumn)).forEach(field -> {
                field.setAccessible(true);

                if (ReflectionUtils.isIgnored(field))
                    return;

                String name = ReflectionUtils.getName(field);

                Object value = null;
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                update.set(name, sql.getSerializerByClass(field.getType()).getSerializer().apply(value));
            });

            return update;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO Update all using 1 query

    /**
     * Deletes the table's values.
     *
     * @return The delete statement.
     */
    @CheckReturnValue
    public Delete delete() {
        return new Delete(sql, this);
    }

    /**
     * Deletes the table's values using an object.
     *
     * @param <T>    The object type to delete.
     * @param object The object to delete.
     *
     * @return The delete statement.
     */
    @CheckReturnValue
    public <T> Delete delete(T object) {

        Field idField = getIdField(object.getClass());

        try {
            String idColumn = idField.getName();
            int id = idField.getInt(object);

            Preconditions.checkNotNull(idField);

            return new Delete(sql, this).where().equals(idColumn, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the Field with the {@link Id} annotation.
     *
     * @param clazz The class to find the field.
     * @param <T>   The class type.
     *
     * @return The Field with the {@link Id} annotation.
     */
    private <T> Field getIdField(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);
    }

    /**
     * Deletes multiples objects in the same query using objects.
     *
     * @param objects The objects to delete.
     * @param <T>     The object to delete type.
     *
     * @return The delete statement.
     */
    @CheckReturnValue
    public <T> Delete deleteAll(T... objects) {
        Delete deleteStatement = new Delete(sql, this);
        Arrays.stream(objects).forEach(object -> {
            try {
                Field idField = getIdField(object.getClass());
                String idColumn = idField.getName();
                int id = idField.getInt(object);

                Preconditions.checkNotNull(idField);

                if (deleteStatement.getWhereConditions().getWhereStatements().isEmpty())
                    deleteStatement.where().equals(idColumn, id);
                else
                    deleteStatement.or().equals(idColumn, id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return deleteStatement;
    }

}
