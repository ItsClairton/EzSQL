package com.gitlab.pauloo27.core.sql;

import java.util.Arrays;

public class DefaultDataTypes {
    public final static DataType BIT = new DataType("BIT", DefaultAttributes.getNumberAttributes());
    public final static DataType TINYINT = new DataType("TINYINT", DefaultAttributes.getNumberAttributes());
    public final static DataType SMALLINT = new DataType("SMALLINT", DefaultAttributes.getNumberAttributes());
    public final static DataType INTEGER = new DataType("INTEGER", DefaultAttributes.getNumberAttributes());
    public final static DataType BIGINT = new DataType("BIGINT", DefaultAttributes.getNumberAttributes());
    public final static DataType FLOAT = new DataType("FLOAT", DefaultAttributes.getNumberAttributes());
    public final static DataType REAL = new DataType("REAL", DefaultAttributes.getNumberAttributes());
    public final static DataType DOUBLE = new DataType("DOUBLE", DefaultAttributes.getNumberAttributes());
    public final static DataType NUMERIC = new DataType("NUMERIC", DefaultAttributes.getNumberAttributes());
    public final static DataType DECIMAL = new DataType("DECIMAL", DefaultAttributes.getNumberAttributes());
    public final static DataType CHAR = new DataType("CHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType VARCHAR = new DataType("VARCHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType LONGVARCHAR = new DataType("LONGVARCHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType DATE = new DataType("DATE", DefaultAttributes.getDefaultAttributes());
    public final static DataType TIME = new DataType("TIME", DefaultAttributes.getDefaultAttributes());
    public final static DataType TIMESTAMP = new DataType("TIMESTAMP", DefaultAttributes.getDefaultAttributes());
    public final static DataType BINARY = new DataType("BINARY", DefaultAttributes.getDefaultAttributes());
    public final static DataType VARBINARY = new DataType("VARBINARY", DefaultAttributes.getDefaultAttributes());
    public final static DataType LONGVARBINARY = new DataType("LONGVARBINARY", DefaultAttributes.getDefaultAttributes());
    public final static DataType NULL = new DataType("NULL", DefaultAttributes.getDefaultAttributes());
    public final static DataType OTHER = new DataType("OTHER", DefaultAttributes.getDefaultAttributes());
    public final static DataType JAVA_OBJECT = new DataType("JAVA_OBJECT", DefaultAttributes.getDefaultAttributes());
    public final static DataType DISTINCT = new DataType("DISTINCT", DefaultAttributes.getDefaultAttributes());
    public final static DataType STRUCT = new DataType("STRUCT", DefaultAttributes.getDefaultAttributes());
    public final static DataType ARRAY = new DataType("ARRAY", DefaultAttributes.getDefaultAttributes());
    public final static DataType BLOB = new DataType("BLOB", DefaultAttributes.getDefaultAttributes());
    public final static DataType CLOB = new DataType("CLOB", DefaultAttributes.getDefaultAttributes());
    public final static DataType REF = new DataType("REF", DefaultAttributes.getDefaultAttributes());
    public final static DataType DATALINK = new DataType("DATALINK", DefaultAttributes.getDefaultAttributes());
    public final static DataType BOOLEAN = new DataType("BOOLEAN", DefaultAttributes.getDefaultAttributes());
    public final static DataType ROWID = new DataType("ROWID", DefaultAttributes.getDefaultAttributes());
    public final static DataType NCHAR = new DataType("NCHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType NVARCHAR = new DataType("NVARCHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType LONGNVARCHAR = new DataType("LONGNVARCHAR", DefaultAttributes.getDefaultAttributes());
    public final static DataType NCLOB = new DataType("NCLOB", DefaultAttributes.getDefaultAttributes());
    public final static DataType SQLXML = new DataType("SQLXML", DefaultAttributes.getDefaultAttributes());
    public final static DataType REF_CURSOR = new DataType("REF_CURSOR", DefaultAttributes.getDefaultAttributes());
    public final static DataType TIME_WITH_TIMEZONE = new DataType("TIME_WITH_TIMEZONE", DefaultAttributes.getDefaultAttributes());
    public final static DataType TIMESTAMP_WITH_TIMEZONE = new DataType("TIMESTAMP_WITH_TIMEZONE", DefaultAttributes.getDefaultAttributes());
    // custom wrappers
    public final static DataType PRIMARY_KEY = new DataType("INTEGER", DefaultAttributes.getNumberAttributes(),
            Arrays.asList(DefaultAttributes.PRIMARY_KEY, DefaultAttributes.AUTO_INCREMENT), "PRIMARY_KEY");

}
