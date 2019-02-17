package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.sql.SQLException;
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
public class Insert extends UpdateStatementBase<Insert> {

    /**
     * The columns name.
     */
    private String columnsName;
    /**
     * The list of values to insert.
     */
    private List<Object> objectList;

    /**
     * Builds a insert statement.
     *
     * @param sql         The EzSQL connection.
     * @param table       The table.
     * @param columnsName The ordered columns name separated by ", ".
     * @param values      The values to insert.
     */
    public Insert(EzSQL sql, Table table, String columnsName, Object... values) {
        super(sql, table);
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", "))
                .allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");

        int columnsCount = columnsName.split(",").length;

        if (columnsCount < values.length)
            Preconditions.checkArgument(values.length % columnsCount == 0,
                    "The value's size (%d) is not compatible with the columns count (%d).",
                    values.length, columnsName.split(",").length);
        else
            Preconditions.checkArgument(values.length == columnsCount,
                    String.format("The value's size (%d) is not compatible with the columns count (%d).",
                            values.length, columnsName.split(",").length));

        this.columnsName = columnsName;
        this.objectList = Arrays.asList(values);
        this.sql = sql;
        this.table = table;
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
        if (getColumnsCount() == objectList.size())
            return String.format("(%s)", Joiner.on(", ").join(Collections.nCopies(getColumnsCount(), "?")));
        else
            return Joiner.on(", ").join(Collections
                    .nCopies(objectList.size() / getColumnsCount(), String.format("(%s)", Joiner.on(", ")
                            .join(Collections.nCopies(getColumnsCount(), "?")))));
    }

    @Override
    protected UpdateResult getResultType() throws SQLException {
        return new UpdateResult(sql.build(this, table));
    }

    /**
     * Gets the statement converted to SQL.
     *
     * @return The class converted to SQL.
     *
     * @deprecated Use {@link EzSQL#build(Insert, Table)} instead.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public String toString() {
        return String.format("INSERT INTO ${table} (%s) VALUES %s;", columnsName, valuesToString());
    }

}
