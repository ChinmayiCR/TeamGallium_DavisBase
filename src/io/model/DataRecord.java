package io.model;

import common.DatabaseConstants;
import common.Utils;
import datatypes.*;

import java.util.ArrayList;
import java.util.List;

public class DataRecord {

    private List<Object> columnValueList;

    private short size;

    private int rowId;

    private int pageLocated;

    private short offset;

    public DataRecord() {
        size = 0;
        columnValueList = new ArrayList<>();
        pageLocated = -1;
        offset = -1;
    }

    public List<Object> getColumnValueList() {
        return columnValueList;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public short getHeaderSize() {
        return (short)(Short.BYTES + Integer.BYTES);
    }

    public void populateSize() {
        this.size = (short) (this.columnValueList.size() + 1);
        for(Object object: columnValueList) {
            if(object.getClass().equals(TinyInt.class)) {
                this.size += ((TinyInt) object).getSIZE();
            }
            else if(object.getClass().equals(SmallInt.class)) {
                this.size += ((SmallInt) object).getSIZE();
            }
            else if(object.getClass().equals(Int.class)) {
                this.size += ((Int) object).getSIZE();
            }
            else if(object.getClass().equals(BigInt.class)) {
                this.size += ((BigInt) object).getSIZE();
            }
            else if(object.getClass().equals(Real.class)) {
                this.size += ((Real) object).getSIZE();
            }
            else if(object.getClass().equals(DataType_Double.class)) {
                this.size += ((DataType_Double) object).getSIZE();
            }
            else if(object.getClass().equals(DateTime.class)) {
                size += ((DateTime) object).getSIZE();
            }
            else if(object.getClass().equals(DateType.class)) {
                this.size += ((DateType) object).getSIZE();
            }
            else if(object.getClass().equals(Text.class)) {
                this.size += ((Text) object).getSize();
            }
        }
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getPageLocated() {
        return pageLocated;
    }

    public void setPageLocated(int pageLocated) {
        this.pageLocated = pageLocated;
    }

    public short getOffset() {
        return offset;
    }

    public void setOffset(short offset) {
        this.offset = offset;
    }

    public byte[] getSerialTypeCodes() {
        byte[] serialTypeCodes = new byte[columnValueList.size()];
        byte index = 0;
        for(Object object: columnValueList) {
            switch (Utils.resolveClass(object)) {
                case DatabaseConstants.TINYINT:
                    serialTypeCodes[index++] = ((TinyInt) object).getSerialCode();
                    break;

                case DatabaseConstants.SMALLINT:
                    serialTypeCodes[index++] = ((SmallInt) object).getSerialCode();
                    break;

                case DatabaseConstants.INT:
                    serialTypeCodes[index++] = ((Int) object).getSerialCode();
                    break;

                case DatabaseConstants.BIGINT:
                    serialTypeCodes[index++] = ((BigInt) object).getSerialCode();
                    break;

                case DatabaseConstants.REAL:
                    serialTypeCodes[index++] = ((Real) object).getSerialCode();
                    break;

                case DatabaseConstants.DOUBLE:
                    serialTypeCodes[index++] = ((DataType_Double) object).getSerialCode();
                    break;

                case DatabaseConstants.DATETIME:
                    serialTypeCodes[index++] = ((DateTime) object).getSerialCode();
                    break;

                case DatabaseConstants.DATE:
                    serialTypeCodes[index++] = ((DateType) object).getSerialCode();
                    break;

                case DatabaseConstants.TEXT:
                    serialTypeCodes[index++] = ((Text) object).getSerialCode();
                    break;
            }
        }
        return serialTypeCodes;
    }
}