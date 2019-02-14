package com.gitlab.pauloo27.core.sql;

/**
 * SQL Attribute.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class Attribute {

    /**
     * The attribute name, used to build the SQL.
     */
    private final String name;

    /**
     * Builds an attribute.
     *
     * @param name The attribute name.
     */
    public Attribute(String name) {
        this.name = name;
    }

    /**
     * Converts the attribute to SQL.
     *
     * @return The attribute name.
     */
    public String toSQL() {
        return name;
    }

}
