package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The insert statement.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class Insert extends StatementBase {

    /**
     * The columns name.
     */
    private String columnsName;
    /**
     * The list of values to insert.
     */
    private List<Object> objectList;

    /**
     * Inserts data in the database.
     *
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     */
    public Insert(String columnsName, Object... values) {
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        Preconditions.checkArgument(columnsName.split(",").length == values.length,
                "The value's size (%d) is not compatible with the columns count (%d).",
                values.length, columnsName.split(",").length);

        this.columnsName = columnsName;
        this.objectList = Arrays.asList(values);
    }

    /**
     * Gets the values to insert if the used constructor is {@link #Insert(String, Object...)}.
     *
     * @return The values.
     */
    public List<Object> getObjectList() {
        return objectList;
    }

    /**
     * Gets the values to insert.
     *
     * @return The values.
     */
    public List<Object> getValues() {
        return objectList;
    }

    /**
     * Gets the ordered columns' name separated by ", ".
     *
     * @return The ordered columns' name to.
     */
    public String getColumnsName() {
        return columnsName;
    }

    /**
     * Counts the columns' name.
     *
     * @return The columns' name count.
     */
    public int getColumnsCount() {
        return columnsName.split(",").length;
    }

    /**
     * Gets the values converted to SQL.
     *
     * @return The values converted to SQL.
     */
    public String valuesToString() {
        return String.format("(%s)", Joiner.on(", ").join(Collections.nCopies(getColumnsCount(), "?")));
    }

    /**
     * Gets the statement converted to SQL.
     *
     * @return The class converted to SQL.
     * @deprecated Use {@link EzSQL#build(Insert, Table)} instead.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public String toString() {
        return String.format("INSERT INTO ${table} (%s) VALUES %s;", columnsName, valuesToString());
    }

}
