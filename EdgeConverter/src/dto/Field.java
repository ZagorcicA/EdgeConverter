package dto;

import java.util.StringTokenizer;

import utilities.parse.FileParser;

public class Field {

    private static final String[] strDataType = { "Varchar", "Boolean", "Integer", "Double" };
    public static int VARCHAR_DEFAULT_LENGTH = 1;

    private final int numFigure;
    private int tableID;
    private int tableBound;
    private int fieldBound;
    private int dataType;
    private int varcharValue;
    private final String name;
    private String defaultValue;
    private boolean disallowNull, isPrimaryKey;

    public Field(String inputString) {
        StringTokenizer st = new StringTokenizer(inputString, FileParser.DELIM);
        numFigure = Integer.parseInt(st.nextToken());
        name = st.nextToken();
        tableID = 0;
        tableBound = 0;
        fieldBound = 0;
        disallowNull = false;
        isPrimaryKey = false;
        defaultValue = "";
        varcharValue = 1;
        dataType = 0;
    }

    public int getNumFigure() {
        return numFigure;
    }

    public String getName() {
        return name;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getTableBound() {
        return tableBound;
    }

    public void setTableBound(int value) {
        tableBound = value;
    }

    public int getFieldBound() {
        return fieldBound;
    }

    public void setFieldBound(int value) {
        fieldBound = value;
    }

    public boolean getDisallowNull() {
        return disallowNull;
    }

    public void setDisallowNull(boolean value) {
        disallowNull = value;
    }

    public boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean value) {
        isPrimaryKey = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String value) {
        defaultValue = value;
    }

    public int getVarcharValue() {
        return varcharValue;
    }

    public void setVarcharValue(int value) {
        if (value > 0) {
            varcharValue = value;
        }
    }

    public int getDataType() {
        return dataType;
    }

    public static String[] getStrDataType() {
        return strDataType;
    }

    public void setDataType(int value) {
        if (value >= 0 && value < strDataType.length) {
            dataType = value;
        }
    }

    public String toString() {
        return numFigure + FileParser.DELIM +
                name + FileParser.DELIM +
                tableID + FileParser.DELIM +
                tableBound + FileParser.DELIM +
                fieldBound + FileParser.DELIM +
                dataType + FileParser.DELIM +
                varcharValue + FileParser.DELIM +
                isPrimaryKey + FileParser.DELIM +
                disallowNull + FileParser.DELIM +
                defaultValue;
    }
}
