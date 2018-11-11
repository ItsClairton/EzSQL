package com.gitlab.pauloo27.core.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported SQL types.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public enum EzSQLType {
    /**
     * SQLite type.
     */
    SQLITE,
    /**
     * MySQL type.
     */
    MYSQL,
    /**
     * MariaDB type.
     */
    MARIADB,
    /**
     * PostgreSQL type.
     */
    POSTGRESQL;

    /**
     * Map with the url bases.
     */
    private static Map<EzSQLType, String> urlBase = new HashMap<>();
    /**
     * Map with driver class path.
     */
    private static Map<EzSQLType, String> driver = new HashMap<>();
    /**
     * Map with the default ports.
     */
    private static Map<EzSQLType, Integer> ports = new HashMap<>();

    static {
        urlBase.put(SQLITE, "jdbc:sqlite:");
        urlBase.put(MYSQL, "jdbc:mysql://");
        urlBase.put(MARIADB, "jdbc:mariadb://");
        urlBase.put(POSTGRESQL, "jdbc:postgresql://");

        driver.put(SQLITE, "org.sqlite.JDBC");
        driver.put(MYSQL, "com.mysql.cj.jdbc.Driver");
        driver.put(MARIADB, "org.mariadb.jdbc.Driver");
        driver.put(POSTGRESQL, "org.postgresql.Driver");

        ports.put(SQLITE, null);
        ports.put(MYSQL, 3306);
        ports.put(MARIADB, 3306);
        ports.put(POSTGRESQL, 5432);
    }

    /**
     * Checks if the type is not SQLite.
     *
     * @return If the type is not SQLite.
     */
    public boolean isServer() {
        return this != SQLITE;
    }

    /**
     * Checks if the type is MariaDB or MySQL.
     *
     * @return If the type is MariaDB or MySQL.
     */
    public boolean isMySQLLike() {
        return this == MYSQL || this == MARIADB;
    }

    /**
     * Gets the URL base.
     *
     * @return The SQL type's URL base.
     */
    public String getURLBase() {
        return urlBase.get(this);
    }

    /**
     * Gets the driver class path.
     *
     * @return The SQL type's driver class path.
     */
    public String getDriverClass() {
        return driver.get(this);
    }

    /**
     * Gets the default port.
     *
     * @return The SQL type's default port.
     */
    public int getPort() {
        return ports.get(this);
    }
}
