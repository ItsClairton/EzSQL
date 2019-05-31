package com.gitlab.pauloo27.core.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultNameConverter implements NameConverter {

    @Override
    public String toTableName(String className) {
        return toSnakeCase(className);
    }

    @Override
    public String toColumnName(String fieldName) {
        return toSnakeCase(fieldName);
    }

    private static String toSnakeCase(String string) {
        if (string == null)
            return null;

        Pattern pattern = Pattern.compile("[A-Z][a-z]*|[a-z]+");
        Matcher matcher = pattern.matcher(string);

        StringBuilder convertedString = new StringBuilder();
        boolean first = true;

        while (matcher.find()) {
            String group = matcher.group();

            if (first) {
                first = false;
            } else {
                convertedString.append("_");
            }

            convertedString.append(group.toLowerCase());
        }

        return convertedString.toString();
    }
}
