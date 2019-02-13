package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The insert statement.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class Insert extends StatementBase {

    private String columnsName;
    private List<EzValue> valueList;
    private List<Object> objectList;

    /**
     * Inserts data in the database. This constructor is more verbose than {@link #Insert(String, Object...)} but is
     * more simple to understand with many values.
     *
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     */
    public Insert(String columnsName, EzValue... values) {
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        this.columnsName = columnsName;
        this.valueList = Arrays.asList(values);
    }

    /**
     * Inserts data in the database. This constructor is less verbose than {@link #Insert(String, EzValue...)} but is
     * more hard to understand with many values.
     *
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     */
    public Insert(String columnsName, Object... values) {
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        this.columnsName = columnsName;
        this.objectList = Arrays.asList(values);
    }

    /**
     * Gets the values to insert if the used constructor is {@link #Insert(String, EzValue...)}.
     *
     * @return The values.
     */
    public List<EzValue> getValueList() {
        return valueList;
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
        if (valueList != null) {
            return valueList.stream()
                    .flatMap(value -> value.getValues().stream())
                    .collect(Collectors.toList());
        } else
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
        return columnsName.split(", ").length;
    }

    /**
     * Gets the values converted to SQL.
     *
     * @return The values converted to SQL.
     */
    public String valuesToString() {
        if (valueList != null) {
            return this.getValueList().stream().map(valueList -> {
                        if (!valueList.isValid(getColumnsCount()))
                            throw new InvalidParameterException(String.format("The value's size (%d) is not compatible with the columns count (%d).", valueList.getValues().size(), getColumnsCount()));

                        return String.format("(%s)", Joiner.on(", ").join(Collections.nCopies(getColumnsCount(), "?")));
                    }
            ).collect(Collectors.joining(", "));
        } else {
            return String.format("(%s)", Joiner.on(", ").join(Collections.nCopies(getColumnsCount(), "?")));
        }
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

    /**
     * Represents the values to insert. Used in {@link Insert#Insert(String, EzValue...)} constructor.
     */
    public static class EzValue {
        private List<Object> values;

        /**
         * Represents the values to insert.
         *
         * @param values The values.
         */
        public EzValue(Object... values) {
            this.values = Arrays.asList(values);
        }

        /**
         * Gets the values to insert.
         *
         * @return The values to insert.
         */
        public List<Object> getValues() {
            return values;
        }

        /**
         * Checks if the values count is the same of the columns count.
         *
         * @param columnsCount The columns count.
         * @return If the values count is the same of the columns count.
         */
        public boolean isValid(int columnsCount) {
            return columnsCount == values.size();
        }
    }

}
