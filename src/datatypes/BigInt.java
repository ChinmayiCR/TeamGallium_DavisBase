package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;


public class BigInt extends Numeric<Long> {

    public BigInt() {
        this(0, true);
    }

    public BigInt(Long value) {
        this(value == null ? 0 : value, value == null);
    }

    public BigInt(long value, boolean isNull) {
        super(DatabaseConstants.BIG_INT_SERIAL_TYPE_CODE, DatabaseConstants.EIGHT_BYTE_NULL_SERIAL_TYPE_CODE, Long.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    @Override
    public void increment(Long value) {
        this.value += value;
    }

    @Override
    public boolean compare(Numeric<Long> object2, short condition) {
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
        BigInt object = new BigInt(object2.getValue(), false);
        return this.compare(object, condition);
    }

    public boolean compare(SmallInt object2, short condition) {
        BigInt object = new BigInt(object2.getValue(), false);
        return this.compare(object, condition);
    }

    public boolean compare(Int object2, short condition) {
        BigInt object = new BigInt(object2.getValue(), false);
        return this.compare(object, condition);
    }
}
