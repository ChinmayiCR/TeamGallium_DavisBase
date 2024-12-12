package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;


public class Int extends Numeric<Integer> {

    public Int() {
        this(0, true);
    }

    public Int(Integer value) {
        this(value == null ? 0 : value, value == null);
    }

    public Int(int value, boolean isNull) {
        super(DatabaseConstants.INT_SERIAL_TYPE_CODE, DatabaseConstants.FOUR_BYTE_NULL_SERIAL_TYPE_CODE, Integer.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    @Override
    public void increment(Integer value) {
        this.value += value;
    }

    @Override
    public boolean compare(Numeric<Integer> object2, short condition) {
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
        Int object = new Int(object2.getValue(), false);
        return this.compare(object, condition);
    }

    public boolean compare(SmallInt object2, short condition) {
        Int object = new Int(object2.getValue(), false);
        return this.compare(object, condition);
    }

    public boolean compare(BigInt object2, short condition) {
        BigInt object = new BigInt(value, false);
        return object.compare(object2, condition);
    }
}
