package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;

import java.text.SimpleDateFormat;


public class DateType extends Numeric<Long> {

    public DateType() {
        this(0, true);
    }

    public DateType(Long value) {
        this(value == null ? 0 : value, value == null);
    }

    public DateType(long value, boolean isNull) {
        super(DatabaseConstants.DATE_SERIAL_TYPE_CODE, DatabaseConstants.EIGHT_BYTE_NULL_SERIAL_TYPE_CODE, Long.BYTES);
        this.value = value;
        this.isNull = isNull;
    }

    public String getStringValue() {
        DateType date = new DateType(this.value);
        return new SimpleDateFormat("MM-dd-yyyy").format(date);
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
