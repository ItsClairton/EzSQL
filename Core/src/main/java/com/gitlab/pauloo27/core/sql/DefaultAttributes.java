package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL attributes.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class DefaultAttributes {

    // Everything
    /**
     * An UNIQUE value.
     */
    public final static Attribute UNIQUE = new Attribute("UNIQUE");
    /**
     * A NOT NULL value.
     */
    public final static Attribute NOT_NULL = new Attribute("NOT NULL");
    /**
     * A NULL value.
     */
    public final static Attribute NULL = new Attribute("NULL");
    // Numbers only
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
