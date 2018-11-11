package com.gitlab.pauloo27.core.sql;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The update statement.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzUpdate extends EzStatement {

    /**
     * The list of map (column's name, value) of values to set.
     */
    private List<Map.Entry<String, Object>> sets = new ArrayList<>();

    /**
     * Sets the values to sets.
     *
     * @param columnName The column' name.
     * @param value      The new value.
     * @return The current object instance.
     */
    public EzUpdate set(String columnName, Object value) {
        sets.add(new AbstractMap.SimpleEntry<>(columnName, value));
        return this;
    }

    /**
     * Gets the sets converted to SQL.
     *
     * @return The set list converted to String.
     */
    public String setsToString() {
        return String.format("SET %s", this.getSets().stream().map(set -> String.format("%s = ?", set.getKey())).collect(Collectors.joining(", ")));
    }

    /**
     * Gets the statement converted to SQL.
     *
     * @return The class converted to SQL.
     * @deprecated Use {{@link EzSQL#build(EzUpdate, EzTable)} instead.
     */
    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public String toString() {
        return String.format("UPDATE ${table} %s %s;", this.setsToString(), super.toString());
    }

    /**
     * Gets the list of values to set.
     *
     * @return The list of sets.
     */
    public List<Map.Entry<String, Object>> getSets() {
        return sets;
    }
}
