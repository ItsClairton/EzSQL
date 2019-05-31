package com.gitlab.pauloo27.core.sql;

/**
 * The table and column name converter.
 *
 * Converts the model fields name to the SQL.
 * @author Paulo
 * @since 0.4.0
 * @version 1.0
 */
public interface NameConverter {

    /**
     * Converts the class name to a table name.
     *
     * @param className The class name.
     *
     * @return The table name.
     */
    String toTableName(String className);

    /**
     * Converts the field name to a column name.
     *
     * @param fieldName The field name.
     *
     * @return The column name.
     */
    String toColumnName(String fieldName);
}
