package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultAttributes {

    // Everything
    public final static Attribute UNIQUE = new Attribute("UNIQUE");
    public final static Attribute NOT_NULL = new Attribute("NOT NULL");
    public final static Attribute NULL = new Attribute("NULL");
    // Numbers only
    public final static Attribute UNSIGNED = new Attribute("UNSIGNED");
    public final static Attribute ZEROFILL = new Attribute("ZEROFILL");
    public final static Attribute PRIMARY_KEY = new Attribute("PRIMARY KEY");
    public final static Attribute AUTO_INCREMENT = new Attribute("AUTO_INCREMENT");

    public static List<Attribute> getDefaultAttributes() {
        return Arrays.asList(UNIQUE, NULL, NOT_NULL);
    }

    public static List<Attribute> getNumberAttributes() {
        List<Attribute> list = new ArrayList<>(getDefaultAttributes());
        list.addAll(Arrays.asList(UNSIGNED, ZEROFILL, PRIMARY_KEY, AUTO_INCREMENT));
        return list;
    }
}
