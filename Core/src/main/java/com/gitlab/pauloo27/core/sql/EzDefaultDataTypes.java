package com.gitlab.pauloo27.core.sql;

import java.util.Arrays;

public class EzDefaultDataTypes {
    public final static EzDataType BIT = new EzDataType("BIT", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType TINYINT = new EzDataType("TINYINT", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType SMALLINT = new EzDataType("SMALLINT", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType INTEGER = new EzDataType("INTEGER", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType BIGINT = new EzDataType("BIGINT", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType FLOAT = new EzDataType("FLOAT", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType REAL = new EzDataType("REAL", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType DOUBLE = new EzDataType("DOUBLE", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType NUMERIC = new EzDataType("NUMERIC", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType DECIMAL = new EzDataType("DECIMAL", EzDefaultAttributes.getNumberAttributes());
    public final static EzDataType CHAR = new EzDataType("CHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType VARCHAR = new EzDataType("VARCHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType LONGVARCHAR = new EzDataType("LONGVARCHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType DATE = new EzDataType("DATE", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType TIME = new EzDataType("TIME", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType TIMESTAMP = new EzDataType("TIMESTAMP", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType BINARY = new EzDataType("BINARY", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType VARBINARY = new EzDataType("VARBINARY", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType LONGVARBINARY = new EzDataType("LONGVARBINARY", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType NULL = new EzDataType("NULL", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType OTHER = new EzDataType("OTHER", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType JAVA_OBJECT = new EzDataType("JAVA_OBJECT", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType DISTINCT = new EzDataType("DISTINCT", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType STRUCT = new EzDataType("STRUCT", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType ARRAY = new EzDataType("ARRAY", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType BLOB = new EzDataType("BLOB", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType CLOB = new EzDataType("CLOB", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType REF = new EzDataType("REF", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType DATALINK = new EzDataType("DATALINK", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType BOOLEAN = new EzDataType("BOOLEAN", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType ROWID = new EzDataType("ROWID", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType NCHAR = new EzDataType("NCHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType NVARCHAR = new EzDataType("NVARCHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType LONGNVARCHAR = new EzDataType("LONGNVARCHAR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType NCLOB = new EzDataType("NCLOB", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType SQLXML = new EzDataType("SQLXML", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType REF_CURSOR = new EzDataType("REF_CURSOR", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType TIME_WITH_TIMEZONE = new EzDataType("TIME_WITH_TIMEZONE", EzDefaultAttributes.getDefaultAttributes());
    public final static EzDataType TIMESTAMP_WITH_TIMEZONE = new EzDataType("TIMESTAMP_WITH_TIMEZONE", EzDefaultAttributes.getDefaultAttributes());
    // custom wrappers
    public final static EzDataType PRIMARY_KEY = new EzDataType("INTEGER", EzDefaultAttributes.getNumberAttributes(),
            Arrays.asList(EzDefaultAttributes.PRIMARY_KEY, EzDefaultAttributes.AUTO_INCREMENT), "PRIMARY_KEY");

}
