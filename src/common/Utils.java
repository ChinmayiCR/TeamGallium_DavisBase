package common;

import datatypes.base.Numeric;
import query.model.parser.Condition;
import query.model.parser.DataType;
import query.model.parser.Literal;
import query.model.parser.Operator;
import datatypes.*;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static String getDatabasePath(String databaseName) {
        return DatabaseConstants.DEFAULT_DATA_DIRNAME + "/" + databaseName;
    }

    public static void printMissingDatabaseError(String databaseName) {
        printMessage("ERROR(105D): The database '" + databaseName + "' does not exist");
    }

    public static void printMissingTableError(String database, String tableName) {
        printMessage("ERROR(105T): Table '" + database + "." + tableName + "' doesn't exist.");
    }

    public static void printDuplicateTableError(String database, String tableName) {
        printMessage("ERROR(104T): Table '" + database + "." + tableName + "' already exist.");
    }

    public static void printMessage(String str) {
        System.out.println(str);
    }

    public static void printUnknownColumnValueError(String columnName, String value) {
        printMessage(String.format("ERROR(101): Invalid value: '%s' for column '%s'", value, columnName));
    }

    public static byte resolveClass(Object object) {
        if(object.getClass().equals(TinyInt.class)) {
            return DatabaseConstants.TINYINT;
        }
        else if(object.getClass().equals(SmallInt.class)) {
            return DatabaseConstants.SMALLINT;
        }
        else if(object.getClass().equals(Int.class)) {
            return DatabaseConstants.INT;
        }
        else if(object.getClass().equals(BigInt.class)) {
            return DatabaseConstants.BIGINT;
        }
        else if(object.getClass().equals(Real.class)) {
            return DatabaseConstants.REAL;
        }
        else if(object.getClass().equals(Double.class)) {
            return DatabaseConstants.DOUBLE;
        }
        else if(object.getClass().equals(Date.class)) {
            return DatabaseConstants.DATE;
        }
        else if(object.getClass().equals(DateTime.class)) {
            return DatabaseConstants.DATETIME;
        }
        else if(object.getClass().equals(Text.class)) {
            return DatabaseConstants.TEXT;
        }
        else {
            return DatabaseConstants.INVALID_CLASS;
        }
    }

    static byte stringToDataType(String string) {
        if(string.compareToIgnoreCase("TINYINT") == 0) {
            return DatabaseConstants.TINYINT;
        }
        else if(string.compareToIgnoreCase("SMALLINT") == 0) {
            return DatabaseConstants.SMALLINT;
        }
        else if(string.compareToIgnoreCase("INT") == 0) {
            return DatabaseConstants.INT;
        }
        else if(string.compareToIgnoreCase("BIGINT") == 0) {
            return DatabaseConstants.BIGINT;
        }
        else if(string.compareToIgnoreCase("REAL") == 0) {
            return DatabaseConstants.REAL;
        }
        else if(string.compareToIgnoreCase("DOUBLE") == 0) {
            return DatabaseConstants.DOUBLE;
        }
        else if(string.compareToIgnoreCase("DATE") == 0) {
            return DatabaseConstants.DATE;
        }
        else if(string.compareToIgnoreCase("DATETIME") == 0) {
            return DatabaseConstants.DATETIME;
        }
        else if(string.compareToIgnoreCase("TEXT") == 0) {
            return DatabaseConstants.TEXT;
        }
        else {
            return DatabaseConstants.INVALID_CLASS;
        }
    }

    public static DataType internalDataTypeToModelDataType(byte type) {
        switch (type) {
            case DatabaseConstants.TINYINT:
                return DataType.TINYINT;
            case DatabaseConstants.SMALLINT:
                return DataType.SMALLINT;
            case DatabaseConstants.INT:
                return DataType.INT;
            case DatabaseConstants.BIGINT:
                return DataType.BIGINT;
            case DatabaseConstants.REAL:
                return DataType.REAL;
            case DatabaseConstants.DOUBLE:
                return DataType.DOUBLE;
            case DatabaseConstants.DATE:
                return DataType.DATE;
            case DatabaseConstants.DATETIME:
                return DataType.DATETIME;
            case DatabaseConstants.TEXT:
                return DataType.TEXT;
            default:
                return null;
        }
    }

    public static boolean isvalidDateFormat(String date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        try {
            formatter.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isvalidDateTimeFormat(String date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        try {
            formatter.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static Short ConvertFromOperator(Operator operator) {
        switch (operator){
            case EQUALS: return Numeric.EQUALS;
            case GREATER_THAN_EQUAL: return Numeric.GREATER_THAN_EQUALS;
            case GREATER_THAN: return Numeric.GREATER_THAN;
            case LESS_THAN_EQUAL: return Numeric.LESS_THAN_EQUALS;
            case LESS_THAN: return Numeric.LESS_THAN;
        }

        return null;
    }

    public static boolean checkConditionValueDataTypeValidity(HashMap<String, Integer> columnDataTypeMapping, List<String> columnsList, Condition condition) {
        String invalidColumn = "";
        Literal literal = null;

        if (columnsList.contains(condition.column)) {
            int dataTypeIndex = columnDataTypeMapping.get(condition.column);
            literal = condition.value;

            if (literal.type != Utils.internalDataTypeToModelDataType((byte)dataTypeIndex)) {
                if (Utils.canUpdateLiteralDataType(literal, dataTypeIndex)) {
                    return true;
                }
            }
        }

        boolean valid = invalidColumn.length() <= 0;
        if (!valid) {
            Utils.printUnknownColumnValueError(invalidColumn, literal.value);
        }

        return valid;
    }

    public static long getDateEpoc(String value, Boolean isDate) {
        DateFormat formatter;
        if (isDate) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        else {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        formatter.setLenient(false);
        Date date;
        try {
            date = formatter.parse(value);

            ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(),
                    ZoneId.systemDefault());

            return zdt.toInstant().toEpochMilli() / 1000;
        }
        catch (ParseException ex) {
            return 0;
        }
    }

    public static String getDateEpocAsString(long value, Boolean isDate) {
        ZoneId zoneId = ZoneId.of ("America/Chicago" );

        Instant i = Instant.ofEpochSecond (value);
        ZonedDateTime zdt2 = ZonedDateTime.ofInstant (i, zoneId);
        Date date = Date.from(zdt2.toInstant());

        DateFormat formatter;
        if (isDate) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        else {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        formatter.setLenient(false);

        return formatter.format(date);
    }

    public boolean checkDataTypeValidity(HashMap<String, Integer> columnDataTypeMapping, List<String> columnsList, List<Literal> values) {
        String invalidColumn = "";
        Literal invalidLiteral = null;

        for (int i =0; i < values.size(); i++) {
            String columnName = columnsList.get(i);

            int dataTypeId = columnDataTypeMapping.get(columnName);

            int idx = columnsList.indexOf(columnName);
            Literal literal = values.get(idx);
            invalidLiteral = literal;

            if (literal.type != Utils.internalDataTypeToModelDataType((byte)dataTypeId)) {

                if (Utils.canUpdateLiteralDataType(literal, dataTypeId)) {
                    continue;
                }

                invalidColumn = columnName;
                break;
            }

            if (literal.type != Utils.internalDataTypeToModelDataType((byte)dataTypeId)) {
                invalidColumn = columnName;
                break;
            }
        }

        boolean valid = invalidColumn.length() <= 0;
        if (!valid) {
            Utils.printUnknownColumnValueError(invalidColumn, invalidLiteral.value);
            return false;
        }

        return true;
    }

    public static boolean RecursiveDeletion(File file){
        if(file == null) return true;
        boolean isDeleted;

        if(file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                if (childFile.isFile()) {
                    isDeleted = childFile.delete();
                    if (!isDeleted) return false;
                } else {
                    isDeleted = RecursiveDeletion(childFile);
                    if (!isDeleted) return false;
                }
            }
        }

        return file.delete();
    }

    public static boolean canUpdateLiteralDataType(Literal literal, int columnType) {
        if (columnType == DatabaseConstants.TINYINT) {
            if (literal.type == DataType.INT) {
                if (Integer.parseInt(literal.value) <= Byte.MAX_VALUE) {
                    literal.type = DataType.TINYINT;
                    return true;
                }
            }
        } else if (columnType == DatabaseConstants.SMALLINT) {
            if (literal.type == DataType.INT) {
                if (Integer.parseInt(literal.value) <= Short.MAX_VALUE) {
                    literal.type = DataType.SMALLINT;
                    return true;
                }
            }
        } else if (columnType == DatabaseConstants.BIGINT) {
            if (literal.type == DataType.INT) {
                if (Integer.parseInt(literal.value) <= Long.MAX_VALUE) {
                    literal.type = DataType.BIGINT;
                    return true;
                }
            }
        } else if (columnType == DatabaseConstants.DOUBLE) {
            if (literal.type == DataType.REAL) {
                literal.type = DataType.DOUBLE;
                return true;
            }
        }
        return false;
    }
}
