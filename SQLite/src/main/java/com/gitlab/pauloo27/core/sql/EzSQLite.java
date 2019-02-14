package com.gitlab.pauloo27.core.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The SQLite implementation of EzSQL.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class EzSQLite extends EzSQL<SQLiteDatabase, SQLiteTable> {

    /**
     * Builds a SQLite connection .
     *
     * @param filePath The database file path.
     */
    public EzSQLite(String filePath) {
        withAddress(filePath);
    }

    /**
     * Gets the SQL server's port.
     *
     * @return NullPointerException, SQLite have not port.
     */
    @Override
    public int getPort() {
        throw new NullPointerException("SQLite have not port");
    }

    /**
     * Gets the default SQL server's port.
     *
     * @return NullPointerException, SQLite have not port.
     */
    @Override
    public int getDefaultPort() {
        throw new NullPointerException("SQLite have not port");
    }

    /**
     * Gets the current database. Return null if the type is SQLite.
     *
     * @return NullPointerException, SQLite have just one database per file.
     * @throws SQLException Problems to execute the statement.
     */
    @Override
    public SQLiteDatabase getCurrentDatabase() throws SQLException {
        throw new NullPointerException("SQLite have just one database per file");
    }

    /**
     * Gets the URL Base.
     *
     * @return {@code jdbc:sqlite:}.
     */
    @Override
    public String getURLBase() {
        return "jdbc:sqlite:";
    }

    /**
     * Gets the SQL driver class path.
     *
     * @return {@code org.sqlite.JDBC}.
     */
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
    public EzSQL<SQLiteDatabase, SQLiteTable> connect() throws SQLException {
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