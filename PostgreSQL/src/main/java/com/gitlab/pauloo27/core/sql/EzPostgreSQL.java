package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

/**
 * The PostgreSQL implementation of EzSQL.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class EzPostgreSQL extends EzSQL<PostgreSQLDatabase, PostgreSQLTable> {
    /**
     * Gets the default SQL server's port.
     *
     * @return {@code 5432}.
     */
    @Override
    public int getDefaultPort() {
        return 5432;
    }

    /**
     * Gets the URL Base.
     *
     * @return {@code jdbc:postgresql://}.
     */
    @Override
    public String getURLBase() {
        return "jdbc:postgresql://";
    }

    /**
     * Gets the SQL driver class path.
     *
     * @return {@code org.postgresql.Driver}.
     */
    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    protected PostgreSQLDatabase getDatabaseByName(String name) {
        return new PostgreSQLDatabase(this, name);
    }

    @Override
    protected PostgreSQLTable getTableByName(String name) {
        return new PostgreSQLTable(this, name);
    }

    @Override
    public boolean readyToConnect() {
        return super.readyToConnect() && defaultDatabase != null;
    }

    @Override
    public String build(Attribute attribute) {
        if (attribute.toSQL().equalsIgnoreCase("AUTO_INCREMENT"))
            // TODO warn that it's not a valid attribute
            return "";

        return super.build(attribute);
    }

    @Override
    public EzSQL<PostgreSQLDatabase, PostgreSQLTable> connect() throws SQLException {
        if (createDefaultDatabaseIfNotExists)
            createDefaultDatabaseIfNotExists = false;
        return super.connect();
    }

    @Override
    public String build(DataType dataType) {
        if (dataType.hasCustomName() && dataType.getCustomName().equalsIgnoreCase("PRIMARY_KEY"))
            return "SERIAL";

        return super.build(dataType);
    }
}

