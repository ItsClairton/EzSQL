package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * The select statement.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class Select extends StatementBase {

    /**
     * The columns' name to select.
     */
    private String columnNames;

    /**
     * Sets the columns to select.
     *
     * @param columnsName The columns name.
     */
    public Select(String columnsName) {
        Preconditions.checkNotNull(columnsName, "Columns cannot be null");
        Preconditions.checkArgument(!columnsName.isEmpty(), "Columns cannot be null");
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        this.columnNames = columnsName;
    }

    /**
     * Gets the column to select names.
     *
     * @return The columns name.
     */

    public String getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the statement converted to SQL.
     *
     * @return The class converted to SQL.
     * @deprecated Use {@link EzSQL#build(Select, Table)} instead.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public String toString() {
        return String.format("SELECT %s FROM ${table} %s;", columnNames, super.toString());
    }

}
