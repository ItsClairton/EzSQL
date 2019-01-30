package com.gitlab.pauloo27.core.sql;

/**
 * SQL Attributes.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public class EzAttribute {

    private final String name;

    public EzAttribute(String name) {
        this.name = name;
    }

    public String toSQL() {
        return name;
    }

}
