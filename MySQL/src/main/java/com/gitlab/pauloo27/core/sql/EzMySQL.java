package com.gitlab.pauloo27.core.sql;

public class EzMySQL extends EzSQL<Database, Table> {
    @Override
    public int getDefaultPort() {
        return 3306;
    }

    @Override
    public String getURLBase() {
        return "jdbc:mysql://";
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    protected Database getDatabaseByName(String name) {
        return new Database(this, name);
    }

    @Override
    protected Table getTableByName(String name) {
        return new Table(this, name);
    }
}
