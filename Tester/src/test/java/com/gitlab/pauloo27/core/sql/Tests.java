package com.gitlab.pauloo27.core.sql;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tests {

    @SuppressWarnings("deprecation")
    @Test
    public void testBuilders() {
        EzSelect s = (EzSelect) new EzSelect("name")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .where().atLeast("age", 18)
                .and()
                .lessThan("age", 60)
                .limit(3);
        System.out.println(s.toString());

        System.out.println(new EzInsert("name, phone, email",
                new EzInsert.EzValue("' or 1=1", 666, "sql_injection@test.com"),
                new EzInsert.EzValue("People0", 30213, "people@sample.com")).toString());
        System.out.println(new EzSelect("name, phone, email")
                .orderBy("name", EzStatement.OrderByType.ASC)
                .limit(3).toString());
        System.out.println((new EzUpdate().set("name", "People01").where().equals("name", "People1").toString()));
        System.out.println(new EzDelete().where().equals("name", "People0").or().moreThan("age", 18).toString());

        System.out.println(
                new EzSelect("pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                        .join(
                                new EzStatement.Join("clientes",
                                        "pedidos.cliente",
                                        "clientes.id",
                                        EzStatement.Join.JoinType.INNER))
                        .toString());

        System.out.println(new EzSelect("pedidos.id, clientes.nome, clientes.telefone, pedidos.criadoEm")
                .join(new EzStatement.Join("clientes", "pedidos.cliente", "clientes.id",
                        EzStatement.Join.JoinType.INNER)).limit(3).toString());
    }

    //@Test
    public void testJoin() throws SQLException {
        EzSQL sql = new EzSQL(EzSQLType.POSTGRESQL).withLogin("ezsql", "1234").withDefaultDatabase("jointest").connect();
        sql.createIfNotExists(
                new EzTableBuilder("clients")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.VARCHAR))
        );
        sql.createIfNotExists(
                new EzTableBuilder("employees")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("name", EzDataType.VARCHAR))
                        .withColumn(new EzColumnBuilder("phone", EzDataType.VARCHAR))
        );
        EzTable requests = sql.createIfNotExists(
                new EzTableBuilder("requests")
                        .withColumn(new EzColumnBuilder("id", EzDataType.PRIMARY_KEY))
                        .withColumn(new EzColumnBuilder("employee", EzDataType.INTEGER))
                        .withColumn(new EzColumnBuilder("client", EzDataType.INTEGER))
                        .withColumn(new EzColumnBuilder("createIn", EzDataType.TIMESTAMP))
        );

        try (EzQueryResult result = requests.select(
                new EzSelect("requests.id, clients.name client, employees.name employee")
                        .join(
                                new EzStatement.Join("clients", "requests.client",
                                        "clients.id", EzStatement.Join.JoinType.INNER))
                        .join(new EzStatement.Join("employees", "requests.employee",
                                "employees.id", EzStatement.Join.JoinType.INNER))
                        .where().notNull("requests.client"))) {
            ResultSet rs = result.getResultSet();
            while (rs.next()) {
                System.out.printf("%d | %s | %s%n", rs.getInt("id"), rs.getString("employee"), rs.getString("client"));
            }
        }
    }

}
