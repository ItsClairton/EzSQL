package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a table.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzTableBuilder {

    /**
     * The table's column.
     */
    private List<EzColumnBuilder> columns = new ArrayList<>();
    /**
     * The table's name.
     */
    private String name;

    /**
     * Builds a table.
     *
     * @param name The table's name.
     */
    public EzTableBuilder(String name) {
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

    public List<EzColumnBuilder> getColumns() {
        return columns;
    }

    /**
     * Adds one or more columns to the table.
     *
     * @param columns The columns.
     * @return The current object instance.
     */
    public EzTableBuilder withColumn(EzColumnBuilder... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Gets the table converted to SQL.
     *
     * @param type The type of the SQL.
     * @return The table converted to SQL query.
     */

    public String toSQL(EzSQLType type) {
        return
                this.getColumns().stream().map(column -> {
                    if (column.getDataType() == EzDataType.PRIMARY_KEY)
                        column.withAttributes(EzAttribute.NOT_NULL, EzAttribute.PRIMARY_KEY, EzAttribute.AUTO_INCREMENT);
                    return column.toSQL(type);
                }).collect(Collectors.joining(", ")).trim();
    }

}
