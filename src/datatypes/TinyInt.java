package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;


public class TinyInt extends Numeric<Byte> {

    public TinyInt() {
        this((byte) 0, true);
    }

    public TinyInt(Byte value) {
        this(value == null ? 0 : value, value == null);
    }

    public TinyInt(byte value, boolean isNull) {
        super(DatabaseConstants.TINY_INT_SERIAL_TYPE_CODE, DatabaseConstants.ONE_BYTE_NULL_SERIAL_TYPE_CODE, Byte.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    @Override
    public void increment(Byte value) {
        this.value = (byte)(this.value + value);
    }

    @Override
    public boolean compare(Numeric<Byte> object2, short condition) {
        if(value == null) return false;
        switch (condition) {
            case Numeric.EQUALS:
                return value == object2.getValue();

            case Numeric.GREATER_THAN:
                return value > object2.getValue();

            case Numeric.LESS_THAN:
                return value < object2.getValue();

            case Numeric.GREATER_THAN_EQUALS:
                return value >= object2.getValue();

            case Numeric.LESS_THAN_EQUALS:
                return value <= object2.getValue();

            default:
                return false;
        }
    }

    public boolean compare(SmallInt object2, short condition) {
        SmallInt object = new SmallInt(value, false);
        return object.compare(object2, condition);
    }

    public boolean compare(Int object2, short condition) {
        Int object = new Int(value, false);
        return object.compare(object2, condition);
    }

    public boolean compare(BigInt object2, short condition) {
        BigInt object = new BigInt(value, false);
        return object.compare(object2, condition);
    }
}
