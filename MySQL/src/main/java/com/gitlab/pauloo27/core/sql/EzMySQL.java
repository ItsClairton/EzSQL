package com.gitlab.pauloo27.core.sql;

/**
 * The MySQL implementation of EzSQL.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class EzMySQL extends EzSQL<Database, Table> {
    /**
     * Gets the default SQL server's port.
     *
     * @return {@code 3306}.
     */
    @Override
    public int getDefaultPort() {
        return 3306;
    }

    /**
     * Gets the URL Base.
     *
     * @return {@code jdbc:mysql://}.
     */
    @Override
    public String getURLBase() {
        return "jdbc:mysql://";
    }

    /**
     * Gets the SQL driver class path.
     *
     * @return {@code com.mysql.cj.jdbc.Driver}.
     */
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
