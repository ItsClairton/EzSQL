package com.gitlab.pauloo27.core.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

public class EzSQLite extends EzSQL<SQLiteDatabase, SQLiteTable> {

    public EzSQLite(String filePath) {
        withAddress(filePath);
    }

    @Override
    public int getPort() {
        throw new NullPointerException("SQLite have not port");
    }

    @Override
    public int getDefaultPort() {
        throw new NullPointerException("SQLite have not port");
    }

    @Override
    public SQLiteDatabase getCurrentDatabase() throws SQLException {
        throw new NullPointerException("SQLite have just one database per file");
    }

    @Override
    public String getURLBase() {
        return "jdbc:sqlite:";
    }

    @Override
    public String getDriverClass() {
        return "org.sqlite.JDBC";
    }

    @Override
    protected SQLiteDatabase getDatabaseByName(String name) {
        return new SQLiteDatabase(this, name);
    }

    @Override
    protected SQLiteTable getTableByName(String name) {
        return new SQLiteTable(this, name);
    }

    @Override
    public EzSQL connect() throws SQLException {
        if (!this.readyToConnect()) throw new SQLException("Not ready to connect");
        this.connection = DriverManager.getConnection(getURLBase() + address);
        return this;
    }

    @Override
    public String build(Attribute attribute) {
        if (attribute.toSQL().equalsIgnoreCase("AUTO_INCREMENT")) {
            return "AUTOINCREMENT";
        }
        return super.build(attribute);
    }
}