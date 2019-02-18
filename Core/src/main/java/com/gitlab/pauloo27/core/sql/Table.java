package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
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
     * The table's name.
     */
    private String name;
    /**
     * The SQL where the table is.
     */
    protected EzSQL<Database, Table> sql;

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
     * Truncates the table.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public void truncate() throws SQLException {
        if (!sql.isConnected()) throw new SQLException("Not connected.");
        sql.executeAndClose(sql.prepareStatement(String.format("TRUNCATE TABLE %s;", this.getName())));
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
     * Drops the table.
     *
     * @return The update result.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public UpdateResult drop() throws SQLException {
        if (!this.sql.isConnected()) throw new SQLException("Not connected.");
        PreparedStatement statement = sql.getConnection().prepareStatement(String.format("DROP TABLE %s;", this.getName()));
        return new UpdateResult(statement);
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
     * Inserts values into the table.
     *
     * @param <T>    The object type to be inserted.
     * @param object The object to the inserted.
     *
     * @return The insert statement.
     */
    public <T> Insert insert(T object) {
        Class<T> clazz = (Class<T>) object.getClass();
        StringBuilder sb = new StringBuilder();
        List<Object> values = new ArrayList<>();

        prepareObjectToInsert(object, clazz, sb, values);

        return new Insert(sql, this, sb.toString(), values.toArray());
    }

    private <T> void prepareObjectToInsert(T object, Class<T> clazz, StringBuilder columns, List<Object> values) {
        // only append the columns name if the StringBuilder is empty
        // if it's not empty, the columns are already in the StringBuilder
        boolean appendColumn = columns.length() == 0;

        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);


            if (columns.length() != 0 && appendColumn)
                columns.append(", ");

            if (field.isAnnotationPresent(Id.class))
                return;

            try {
                String name = field.getName();
                Object value = field.get(object);

                if (value == null)
                    return;

                if (field.isAnnotationPresent(Name.class))
                    name = field.getAnnotation(Name.class).value();

                if (appendColumn)
                    columns.append(name);

                values.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    public <T> Insert insertAll(T... objects) {
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
    public Select select() {
        return this.select("*");
    }

    /**
     * Updates the table's values.
     *
     * @return The update statement.
     */
    public Update update() {
        return new Update(sql, this);
    }

    /**
     * Updates the table's values using an object.
     *
     * @param <T>    The object type to be updated.
     * @param object The object to be updated.
     *
     * @return The update statement.
     */
    public <T> Update update(T object) {
        Class<T> clazz = (Class<T>) object.getClass();

        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        try {
            String idColumn = idField.getName();
            int id = idField.getInt(object);

            Preconditions.checkNotNull(idField);

            Update update = new Update(sql, this).where().equals(idColumn, id);

            Arrays.stream(clazz.getDeclaredFields()).filter(field -> !field.getName().equals(idColumn))
                    .forEach(field -> {
                        field.setAccessible(true);

                        String name = field.getName();

                        if (field.isAnnotationPresent(Name.class))
                            name = field.getAnnotation(Name.class).value();

                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        update.set(name, value);
                    });

            return update;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes the table's values.
     *
     * @return The delete statement.
     */
    public Delete delete() {
        return new Delete(sql, this);
    }

    /**
     * Deletes the table's values using a object.
     *
     * @param <T>    The object type to be deleted.
     * @param object The object to the deleted.
     *
     * @return The delete statement.
     */
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

    private <T> Field getIdField(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);
    }

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
