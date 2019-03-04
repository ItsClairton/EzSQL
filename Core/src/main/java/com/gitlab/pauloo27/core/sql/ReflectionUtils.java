package com.gitlab.pauloo27.core.sql;

import java.lang.reflect.Field;

class ReflectionUtils {

    public static String getName(Field field) {
        if (field.isAnnotationPresent(Name.class))
            return field.getAnnotation(Name.class).value();

        return field.getName();
    }

    public static boolean isIgnored(Field field) {
        return field.isAnnotationPresent(Ignore.class);
    }

    public static boolean isId(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    public static <T> String getName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Name.class))
            return clazz.getAnnotation(Name.class).value();

        return clazz.getSimpleName();
    }

    public static boolean hasLength(Field field) {
        return field.isAnnotationPresent(Length.class);
    }

    public static int getLength(Field field) {
        if (!hasLength(field))
            return -1;

        return field.getAnnotation(Length.class).value();
    }
}
