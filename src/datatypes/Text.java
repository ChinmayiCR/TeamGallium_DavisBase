package datatypes;

import common.DatabaseConstants;
import datatypes.base.DataType;


public class Text extends DataType<String> {

    public Text() {
        this("", true);
    }

    public Text(String value) {
        this(value, value == null);
    }

    public Text(String value, boolean isNull) {
        super(DatabaseConstants.TEXT_SERIAL_TYPE_CODE, DatabaseConstants.ONE_BYTE_NULL_SERIAL_TYPE_CODE);
        this.value = value;
        this.isNull = isNull;
    }

    public byte getSerialCode() {
        if(isNull)
            return nullSerialCode;
        else
            return (byte)(valueSerialCode + this.value.length());
    }

    public int getSize() {
        if(isNull)
            return 0;
        return this.value.length();
    }
}
