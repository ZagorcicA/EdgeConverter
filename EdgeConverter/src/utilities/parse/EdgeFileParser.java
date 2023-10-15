package utilities.parse;

import controller.Controller;
import dto.Connector;
import dto.Field;
import dto.Table;

import javax.swing.*;
import java.io.*;

public class EdgeFileParser extends FileParser {

    public static final String EDGE_ID = "EDGE Diagram File"; 
    private Connector[] connectors;
    private boolean isEntity, isAttribute, isUnderlined = false;


    public EdgeFileParser(File constrFile) {
        super(constrFile);
        isEntity = false;
        isAttribute = false;
        this.openFile(fileParsed);
    }

    public void parse() throws IOException {
        while ((currentLine = br.readLine()) != null) {
            currentLine = currentLine.trim();
            if (currentLine.startsWith("Figure ")) { //this is the start of a Figure entry
                numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
                currentLine = br.readLine().trim(); // this should be "{"
                currentLine = br.readLine().trim();
                currentLine = br.readLine().trim(); // this should be "SheetNumber 1"
                if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
                    continue;
                } else {
                    String style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the Style parameter
                    if (style.startsWith("Relation")) { //presence of Relations implies lack of normalization
                        JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + fileParsed + "\ncontains relations.  Please resolve them and try again.");
                        Controller.setreadTrue(false);
                        break;
                    }
                    if (style.startsWith("Entity")) {
                        isEntity = true;
                    }
                    if (style.startsWith("Attribute")) {
                        isAttribute = true;
                    }
                    if (!(isEntity || isAttribute)) { //these are the only Figures we're interested in
                        continue;
                    }
                    currentLine = br.readLine().trim(); //this should be Text
                    String text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", ""); //get the Text parameter
                    if (text.equals("")) {
                        JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
                        Controller.setreadTrue(false);
                        break;
                    }
                    int escape = text.indexOf("\\");
                    if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
                        text = text.substring(0, escape);
                    }

                    do { //advance to end of record, look for whether the text is underlined
                        currentLine = br.readLine().trim();
                        if (currentLine.startsWith("TypeUnderl")) {
                            isUnderlined = true;
                        }
                    } while (!currentLine.equals("}")); // this is the end of a Figure entry

                    if (isEntity) { //create a new EdgeTable object and add it to the alTables ArrayList
                        if (isTableDup(text)) {
                            JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
                            Controller.setreadTrue(false);
                            break;
                        }
                       // System.out.println("Content: " + numFigure + DELIM + text);
                        alTables.add(new Table(numFigure + DELIM + text));
                    }
                    if (isAttribute) { //create a new EdgeField object and add it to the alFields ArrayList
                        // Needs to be agnostic (they currently contain edge specific implementations)
                        Field tempField = new Field(numFigure + DELIM + text);
                        tempField.setIsPrimaryKey(isUnderlined);
                        alFields.add(tempField);
                    }
                    //reset flags
                    isEntity = false;
                    isAttribute = false;
                    isUnderlined = false;
                }
            } // if("Figure")
            if (currentLine.startsWith("Connector ")) { //this is the start of a Connector entry
                numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Connector number
                currentLine = br.readLine().trim(); // this should be "{"
                currentLine = br.readLine().trim(); // not interested in SheetNumber 1
                currentLine = br.readLine().trim(); // not interested in Style
                currentLine = br.readLine().trim(); // Figure1
                int endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                currentLine = br.readLine().trim(); // Figure2
                int endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                currentLine = br.readLine().trim(); // not interested in EndPoint1
                currentLine = br.readLine().trim(); // not interested in EndPoint2
                currentLine = br.readLine().trim(); // not interested in SuppressEnd1
                currentLine = br.readLine().trim(); // not interested in SuppressEnd2
                currentLine = br.readLine().trim(); // End1
                String endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End1 parameter
                currentLine = br.readLine().trim(); // End2
                String endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the End2 parameter

                do { //advance to end of record
                    currentLine = br.readLine().trim();
                } while (!currentLine.equals("}")); // this is the end of a Connector entry

                alConnectors.add(new Connector(numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
            } // if("Connector")
        } // while()
    } // parseEdgeFile()

    protected void resolveConnectors() { //Identify nature of Connector endpoints
        int endPoint1, endPoint2;
        int fieldIndex = 0, table1Index = 0, table2Index = 0;
        for (Connector connector : connectors) {
            endPoint1 = connector.getEndPoint1();
            endPoint2 = connector.getEndPoint2();
            fieldIndex = -1;
            for (int fIndex = 0; fIndex < fields.length; fIndex++) { //search fields array for endpoints
                if (endPoint1 == fields[fIndex].getNumFigure()) { //found endPoint1 in fields array
                    connector.setIsEP1Field(true); //set appropriate flag
                    fieldIndex = fIndex; //identify which element of the fields array that endPoint1 was found in
                }
                if (endPoint2 == fields[fIndex].getNumFigure()) { //found endPoint2 in fields array
                    connector.setIsEP2Field(true); //set appropriate flag
                    fieldIndex = fIndex; //identify which element of the fields array that endPoint2 was found in
                }
            }
            for (int tIndex = 0; tIndex < tables.length; tIndex++) { //search tables array for endpoints
                if (endPoint1 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
                    connector.setIsEP1Table(true); //set appropriate flag
                    table1Index = tIndex; //identify which element of the tables array that endPoint1 was found in
                }
                if (endPoint2 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
                    connector.setIsEP2Table(true); //set appropriate flag
                    table2Index = tIndex; //identify which element of the tables array that endPoint2 was found in
                }
            }

            if (connector.getIsEP1Field() && connector.getIsEP2Field()) { //both endpoints are fields, implies lack of normalization
                JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + fileParsed + "\ncontains composite attributes. Please resolve them and try again.");
                Controller.setreadTrue(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }

            if (connector.getIsEP1Table() && connector.getIsEP2Table()) { //both endpoints are tables
                if ((connector.getEndStyle1().contains("many")) &&
                        (connector.getEndStyle2().contains("many"))) { //the connector represents a many-many relationship, implies lack of normalization
                    JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getName() + "\" and \"" + tables[table2Index].getName() + "\"" + "\nPlease resolve this and try again.");
                    Controller.setreadTrue(false); //this tells GUI not to populate JList components
                    break; //stop processing list of Connectors
                } else { //add Figure number to each table's list of related tables
                    tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
                    tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
                    continue; //next Connector
                }
            }

            if (fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0) { //field has not been assigned to a table yet
                if (connector.getIsEP1Table()) { //endpoint1 is the table
                    tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
                } else { //endpoint2 is the table
                    tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
                }
            } else if (fieldIndex >= 0) { //field has already been assigned to a table
                JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
                Controller.setreadTrue(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }
        } // connectors for() loop
    } // resolveConnectors()

    protected void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type
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

    protected boolean isTableDup(String testTableName) {
        for (Object alTable : alTables) {
            Table tempTable = (Table) alTable;
            if (tempTable.getName().equals(testTableName)) {
                return true;
            }
        }
        return false;
    }

    public void openFile(File inputFile) {
        try {
            fileRead = new FileReader(inputFile);
            br = new BufferedReader(fileRead);
            //test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
                this.parse(); //parse the file
                br.close();
                this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
                this.resolveConnectors(); //Identify nature of Connector endpoints
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
} // EdgeConvertFileHandler