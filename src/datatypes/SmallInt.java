package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;


public class SmallInt extends Numeric<Short> {

    public SmallInt() {
        this((short) 0, true);
    }

    public SmallInt(Short value) {
        this(value == null ? 0 : value, value == null);
    }

    public SmallInt(short value, boolean isNull) {
        super(DatabaseConstants.SMALL_INT_SERIAL_TYPE_CODE, DatabaseConstants.TWO_BYTE_NULL_SERIAL_TYPE_CODE, Short.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    @Override
    public void increment(Short value) {
        this.value = (short)(this.value + value);
    }

    @Override
    public boolean compare(Numeric<Short> object2, short condition) {
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

    public boolean compare(TinyInt object2, short condition) {
        SmallInt object = new SmallInt(object2.getValue(), false);
        return this.compare(object, condition);
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
