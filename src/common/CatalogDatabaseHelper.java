package common;

import datatypes.Int;
import datatypes.Text;
import query.model.parser.DataType;
import exceptions.ExceptionHandler;
import io.IOHandler;
import io.model.DataRecord;
import io.model.InternalColumn;
import io.model.InternalCondition;
import io.model.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CatalogDatabaseHelper {

    public static final byte TABLES_TABLE_SCHEMA_ROWID = 0;
    public static final byte TABLES_TABLE_SCHEMA_DATABASE_NAME = 1;
    public static final byte TABLES_TABLE_SCHEMA_TABLE_NAME = 2;
    public static final byte TABLES_TABLE_SCHEMA_RECORD_COUNT = 3;
    public static final byte TABLES_TABLE_SCHEMA_COL_TBL_ST_ROWID = 4;
    public static final byte TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID = 5;


    public static final byte COLUMNS_TABLE_SCHEMA_ROWID = 0;
    public static final byte COLUMNS_TABLE_SCHEMA_DATABASE_NAME = 1;
    public static final byte COLUMNS_TABLE_SCHEMA_TABLE_NAME = 2;
    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_NAME = 3;
    public static final byte COLUMNS_TABLE_SCHEMA_DATA_TYPE = 4;
    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_KEY = 5;
    public static final byte COLUMNS_TABLE_SCHEMA_ORDINAL_POSITION = 6;
    public static final byte COLUMNS_TABLE_SCHEMA_IS_NULLABLE = 7;

    public static final String PRIMARY_KEY_IDENTIFIER = "PRI";

    public static void InitializeDatabase() {
        File baseDir = new File(DatabaseConstants.DEFAULT_DATA_DIRNAME);
        if(!baseDir.exists()) {
            File catalogDir = new File(DatabaseConstants.DEFAULT_DATA_DIRNAME + "/" + DatabaseConstants.DEFAULT_CATALOG_DATABASENAME);
            if(!catalogDir.exists()) {
                if(catalogDir.mkdirs()) {
                    new CatalogDatabaseHelper().createCatalogDatabase();
                }
            }
        }

    }

    public boolean createCatalogDatabase() {
        try {
            IOHandler manager = new IOHandler();
            manager.createTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME + DatabaseConstants.DEFAULT_FILE_EXTENSION);
            manager.createTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME + DatabaseConstants.DEFAULT_FILE_EXTENSION);
            int startingRowId = this.updateSystemTablesTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, 6);
            startingRowId *= this.updateSystemTablesTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, 8);
            if (startingRowId >= 0) {
                List<InternalColumn> columns = new ArrayList<>();
                columns.add(new InternalColumn("rowid", DataType.INT.toString(), false, false));
                columns.add(new InternalColumn("database_name", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("record_count", DataType.INT.toString(), false, false));
                columns.add(new InternalColumn("col_tbl_st_rowid", DataType.INT.toString(), false, false));
                columns.add(new InternalColumn("nxt_avl_col_tbl_rowid", DataType.INT.toString(), false, false));
                this.updateSystemColumnsTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, 1, columns);
                columns.clear();
                columns.add(new InternalColumn("rowid", DataType.INT.toString(), false, false));
                columns.add(new InternalColumn("database_name", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("column_name", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("data_type", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("column_key", DataType.TEXT.toString(), false, false));
                columns.add(new InternalColumn("ordinal_position", DataType.TINYINT.toString(), false, false));
                columns.add(new InternalColumn("is_nullable", DataType.TEXT.toString(), false, false));
                this.updateSystemColumnsTable(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, 7, columns);
            }
            return true;
        }
        catch (ExceptionHandler e) {
            Utils.printMessage(e.getMessage());
        }
        return false;
    }

    public int updateSystemTablesTable(String databaseName, String tableName, int columnCount) {
        try {
        /*
         * System Tables Table Schema:
         * Column_no    Name                                    Data_type
         *      1       rowid                                   INT
         *      2       database_name                           TEXT
         *      3       table_name                              TEXT
         *      4       record_count                            INT
         *      5       col_tbl_st_rowid                        INT
         *      6       nxt_avl_col_tbl_rowid                   INT
         */
            IOHandler manager = new IOHandler();
            List<InternalCondition> conditions = new ArrayList<>();
            conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));
            conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
            List<DataRecord> result = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, conditions, true);
            if (result != null && result.size() == 0) {
                int returnValue = 1;
                Page<DataRecord> page = manager.getLastRecordAndPage(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME);
                //Check if record exists
                DataRecord lastRecord = null;
                if (page.getPageRecords().size() > 0) {
                    lastRecord = page.getPageRecords().get(0);
                }
                DataRecord record = new DataRecord();
                if (lastRecord == null) {
                    record.setRowId(1);
                } else {
                    record.setRowId(lastRecord.getRowId() + 1);
                }
                record.getColumnValueList().add(new Int(record.getRowId()));
                record.getColumnValueList().add(new Text(databaseName));
                record.getColumnValueList().add(new Text(tableName));
                record.getColumnValueList().add(new Int(0));
                if (lastRecord == null) {
                    record.getColumnValueList().add(new Int(1));
                    record.getColumnValueList().add(new Int(columnCount + 1));
                } else {
                    Int startingColumnIndex = (Int) lastRecord.getColumnValueList().get(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID);
                    returnValue = startingColumnIndex.getValue();
                    record.getColumnValueList().add(new Int(returnValue));
                    record.getColumnValueList().add(new Int(returnValue + columnCount));
                }
                record.populateSize();
                manager.writeRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, record);
                return returnValue;
            } else {
                Utils.printMessage(String.format("Table '%s.%s' already exists.", databaseName, tableName));
                return -1;
            }
        }
        catch (ExceptionHandler e) {
            Utils.printMessage(e.getMessage());
            return -1;
        }
    }

    public boolean updateSystemColumnsTable(String databaseName, String tableName, int startingRowId, List<InternalColumn> columns) {
        try {
        /*
         * System Tables Table Schema:
         * Column_no    Name                                    Data_type
         *      1       rowid                                   INT
         *      2       database_name                           TEXT
         *      3       table_name                              TEXT
         *      4       column_name                             TEXT
         *      5       data_type                               TEXT
         *      6       column_key                              TEXT
         *      7       ordinal_position                        TINYINT
         *      8       is_nullable                             TEXT
         */
            IOHandler manager = new IOHandler();
            if (columns != null && columns.size() == 0) return false;
            int i = 0;
            for (; i < columns.size(); i++) {
                DataRecord record = new DataRecord();
                record.setRowId(startingRowId++);
                record.getColumnValueList().add(new Int(record.getRowId()));
                record.getColumnValueList().add(new Text(databaseName));
                record.getColumnValueList().add(new Text(tableName));
                record.getColumnValueList().add(new Text(columns.get(i).getName()));
                record.getColumnValueList().add(new Text(columns.get(i).getDataType()));
                record.getColumnValueList().add(new Text(columns.get(i).getStringIsPrimary()));
                record.getColumnValueList().add(new Int(i + 1));
                record.getColumnValueList().add(new Text(columns.get(i).getStringIsNullable()));
                record.populateSize();
                if (!manager.writeRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, record)) {
                    break;
                }
            }
            return true;
        }
        catch (ExceptionHandler e) {
            Utils.printMessage(e.getMessage());
        }
        return false;
    }

    public static int incrementRowCount(String databaseName, String tableName) {
        return updateRowCount(databaseName, tableName, 1);
    }

    public static int decrementRowCount(String databaseName, String tableName) {
        return updateRowCount(databaseName, tableName, -1);
    }

    private static int updateRowCount(String databaseName, String tableName, int rowCount) {
        try {
            IOHandler manager = new IOHandler();
            List<InternalCondition> conditions = new ArrayList<>();
            conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
            conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));
            List<Byte> updateColumnsIndexList = new ArrayList<>();
            updateColumnsIndexList.add(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_RECORD_COUNT);
            List<Object> updateValueList = new ArrayList<>();
            updateValueList.add(new Int(rowCount));
            return manager.updateRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, conditions, updateColumnsIndexList, updateValueList, true);
        }
        catch (ExceptionHandler e) {
            Utils.printMessage(e.getMessage());
        }
        return -1;
    }
}

