package datatypes.base;

import common.DatabaseConstants;
import common.Utils;
import datatypes.*;
import query.model.parser.Literal;
public abstract class DataType<T>
{

    protected T value;
    protected boolean isNull;
    protected final byte valueSerialCode;
    protected final byte nullSerialCode;
    public static DataType CreateDT(Literal value)
    {
        switch(value.type)
        {
            case TINYINT:
                return new TinyInt(Byte.valueOf(value.value));
            case SMALLINT:
                return new SmallInt(Short.valueOf(value.value));
            case BIGINT:
                return new BigInt(Long.valueOf(value.value));
            case INT:
                return new Int(Integer.valueOf(value.value));
            case REAL:
                return new Real(Float.valueOf(value.value));
            case DOUBLE:
                return new DataType_Double(Double.valueOf(value.value));
            case DATETIME:
                return new DateTime(Utils.getDateEpoc(value.value, false));
            case DATE:
                return new DateType(Utils.getDateEpoc(value.value, true));
            case TEXT:
                return new Text(value.value);
        }
        return null;
    }
    public static DataType createSystemDT(String value, byte dataType)
    {
        switch(dataType)
        {
            case DatabaseConstants.TINYINT:
                return new TinyInt(Byte.valueOf(value));
            case DatabaseConstants.SMALLINT:
                return new SmallInt(Short.valueOf(value));
            case DatabaseConstants.BIGINT:
                return new BigInt(Long.valueOf(value));
            case DatabaseConstants.INT:
                return new Int(Integer.valueOf(value));
            case DatabaseConstants.REAL:
                return new Real(Float.valueOf(value));
            case DatabaseConstants.DOUBLE:
                return new DataType_Double(Double.valueOf(value));
            case DatabaseConstants.DATETIME:
                return new DateTime(Utils.getDateEpoc(value, false));
            case DatabaseConstants.DATE:
                return new DateType(Utils.getDateEpoc(value, true));
            case DatabaseConstants.TEXT:
                return new Text(value);
        }
        return null;
    }
    protected DataType(int valueSerialCode, int nullSerialCode)
    {
        this.valueSerialCode = (byte) valueSerialCode;
        this.nullSerialCode = (byte) nullSerialCode;
    }
    public T getValue()
    {
        return value;
    }
    public String getStringValue()
    {
        if(value == null)
            return "NULL";
        return value.toString();
    }
    public void setValue(T v)
    {
        this.value = v;
         if (v != null)
             this.isNull = false;
    }
    public boolean isNull()
    {
        return isNull;
    }
    public void setNull(boolean a)
    {
        isNull = a;
    }

    public byte getValueSerialCode()
    {
        return valueSerialCode;
    }
    public byte getNullSerialCode()
    {
        return nullSerialCode;
    }
};