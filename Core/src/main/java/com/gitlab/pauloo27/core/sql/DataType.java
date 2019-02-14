package com.gitlab.pauloo27.core.sql;

import java.util.List;

/**
 * SQL data types.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class DataType {

    /**
     * The data type name, used to build the SQL.
     */
    private final String sql;
    /**
     * A list of the valid attributes.
     */
    private final List<Attribute> validAttributes;
    /**
     * A list of the forced attributes of this type.
     */
    private final List<Attribute> forcedAttributes;
    /**
     * An optional type custom name.
     */
    private final String customName;

    /**
     * Builds a data type.
     * @param sql The type in SQL.
     * @param validAttributes A list of valid attributes.
     */
    public DataType(String sql, List<Attribute> validAttributes) {
        this(sql, validAttributes, null, null);
    }

    /**
     * Builds a data type.
     * @param sql The type in SQL.
     * @param validAttributes A list of valid attributes.
     * @param forcedAttributes A list of forced attributes.
     * @param customName A custom name.
     */
    public DataType(String sql, List<Attribute> validAttributes, List<Attribute> forcedAttributes, String customName) {
        this.sql = sql;
        this.validAttributes = validAttributes;
        this.forcedAttributes = forcedAttributes;
        this.customName = customName;
    }

    public String toSQL() {
        return sql;
    }

    public boolean hasCustomName() {
        return customName != null;
    }

    public String getCustomName() {
        return customName;
    }

    public List<Attribute> getValidAttributes() {
        return validAttributes;
    }

    public List<Attribute> getForcedAttributes() {
        return forcedAttributes;
    }

    public boolean isValid(Attribute attribute) {
        return validAttributes.contains(attribute);
    }
}
