package com.gitlab.pauloo27.core.sql;


import java.sql.SQLException;

public class EzPostgreSQL extends EzSQL<EzPostgreSQLDatabase, EzPostgreSQLTable> {
    @Override
    public int getDefaultPort() {
        return 5432;
    }

    @Override
    public String getURLBase() {
        return "jdbc:postgresql://";
    }

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    protected EzPostgreSQLDatabase getDatabaseByName(String name) {
        return new EzPostgreSQLDatabase(this, name);
    }

    @Override
    protected EzPostgreSQLTable getTableByName(String name) {
        return new EzPostgreSQLTable(this, name);
    }

    @Override
    public boolean readyToConnect() {
        return super.readyToConnect() && defaultDatabase != null;
    }

    @Override
    public String build(EzAttribute attribute) {
        if (attribute.toSQL().equalsIgnoreCase("AUTO_INCREMENT"))
            // TODO warn that it is not a valid attribute
            return "";

        return super.build(attribute);
    }

    @Override
    public String build(EzDataType dataType) {
        if (dataType.hasCustomName() && dataType.getCustomName().equalsIgnoreCase("PRIMARY_KEY"))
            return "SERIAL";

        return super.build(dataType);
    }

    @Override
    public EzSQL<EzPostgreSQLDatabase, EzPostgreSQLTable> connect() throws SQLException {
        if (createDefaultDatabaseIfNotExists)
            createDefaultDatabaseIfNotExists = false;
        return super.connect();
    }
}

