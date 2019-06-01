package com.gitlab.pauloo27.core.sql;

/**
 * The MariaDB implementation of EzSQL.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.4.0
 */
public class EzMariaDB extends EzSQL<Database, Table> {

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
     * @return {@code jdbc:mariadb://}.
     */
    @Override
    public String getURLBase() {
        return "jdbc:mariadb://";
    }

    /**
     * Gets the SQL driver class path.
     *
     * @return {@code org.mariadb.jdbc.Driver}.
     */
    @Override
    public String getDriverClass() {
        return "org.mariadb.jdbc.Driver";
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
