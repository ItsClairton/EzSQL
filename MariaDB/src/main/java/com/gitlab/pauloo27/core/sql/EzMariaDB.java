package com.gitlab.pauloo27.core.sql;

public class EzMariaDB extends EzSQL<EzDatabase, EzTable> {
    @Override
    public int getDefaultPort() {
        return 3306;
    }

    @Override
    public String getURLBase() {
        return "jdbc:mariadb://";
    }

    @Override
    public String getDriverClass() {
        return "org.mariadb.jdbc.Driver";
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
