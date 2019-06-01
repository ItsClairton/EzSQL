package com.gitlab.pauloo27.core.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default table and column name converter.
 * <p>
 * Converts from camel case to snake case.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class DefaultNameConverter implements NameConverter {

    /**
     * Converts a String from camel case to snake case.
     *
     * @param string The String to convert.
     *
     * @return The String in snake case.
     */
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

    @Override
    public String toTableName(String className) {
        return toSnakeCase(className);
    }

    @Override
    public String toColumnName(String fieldName) {
        return toSnakeCase(fieldName);
    }
}
