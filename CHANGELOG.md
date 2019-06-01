# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [0.4.0] - 2019-06-01

### Added
- `QueryResult#to()` and `QueryResult#toList()` (6e2c4aa6b92e0dc4d4f99102ec7d6b5a7c4e453b).  
- `Table#insert(Object)`, `Table#update(Object)` and `Table#delete(Object)` (6e2c4aa6b92e0dc4d4f99102ec7d6b5a7c4e453b).  
- `Table#insertAll(Object)`, `Table#deleteAll(Object)` and `Table#insertAll(Object)` (043bd3ed34d8c0ec1b7562c826a45a64f0b14c3d).  
- `WhereStatement#like()` (af424c7526633d62813acf033b69e015e516199b).  
- Exception Handler (d11800b34555325684a9a06dff86ecca8a100304).  
- `Table#dropReturningUpdatedLines()` and `Table#truncateReturningUpdatedLines()` (2ecac249789ed2295f4962d9237098ccda2dcbc1).  
- Warning annotations in the incomplete statements (318bf557fe2dc3343e5c3e6c6ede0cacaf2e7546).  
- `Table#count()` function (eeb778434b5f53fd68009af74d61f5252382a268).  
- `Table#sum()` function (eeb778434b5f53fd68009af74d61f5252382a268).  
- `Table#avg()` function (eeb778434b5f53fd68009af74d61f5252382a268).  
- `QueryResult#getFirstColumn()` (and `#getFirstColumnAsTYPE()`) function (fa18fec316d40c2b30bad6ceaca5cad46ea96cb2).  
- `StatementBase#executeThrowing()` function (05dd04c7fa7d7261b6910a063637236eb0afe8a5).  
- `EzSQL#executeUnsafeStatementQuery()` function (5138f3534786cdd2b7ea5cf5c8d1ba8b9cae9c2c).  
- Name Converter for the models (ab068f9e112efbfd9d94451fbcad64a483471be2).  
- Add support to model id by field name (ab068f9e112efbfd9d94451fbcad64a483471be2).  

### Removed
- The `Ez` classes prefix (3ae48fa60655a9f0c46d7b97e6e072a13d72f884).  

### Changed
- The project structure. Now the SQL have their implementation in the module (34d023727a146b9110ffe2dd20266d0a3500015f).  
- The `DataType` and `Attribute` structure (ab65afca3efaf8dbbafbae66525e8ba155ab1d38).  
- The statements structure to a less verbose usage (5639c9cdc4dcfc4c46209f4e0a90ff3d7c9df0d2).  
- Change port type to the primitive `int` (15ee7d6e474d8cb0f01974eb27d56bd4ed1ab571).  
- The unit tests (0085538fae23c41c54c85028a022607cac5a4561).  
- The `EzSQL#executeStatementAndClose()` function name to `executeUnsafeStatementAndClose()` (5138f3534786cdd2b7ea5cf5c8d1ba8b9cae9c2c).  

### Fixed
- `SELECT *` queries (8f63156f7d18285d687cf7575c8a9e16a0b9ad81).
- `Where isNull` condition (b7aa2af407637bff5b3f0f7ca226da83e48f1d83).  

### Deprecated
- All the `ColumnBuilder()` constructors that have the Attribute and the Data Type as a String (7c439864f9c4247692dfb363165c27c5d3146bc4).  

### Security
- Add column and table name checker to avoid SQL Injection (d37602e38764bab01eab7f773cfba1cb444c65db).  

## [0.3.0] - 2018-12-15

### Added
- `withDefaultValue(Object defaultValue, boolean asString)` function.
- Javadoc deploy to the GitLab pages in the GitLab CI (27dfd06e092d9a6912d39b21d9debff71b794d74).
- `INTERVAL` Data Type (from PostgreSQL) (d21e91a7d6d86de7d17046a048630be7d65167e1).
- `EzColumnBuilder(name, dataTypeName, attributes...)` constructor that use the data type name instead of a EzDataType (d21e91a7d6d86de7d17046a048630be7d65167e1).
- `withCustomDriver(driverClass)` function (7cc695ae39b873d71d528011c008a35bd5af7928).
- `executeStatementAndClose(statement, values)` function (8c9d456291299cb97abf520163af2932942ff35b).
- `prepareStatement(statement, values)` function (8c9d456291299cb97abf520163af2932942ff35b).
- `insertAndClose(statement)` function (2427248d5f5fa9c73bfd8687119c3ae31c12a284).

### Changed
- The project structure. The EzSQL code is separated from the drivers.
- The EzTable#truncate() structure. Instead of returning a result its just close the statement (dbaf7df83c4dcd30b7c1ce7aecb7299816f70b1a).

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
- `EzInsert` usage (see issue #20 for more information).

### Deprecated
- `toString()` function in all subclasses of `EzStatement`.

### Fixed
- Typo in the `registerDriver()` function name, from (`registerDrive()` to `registerDriver()`).

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
