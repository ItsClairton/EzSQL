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

    /**
     * An UNIQUE value.
     */
    public final static Attribute UNIQUE = new Attribute("UNIQUE");
    /**
     * A NOT NULL value.
     */
    public final static Attribute NOT_NULL = new Attribute("NOT NULL");

    // Everything
    /**
     * A NULL value.
     */
    public final static Attribute NULL = new Attribute("NULL");
    /**
     * An UNSIGNED number.
     */
    public final static Attribute UNSIGNED = new Attribute("UNSIGNED");
    /**
     * A ZERO FILLED number.
     */
    public final static Attribute ZEROFILL = new Attribute("ZEROFILL");
    /**
     * A PRIMARY KEY number.
     */
    public final static Attribute PRIMARY_KEY = new Attribute("PRIMARY KEY");
    /**
     * An AUTO INCREMENT number.
     */
    public final static Attribute AUTO_INCREMENT = new Attribute("AUTO_INCREMENT");
    /**
     * The attribute object by the annotation.
     */
    private static Map<Object, Attribute> attributeByAnnotation = new HashMap<>();
    // Numbers only

    /**
     * Gets the attribute by the annotation.
     *
     * @param object The attribute by the annotation.
     *
     * @return The attribute by the annotation.
     */
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

    /**
     * Marks an UNIQUE value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unique {
    }

    /**
     * Marks a NOT NULL value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NotNull {
    }

    /**
     * Marks a NULL value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Null {
    }

    /**
     * Marks a UNSIGNED value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unsigned {
    }

    /**
     * Marks a ZERO FILLED value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Zerofill {
    }

    /**
     * Marks a PRIMARY KEY value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PrimaryKey {
    }

    /**
     * Marks a NULL value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AutoIncrement {
    }

}
