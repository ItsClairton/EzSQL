package com.gitlab.pauloo27.core.sql;

import java.lang.reflect.Field;

/**
 * Commons reflection methods used in the project.
 */
class ReflectionUtils {

    /**
     * Gets the column name by a field.
     *
     * @param nameConverter The name converter policy implementation.
     * @param field         The field.
     *
     * @return The value of the {@link Name} annotation or the field name if it's absent. The result will be converted
     * by the name converter if it's not null.
     */
    public static String getName(NameConverter nameConverter, Field field) {
        String defaultName = field.getName();
        if (field.isAnnotationPresent(Name.class))
            defaultName = field.getAnnotation(Name.class).value();

        if (nameConverter == null)
            return defaultName;
        else
            return nameConverter.toColumnName(defaultName);
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
     * @return If the {@link Id} annotation is present or the field name is equals {@code id}.
     */
    public static boolean isId(Field field) {
        return field.isAnnotationPresent(Id.class) || field.getName().equalsIgnoreCase("id");
    }

    /**
     * Gets the table name by a field.
     *
     * @param nameConverter The name converter policy implementation.
     * @param clazz         The class.
     * @param <T>           The class type.
     *
     * @return The value of the {@link Name} annotation or the class name if it's absent. The result will be converted
     * by the name converter if it's not null.
     */
    public static <T> String getName(NameConverter nameConverter, Class<T> clazz) {
        String defaultName = clazz.getSimpleName();
        if (clazz.isAnnotationPresent(Name.class))
            defaultName = clazz.getAnnotation(Name.class).value();

        if (nameConverter == null)
            return defaultName;
        else
            return nameConverter.toTableName(defaultName);

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
