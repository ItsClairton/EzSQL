package com.gitlab.pauloo27.core.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EzDefaultAttributes {

    // Everything
    public final static EzAttribute UNIQUE = new EzAttribute("UNIQUE");
    public final static EzAttribute NOT_NULL = new EzAttribute("NOT NULL");
    public final static EzAttribute NULL = new EzAttribute("NULL");
    // Numbers only
    public final static EzAttribute UNSIGNED = new EzAttribute("UNSIGNED");
    public final static EzAttribute ZEROFILL = new EzAttribute("ZEROFILL");
    public final static EzAttribute PRIMARY_KEY = new EzAttribute("PRIMARY KEY");
    public final static EzAttribute AUTO_INCREMENT = new EzAttribute("AUTO_INCREMENT");

    public static List<EzAttribute> getDefaultAttributes() {
        return Arrays.asList(UNIQUE, NULL, NOT_NULL);
    }

    public static List<EzAttribute> getNumberAttributes() {
        List<EzAttribute> list = new ArrayList<>(getDefaultAttributes());
        list.addAll(Arrays.asList(UNSIGNED, ZEROFILL, PRIMARY_KEY, AUTO_INCREMENT));
        return list;
    }
}
