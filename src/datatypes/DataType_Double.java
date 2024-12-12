package datatypes;

import common.DatabaseConstants;
import datatypes.base.Numeric;
public class DataType_Double extends Numeric<Double>
{
    public DataType_Double()
    {
        this(0, true);
    }
    public DataType_Double(Double value)
    {
        this(value == null ? 0 : value, value == null);
    }
    public DataType_Double(double value, boolean isNull)
    {
        super(DatabaseConstants.DOUBLE_SERIAL_TYPE_CODE, DatabaseConstants.EIGHT_BYTE_NULL_SERIAL_TYPE_CODE, Double.BYTES);
        this.value = value;
        this.isNull = isNull;
    }
    @Override
    public void increment(Double value)
    {
        this.value += value;
    }
    @Override
    public boolean compare(Numeric<Double> object2, short condition)
    {
        if(value == null) 
        	return false;
        switch (condition)
        {
            case Numeric.EQUALS:
                return Double.doubleToLongBits(value) == Double.doubleToLongBits(object2.getValue());
            case Numeric.GREATER_THAN:
                return value > object2.getValue();
            case Numeric.LESS_THAN:
                return value < object2.getValue();
            case Numeric.GREATER_THAN_EQUALS:
                return Double.doubleToLongBits(value) >= Double.doubleToLongBits(object2.getValue());
            case Numeric.LESS_THAN_EQUALS:
                return Double.doubleToLongBits(value) <= Double.doubleToLongBits(object2.getValue());
            default:
                return false;
        }
    }
    public boolean compare(Real object2, short condition)
    {
        DataType_Double object = new DataType_Double(object2.getValue(), false);
        return this.compare(object, condition);
    }
};