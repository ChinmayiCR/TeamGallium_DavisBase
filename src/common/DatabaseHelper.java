package common;

import datatypes.Int;
import datatypes.Text;
import datatypes.base.DataType;
import exceptions.ExceptionHandler;
import io.IOHandler;
import io.model.DataRecord;
import io.model.InternalCondition;
import query.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static query.Handler.USE_HELP_MESSAGE;

public class DatabaseHelper {

    private static DatabaseHelper databaseHelper = null;

    public static DatabaseHelper getDatabaseHelper() {
        if(databaseHelper == null) {
            return new DatabaseHelper();
        }
        return databaseHelper;
    }

    private IOHandler manager;

    private DatabaseHelper() {
        manager = new IOHandler();
    }

    public boolean databaseExists(String databaseName) {

        if (databaseName == null || databaseName.length() == 0) {
            Handler.UnrecognisedCommand("", USE_HELP_MESSAGE);
            return false;
        }

        return new IOHandler().databaseExists(databaseName);
    }

    public boolean tableExists(String databaseName, String tableName) {
        if (tableName == null || databaseName == null || tableName.length() == 0 || databaseName.length() == 0) {
            Handler.UnrecognisedCommand("", USE_HELP_MESSAGE);
            return false;
        }

        return new IOHandler().checkTableExists(databaseName, tableName);
    }

    public List<String> fetchAllTableColumns(String databaseName, String tableName) throws ExceptionHandler {
        List<String> columnNames = new ArrayList<>();
        List<InternalCondition> conditions = new ArrayList<>();
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));

        List<DataRecord> records = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, false);

        for (DataRecord record : records) {
            Object object = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);
            columnNames.add(((DataType) object).getStringValue());
        }

        return columnNames;
    }

    public boolean checkNullConstraint(String databaseName, String tableName, HashMap<String, Integer> columnMap) throws ExceptionHandler {

        List<InternalCondition> conditions = new ArrayList<>();
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));

        List<DataRecord> records = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, false);

        for (DataRecord record : records) {
            Object nullValueObject = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_IS_NULLABLE);
            Object object = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);

            String isNullStr = ((DataType) nullValueObject).getStringValue().toUpperCase();
            boolean isNullable = isNullStr.equals("YES");

            if (!columnMap.containsKey(((DataType) object).getStringValue()) && !isNullable) {
                Utils.printMessage("ERROR(100N): Field '" + ((DataType) object).getStringValue() + "' cannot be NULL");
                return false;
            }

        }

        return true;
    }

    public HashMap<String, Integer> fetchAllTableColumnDataTypes(String databaseName, String tableName) throws ExceptionHandler {
        List<InternalCondition> conditions = new ArrayList<>();
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));

        List<DataRecord> records = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, false);
        HashMap<String, Integer> columDataTypeMapping = new HashMap<>();

        for (DataRecord record : records) {
            Object object = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);
            Object dataTypeObject = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATA_TYPE);

            String columnName = ((DataType) object).getStringValue();
            int columnDataType = Utils.stringToDataType(((DataType) dataTypeObject).getStringValue());
            columDataTypeMapping.put(columnName.toLowerCase(), columnDataType);
        }

        return columDataTypeMapping;
    }

    public String getTablePrimaryKey(String databaseName, String tableName) throws ExceptionHandler {
        List<InternalCondition> conditions = new ArrayList<>();
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_COLUMN_KEY, InternalCondition.EQUALS, new Text(CatalogDatabaseHelper.PRIMARY_KEY_IDENTIFIER)));

        List<DataRecord> records = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, true);
        String columnName = "";
        if(records.size() > 0) {
            DataRecord record = records.get(0);
            Object object = record.getColumnValueList().get(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);
            columnName = ((DataType) object).getStringValue();
        }

        return columnName;
    }

    public int getTableRecordCount(String databaseName, String tableName) throws ExceptionHandler {
        List<InternalCondition> conditions = new ArrayList<>();
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new Text(databaseName)));
        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_TABLE_NAME, InternalCondition.EQUALS, new Text(tableName)));

        List<DataRecord> records = manager.findRecord(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, conditions, true);
        int recordCount = 0;

        if(records.size() > 0) {
            DataRecord record = records.get(0);
            Object object = record.getColumnValueList().get(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_RECORD_COUNT);
            recordCount = Integer.valueOf(((DataType) object).getStringValue());
        }

        return recordCount;
    }

    public boolean checkIfValueForPrimaryKeyExists(String databaseName, String tableName, int value) throws ExceptionHandler {
        IOHandler manager = new IOHandler();
        InternalCondition condition = InternalCondition.CreateCondition(0, InternalCondition.EQUALS, new Int(value));

        List<DataRecord> records = manager.findRecord(databaseName, tableName, condition, false);
        return records.size() > 0;
    }
}
