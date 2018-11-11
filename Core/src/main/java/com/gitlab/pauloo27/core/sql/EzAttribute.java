package com.gitlab.pauloo27.core.sql;

/**
 * SQL Attributes.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public enum EzAttribute {
    // Numbers
    /**
     * Unsigned integer.
     */
    UNSIGNED,
    /**
     * Fill with zeroes.
     */
    ZEROFILL,
    /**
     * Auto increment an integer value. Not valid in case of PostgreSQL.
     */
    AUTO_INCREMENT,
    /**
     * Primary key integer.
     */
    PRIMARY_KEY,
    // All
    /**
     * Not null value.
     */
    NOT_NULL,
    /**
     * Nullable value.
     */
    NULL,
    /**
     * Unique value.
     */
    UNIQUE;

    /**
     * Converts the statement to SQL.
     *
     * @param type The type of the SQL.
     * @return The attribute converted to SQL query.
     */
    public String toSQL(EzSQLType type) {
        if (type.isMySQLLike()) return toMySQL();
        if (type == EzSQLType.POSTGRESQL)
            return toPostgreSQL();
        if (type == EzSQLType.SQLITE)
            return toSQLite();
        else
            return null;
    }

    /**
     * Converts the statement to MySQL.
     *
     * @return The enum converted to MySQL query.
     */
    private String toMySQL() {
        if (this == NOT_NULL) return "NOT NULL";
        if (this == PRIMARY_KEY) return "PRIMARY KEY";
        return this.name();
    }

    /**
     * Converts the statement to SQLite.
     *
     * @return The enum converted to SQLite query.
     */
    private String toSQLite() {
        if (this == NOT_NULL) return "NOT NULL";
        if (this == PRIMARY_KEY) return "PRIMARY KEY";
        if (this == AUTO_INCREMENT) return "AUTOINCREMENT";
        return this.name();
    }

    /**
     * Converts the statement to PostgreSQL.
     *
     * @return The enum converted to PostgreSQL query.
     */
    private String toPostgreSQL() {
        if (this == NOT_NULL) return "NOT NULL";
        if (this == PRIMARY_KEY) return "PRIMARY KEY";
        if (this == AUTO_INCREMENT) return "";
        return this.name();
    }

}
