package com.gitlab.pauloo27.core.sql;

/**
 * Builds a database.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public class EzDatabaseBuilder {

    /**
     * The database's name.
     */
    private String name;

    /**
     * Builds a database.
     *
     * @param name The database's name.
     */
    public EzDatabaseBuilder(String name) {
        this.name = name;
    }

    /**
     * Gets the database's name.
     *
     * @return The database's name.
     */
    public String getName() {
        return name;
    }

}
