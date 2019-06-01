package com.gitlab.pauloo27.core.sql;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>The main EzSQL class with the builds and main functions. Where the magic happens.</h1>
 * <p>EzSQL is simple way to connect and use SQL, without writing queries, just with code.</p>
 *
 * @param <DatabaseType> The Database class.
 * @param <TableType>    The Table class.
 *
 * @author Paulo
 * @version 3.0
 * @since 0.1.0
 */
public abstract class EzSQL<DatabaseType extends Database, TableType extends Table> {

    /**
     * The default Data Type.
     */
    private static DataType DEFAULT_DATATYPE = new DataType(
            "VARCHAR",
            DefaultAttributes.getDefaultAttributes(),
            null,
            "DEFAULT_DATATYPE"
    );
    /**
     * The default enum serializer.
     */
    private static DataType ENUM_NAME = new DataType(
            "VARCHAR",
            DefaultAttributes.getDefaultAttributes(),
            null,
            "ENUM_NAME"
    );
    /**
     * The default data serializer.
     */
    private static DataSerializer DEFAULT_DATA_SERIALIZER = new DataSerializer<>(
            object -> {
                if (object == null)
                    return null;

                if (object.getClass().isEnum()) {
                    return ((Enum) object).name();
                }

                return object;
            },

            (clazz, object) -> {
                if (clazz.isEnum())
                    return Arrays.stream(clazz.getEnumConstants())
                            .filter(member -> ((Enum) member).name().equals(object))
                            .findFirst()
                            .orElse(null);

                return object;
            }
    );
    /**
     * The SQL connection.
     */
    protected Connection connection;
    /**
     * The SQL address.
     */
    protected String address = "localhost";
    /**
     * The SQL default database.
     */
    protected String defaultDatabase;
    /**
     * The SQL user name.
     */
    protected String username;
    /**
     * The SQL password.
     */
    protected String password;
    /**
     * The SQL port.
     */
    protected int port = -1;
    /**
     * If true, the database will be created if not exists.
     */
    protected boolean createDefaultDatabaseIfNotExists = false;
    /**
     * A custom JDBC driver.
     */
    protected String customDriverClass;

    /**
     * The name converter used to convert the models (class and field) name to SQL.
     */
    protected NameConverter nameConverter = new DefaultNameConverter();
    /**
     * The Data Type by the Object.
     */
    private Map<Class<?>, DataType> typesByClass = new HashMap<>();
    /**
     * A map of the serializers.
     */
    private Map<Class, DataSerializer> typeSerializer = new HashMap<>();

    {
        registerDataType(String.class, DefaultDataTypes.VARCHAR);

        registerDataType(byte.class, DefaultDataTypes.TINYINT);
        registerDataType(short.class, DefaultDataTypes.SMALLINT);
        registerDataType(int.class, DefaultDataTypes.INTEGER);
        registerDataType(long.class, DefaultDataTypes.BIGINT);

        registerDataType(float.class, DefaultDataTypes.FLOAT);
        registerDataType(double.class, DefaultDataTypes.DOUBLE);

        registerDataType(boolean.class, DefaultDataTypes.BOOLEAN);

        registerDataType(char.class, DefaultDataTypes.CHAR);

        registerDataType(Date.class, DefaultDataTypes.DATE);
    }

    /**
     * Builds the EzSQL.
     */
    public EzSQL() {
    }

