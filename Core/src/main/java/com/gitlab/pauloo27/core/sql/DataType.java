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
     *
     * @see #getCustomName() if you want to know more about the custom name.
     */
    private final String customName;

    /**
     * Builds a data type.
     *
     * @param sql             The type in SQL.
     * @param validAttributes A list of valid attributes.
     */
    public DataType(String sql, List<Attribute> validAttributes) {
        this(sql, validAttributes, null, null);
    }

    /**
     * Builds a data type.
     *
     * @param sql              The type in SQL.
     * @param validAttributes  A list of valid attributes.
     * @param forcedAttributes A list of forced attributes.
     * @param customName       A custom name.
     */
    public DataType(String sql, List<Attribute> validAttributes, List<Attribute> forcedAttributes, String customName) {
        this.sql = sql;
        this.validAttributes = validAttributes;
        this.forcedAttributes = forcedAttributes;
        this.customName = customName;
    }

    /**
     * Converts the data type to SQL.
     *
     * @return The data type name.
     */
    public String toSQL() {
        return sql;
    }

    /**
     * Checks if the data type has a custom name.
     *
     * @return If the data type has a custom name.
     * @see #getCustomName() if you want to know more about the custom name.
     */
    public boolean hasCustomName() {
        return customName != null;
    }

    /**
     * Gets the custom name. The custom name is the name of the custom data type.
     * <p>
     * Using the wrapper {@link DefaultDataType#PRIMARY_KEY} as example, the type name is {@code INTEGER} because it's
     * a {@code INTEGER} value while the custom name is {@code PRIMARY_KEY}. The custom name is used to the build to
     * check if the type is a real {@code INTEGER} or a {@code PRIMARY_KEY} wrapped to {@code INTEGER}.
     *
     * @return The custom name.
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * Gets the valid attributes list.
     *
     * @return The valid attributes list.
     */
    public List<Attribute> getValidAttributes() {
        return validAttributes;
    }

    /**
     * Gets the forced attributes list.
     *
     * @return The forced attributes list.
     */
    public List<Attribute> getForcedAttributes() {
        return forcedAttributes;
    }

    /**
     * Checks if the {@link #validAttributes} contains the attribute parameter.
     *
     * @param attribute The attribute to check.
     * @return If the {@link #validAttributes} list contains the attribute.
     */
    public boolean isValid(Attribute attribute) {
        return validAttributes.contains(attribute);
    }
}
