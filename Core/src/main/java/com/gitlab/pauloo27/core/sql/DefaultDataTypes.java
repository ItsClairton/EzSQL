package com.gitlab.pauloo27.core.sql;

import java.util.Arrays;

/**
 * SQL data types.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class DefaultDataTypes {
    public final static DataType BIT = new DataType("BIT");
    public final static DataType TINYINT = new DataType("TINYINT");
    public final static DataType SMALLINT = new DataType("SMALLINT");
    public final static DataType INTEGER = new DataType("INTEGER");
    public final static DataType BIGINT = new DataType("BIGINT");
    public final static DataType FLOAT = new DataType("FLOAT");
    public final static DataType REAL = new DataType("REAL");
    public final static DataType DOUBLE = new DataType("DOUBLE");
    public final static DataType NUMERIC = new DataType("NUMERIC");
    public final static DataType DECIMAL = new DataType("DECIMAL");
    public final static DataType CHAR = new DataType("CHAR");
    public final static DataType VARCHAR = new DataType("VARCHAR", "64");
    public final static DataType TEXT = new DataType("TEXT");
    public final static DataType DATE = new DataType("DATE");
    public final static DataType TIME = new DataType("TIME");
    public final static DataType TIMESTAMP = new DataType("TIMESTAMP");
    public final static DataType BOOLEAN = new DataType("BOOLEAN");

    // custom wrappers
    public final static DataType PRIMARY_KEY = new DataType("INTEGER", false, null,
            Arrays.asList(DefaultAttributes.PRIMARY_KEY, DefaultAttributes.AUTO_INCREMENT), "PRIMARY_KEY");

}
