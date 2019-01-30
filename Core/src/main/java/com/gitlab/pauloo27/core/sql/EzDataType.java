package com.gitlab.pauloo27.core.sql;

import java.util.List;

/**
 * SQL data types.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class EzDataType {

    private final String sql;
    private final List<EzAttribute> validAttributes;
    private final List<EzAttribute> forcedAttributes;
    private final String customName;

    public EzDataType(String sql, List<EzAttribute> validAttributes) {
        this(sql, validAttributes, null, null);
    }

    public EzDataType(String sql, List<EzAttribute> validAttributes, List<EzAttribute> forcedAttributes, String customName) {
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

    public List<EzAttribute> getValidAttributes() {
        return validAttributes;
    }

    public List<EzAttribute> getForcedAttributes() {
        return forcedAttributes;
    }

    public boolean isValid(EzAttribute attribute) {
        return validAttributes.contains(attribute);
    }
}
