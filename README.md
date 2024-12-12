# DavisBase
A Database Engine with terminal access to perform CRUD operations on database.

ReadMe for GalliumDavisBase:


## How to Build?


Steps:
1. clone the repo
2. cd <path_to_repo>/src/
3. javac DavisBasePrompt.java
4. java DavisBasePrompt

On running the above command successfully the following text should be seen:
--------------------------------------------------------------------------------
Welcome to DavisBase

Type 'help;' to display supported commands.
--------------------------------------------------------------------------------
gallium>

## Supported Commands

All commands below are case insensitive

	USE DATABASE database_name;                      Changes current database.
	CREATE DATABASE database_name;                   Creates an empty database.
	SHOW DATABASES;                                  Displays all databases.
	DROP DATABASE database_name;                     Deletes a database.
	SHOW TABLES;                                     Displays all tables in current database.
	DESC table_name;                                 Displays table schema.
	CREATE TABLE table_name (                        Creates a table in current database.
		<column_name> <datatype> [PRIMARY KEY | NOT NULL]
		...);
	DROP TABLE table_name;                           Deletes a table data and its schema.
	SELECT <column_list> FROM table_name             Display records whose rowid is <id>.
		[WHERE rowid = <value>];
	INSERT INTO table_name                           Inserts a record into the table.
		[(<column1>, ...)] VALUES (<value1>, <value2>, ...);
	DELETE FROM table_name [WHERE condition];        Deletes a record from a table.
	UPDATE table_name SET <conditions>               Updates a record from a table.
		[WHERE condition];
	VERSION;                                         Display current database engine version.
	HELP;                                            Displays help information
	EXIT;                                            Exits the program

## Try out these basic commands

1. show databases;

2. use catalog;

3. show tables;

4. desc davisbase_columns;

5. select * from davisbase_columns;

6. select * from davisbase_columns where table_name="davisbase_tables";

7. create database example;

8. CREATE TABLE Employee (ssn INT PRIMARY KEY, fName TEXT NOT NULL, nName TEXT, address TEXT, DOB DATE NOT NULL,  DOB1 DATETIME, ti TINYINT, si SMALLINT, i INT, bi BIGINT, r REAL, d DOUBLE);

9. insert into Employee values (116 , 'ccr241234', 'mxcjkdwhkj', 'uihjh', '1989-11-12', '1989-11-12 12:12:12', 12, 256, 500, 50000, 45.56, 45.7989);

10. update Employee set i=1000 where ssn=116;

11. delete from Employee;

12. drop table Employee;

13. drop database example;

14. exit;