package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a column.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class ColumnBuilder {

    /**
     * The column's data type.
     */
    private DataType dataType;
    /**
     * The columns' name.
     */
    private String name;
    /**
     * The column's attributes. An ArrayList of {@link Attribute}.
     */
    private List<Attribute> attributes = new ArrayList<>();
    /**
     * The column's attributes. An ArrayList of String. If an attribute is not in {@link Attribute}, this store an array
     * of raw attribute names.
     */
    private List<String> attributeNames = new ArrayList<>();
    /**
     * The parameter list.
     */
    private List<String> parameters;
    /**
     * The column's default value.
     */
    private Object defaultValue;

    /**
     * Builds a column using a data type from {@link DataType} and attributes from {@link Attribute}.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param attributes The column's attributes.
     */
    public ColumnBuilder(String name, DataType dataType, Attribute... attributes) {
        this(name, dataType);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a data type from {@link DataType}.
     *
     * @param name     The column's name.
     * @param dataType The column's data type.
     */
    public ColumnBuilder(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
        this.parameters = new ArrayList<>();
    }

    /**
     * Builds a column using a data type from {@link DataType} and a length.
     *
     * @param name     The column's name.
     * @param dataType The column's data type.
     * @param length   The column's length.
     */
    public ColumnBuilder(String name, DataType dataType, int length) {
        this(name, dataType);
        withLength(length);
    }

    /**
     * Builds a column using a data type from {@link DataType}, attributes from {@link Attribute} and a length.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param length     The column's length.
     * @param attributes The column's attributes.
     */
    public ColumnBuilder(String name, DataType dataType, int length, Attribute... attributes) {
        this(name, dataType);
        withLength(length);
        withAttributes(attributes);
    }

    /**
     * Adds attributes to the column.
     *
     * @param attributes Array of attributes.
     *
     * @return The current object instance.
     */
    public ColumnBuilder withAttributes(Attribute... attributes) {
        Arrays.stream(attributes).filter(attribute -> !this.attributes.contains(attribute)).forEach(attribute -> this.attributes.add(attribute));
        return this;
    }

    /**
     * Sets the length of the column. Eg: {@code VARCHAR(20)} - the length is "20".
     *
     * @param length The column's value.
     *
     * @return The current object instance.
     */
    public ColumnBuilder withLength(int length) {
        if (length != -1) withParameter(String.valueOf(length));
        return this;
    }

    /**
     * Adds parameter to the column.
     *
     * @param parameter The parameter
     *
     * @return The current object instance.
     */
    public ColumnBuilder withParameter(String parameter) {
        this.parameters.add(parameter);
        return this;
    }

    /**
     * Sets the default column value.
     *
     * @param defaultValue The default column's value.
     * @param asString     Case the default value is a String and this parameter true, put the value between {@code '}
     *                     (e.g. {@code 'hello'}), otherwise as plain text.
     *
     * @return The current object instance.
     */
    public ColumnBuilder withDefaultValue(Object defaultValue, boolean asString) {
        if (defaultValue instanceof String && asString) this.defaultValue = "'" + defaultValue + "'";
        else this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Sets the default column value. If the default value is a String put the value between {@code '}, to use as plain
     * text use {@link #withDefaultValue(Object, boolean)}.
     *
     * @param defaultValue The default column's value.
     *
     * @return The current object instance.
     */
    public ColumnBuilder withDefaultValue(Object defaultValue) {
        withDefaultValue(defaultValue, true);
        return this;
    }

    /**
     * Gets the parameter list.
     *
     * @return The parameter list.
     */
    public List<String> getParameters() {
        return parameters;
    }

    /**
     * Gets the column's name.
     *
     * @return The column's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the column's data type.
     *
     * @return The column's data type.
     */
    public DataType getDataType() {
        return this.dataType;
    }

    /**
     * Gets the column data type name.
     *
     * @param sql The current EzSQL connection.
     *
     * @return The data type converted to String.
     */
    public String dataTypeToString(EzSQL sql) {
        return sql.build(this.dataType);
    }

    /**
     * Gets the column length converted to string or a empty string if the length is null.
     *
     * @return The length converted to String or a empty String if the length is null.
     */
    public String parametersToString() {
        if (parameters.size() > 0) {
            return String.format("(%s)", String.join(", ", parameters));
        } else {
            if (dataType.hasParameters() && dataType.getDefaultParameters() != null)
                return String.format("(%s)", dataType.getDefaultParameters());

            return "";
        }
    }

    /**
     * Gets the column attributes converted to string. Join the {@link #attributes} and the {@link #attributeNames}
     *
     * @param sql The current EzSQL connection.
     *
     * @return The attributes converted to String.
     */
    public String attributesToString(EzSQL sql) {
        return String.format("%s %s", this.attributes.stream().map(sql::build)
                .collect(Collectors.joining(" ")), String.join(" ", this.attributeNames)).trim();
    }

    /**
     * Gets the default value converted to string. Return the "DEFAULT VALUE " followed of the default value. If the
     * default value is null return a empty string.
     *
     * @return The default value converted to String.
     */
    public String defaultValueToString() {
        return defaultValue != null ? String.format("DEFAULT %s", defaultValue) : "";
    }

    /**
     * Converts the column to SQL.
     *
     * @param sql The current EzSQL connection.
     *
     * @return The column converted to SQL query.
     */
    public String toSQL(EzSQL sql) {
        // name data_type len? attributes? ('DEFAULT' default_value)?
        return String.format("%s %s%s %s %s",
                this.name,
                dataTypeToString(sql),
                parametersToString(),
                attributesToString(sql),
                defaultValueToString()
        ).replaceAll("\\s+", " ").trim();
    }

    /**
     * Gets the attribute list.
     *
     * @return The attribute list.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets the attribute name list.
     *
     * @return The attribute name list.
     */
    public List<String> getAttributeNames() {
        return attributeNames;
    }

    /**
     * Gets the default value.
     *
     * @return The default value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

}
