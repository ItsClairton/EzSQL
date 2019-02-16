package com.gitlab.pauloo27.core.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;

/**
 * SQL attributes.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class DefaultAttributes {

    private static Map<Object, Attribute> attributeByAnnotation = new HashMap<>();

    public static Attribute getAttribute(Object object) {
        if (attributeByAnnotation.isEmpty()) {
            attributeByAnnotation.put(Unique.class, DefaultAttributes.UNIQUE);
            attributeByAnnotation.put(NotNull.class, DefaultAttributes.NOT_NULL);
            attributeByAnnotation.put(Null.class, DefaultAttributes.NULL);
            attributeByAnnotation.put(Unsigned.class, DefaultAttributes.UNSIGNED);
            attributeByAnnotation.put(Zerofill.class, DefaultAttributes.ZEROFILL);
            attributeByAnnotation.put(PrimaryKey.class, DefaultAttributes.PRIMARY_KEY);
            attributeByAnnotation.put(AutoIncrement.class, DefaultAttributes.AUTO_INCREMENT);
            return getAttribute(object);
        }

        return attributeByAnnotation.get(object);
    }

    // Everything
    /**
     * An UNIQUE value.
     */
    public final static Attribute UNIQUE = new Attribute("UNIQUE");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unique {
    }

    /**
     * A NOT NULL value.
     */
    public final static Attribute NOT_NULL = new Attribute("NOT NULL");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotNull {
    }

    /**
     * A NULL value.
     */
    public final static Attribute NULL = new Attribute("NULL");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Null {
    }
    // Numbers only
    /**
     * An UNSIGNED number.
     */
    public final static Attribute UNSIGNED = new Attribute("UNSIGNED");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unsigned {
    }

    /**
     * A ZERO FILLED number.
     */
    public final static Attribute ZEROFILL = new Attribute("ZEROFILL");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Zerofill {
    }

    /**
     * A PRIMARY KEY number.
     */
    public final static Attribute PRIMARY_KEY = new Attribute("PRIMARY KEY");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PrimaryKey {
    }

    /**
     * An AUTO INCREMENT number.
     */
    public final static Attribute AUTO_INCREMENT = new Attribute("AUTO_INCREMENT");

    @Retention(RetentionPolicy.RUNTIME)
    public @interface AutoIncrement {
    }

    /**
     * Gets the default attributes for a value.
     *
     * @return The default attributes for a value.
     */
    public static List<Attribute> getDefaultAttributes() {
        return Arrays.asList(UNIQUE, NULL, NOT_NULL);
    }

    /**
     * Gets the default attributes for a value and the default attributes for a number.
     *
     * @return The default attributes for a value and the default attributes for a number.
     */
    public static List<Attribute> getNumberAttributes() {
        List<Attribute> list = new ArrayList<>(getDefaultAttributes());
        list.addAll(Arrays.asList(UNSIGNED, ZEROFILL, PRIMARY_KEY, AUTO_INCREMENT));
        return list;
    }

}
