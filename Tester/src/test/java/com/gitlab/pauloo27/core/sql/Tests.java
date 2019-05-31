package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class Tests {

    public static void printAndAssert(String expected, String actual, String message) {
        if (message != null)
            System.out.printf("[%s] ", message);
        System.out.printf("%s > %s%n", expected, actual);

        Assert.assertEquals(expected, actual);
    }

    public void printAndAssert(String expected, String actual) {
        printAndAssert(expected, actual, null);
    }

    @Test
    public void testEntryChecker() {
        Map<String, Boolean> values = new LinkedHashMap<>();
        values.put("id", true);
        values.put("name", true);

        values.put("'id", false);
        values.put("'id'", false);

        values.put("COUNT(*)", true);
        values.put("COUNT(id)", true);

        values.put("COUNT(id')", false);
        values.put("COUNT('id', name)", false);
        values.put("COUNT()", false);

        values.put("COUNT", true); // a column called COUNT

        values.put("COUNT(", false);
        values.put("COUNT)", false);
        values.put("COUNT(!!!!!)", false);

        values.put("COUNT(user_email)", true);

        values.put("SUM()", false);
        values.put("SUM(amount)", true);

        values.put("AVG()", false);
        values.put("AVG(amount)", true);

        values.put("count(amount)", true);
        values.put("cOUNt(amount)", true);

        values.put("asd(amount)", false);
        values.put("COUN(amount)", false);
        values.put("COUNTSUM(amount)", false);



        System.out.println("Testing the Entry Checker\n[value] expect > actual\n");
        values.forEach((value, expected) -> printAndAssert(expected.toString(), String.valueOf(EzSQL.checkEntryName(value)), value));

    }

    @SuppressWarnings("deprecation")
    @Test
    public void testBuilders() {
        // Test select
        printAndAssert("SELECT name FROM ${table} WHERE age >= ? AND age < ? ORDER BY name ASC LIMIT 3;",
                new Select(null, null, "name")
                        .orderBy("name", StatementBase.OrderByType.ASC)
                        .where().atLeast("age", 18)
                        .and()
                        .lessThan("age", 60)
                        .limit(3).toString());

        // Test insert
        printAndAssert("INSERT INTO ${table} (name, phone, email) VALUES (?, ?, ?), (?, ?, ?);",
                new Insert(null, null,
                        "name, phone, email",
                        "' or 1=1", 666, "sql_injection@test.com",
                        "People0", 30213, "people@sample.com"
                ).toString());

        // Test select
        printAndAssert("SELECT name, phone, email FROM ${table} ORDER BY name ASC LIMIT 3;",
                new Select(null, null, "name, phone, email")
                        .orderBy("name", StatementBase.OrderByType.ASC)
                        .limit(3).toString());

        // Test update
        printAndAssert("UPDATE ${table} SET name = ? WHERE name = ?;",
                new Update(null, null).set("name", "People01")
                        .where().equals("name", "People1").toString());

        // Test delete
        printAndAssert("DELETE FROM ${table} WHERE name = ? OR age > ?;",
                new Delete(null, null)
                        .where().equals("name", "People0")
                        .or().moreThan("age", 18).toString());

        // Test select with join
        printAndAssert(
                "SELECT pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm FROM ${table} " +
                        "INNER JOIN clientes ON pedidos.cliente = clientes.id;",
                new Select(null, null, "pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                        .join(
                                new StatementBase.Join("clientes",
                                        "pedidos.cliente",
                                        "clientes.id",
                                        StatementBase.Join.JoinType.INNER))
                        .toString());

        // Test select with join and limit
        printAndAssert("SELECT pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm FROM ${table} " +
                        "INNER JOIN clientes ON pedidos.cliente = clientes.id LIMIT 3;",
                new Select(null, null,
                        "pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                        .join(new StatementBase.Join("clientes", "pedidos.cliente", "clientes.id",
                                StatementBase.Join.JoinType.INNER)).limit(3).toString());
    }

}
