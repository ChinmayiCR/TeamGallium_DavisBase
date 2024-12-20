package query.model.parser;

import common.DatabaseConstants;
import query.Handler;
import common.Utils;


public class Literal {
    public DataType type;
    public String value;

    public static Literal CreateLiteral(datatypes.base.DataType value, Byte type) {
        if(type == DatabaseConstants.INVALID_CLASS) {
            return null;
        }
        else if (value.isNull()) {
            return new Literal(DataType.DOUBLE_DATETIME_NULL, value.getStringValue());
        }

        switch(type) {
            case DatabaseConstants.TINYINT:
                return new Literal(DataType.TINYINT, value.getStringValue());
            case DatabaseConstants.SMALLINT:
                return new Literal(DataType.SMALLINT, value.getStringValue());
            case DatabaseConstants.INT:
                return new Literal(DataType.INT, value.getStringValue());
            case DatabaseConstants.BIGINT:
                return new Literal(DataType.BIGINT, value.getStringValue());
            case DatabaseConstants.REAL:
                return new Literal(DataType.REAL, value.getStringValue());
            case DatabaseConstants.DOUBLE:
                return new Literal(DataType.DOUBLE, value.getStringValue());
            case DatabaseConstants.DATE:
                return new Literal(DataType.DATE, Utils.getDateEpocAsString((long)value.getValue(), true));
            case DatabaseConstants.DATETIME:
                return new Literal(DataType.DATETIME, Utils.getDateEpocAsString((long)value.getValue(), false));
            case DatabaseConstants.TEXT:
                return new Literal(DataType.TEXT, value.getStringValue());
        }

        return null;
    }

    public static Literal CreateLiteral(String literalString){
        if(literalString.startsWith("'") && literalString.endsWith("'")){
            literalString = literalString.substring(1, literalString.length()-1);

            if (Utils.isvalidDateTimeFormat(literalString)) {
                return new Literal(DataType.DATETIME, literalString);
            }

            if (Utils.isvalidDateFormat(literalString)) {
                return new Literal(DataType.DATE, literalString);
            }

            return new Literal(DataType.TEXT, literalString);
        }

        if(literalString.startsWith("\"") && literalString.endsWith("\"")){
            literalString = literalString.substring(1, literalString.length()-1);

            if (Utils.isvalidDateTimeFormat(literalString)) {
                return new Literal(DataType.DATETIME, literalString);
            }

            if (Utils.isvalidDateFormat(literalString)) {
                return new Literal(DataType.DATE, literalString);
            }

            return new Literal(DataType.TEXT, literalString);
        }

        try{
            Integer.parseInt(literalString);
            return new Literal(DataType.INT, literalString);
        }
        catch (Exception e){}

        try{
            Long.parseLong(literalString);
            return new Literal(DataType.BIGINT, literalString);
        }
        catch (Exception e){}

        try{
            Double.parseDouble(literalString);
            return new Literal(DataType.REAL, literalString);
        }
        catch (Exception e){}

            Handler.UnrecognisedCommand(literalString, "Unrecognised Literal Found. Please use integers, real or strings ");
        return null;
    }

    private Literal(DataType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        if (this.type == DataType.TEXT) {
            return this.value;
        } else if (this.type == DataType.INT || this.type == DataType.TINYINT ||
                this.type == DataType.SMALLINT || this.type == DataType.BIGINT) {
            return this.value;
        } else if (this.type == DataType.REAL || this.type == DataType.DOUBLE) {
            return String.format("%.2f", Double.parseDouble(this.value));
        } else if (this.type == DataType.INT_REAL_NULL || this.type == DataType.SMALL_INT_NULL || this.type == DataType.TINY_INT_NULL || this.type == DataType.DOUBLE_DATETIME_NULL) {
            return "NULL";
        } else if (this.type == DataType.DATE || this.type == DataType.DATETIME) {
            return this.value;
        }

        return "";
    }
}