    /**
     * Checks if a entry name is valid. Used to protect the column, table and database names from SQL Injection. Uses a
     * regex that checks if the string is only of alphabetical characters ({@code \w*}) and a dot, an asterisk or a
     * {@code AVG, SUM or COUNT} call.
     *
     * @param name The string to check.
     *
     * @return If the string is only of alphabetical characters ({@code \w*}) and a dot, an asterisk or a {@code AVG,
     * SUM or COUNT} call.
     */
    public static boolean checkEntryName(String name) {
        if (name.toLowerCase().matches("(avg|sum|count)\\([\\w|\'|\"|\\*]+\\)")) {
            String method = name.toLowerCase().split("\\(")[0];
            if (!name.toLowerCase().startsWith(method + "(") || !name.endsWith(")"))
                return false;

            // method.length() + 1 : the method plus a '('
            String column = name.substring(method.length() + 1, name.length() - 1);

            return checkEntryName(column);
        }

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
     * Sets the address and the port used to connect. The default address value is "localhost" and the default port
     * value will get from the {@link #getDefaultPort()} ()}.
     *
     * @param address The server's address.
     * @param port    The server's port. If the port is less than 0 the default port will be used.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withAddress(String address, int port) {
        this.address = address;
        this.port = port;
        return this;
    }

    /**
     * Sets the name converter used to convert the models (class and field) name. The default is {@link
     * DefaultNameConverter}.
     *
     * @param nameConverter The name converter policy implementation.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withNameConverter(NameConverter nameConverter) {
        this.nameConverter = nameConverter;
        return this;
    }

    /**
     * Gets the name converter policy implementation.The default is {@link DefaultNameConverter}.
     *
     * @return the name converter policy implementation.
     */
    public NameConverter getNameConverter() {
        return nameConverter;
    }

    /**
     * Sets the server address (without the port) used to connect. The default value "localhost". The default port (from
     * the type) will be used. To use another port see {@link #withAddress(String, int)}.
     *
     * @param address The server's address.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Sets the server login information.
     *
     * @param username The user's name.
     * @param password The user's password.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withLogin(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Sets the server login information. To also sets the password {@link #withLogin(String, String)}
     *
     * @param username The user's name.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withLogin(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the default database. If the default database is not null the database will be automatically selected when
     * connected. If the default database not exists, a exception will be throw. To create this, use {@link
     * #withDefaultDatabase(String, boolean)}.
     *
     * @param defaultDatabase The default database name.
     *
     * @return The current object instance.
     */

    public EzSQL<DatabaseType, TableType> withDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
        return this;
    }

    /**
     * Sets the default database. If the default database is not null the database will be automatically selected when
     * connected.
     *
     * @param defaultDatabase   The default database name.
     * @param createIfNotExists If true, the database will be created if not exists. Doesn't work with Postgresql.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withDefaultDatabase(String defaultDatabase, boolean createIfNotExists) {
        this.defaultDatabase = defaultDatabase;
        this.createDefaultDatabaseIfNotExists = createIfNotExists;
        return this;
    }

    /**
     * Sets a custom JDBC driver. If it's null (and it's by default) the default driver for the SQL Type will be used.
     *
     * @param customDriverClass The custom driver class.
     *
     * @return The current object instance.
     */
    public EzSQL<DatabaseType, TableType> withCustomDriver(String customDriverClass) {
        this.customDriverClass = customDriverClass;
        return this;
    }

    /**
     * Checks if the SQL is ready to connect.
     *
     * @return If the address is not null.
     */
    public boolean readyToConnect() {
        return (this.address != null);
    }

    /**
     * Gets the default SQL server's port.
     *
     * @return The default SQL server's port.
     */
    public abstract int getDefaultPort();

    /**
     * Gets the URL Base.
     *
     * @return The URL Base.
     */
    public abstract String getURLBase();

    /**
     * Gets the SQL server's port.
     *
     * @return The server's port.
     */
    public int getPort() {
        return port < 0 ? getDefaultPort() : port;
    }

    /**
     * Gets the SQL driver class path.
     *
     * @return The SQL driver class path.
     */
    public abstract String getDriverClass();

    /**
     * Register the SQL Driver. In JDBC's versions equals or newer than 4.0 (Java 7) is not necessary if the services
     * file (META-INF.services/java.sql.Driver) is in the jar.
     *
     * @return The current object instance.
     *
     * @throws ClassNotFoundException Invalid driver class.
     */
    public EzSQL<DatabaseType, TableType> registerDriver() throws ClassNotFoundException {
        Class.forName(customDriverClass == null ? this.getDriverClass() : customDriverClass);
        return this;
    }

    /**
     * Connects to the SQL.
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to connect.
     */
    public EzSQL<DatabaseType, TableType> connect() throws SQLException {
        if (!this.readyToConnect()) throw new SQLException("Not ready to connect");

        if (defaultDatabase != null && !checkEntryName(defaultDatabase))
            throw new SQLException(defaultDatabase + " is not a valid name");

        String url = String.format("%s%s:%d", this.getURLBase(), this.address, this.getPort());

        if (this.defaultDatabase != null && !this.createDefaultDatabaseIfNotExists)
            url += "/" + this.defaultDatabase;

        if (this.username == null)
            this.connection = DriverManager.getConnection(url);
        else
            this.connection = DriverManager.getConnection(url, username, password);

        if (this.createDefaultDatabaseIfNotExists)
            this.changeDatabase(this.createIfNotExists(new DatabaseBuilder(this.defaultDatabase)));

        return this;
    }

