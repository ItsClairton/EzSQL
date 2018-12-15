package com.gitlab.pauloo27.core.sql;

import org.junit.Assert;
import org.junit.Test;

public class Tests {

    public void printAndAssert(String expected, String actual) {
        System.out.println(actual);
        Assert.assertEquals(expected, actual);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testBuilders() {
        // Test select
        printAndAssert("SELECT name FROM ${table} WHERE age >= ? AND age < ? ORDER BY name ASC LIMIT 3;",
                new EzSelect("name")
                        .orderBy("name", EzStatement.OrderByType.ASC)
                        .where().atLeast("age", 18)
                        .and()
                        .lessThan("age", 60)
                        .limit(3).toString());

        // Test insert
        printAndAssert("INSERT INTO ${table} (name, phone, email) VALUES (?, ?, ?), (?, ?, ?);", new EzInsert("name, phone, email",
                new EzInsert.EzValue("' or 1=1", 666, "sql_injection@test.com"),
                new EzInsert.EzValue("People0", 30213, "people@sample.com")).toString());

        // Test select
        printAndAssert("SELECT name, phone, email FROM ${table} ORDER BY name ASC LIMIT 3;", new EzSelect("name, phone, email")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3).toString());

        // Test update
        printAndAssert("UPDATE ${table} SET name = ? WHERE name = ?;", (new EzUpdate().set("name", "People01").where().equals("name", "People1").toString()));

        // Test delete
        printAndAssert("DELETE FROM ${table} WHERE name = ? OR age > ?;", new EzDelete().where().equals("name", "People0").or().moreThan("age", 18).toString());

        // Test select with join
        printAndAssert(
                "SELECT pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm FROM ${table} " +
                        "INNER JOIN clientes ON pedidos.cliente = clientes.id;",
                new EzSelect("pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                        .join(
                                new EzStatement.Join("clientes",
                                        "pedidos.cliente",
                                        "clientes.id",
                                        EzStatement.Join.JoinType.INNER))
                        .toString());

        // Test select with join and limit
        printAndAssert("SELECT pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm FROM ${table} " +
                        "INNER JOIN clientes ON pedidos.cliente = clientes.id LIMIT 3;",
                new EzSelect("pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                        .join(new EzStatement.Join("clientes", "pedidos.cliente", "clientes.id",
                                EzStatement.Join.JoinType.INNER)).limit(3).toString());
    }

}
