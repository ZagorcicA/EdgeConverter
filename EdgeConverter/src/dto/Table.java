package dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import utilities.parse.FileParser;

public class Table {
    private final int numFigure;
    private final String name;
    private final ArrayList alRelatedTables;
    private final ArrayList alNativeFields;
    private int[] relatedTables, relatedFields, nativeFields;

    public Table(String inputString) {
        StringTokenizer st = new StringTokenizer(inputString, FileParser.DELIM);
        numFigure = Integer.parseInt(st.nextToken());
        name = st.nextToken();
        alRelatedTables = new ArrayList<>();
        alNativeFields = new ArrayList<>();
    }

    public int getNumFigure() {
        return numFigure;
    }

    public String getName() {
        return name;
    }

    public void addRelatedTable(int relatedTable) {
        alRelatedTables.add(relatedTable);
    }

    public int[] getRelatedTablesArray() {
        return relatedTables;
    }

    public int[] getRelatedFieldsArray() {
        return relatedFields;
    }

    public void setRelatedField(int index, int relatedValue) {
        relatedFields[index] = relatedValue;
    }

    public int[] getNativeFieldsArray() {
        return nativeFields;
    }

    public void addNativeField(int value) {
        alNativeFields.add(value);
    }

    public void moveFieldUp(int index) { // move the field closer to the beginning of the list
        if (index == 0) {
            return;
        }
        int tempNative = nativeFields[index - 1]; // save element at destination index
        nativeFields[index - 1] = nativeFields[index]; // copy target element to destination
        nativeFields[index] = tempNative; // copy saved element to target's original location
        int tempRelated = relatedFields[index - 1]; // save element at destination index
        relatedFields[index - 1] = relatedFields[index]; // copy target element to destination
        relatedFields[index] = tempRelated; // copy saved element to target's original location
    }

    public void moveFieldDown(int index) { // move the field closer to the end of the list
        if (index == (nativeFields.length - 1)) {
            return;
        }
        int tempNative = nativeFields[index + 1]; // save element at destination index
        nativeFields[index + 1] = nativeFields[index]; // copy target element to destination
        nativeFields[index] = tempNative; // copy saved element to target's original location
        int tempRelated = relatedFields[index + 1]; // save element at destination index
        relatedFields[index + 1] = relatedFields[index]; // copy target element to destination
        relatedFields[index] = tempRelated; // copy saved element to target's original location
    }

    public void makeArrays() { // convert the ArrayLists into int[]
        Integer[] temp;
        temp = (Integer[]) alNativeFields.toArray(new Integer[alNativeFields.size()]);
        nativeFields = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            nativeFields[i] = temp[i];
        }

        temp = (Integer[]) alRelatedTables.toArray(new Integer[alRelatedTables.size()]);
        relatedTables = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            relatedTables[i] = temp[i];
        }

        relatedFields = new int[nativeFields.length];
        Arrays.fill(relatedFields, 0);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Table: " + numFigure + "\r\n");
        sb.append("{\r\n");
        sb.append("TableName: " + name + "\r\n");
        sb.append("NativeFields: ");
        for (int i = 0; i < nativeFields.length; i++) {
            sb.append(nativeFields[i]);
            if (i < (nativeFields.length - 1)) {
                sb.append(FileParser.DELIM);
            }
        }
        sb.append("\r\nRelatedTables: ");
        for (int i = 0; i < relatedTables.length; i++) {
            sb.append(relatedTables[i]);
            if (i < (relatedTables.length - 1)) {
                sb.append(FileParser.DELIM);
            }
        }
        sb.append("\r\nRelatedFields: ");
        for (int i = 0; i < relatedFields.length; i++) {
            sb.append(relatedFields[i]);
            if (i < (relatedFields.length - 1)) {
                sb.append(FileParser.DELIM);
            }
        }
        sb.append("\r\n}\r\n");

        return sb.toString();
    }
}
