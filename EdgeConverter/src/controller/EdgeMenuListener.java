package controller;

import view.EdgeConvertGUI;

import javax.swing.*;

import utilities.parse.EdgeFileParser;
import utilities.parse.SaveFileParser;
import utilities.parse.XmlFileParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static view.EdgeConvertGUI.*;

public class EdgeMenuListener implements ActionListener {
    private final Controller controller;

    public EdgeMenuListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int returnVal;

        if ((ae.getSource() == EdgeConvertGUI.jmiDTOpenFile) || (ae.getSource() == EdgeConvertGUI.jmiDROpenFile)) {

            jfcOpenFile.addChoosableFileFilter(effEdge);
            jfcOpenFile.addChoosableFileFilter(effSave);
            jfcOpenFile.addChoosableFileFilter(effXML);

            returnVal = jfcOpenFile.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                controller.fileParsed = jfcOpenFile.getSelectedFile();

                switch (controller.fileParsed.getName().substring(controller.fileParsed.getName().indexOf("."))) {
                    case ".edg":
                        controller.fileParser = new EdgeFileParser(controller.fileParsed);
                        break;
                    case ".sav":
                        controller.fileParser = new SaveFileParser(controller.fileParsed);
                        break;
                    case ".xml":
                        controller.fileParser = new XmlFileParser(controller.fileParsed);
                        break;
                }

                controller.tables = controller.fileParser.getTables();
                for (int i = 0; i < controller.tables.length; i++) {
                    controller.tables[i].makeArrays();
                }
                controller.fields = controller.fileParser.getFields();
                controller.fileParser = null;

                controller.populateLists();
                controller.saveFile = null;

                EdgeConvertGUI.jbDRCreateDDL.setEnabled(true);

                File fileParsed = controller.fileParsed;
                controller.truncFilename = fileParsed.getName()
                        .substring(fileParsed.getName().lastIndexOf(File.separator) + 1);
                EdgeConvertGUI.jfDT.setTitle(EdgeConvertGUI.DEFINE_TABLES + " - " + controller.truncFilename);
            } else {
                return;
            }
            controller.savedData = true;
        }
    } // EdgeMenuListener.actionPerformed()
} // EdgeMenuListener