package controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dto.Field;
import dto.Table;
import utilities.parse.FileParser;
import view.EdgeConvertGUI;

public class Controller {

    static boolean readTrue = true;
    public EdgeMenuListener menuListener = new EdgeMenuListener(this);
    public EdgeWindowListener edgeWindowListener = new EdgeWindowListener(this);
    public EdgeRadioButtonListener radioButtonListener = new EdgeRadioButtonListener(this);

    private EdgeConvertGUI view;

    public boolean savedData = true;

    public File fileParsed;

    FileParser fileParser;

    public Table[] tables;
    public Field[] fields;

    public File saveFile;
    String truncFilename;

    public File outputDir;

    public ArrayList alSubclasses, alProductNames;

    public String[] productNames;
    public Object[] objSubclasses;

    public String databaseName;

    public static String[] strDataType;

    public Field currentDTField;
    public Table currentDTTable;

    public static boolean getreadTrue() {
        return readTrue;
    }

    public static void setreadTrue(boolean value) {
        readTrue = value;
    }

    public Controller() {
        this.view = new EdgeConvertGUI(this);
    }

    public void populateLists() {
        if (readTrue) {
            EdgeConvertGUI.jfDT.setVisible(true);
            EdgeConvertGUI.jfDR.setVisible(false);
            depopulateLists();
            for (Table table : tables) {
                String tempName = table.getName();
                EdgeConvertGUI.dlmDTTablesAll.addElement(tempName);
                int[] relatedTables = table.getRelatedTablesArray();
                if (relatedTables.length > 0) {
                    EdgeConvertGUI.dlmDRTablesRelations.addElement(tempName);
                }
            }
        }
        readTrue = true;
    }

    public void depopulateLists() {
        EdgeConvertGUI.dlmDTTablesAll.clear();
        EdgeConvertGUI.dlmDTFieldsTablesAll.clear();
        EdgeConvertGUI.dlmDRTablesRelations.clear();
        EdgeConvertGUI.dlmDRFieldsTablesRelations.clear();
        EdgeConvertGUI.dlmDRTablesRelatedTo.clear();
        EdgeConvertGUI.dlmDRFieldsTablesRelatedTo.clear();
    }

    public void enableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            EdgeConvertGUI.jrbDataType[i].setEnabled(true);
        }
        EdgeConvertGUI.jcheckDTPrimaryKey.setEnabled(true);
        EdgeConvertGUI.jcheckDTDisallowNull.setEnabled(true);
        EdgeConvertGUI.jbDTVarchar.setEnabled(true);
        EdgeConvertGUI.jbDTDefaultValue.setEnabled(true);
    }

    public void disableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            EdgeConvertGUI.jrbDataType[i].setEnabled(false);
        }
        EdgeConvertGUI.jcheckDTPrimaryKey.setEnabled(false);
        EdgeConvertGUI.jcheckDTDisallowNull.setEnabled(false);
        EdgeConvertGUI.jbDTDefaultValue.setEnabled(false);
        EdgeConvertGUI.jtfDTVarchar.setText("");
        EdgeConvertGUI.jtfDTDefaultValue.setText("");
    }

    public void setCurrentDTTable(String selText) {
        for (Table table : tables) {
            if (selText.equals(table.getName())) {
                currentDTTable = table;
                return;
            }
        }
    }

    public void setCurrentDTField(String selText) {
        for (Field field : fields) {
            if (selText.equals(field.getName()) && field.getTableID() == currentDTTable.getNumFigure()) {
                currentDTField = field;
                return;
            }
        }
    }

    public String getFieldName(int numFigure) {
        for (Field field : fields) {
            if (field.getNumFigure() == numFigure) {
                return field.getName();
            }
        }
        return "";
    }


    public ListSelectionListener jlDTTablesAllListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent lse) {
            int selIndex = EdgeConvertGUI.jlDTTablesAll.getSelectedIndex();
            if (selIndex >= 0) {
                String selText = EdgeConvertGUI.dlmDTTablesAll.getElementAt(selIndex).toString();
                setCurrentDTTable(selText); // set pointer to the selected table
                int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
                EdgeConvertGUI.jlDTFieldsTablesAll.clearSelection();
                EdgeConvertGUI.dlmDTFieldsTablesAll.removeAllElements();
                EdgeConvertGUI.jbDTMoveUp.setEnabled(false);
                EdgeConvertGUI.jbDTMoveDown.setEnabled(false);
                for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                    EdgeConvertGUI.dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
                }
            }
            disableControls();
        }
    };

}
