package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>The main EzSQL class with the builds and main functions. Where the magic happens.</h1>
 * <p>EzSQL is simple way to connect and use SQL, without writing queries, just with code.</p>
 *
 * @author Paulo
 * @version 1.0
 * @since 0.1.0
 */
public class EzSQL {

    /**
     * The SQL type.
     */
    private final EzSQLType type;
    /**
     * The SQL connection.
     */
    private Connection connection;
    /**
     * The SQL address.
     */
    private String address = "localhost";
    /**
     * The SQL default database.
     */
    private String defaultDatabase;
    /**
     * The SQL user name.
     */
    private String username;
    /**
     * The SQL password.
     */
    private String password;
    /**
     * The SQL port.
     */
    private Integer port;
    /**
     * If true, the database will be created if not exists.
     */
    private boolean createDefaultDatabaseIfNotExists = false;
    /**
     * A custom JDBC driver.
     */
    private String customDriverClass;

    /**
     * Builds the EzSQL.
     *
     * @param type The SQL's type.
     */
    public EzSQL(EzSQLType type) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        this.type = type;
    }

    /**
     * Checks if a entry name is valid. Used to protect the column, table and database names from SQL Injection.
     * Uses a regex that checks if the string is only of alphabetical characters ({@code \w*}).
     *
     * @param name The string to check.
     * @return If the name contains only alphabetical characters and a dot or it's a asterisk.
     */
    public static boolean checkEntryName(String name) {
        if (name.equals("*"))
            return true;

        if (name.contains(".")) {
            String[] names = name.split("\\.");
            if (names.length != 2)
                return false;

            return Arrays.stream(names).allMatch(EzSQL::checkEntryName);
        } else
            return name.matches("\\w*");
    }

    /**
     * Sets the SQL Type to SQLITE.
     *
     * @param filePath The SQLIte file path.
     * @return The current object instance.
     */
    public EzSQL asSQLite(String filePath) {
        this.address = filePath;
        return this;
    }

    /**
     * Sets the address and the port used to connect. The default address value is "localhost" and the default port
     * value will get from the {@link EzSQLType#getPort()}.
     *
     * @param address The server's address.
     * @param port    The server's port. If the port is less than 0 the default port will be used.
     * @return The current object instance.
     */
    public EzSQL withAddress(String address, int port) {
        this.address = address;
        this.port = port;
        return this;
    }

    /**
     * Sets the server address (without the port) used to connect. The default value "localhost". The default port (from
     * the type) will be used. To use another port see {@link #withAddress(String, int)}.
     *
     * @param address The server's address.
     * @return The current object instance.
     */
    public EzSQL withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Sets the server login information.
     *
     * @param username The user's name.
     * @param password The user's password.
     * @return The current object instance.
     */
    public EzSQL withLogin(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Sets the server login information. To also sets the password {@link #withLogin(String, String)}
     *
     * @param username The user's name.
     * @return The current object instance.
     */
    public EzSQL withLogin(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the default database. If the default database is not null the database will be automatically selected when
     * connected. If the default database not exists, a exception will be throw. To create this, use {@link
     * #withDefaultDatabase(String, boolean)}.
     *
     * @param defaultDatabase The default database name.
     * @return The current object instance.
     */

    public EzSQL withDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
        return this;
    }

    /**
     * Sets the default database. If the default database is not null the database will be automatically selected when
     * connected.
     *
     * @param defaultDatabase   The default database name.
     * @param createIfNotExists If true, the database will be created if not exists. Doesn't work with Postgresql.
     * @return The current object instance.
     */
    public EzSQL withDefaultDatabase(String defaultDatabase, boolean createIfNotExists) {
        this.defaultDatabase = defaultDatabase;
        this.createDefaultDatabaseIfNotExists = createIfNotExists;
        return this;
    }

    /**
     * Sets a custom JDBC driver. If it's null (and it's by default) the default driver for the SQL Type will be used.
     *
     * @param customDriverClass The custom driver class.
     * @return The current object instance.
     */
    public EzSQL withCustomDriver(String customDriverClass) {
        this.customDriverClass = customDriverClass;
        return this;
    }

    /**
     * Checks if the SQL is ready to connect.
     *
     * @return If the address is not null.
     */
    public boolean readyToConnect() {
        return (this.address != null) && !(isType(EzSQLType.POSTGRESQL) && defaultDatabase == null);
    }

    /**
     * Gets the SQL server's port.
     *
     * @return The server's port.
     */
    public int getPort() {
        if (type == EzSQLType.SQLITE) throw new NullPointerException("SQLite have not port");
        return port == null || port < 0 ? this.type.getPort() : port;
    }

    /**
     * Register the SQL Driver. In JDBC's versions equals or newer than 4.0 (Java 7) is not necessary if the services
     * file (META-INF.services/java.sql.Driver) is in the jar.
     *
     * @return The current object instance.
     * @throws ClassNotFoundException Invalid driver class.
     */
    public EzSQL registerDriver() throws ClassNotFoundException {
        Class.forName(customDriverClass == null ? this.type.getDriverClass() : customDriverClass);
        return this;
    }

    /**
     * Connects to the SQL.
     *
     * @return The current object instance.
     * @throws SQLException Problems to connect.
     */
    public EzSQL connect() throws SQLException {
        if (!this.readyToConnect()) throw new SQLException("Not ready to connect");
        if (defaultDatabase != null && !checkEntryName(defaultDatabase))
            throw new SQLException(defaultDatabase + " is not a valid name");
        if (this.type.isServer()) {
            String url = this.type.getURLBase() + this.address + ":" + this.getPort();

            if (this.defaultDatabase != null && (!this.createDefaultDatabaseIfNotExists || isType(EzSQLType.POSTGRESQL))) {
                url += "/" + this.defaultDatabase;
            }

            if (this.username == null) {
                this.connection = DriverManager.getConnection(url);
            } else {
                this.connection = DriverManager.getConnection(url, username, password);
            }

            if (createDefaultDatabaseIfNotExists && this.type != EzSQLType.POSTGRESQL)
                this.changeDatabase(this.createIfNotExists(new EzDatabaseBuilder(this.defaultDatabase)));
        } else {
            this.connection = DriverManager.getConnection(type.getURLBase() + address);
        }
        return this;

    }

    /**
     * Checks if the type of the SQL is the same of the parameter type.
     *
     * @param type The type to compare.
     * @return If the type of the SQL is the same of the parameter type.
     */
    public boolean isType(EzSQLType type) {
        return this.type == type;
    }

    /**
     * Gets the current database. Return null if the type is SQLite.
     *
     * @return The current database. If the SQL is SQLite return null.
     * @throws SQLException Problems to execute the statement.
     */
    public EzDatabase getCurrentDatabase() throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        if (this.getType() == EzSQLType.SQLITE) return null; // SQLite have just one database per file.
        return new EzDatabase(this, this.getConnection().getCatalog());
    }

    /**
     * Changes the selected database.
     *
     * @param database The database to selected.
     * @return The current object instance.
     * @throws SQLException Problems to execute the statement.
     */
    public EzSQL changeDatabase(EzDatabase database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeStatementAndClose("USE %s", database.getName());
        return this;
    }

    /**
     * Executes a PreparedStatement and close.
     *
     * @param statement The statement.
     * @throws SQLException Problems to execute the statement.
     */
    public void executeAndClose(PreparedStatement statement) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        statement.execute();
        statement.close();

    }

    /**
     * Builds a select statement.
     *
     * @param select The select statement.
     * @param table  The table.
     * @return The current object instance.
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(EzSelect select, EzTable table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("SELECT %s FROM %s;", String.join(", ", select.getColumnNames()), (table.getName() + " " +
                        select.joinToString() + " " +
                        select.whereToString() + " " +
                        select.orderByToString() + " " +
                        select.limitToString()).trim()).replaceAll("\\s+", " ")
        );

        setWhereObjects(statement, new AtomicInteger(), select.getWhereConditions().getWhereStatements());
        return statement;
    }

    /**
     * Builds a insert statement.
     *
     * @param insert The insert statement.
     * @param table  The table.
     * @return The current object instance.
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(EzInsert insert, EzTable table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("INSERT INTO %s (%s) VALUES %s;", table.getName(), insert.getColumnsName(), insert.valuesToString()));

        setValuesObjects(statement, new AtomicInteger(), insert.getValues());
        return statement;
    }

    /**
     * Builds a insert returning statement.
     *
     * @param insert      The insert statement.
     * @param columnsName The returning columns' names separated by ", ".
     * @param table       The table.
     * @return The current object instance.
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(EzInsert insert, String columnsName, EzTable table) throws SQLException {
        Preconditions.checkArgument(Arrays.stream(columnsName.split(", ")).allMatch(EzSQL::checkEntryName), columnsName + " is not a valid name");
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("INSERT INTO %s (%s) VALUES %s RETURNING %s;", table.getName(), insert.getColumnsName(), insert.valuesToString(), columnsName));

        setValuesObjects(statement, new AtomicInteger(), insert.getValues());
        return statement;
    }

    /**
     * Builds a update statement.
     *
     * @param update The update statement.
     * @param table  The table.
     * @return The current object instance.
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(EzUpdate update, EzTable table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("UPDATE %s %s %s %s %s %s;",
                        table.getName(),
                        update.setsToString(),
                        update.joinToString(),
                        update.whereToString(),
                        update.orderByToString(),
                        update.limitToString()
                ).trim().replaceAll("\\s+", " "));

        AtomicInteger i = new AtomicInteger();
        setSetObjects(statement, i, update.getSets());
        setWhereObjects(statement, i, update.getWhereConditions().getWhereStatements());

        return statement;
    }

    /**
     * Builds a PreparedStatement in inline.
     *
     * @param statement The SQL statement.
     * @param values    The array os values.
     * @return The builded PreparedStatement.
     * @throws SQLException Error to create the statement.
     */
    public PreparedStatement prepareStatement(String statement, Object... values) throws SQLException {
        PreparedStatement stmt = getConnection().prepareStatement(statement);
        setValuesObjects(stmt, new AtomicInteger(), Arrays.asList(values));
        return stmt;
    }

    /**
     * Executes a non-PreparedStatement then and close. Note that use statement is unsafe for users values. The values
     * will be defined using {@link String#format(String, Object...)}, so take care of the user inputs.
     *
     * @param statement    The Statement.
     * @param unsafeValues The array of values.
     * @throws SQLException Error to create or run the statement.
     */
    public void executeStatementAndClose(String statement, Object... unsafeValues) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(String.format(statement, unsafeValues));
        stmt.close();
    }

    /**
     * Sets the where condition objects to a statement for each '?' in the query.
     *
     * @param statement  The statement.
     * @param i          A atomic int of the index of the first '?'.
     * @param statements The list of the where conditions.
     */
    public void setWhereObjects(PreparedStatement statement, AtomicInteger i, Collection<EzWhereCondition.WhereStatementBase> statements) {
        statements.forEach(statementBase -> {
            if (statementBase instanceof EzWhereCondition.Parentheses)
                return;

            EzWhereCondition.Where where = ((EzWhereCondition.WhereStatement) statementBase).getWhere();
            if (where.getType() == EzWhereCondition.Where.WhereType.NOT_NULL)
                return;
            try {
                // Note that the setObject function starts with 1
                statement.setObject(i.incrementAndGet(), where.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the set statements objects to a statement for each '?' in the query.
     *
     * @param statement The statement.
     * @param i         A atomic int of the index of the first '?'.
     * @param setList   The list of the set statements.
     */
    public void setSetObjects(PreparedStatement statement, AtomicInteger i, Collection<Map.Entry<String, Object>> setList) {
        setList.forEach(set -> {
            try {
                // Note that the setObject function starts with 1
                statement.setObject(i.incrementAndGet(), set.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the value statements objects to a statement for each '?' in the query.
     *
     * @param statement The statement.
     * @param i         A atomic int of the index of the first '?'.
     * @param values    The list of the values.
     */
    public void setValuesObjects(PreparedStatement statement, AtomicInteger i, List<Object> values) {
        values.forEach(value -> {
            try {
                // Note that the setObject function starts with 1
                statement.setObject(i.incrementAndGet(), value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Builds a delete statement.
     *
     * @param delete The delete statement.
     * @param table  The table.
     * @return The current object instance.
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(EzDelete delete, EzTable table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("DELETE FROM %s %s %s %s %s;",
                        table.getName(),
                        delete.whereToString(),
                        delete.joinToString(),
                        delete.orderByToString(),
                        delete.limitToString()
                ).trim().replaceAll("\\s+", " ")
        );
        setWhereObjects(statement, new AtomicInteger(), delete.getWhereConditions().getWhereStatements());
        return statement;
    }

    /**
     * Gets the SQL type.
     *
     * @return The SQL type.
     */
    public EzSQLType getType() {
        return type;
    }

    /**
     * Gets the connections.
     *
     * @return The connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Checks if is connected.
     *
     * @return If is connected.
     */
    public boolean isConnected() {
        if (connection == null) return false;
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;

        }
    }

    /**
     * Gets an instance of {@link EzTable} using the table name.
     *
     * @param name The table's name.
     * @return The table.
     */
    public EzTable getTable(String name) {
        return new EzTable(this, name);
    }

    /**
     * Gets an instance of {@link EzDatabase} using the database name.
     *
     * @param name The database's name.
     * @return The database.
     */
    public EzDatabase getDatabase(String name) {
        return new EzDatabase(this, name);
    }

    /**
     * Creates a table if not exists.
     *
     * @param table The table's builder.
     * @return The table.
     * @throws SQLException Problems to execute the statement.
     */
    public EzTable createIfNotExists(EzTableBuilder table) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeStatementAndClose("CREATE TABLE IF NOT EXISTS %s (%s)", table.getName(), table.toSQL(type));
        return new EzTable(this, table.getName());
    }

    /**
     * Creates a database if not exists.
     *
     * @param database The database's builder.
     * @return The database.
     * @throws SQLException Problems to execute the statement.
     */
    public EzDatabase createIfNotExists(EzDatabaseBuilder database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeStatementAndClose("CREATE DATABASE IF NOT EXISTS %s", database.getName());
        return new EzDatabase(this, database.getName());
    }

    /**
     * Creates a table.
     *
     * @param table The table's builder.
     * @return The table.
     * @throws SQLException Problems to execute the statement.
     */
    public EzTable create(EzTableBuilder table) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeStatementAndClose("CREATE TABLE %s (%s);", table.getName(), table.toSQL(type));
        return new EzTable(this, table.getName());
    }

    /**
     * Creates a database.
     *
     * @param database The database's name.
     * @return The database.
     * @throws SQLException Problems to execute the statement.
     */
    public EzDatabase create(EzDatabaseBuilder database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeStatementAndClose("CREATE DATABASE %s", database.getName());
        return new EzDatabase(this, database.getName());
    }

    /**
     * Disconnects from the SQL.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public void disconnect() throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.connection.close();
    }

}
