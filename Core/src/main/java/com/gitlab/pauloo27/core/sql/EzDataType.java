package com.gitlab.pauloo27.core.sql;

/**
 * SQL data types.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public enum EzDataType {
    // JDBC standart types
    BIT,
    TINYINT,
    SMALLINT,
    INTEGER,
    BIGINT,
    FLOAT,
    REAL,
    DOUBLE,
    NUMERIC,
    DECIMAL,
    CHAR,
    VARCHAR,
    LONGVARCHAR,
    DATE,
    TIME,
    TIMESTAMP,
    BINARY,
    VARBINARY,
    LONGVARBINARY,
    NULL,
    OTHER,
    JAVA_OBJECT,
    DISTINCT,
    STRUCT,
    ARRAY,
    BLOB,
    CLOB,
    REF,
    DATALINK,
    BOOLEAN,
    ROWID,
    NCHAR,
    NVARCHAR,
    LONGNVARCHAR,
    NCLOB,
    SQLXML,
    REF_CURSOR,
    TIME_WITH_TIMEZONE,
    TIMESTAMP_WITH_TIMEZONE,
    // POSTGRESQL
    SERIAL,
    BIGSERIAL,
    INTERVAL,
    // CUSTOM TYPES
    PRIMARY_KEY;

    /**
     * Converts the data type to MySQL.
     *
     * @return The enum converted to MySQL.
     */
    private String toMySQL() {
        if (this == PRIMARY_KEY)
            return "INTEGER";
        else
            return this.name();
    }

    /**
     * Converts the data type to SQLite.
     *
     * @return The enum converted to SQLite.
     */
    private String toSQLite() {
        if (this == PRIMARY_KEY)
            return "INTEGER";
        else
            return this.name();
    }

    /**
     * Converts the data type to PostgreSQL.
     *
     * @return The enum converted to PostgreSQL.
     */
    private String toPostgreSQL() {
        if (this == PRIMARY_KEY)
            return "SERIAL";
        else
            return this.name();
    }

    /**
     * Gets the data type converted to SQL.
     *
     * @param type The type of the SQL.
     * @return The data type converted to SQL query.
     */
    public String toSQL(EzSQLType type) {
        if (type.isMySQLLike())
            return toMySQL();
        if (type == EzSQLType.POSTGRESQL)
            return toPostgreSQL();
        if (type == EzSQLType.SQLITE)
            return toSQLite();
        else
            return null;
    }
}
