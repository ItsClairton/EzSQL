package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a table.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class TableBuilder {

    /**
     * The table's column.
     */
    private List<ColumnBuilder> columns = new ArrayList<>();
    /**
     * The table's name.
     */
    private String name;

    /**
     * Builds a table.
     *
     * @param name The table's name.
     */
    public TableBuilder(String name) {
        this.name = name;
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
     * Gets the table's columns.
     *
     * @return The table's columns.
     */

    public List<ColumnBuilder> getColumns() {
        return columns;
    }

    /**
     * Adds one or more columns to the table.
     *
     * @param columns The columns.
     *
     * @return The current object instance.
     */
    public TableBuilder withColumn(ColumnBuilder... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Gets the table converted to SQL.
     *
     * @param sql The current EzSQL connection.
     *
     * @return The table converted to SQL query.
     */
    public String toSQL(EzSQL sql) {
        return this.getColumns().stream()
                .map(column -> {
                    if (column.getDataType().getForcedAttributes() != null) {
                        column.withAttributes(column.getDataType().getForcedAttributes().toArray(new Attribute[0]));
                    }
                    return column.toSQL(sql);
                }).collect(Collectors.joining(", ")).trim();
    }

}
