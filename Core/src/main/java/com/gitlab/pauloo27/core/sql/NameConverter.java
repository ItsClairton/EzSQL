package com.gitlab.pauloo27.core.sql;

public interface NameConverter {

    String convertTableName(String className);

    String convertColumnName(String fieldName);
}
