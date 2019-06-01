package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class NamingTest {


    @Test
    public void testNaming() {
        DefaultNameConverter a = new DefaultNameConverter();

        Map<String, String> values = new LinkedHashMap<>();
        values.put(
                "toColumnName",
                "to_column_name"
        );

        values.put(
                "userName",
                "user_name"
        );

        values.put(
                "aColor",
                "a_color"
        );

        values.put(
                "userPhone",
                "user_phone"
        );

        values.put(
                "iMVerySmart",
                "i_m_very_smart"
        );

        values.put(
                "iAmALiar",
                "i_am_a_liar"
        );


        System.out.println("Testing the Default Name Converter\n[value] expect > actual\n");
        values.forEach((value, expected) -> Tests.printAndAssert(expected, String.valueOf(a.toColumnName(value)), value));
    }

}
