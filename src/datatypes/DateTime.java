package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;

import java.util.Date;


public class DateTime extends Numeric<Long> {

    public DateTime() {
        this(0, true);
    }

    public DateTime(Long value) {
        this(value == null ? 0 : value, value == null);
    }

    public DateTime(long value, boolean isNull) {
        super(DatabaseConstants.DATE_TIME_SERIAL_TYPE_CODE, DatabaseConstants.EIGHT_BYTE_NULL_SERIAL_TYPE_CODE, Long.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    public String getStringValue() {
        Date date = new Date(this.value);
        return date.toString();
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
}
