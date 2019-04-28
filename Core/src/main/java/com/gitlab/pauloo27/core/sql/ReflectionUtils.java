package com.gitlab.pauloo27.core.sql;

import java.lang.reflect.Field;

/**
 * Commons reflection methods used in the project.
 */
class ReflectionUtils {

    /**
     * Gets the column name by a field.
     *
     * @param field The field.
     *
     * @return The value of the {@link Name} annotation or the field name if it's absent.
     */
    public static String getName(Field field) {
        if (field.isAnnotationPresent(Name.class))
            return field.getAnnotation(Name.class).value();

        return field.getName();
    }

    /**
     * Checks if the the field should be ignored.
     *
     * @param field The field.
     *
     * @return If the {@link Ignore} annotation is present.
     */
    public static boolean isIgnored(Field field) {
        return field.isAnnotationPresent(Ignore.class);
    }

    /**
     * Checks if the the field should be the ID.
     *
     * @param field The field.
     *
     * @return If the {@link Id} annotation is present.
     */
    public static boolean isId(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    /**
     * Gets the table name by a field.
     *
     * @param clazz The class.
     * @param <T>   The class type.
     *
     * @return The value of the {@link Name} annotation or the class name if it's absent.
     */
    public static <T> String getName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Name.class))
            return clazz.getAnnotation(Name.class).value();

        return clazz.getSimpleName();
    }

    /**
     * Checks if the the field should has a length.
     *
     * @param field The field.
     *
     * @return If the {@link Length} annotation is present.
     */
    public static boolean hasLength(Field field) {
        return field.isAnnotationPresent(Length.class);
    }

    /**
     * Gets column length.
     *
     * @param field The field.
     *
     * @return The value of the {@link Length} annotation or {@code -1} if it's absent.
     */
    public static int getLength(Field field) {
        if (!hasLength(field))
            return -1;

        return field.getAnnotation(Length.class).value();
    }
}
