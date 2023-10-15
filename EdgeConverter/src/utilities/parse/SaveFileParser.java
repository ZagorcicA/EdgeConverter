package utilities.parse;

import dto.Field;
import dto.Table;

import javax.swing.*;
import java.io.*;
import java.util.StringTokenizer;

public class SaveFileParser extends FileParser {

    public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this

    public SaveFileParser(File constrFile) {
        super(constrFile);
        this.openFile(fileParsed);
    }

    public void parse() throws IOException {
        StringTokenizer stTables, stNatFields, stRelFields, stField;
        Table tempTable;
        Field tempField;
        currentLine = br.readLine();
        currentLine = br.readLine(); //this should be "Table: "
        while (currentLine.startsWith("Table: ")) {
            numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Table number
            currentLine = br.readLine(); //this should be "{"
            currentLine = br.readLine(); //this should be "TableName"
            tableName = currentLine.substring(currentLine.indexOf(" ") + 1);
            tempTable = new Table(numFigure + DELIM + tableName);

            currentLine = br.readLine(); //this should be the NativeFields list
            stNatFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numFields = stNatFields.countTokens();
            for (int i = 0; i < numFields; i++) {
                tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
            }

            currentLine = br.readLine(); //this should be the RelatedTables list
            stTables = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numTables = stTables.countTokens();
            for (int i = 0; i < numTables; i++) {
                tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
            }
            tempTable.makeArrays();

            currentLine = br.readLine(); //this should be the RelatedFields list
            stRelFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
            numFields = stRelFields.countTokens();

            for (int i = 0; i < numFields; i++) {
                tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
            }

            alTables.add(tempTable);
            currentLine = br.readLine(); //this should be "}"
            currentLine = br.readLine(); //this should be "\n"
            currentLine = br.readLine(); //this should be either the next "Table: ", #Fields#
        }
        while ((currentLine = br.readLine()) != null) {
            stField = new StringTokenizer(currentLine, DELIM);
            numFigure = Integer.parseInt(stField.nextToken());
            fieldName = stField.nextToken();
            tempField = new Field(numFigure + DELIM + fieldName);
            tempField.setTableID(Integer.parseInt(stField.nextToken()));
            tempField.setTableBound(Integer.parseInt(stField.nextToken()));
            tempField.setFieldBound(Integer.parseInt(stField.nextToken()));
            tempField.setDataType(Integer.parseInt(stField.nextToken()));
            tempField.setVarcharValue(Integer.parseInt(stField.nextToken()));
            tempField.setIsPrimaryKey(Boolean.parseBoolean(stField.nextToken()));
            tempField.setDisallowNull(Boolean.parseBoolean(stField.nextToken()));
            if (stField.hasMoreTokens()) { //Default Value may not be defined
                tempField.setDefaultValue(stField.nextToken());
            }
            alFields.add(tempField);
        }
    } // parseSaveFile()

    @Override
    public void openFile(File inputFile) {
        try {
            fileRead = new FileReader(inputFile);
            br = new BufferedReader(fileRead);
            //test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(SAVE_ID)) { //the file chosen is an Edge Diagrammer file
                this.parse(); //parse the file
                br.close();
                this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
            } else { //the file chosen is something else
                JOptionPane.showMessageDialog(null, "Unrecognized file format");
            }
        } // try
        catch (FileNotFoundException fnfe) {
            System.exit(0);
        } // catch FileNotFoundException
        catch (IOException ioe) {
            System.exit(0);
        } // catch IOException
    } // openFile()

    @Override
    protected boolean isTableDup(String testTableName) {
        for (Object alTable : alTables) {
            Table tempTable = (Table) alTable;
            if (tempTable.getName().equals(testTableName)) {
                return true;
            }
        }
        return false;
    }
}