    /**
     * Gets the current database. Return null if the type is SQLite.
     *
     * @return The current database. If the SQL is SQLite return null.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public DatabaseType getCurrentDatabase() throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        return this.getDatabaseByName(this.getConnection().getCatalog());
    }

    /**
     * Changes the selected database.
     *
     * @param database The database to selected.
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public EzSQL<DatabaseType, TableType> changeDatabase(DatabaseType database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeUnsafeStatementAndClose("USE %s", database.getName());
        return this;
    }

    /**
     * Executes a PreparedStatement and close.
     *
     * @param statement The statement.
     *
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
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(Select select, TableType table) throws SQLException {
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
     * Builds a data type.
     *
     * @param dataType The data type object to build.
     *
     * @return The data type converted to SQL.
     */
    public String build(DataType dataType) {
        return dataType.toSQL();
    }

    /**
     * Builds a data attribute.
     *
     * @param attribute The data attribute object to build.
     *
     * @return The data attribute converted to SQL.
     */
    public String build(Attribute attribute) {
        return attribute.toSQL();
    }

    /**
     * Builds a insert statement.
     *
     * @param insert The insert statement.
     * @param table  The table.
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(Insert insert, TableType table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("INSERT INTO %s (%s) VALUES %s;",
                        table.getName(), insert.getColumnsName(), insert.valuesToString()));

        setValuesObjects(statement, new AtomicInteger(), insert.getValues());
        return statement;
    }

    /**
     * Builds a update statement.
     *
     * @param update The update statement.
     * @param table  The table.
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(Update update, TableType table) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(
                String.format("UPDATE %s %s %s %s %s %s;",
                        table.getName(),
                        update.setsToString(),
                        update.joinToString(),
                        update.whereToString(),
                        update.orderByToString(),
                        update.limitToString()
                ).trim().replaceAll("\\s+", " ")
        );
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
     *
     * @return The built PreparedStatement.
     *
     * @throws SQLException Error to create the statement.
     */
    public PreparedStatement prepareStatement(String statement, Object... values) throws SQLException {
        PreparedStatement stmt = this.getConnection().prepareStatement(statement);
        this.setValuesObjects(stmt, new AtomicInteger(), Arrays.asList(values));
        return stmt;
    }

    /**
     * Builds a non-PreparedStatement in inline using {@link String#format(String, Object...)}.
     *
     * @param statement The SQL statement.
     * @param values    The array os values.
     *
     * @return The built Statement.
     *
     * @throws SQLException Error to create the statement.
     */
    public ResultSet executeUnsafeStatementQuery(String statement, Object... values) throws SQLException {
        return this.getConnection().createStatement().executeQuery(String.format(statement, values));
    }

