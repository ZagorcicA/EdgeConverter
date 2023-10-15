package utilities.parse;

import dto.Connector;
import dto.Field;
import dto.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public abstract class FileParser {

    public static final String DELIM = "|";

    protected File fileParsed;
    protected FileReader fileRead;
    protected BufferedReader br;
    protected String currentLine;
    protected int numLine;
    protected String tableName;
    protected String fieldName;
    protected int numFigure, numConnector, numFields, numTables, numNativeRelatedFields;
    protected ArrayList alTables, alFields, alConnectors;

    protected Table[] tables;
    protected Field[] fields;
    protected Field tempField;
    protected Connector[] connectors;

    public FileParser(File constrFile) {
        numFigure = 0;
        numConnector = 0;
        alTables = new ArrayList();
        alFields = new ArrayList();
        alConnectors = new ArrayList();
        fileParsed = constrFile;
        numLine = 0;
    }

    public abstract void parse() throws Exception;

    public abstract void openFile(File inputFile);

    public Table[] getTables() {
        return tables;
    }

    public Field[] getFields() {
        return fields;
    }

    protected abstract boolean isTableDup(String testTableName);

    protected void makeArrays() {
        if (alTables != null) {
            tables = (Table[]) alTables.toArray(new Table[alTables.size()]);
        }
        if (alFields != null) {
            fields = (Field[]) alFields.toArray(new Field[alFields.size()]);
        }
        if (alConnectors != null) {
            connectors = (Connector[]) alConnectors.toArray(new Connector[alConnectors.size()]);
        }
    }
}
