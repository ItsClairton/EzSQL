package com.gitlab.pauloo27.core.sql;

public class EzMySQL extends EzSQL<EzDatabase, EzTable> {
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
    protected EzDatabase getDatabaseByName(String name) {
        return new EzDatabase(this, name);
    }

    @Override
    protected EzTable getTableByName(String name) {
        return new EzTable(this, name);
    }
}
