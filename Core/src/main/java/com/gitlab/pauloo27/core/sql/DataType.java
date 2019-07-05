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
     * A list of the forced attributes of this type.
     */
    private final List<Attribute> forcedAttributes;
    /**
     * If the data type have parameters ({@code (parameters)}), like length.
     */
    private final boolean hasParameters;
    /**
     * The default parameters.
     */
    private final String defaultParameters;
    /**
     * An optional type custom name.
     *
     * @see #getCustomName() if you want to know more about the custom name.
     */
    private final String customName;

    /**
     * Builds a data type.
     *
     * @param sql The type in SQL.
     */
    public DataType(String sql) {
        this(sql, false, null, null, null);
    }

    /**
     * Builds a data type that has parameters.
     *
     * @param sql               The type in SQL.
     * @param defaultParameters The default parameters.
     */
    public DataType(String sql, String defaultParameters) {
        this(sql, true, defaultParameters, null, null);
    }

    /**
     * Builds a data type.
     *
     * @param sql               The type in SQL.
     * @param hasParameters     If the type has parameters.
     * @param defaultParameters The default parameters.
     * @param forcedAttributes  A list of forced attributes.
     * @param customName        A custom name.
     */
    public DataType(String sql, boolean hasParameters, String defaultParameters, List<Attribute> forcedAttributes, String customName) {
        this.sql = sql;
        this.hasParameters = hasParameters;
        this.defaultParameters = defaultParameters;
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
     *
     * @see #getCustomName() if you want to know more about the custom name.
     */
    public boolean hasCustomName() {
        return customName != null;
    }

    /**
     * Gets the custom name. The custom name is the name of the custom data type.
     * <p>
     * Using the wrapper {@link DefaultDataTypes#PRIMARY_KEY} as example, the type name is {@code INTEGER} because it's
     * a {@code INTEGER} value while the custom name is {@code PRIMARY_KEY}. The custom name is used to the build to
     * check if the type is a real {@code INTEGER} or a {@code PRIMARY_KEY} wrapped to {@code INTEGER}.
     *
     * @return The custom name.
     */
    public String getCustomName() {
        return customName;
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
     * Checks if the data type has parameters.
     * @return If the data type has parameters.
     */
    public boolean hasParameters() {
        return hasParameters;
    }

    /**
     * Gets the default parameters.
     * @return The default parameters.
     */
    public String getDefaultParameters() {
        return defaultParameters;
    }
}
