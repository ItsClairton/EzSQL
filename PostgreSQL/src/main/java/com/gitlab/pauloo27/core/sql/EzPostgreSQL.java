package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Builds a insert returning statement.
     *
     * @param insert      The insert statement.
     * @param columnsName The returning columns' names separated by ", ".
     * @param table       The table.
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(Insert insert, String columnsName, PostgreSQLTable table) throws SQLException {
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("INSERT INTO %s (%s) VALUES %s RETURNING %s;", table.getName(), insert.getColumnsName(), insert.valuesToString(), columnsName));

        setValuesObjects(statement, new AtomicInteger(), insert.getValues());
        return statement;
    }

    @Override
    public String build(DataType dataType) {
        if (dataType.hasCustomName() && dataType.getCustomName().equalsIgnoreCase("PRIMARY_KEY"))
            return "SERIAL";

        if (dataType.toSQL().equalsIgnoreCase("DOUBLE"))
            return "DOUBLE PRECISION";

        if(dataType.toSQL().equalsIgnoreCase("TINYINT"))
            return "SMALLINT";

        if(dataType.toSQL().equalsIgnoreCase("TIME_WITH_TIMEZONE"))
            return "TIME WITH TIME ZONE";

        if(dataType.toSQL().equalsIgnoreCase("TIMESTAMP_WITH_TIMEZONE"))
            return "TIMESTAMP WITH TIME ZONE";

        return super.build(dataType);
    }
}

