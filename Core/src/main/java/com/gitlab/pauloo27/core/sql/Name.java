package com.gitlab.pauloo27.core.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the table or the column name if the shouldn't be the same as the field or class name.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Name {
    String value();
}
