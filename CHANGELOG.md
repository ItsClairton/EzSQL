# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased] - ????-??-??

### Added
- `QueryResult#to()` and `QueryResult#toList()`.  
- `Table#insert(Object)`, `Table#update(Object)` and `Table#delete(Object)`.  
- `Table#insertAll(Object)`, `Table#deleteAll(Object)` and `Table#insertAll(Object)`.  
- `WhereStatement#like()`.  
- Exception Handler.  
- `Table#dropReturningUpdatedLines()` and `Table#truncateReturningUpdatedLines()`.  
- Warning annotations in the incomplete statements.  
- 

### Removed
- The `Ez` classes prefix.  

### Changed
- The project structure. Now the SQL have their implementation in the module.  
- The `DataType` and `Attribute` structure.  
- The statements structure now, it's verbose less.  
- Change port type to the primitive `int`.  
- The unit tests.

### Fixed
- `SELECT *` queries.  

### Deprecated
- All the `ColumnBuilder()` constructors that have the Attribute and the Data Type as a String.  

### Security
- Add column and table name checker to avoid SQL Injection.  

## [0.3.0] - 2018-12-15

### Added
- `withDefaultValue(Object defaultValue, boolean asString)` function.
- Javadoc deploy to the Gitlab pages in the Gitlab CI.
- `INTERVAL` Data Type (from PostgreSQL).
- `EzColumnBuilder(name, dataTypeName, attributes...)` constructor that use the data type name instead of a EzDataType.
- `withCustomDriver(driverClass)` function.
- `executeStatementAndClose(statement, values)` function.
- `prepareStatement(statement, values)` function.
- `insertAndClose(statement)` function.

### Changed
- The project structure. The EzSQL code is separated from the drivers.
- The EzTable#truncate() structure. Instead of returning a result its just close the statement.

### Fixed
- The space before the comma (`,`) between the columns in the `CREATE TABLE` statement.
- `EzSelect` constructor now uses one String split by comma, like the others statements constructors.

## [0.2.0] - 2018-09-24

### Added
- Where `at least`.
- Where `at most`.
- Where `less than`.
- Where `more than`.
- `OR` and `AND` options to `WHERE`.
- `toSQL(type)` function.
- A EzTableBuilder's constructor with the column's length and attributes.
- `insertReturning()` function to `EzTable`.
- Parentheses support to `WHERE` conditions.
- `SERIAL` and `BIGSERIAL` types to `EzDataTypes`.
- Support to custom server's HOSTs used in the unit tests using system envs.

### Changed
- `toMySQL()` functions access to private. 
- `toSQLite()` functions access to private. 
- `toPostgreSQL()` functions access to private. 
- `withValue(int)` function to `withLength(int)`.
- `getValue()` function to `getLength()`.
- `setOrderBy()` function to `orderBy()`.
- `setLimit()` function to `limit()`.
- `EzInsert` usage (see issue #5 for more information).

### Deprecated
- `toString()` function in all subclasses of `EzStatement`.

### Fixed
- Type in the `registerDriver()` function name, from (`registerDrive()` to `registerDriver()`).

### Removed
- The type's id from `EzDataType`.

## [0.1.0-SNAPSHOT] - 2018-04-26

### Added
- `EzStatements` and subclasses.
- `EzResult` and subclasses.
- `JOIN` statement.
- `EzColumnBuilder`.
- `EzTableBuilder`.
- `EzDatabaseBuilder`.
- PostgreSQL support.