    /**
     * Executes a non-PreparedStatement then and close. Note that use statement is unsafe for users values. The values
     * will be defined using {@link String#format(String, Object...)}, so take care of the user inputs.
     *
     * @param statement    The Statement.
     * @param unsafeValues The array of values.
     *
     * @throws SQLException Error to create or run the statement.
     */
    public void executeUnsafeStatementAndClose(String statement, Object... unsafeValues) throws SQLException {
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
    public void setWhereObjects(PreparedStatement statement, AtomicInteger i, Collection<WhereCondition.WhereStatementBase> statements) {
        statements.forEach(statementBase -> {
            if (statementBase instanceof WhereCondition.Parentheses)
                return;

            WhereCondition.Where where = ((WhereCondition.WhereStatement) statementBase).getWhere();
            if (where.getType() == WhereCondition.Where.WhereType.NOT_NULL || where.getType() == WhereCondition.Where.WhereType.NULL)
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
     *
     * @return The current object instance.
     *
     * @throws SQLException Problems to prepare the statement.
     */
    public PreparedStatement build(Delete delete, TableType table) throws SQLException {
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
     * Gets an instance of {@link Table} using the table name.
     *
     * @param name The table's name.
     *
     * @return The table.
     */
    public TableType getTable(String name) {
        return getTableByName(name);
    }

    /**
     * Gets an instance of {@link Database} using the database name.
     *
     * @param name The database's name.
     *
     * @return The database.
     */
    public DatabaseType getDatabase(String name) {
        return getDatabaseByName(name);
    }

    /**
     * Creates a table if not exists.
     *
     * @param table The table's builder.
     *
     * @return The table.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public TableType createIfNotExists(TableBuilder table) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeUnsafeStatementAndClose("CREATE TABLE IF NOT EXISTS %s (%s)", table.getName(), table.toSQL(this));
        return getTableByName(table.getName());
    }

    /**
     * Register a new data type.
     *
     * @param clazz    The class type.
     * @param dataType The data type.
     *
     * @return The current object instance.
     */
    public EzSQL<? extends Database, ? extends Table> registerDataType(Class clazz, DataType dataType) {
        typesByClass.put(clazz, dataType);
        return this;
    }

    /**
     * Register a new serializer.
     *
     * @param clazz      The class type.
     * @param serializer The serializer.
     * @param <T>        The serializer class type.
     *
     * @return The current object instance.
     */
    public <T> EzSQL<? extends Database, ? extends Table> registerSerializer(Class<T> clazz, DataSerializer<T> serializer) {
        typeSerializer.put(clazz, serializer);
        return this;
    }

    /**
     * Gets the data type by the class. The default data type is {@link #DEFAULT_DATATYPE}.
     *
     * @param clazz The class
     *
     * @return The data type.
     */
    public DataType getDateTypeByClass(Class clazz) {
        if (!typesByClass.containsKey(clazz)) {
            if (clazz.isEnum()) {
                return ENUM_NAME;
            }

            return DEFAULT_DATATYPE;
        }

        return typesByClass.get(clazz);
    }

    /**
     * Gets the serializer by the class. The default data type is {@link #DEFAULT_DATA_SERIALIZER}.
     *
     * @param clazz The class
     *
     * @return The serializer.
     */
    public DataSerializer getSerializerByClass(Class clazz) {
        if (!typeSerializer.containsKey(clazz))
            return DEFAULT_DATA_SERIALIZER;

        return typeSerializer.get(clazz);
    }

    /**
     * Creates a table if not exists using a object.
     *
     * @param clazz The object to build.
     * @param <T>   The object type.
     *
     * @return The table.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public <T> TableType createIfNotExists(Class<T> clazz) throws SQLException {
        String tableName = ReflectionUtils.getName(nameConverter, clazz);

        TableBuilder tableBuilder = new TableBuilder(tableName);

        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            if (ReflectionUtils.isIgnored(field))
                return;

            DataType dataType = getDateTypeByClass(field.getType());

            int length = -1;

            if (dataType == DEFAULT_DATATYPE || dataType == ENUM_NAME)
                length = 64;

            if (ReflectionUtils.hasLength(field)) {
                length = ReflectionUtils.getLength(field);
            }

            if (ReflectionUtils.isId(field)) {
                dataType = DefaultDataTypes.PRIMARY_KEY;
            }

            List<Attribute> attributes = new ArrayList<>();
            Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation -> {
                if (annotation.annotationType().isMemberClass() && annotation.annotationType().getDeclaringClass() == DefaultAttributes.class) {
                    attributes.add(DefaultAttributes.getAttribute(annotation.annotationType()));
                }
            });

            try {
                String columnName = ReflectionUtils.getName(nameConverter, field);

                Object defaultValue = null;
                if (!field.getType().isPrimitive() || field.isAnnotationPresent(DefaultValue.class)) {
                    Object value = field.get(clazz.newInstance());
                    defaultValue = value == null ? null : getSerializerByClass(field.getType()).getSerializer().apply(value);
                }

                tableBuilder.withColumn(
                        new ColumnBuilder(columnName, dataType, length, attributes.toArray(new Attribute[]{}))
                                .withDefaultValue(defaultValue));
            } catch (SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return createIfNotExists(tableBuilder);
    }

    /**
     * Creates a database if not exists.
     *
     * @param database The database's builder.
     *
     * @return The database.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public DatabaseType createIfNotExists(DatabaseBuilder database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeUnsafeStatementAndClose("CREATE DATABASE IF NOT EXISTS %s", database.getName());
        return getDatabaseByName(database.getName());
    }

    /**
     * Gets a database by its name.
     *
     * @param name The database name.
     *
     * @return The database object.
     */
    protected abstract DatabaseType getDatabaseByName(String name);

    /**
     * Gets a table by its name.
     *
     * @param name The table name.
     *
     * @return The table object.
     */
    protected abstract TableType getTableByName(String name);

    /**
     * Creates a table.
     *
     * @param table The table's builder.
     *
     * @return The table.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public TableType create(TableBuilder table) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeUnsafeStatementAndClose("CREATE TABLE %s (%s);", table.getName(), table.toSQL(this));
        return getTableByName(table.getName());
    }

    /**
     * Creates a database.
     *
     * @param database The database's name.
     *
     * @return The database.
     *
     * @throws SQLException Problems to execute the statement.
     */
    public DatabaseType create(DatabaseBuilder database) throws SQLException {
        if (!this.isConnected()) throw new SQLException("Not connected");
        this.executeUnsafeStatementAndClose("CREATE DATABASE %s", database.getName());
        return getDatabaseByName(database.getName());
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
