package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a column.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzColumnBuilder {

    /**
     * The column's data type.
     */
    private EzDataType dataType;
    /**
     * The columns' name.
     */
    private String name;
    /**
     * The column's attributes. An ArrayList of {@link EzAttribute}.
     */
    private List<EzAttribute> attributes = new ArrayList<>();
    /**
     * The column's attributes. An ArrayList of String. If an attribute is not in {@link EzAttribute}, this store an
     * array of raw attribute names.
     */
    private List<String> attributeNames = new ArrayList<>();
    /**
     * The max column value length. Eg: {@code VARCHAR(20)} - 20 is the length.
     */
    private Integer length;
    /**
     * The column's default value.
     */
    private Object defaultValue;
    /**
     * The columns data type raw name. If the data type is not in {@link EzDataType}, this store the raw data type
     * name.
     */
    private String dataTypeName;

    /**
     * Builds a column using a data type from {@link EzDataType} and attributes from {@link EzAttribute}.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param attributes The column's attributes.
     */
    public EzColumnBuilder(String name, EzDataType dataType, EzAttribute... attributes) {
        this(name, dataType);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a data type from {@link EzDataType} and attributes from raw String.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param attributes The column's attributes.
     */
    public EzColumnBuilder(String name, EzDataType dataType, String... attributes) {
        this(name, dataType);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a data type from {@link EzDataType}.
     *
     * @param name     The column's name.
     * @param dataType The column's data type.
     * @see #EzColumnBuilder(String, String) Builds using a String as data type.
     */
    public EzColumnBuilder(String name, EzDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * Builds a column using a data type from {@link EzDataType} and a length.
     *
     * @param name     The column's name.
     * @param dataType The column's data type.
     * @param length   The column's length.
     */
    public EzColumnBuilder(String name, EzDataType dataType, int length) {
        this(name, dataType);
        withLength(length);
    }

    /**
     * Builds a column using a data type from {@link EzDataType}, attributes from {@link EzAttribute} and a length.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param length     The column's length.
     * @param attributes The column's attributes.
     */
    public EzColumnBuilder(String name, EzDataType dataType, int length, EzAttribute... attributes) {
        this(name, dataType);
        withLength(length);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a data type from {@link EzDataType}, attributes from a raw String and a length.
     *
     * @param name       The column's name.
     * @param dataType   The column's data type.
     * @param length     The column's length.
     * @param attributes The column's attributes.
     */
    public EzColumnBuilder(String name, EzDataType dataType, int length, String... attributes) {
        this(name, dataType);
        withLength(length);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a String with the raw data type name.
     *
     * @param name         The column's name.
     * @param dataTypeName The column's data type.
     */
    public EzColumnBuilder(String name, String dataTypeName) {
        this.name = name;
        this.dataTypeName = dataTypeName;
    }

    /**
     * Builds a column using a String with the raw data type name and a length.
     *
     * @param name         The column's name.
     * @param dataTypeName The column's data type.
     * @param length       The column's length.
     */
    public EzColumnBuilder(String name, String dataTypeName, int length) {
        this(name, dataTypeName);
        withLength(length);
    }

    /**
     * Builds a column using a String with the raw data type name, a length and attributes from {@link EzAttribute}.
     *
     * @param name         The column's name.
     * @param dataTypeName The column's data type.
     * @param length       The column's length.
     * @param attributes   The column's attributes.
     */
    public EzColumnBuilder(String name, String dataTypeName, int length, EzAttribute... attributes) {
        this(name, dataTypeName);
        withLength(length);
        withAttributes(attributes);
    }

    /**
     * Builds a column using a String with the raw data type name, a length and attributes from a raw String.
     *
     * @param name         The column's name.
     * @param dataTypeName The column's data type.
     * @param length       The column's length.
     * @param attributes   The column's attributes.
     */
    public EzColumnBuilder(String name, String dataTypeName, int length, String... attributes) {
        this(name, dataTypeName);
        withLength(length);
        withAttributes(attributes);
    }

    /**
     * Adds attributes to the column. If the attribute isn't in the Enum, use {@link #withAttributes(String...)}
     *
     * @param attributes Array of attributes.
     * @return The current object instance.
     */
    public EzColumnBuilder withAttributes(EzAttribute... attributes) {
        Arrays.stream(attributes).filter(attribute -> !this.attributes.contains(attribute)).forEach(attribute -> this.attributes.add(attribute));
        return this;
    }

    /**
     * Adds attributes to the column.
     *
     * @param attributes Array of attributes.
     * @return The current object instance.
     */
    public EzColumnBuilder withAttributes(String... attributes) {
        Arrays.stream(attributes).filter(attribute -> !this.attributeNames.contains(attribute)).forEach(attribute -> this.attributeNames.add(attribute));
        return this;
    }

    /**
     * Sets the length of the column. Eg: {@code VARCHAR(20)} - the length is "20".
     *
     * @param length The column's value.
     * @return The current object instance.
     */
    public EzColumnBuilder withLength(int length) {
        this.length = length;
        return this;
    }

    /**
     * Sets the default column value.
     *
     * @param defaultValue The default column's value.
     * @param asString     Case the default value is a String and this paramater true, put the value between
     *                     {@code '} (e.g. {@code 'hello'}), otherwise as plain text.
     * @return The current object instance.
     */
    public EzColumnBuilder withDefaultValue(Object defaultValue, boolean asString) {
        if (defaultValue instanceof String && asString) this.defaultValue = "'" + defaultValue + "'";
        else this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Sets the default column value. If the default value is a String put the value between {@code '}, to use as plain
     * text use {@link #withDefaultValue(Object, boolean)}.
     *
     * @param defaultValue The default column's value.
     * @return The current object instance.
     */
    public EzColumnBuilder withDefaultValue(Object defaultValue) {
        withDefaultValue(defaultValue, true);
        return this;
    }

    /**
     * Gets the  column's length. Eg: {@code VARCHAR(20)} - the length is "20".
     *
     * @return The length.
     */

    public Integer getLength() {
        return length;
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
    public EzDataType getDataType() {
        return this.dataType;
    }

    /**
     * Gets the column data type name. Return {@link #dataType} if not null and otherwise {@link #dataTypeName}.
     *
     * @param type The type of the SQL.
     * @return The data type converted to String.
     */

    public String dataTypeToString(EzSQLType type) {
        return (this.dataType == null ? dataTypeName : this.dataType.toSQL(type));
    }

    /**
     * Gets the column length converted to string or a empty string if the length is null.
     *
     * @return The length converted to String or a empty String if the length is null.
     */

    public String lengthToString() {
        return length != null ? String.format("(%d)", length) : "";
    }

    /**
     * Gets the column attributes converted to string. Join the {@link #attributes} and the {@link #attributeNames}
     *
     * @param type The type of the SQL.
     * @return The attributes converted to String.
     */

    public String attributesToString(EzSQLType type) {
        return String.format("%s %s", this.attributes.stream().map(attribute -> attribute.toSQL(type))
                .collect(Collectors.joining(" ")), this.attributeNames.stream().collect(Collectors.joining(" "))).trim();
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
     * @param type The type of the SQL.
     * @return The column converted to SQL query.
     */

    public String toSQL(EzSQLType type) {
        // name data_type len? attributes? ('DEFAULT' default_value)?
        return String.format("%s %s %s %s %s",
                this.name,
                dataTypeToString(type),
                lengthToString(),
                attributesToString(type),
                defaultValueToString()
        ).replaceAll("\\s+", " ").trim();
    }

}