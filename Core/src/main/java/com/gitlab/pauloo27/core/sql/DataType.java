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

    private final String sql;
    private final List<Attribute> validAttributes;
    private final List<Attribute> forcedAttributes;
    private final String customName;

    public DataType(String sql, List<Attribute> validAttributes) {
        this(sql, validAttributes, null, null);
    }

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
