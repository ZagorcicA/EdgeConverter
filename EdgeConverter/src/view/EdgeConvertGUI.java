package view;


import controller.Controller;
import dto.Field;
import utilities.filter.FileFiltering;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EdgeConvertGUI {

    public Controller controller;
    public static final int HORIZ_SIZE = 635;
    public static final int VERT_SIZE = 400;
    public static final int HORIZ_LOC = 100;
    public static final int VERT_LOC = 100;
    public static final String DEFINE_TABLES = "Define Tables";
    public static final String DEFINE_RELATIONS = "Define Relations";
    public static final String CANCELLED = "CANCELLED";
    //Define Tables screen objects
    public static JFrame jfDT;
    public static JButton jbDTCreateDDL,jbDTDefineRelations,jbDTVarchar,jbDTDefaultValue,jbDTMoveUp,jbDTMoveDown;
    static ButtonGroup bgDTDataType;
    public static JRadioButton[] jrbDataType;
    // public static String[] strDataType;
    public static JCheckBox jcheckDTDisallowNull,jcheckDTPrimaryKey;
    public static JTextField jtfDTVarchar,jtfDTDefaultValue;
    public static JList jlDTTablesAll,jlDTFieldsTablesAll;
    public static DefaultListModel dlmDTTablesAll,dlmDTFieldsTablesAll;
    public static JMenuItem jmiDTOpenFile, jmiDTExit, jmiDTOptionsOutputLocation, jmiDTOptionsShowProducts;
    //Define Relations screen objects
    public static JFrame jfDR;
    public static JPanel jpDRBottom, jpDRCenter, jpDRCenter1, jpDRCenter2, jpDRCenter3, jpDRCenter4;
    public static JButton jbDRCreateDDL, jbDRDefineTables, jbDRBindRelation;
    public static JList jlDRTablesRelations, jlDRTablesRelatedTo, jlDRFieldsTablesRelations, jlDRFieldsTablesRelatedTo;
    public static DefaultListModel dlmDRTablesRelations, dlmDRTablesRelatedTo, dlmDRFieldsTablesRelations, dlmDRFieldsTablesRelatedTo;
    public static JLabel jlabDRTablesRelations, jlabDRTablesRelatedTo, jlabDRFieldsTablesRelations, jlabDRFieldsTablesRelatedTo;
    public static JScrollPane jspDRTablesRelations, jspDRTablesRelatedTo, jspDRFieldsTablesRelations, jspDRFieldsTablesRelatedTo;
    public static JMenuBar jmbDRMenuBar;
    public static JMenu jmDRFile, jmDROptions;
    public static JMenuItem jmiDROpenFile, jmiDRExit ;
    public static JFileChooser jfcOpenFile, jfcGetClass;
    public static FileFiltering effEdge, effSave,effXML, effClass;
    static JPanel jpDTBottom, jpDTCenter, jpDTCenter1, jpDTCenter2, jpDTCenterRight, jpDTCenterRight1, jpDTCenterRight2, jpDTMove;

    static JLabel jlabDTTables, jlabDTFields;
    static JScrollPane jspDTTablesAll, jspDTFieldsTablesAll;
    static JMenuBar jmbDTMenuBar;
    static JMenu jmDTFile;

    public EdgeConvertGUI(Controller controller) {
        this.controller = controller;
        this.showGUI();
    } // EdgeConverterGUI.EdgeConverterGUI()

    public void showGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //use the OS native LAF, as opposed to default Java LAF
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
        createDTScreen();
        createDRScreen();
    } //showGUI()

    public void createDTScreen() {//create Define Tables screen
        jfDT = new JFrame(DEFINE_TABLES);
        jfDT.setLocation(HORIZ_LOC, VERT_LOC);
        Container cp = jfDT.getContentPane();
        jfDT.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jfDT.addWindowListener(controller.edgeWindowListener);
        jfDT.getContentPane().setLayout(new BorderLayout());
        jfDT.setVisible(true);
        jfDT.setSize(HORIZ_SIZE + 150, VERT_SIZE);

        //setup menubars and menus
        jmbDTMenuBar = new JMenuBar();
        jfDT.setJMenuBar(jmbDTMenuBar);

        jmDTFile = new JMenu("File");
        jmDTFile.setMnemonic(KeyEvent.VK_F);
        jmbDTMenuBar.add(jmDTFile);
        jmiDTOpenFile = new JMenuItem("Open File...");
        jmiDTOpenFile.setMnemonic(KeyEvent.VK_E);
        jmiDTOpenFile.addActionListener(controller.menuListener);
        jmiDTExit = new JMenuItem("Exit");
        jmiDTExit.setMnemonic(KeyEvent.VK_X);
        jmiDTExit.addActionListener(controller.menuListener);
        jmDTFile.add(jmiDTOpenFile);
        jmDTFile.add(jmiDTExit);

        jmiDTOptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
        jmiDTOptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDTOptionsOutputLocation.addActionListener(controller.menuListener);
        jmiDTOptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDTOptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDTOptionsShowProducts.setEnabled(false);
        jmiDTOptionsShowProducts.addActionListener(controller.menuListener);


        jfcOpenFile = new JFileChooser();
        jfcOpenFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
        effEdge = new FileFiltering("edg", "Edge Diagrammer Files");
        effSave = new FileFiltering("sav", "Edge Convert Save Files");
        effXML = new FileFiltering("xml", "XML Files");

        jpDTBottom = new JPanel(new GridLayout(1, 2));

        jbDTCreateDDL = new JButton("Create DDL");
        jbDTCreateDDL.setEnabled(false);

        jbDTDefineRelations = new JButton(DEFINE_RELATIONS);
        jbDTDefineRelations.setEnabled(false);
        jbDTDefineRelations.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jpDTBottom.add(jbDTDefineRelations);
        jpDTBottom.add(jbDTCreateDDL);
        jfDT.getContentPane().add(jpDTBottom, BorderLayout.SOUTH);

        jpDTCenter = new JPanel(new GridLayout(1, 3));
        jpDTCenterRight = new JPanel(new GridLayout(1, 2));
        dlmDTTablesAll = new DefaultListModel();
        jlDTTablesAll = new JList(dlmDTTablesAll);
        jlDTTablesAll.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
                controller.jlDTTablesAllListener.valueChanged(e);
            }

        });

        dlmDTFieldsTablesAll = new DefaultListModel();
        jlDTFieldsTablesAll = new JList(dlmDTFieldsTablesAll);
        jlDTFieldsTablesAll.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jpDTMove = new JPanel(new GridLayout(2, 1));
        jbDTMoveUp = new JButton("^");
        jbDTMoveUp.setEnabled(false);
        jbDTMoveUp.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });
        jbDTMoveDown = new JButton("v");
        jbDTMoveDown.setEnabled(false);
        jbDTMoveDown.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });
        jpDTMove.add(jbDTMoveUp);
        jpDTMove.add(jbDTMoveDown);

        jspDTTablesAll = new JScrollPane(jlDTTablesAll);
        jspDTFieldsTablesAll = new JScrollPane(jlDTFieldsTablesAll);
        jpDTCenter1 = new JPanel(new BorderLayout());
        jpDTCenter2 = new JPanel(new BorderLayout());
        jlabDTTables = new JLabel("All Tables", SwingConstants.CENTER);
        jlabDTFields = new JLabel("Fields List", SwingConstants.CENTER);
        jpDTCenter1.add(jlabDTTables, BorderLayout.NORTH);
        jpDTCenter2.add(jlabDTFields, BorderLayout.NORTH);
        jpDTCenter1.add(jspDTTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jspDTFieldsTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jpDTMove, BorderLayout.EAST);
        jpDTCenter.add(jpDTCenter1);
        jpDTCenter.add(jpDTCenter2);
        jpDTCenter.add(jpDTCenterRight);

        controller.strDataType = Field.getStrDataType(); //get the list of currently supported data types
        jrbDataType = new JRadioButton[controller.strDataType.length]; //create array of JRadioButtons, one for each supported data type
        bgDTDataType = new ButtonGroup();
        jpDTCenterRight1 = new JPanel(new GridLayout(controller.strDataType.length, 1));
        for (int i = 0; i < controller.strDataType.length; i++) {
            jrbDataType[i] = new JRadioButton(controller.strDataType[i]); //assign label for radio button from String array
            jrbDataType[i].setEnabled(false);
            jrbDataType[i].addActionListener(controller.radioButtonListener);
            bgDTDataType.add(jrbDataType[i]);
            jpDTCenterRight1.add(jrbDataType[i]);
        }
        jpDTCenterRight.add(jpDTCenterRight1);

        jcheckDTDisallowNull = new JCheckBox("Disallow Null");
        jcheckDTDisallowNull.setEnabled(false);
        jcheckDTDisallowNull.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jcheckDTPrimaryKey = new JCheckBox("Primary Key");
        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTPrimaryKey.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jbDTDefaultValue = new JButton("Set Default Value");
        jbDTDefaultValue.setEnabled(false);
        jbDTDefaultValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        }); //jbDTDefaultValue.addActionListener
        jtfDTDefaultValue = new JTextField();
        jtfDTDefaultValue.setEditable(false);

        jbDTVarchar = new JButton("Set Varchar Length");
        jbDTVarchar.setEnabled(false);
        jbDTVarchar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });
        jtfDTVarchar = new JTextField();
        jtfDTVarchar.setEditable(false);

        jpDTCenterRight2 = new JPanel(new GridLayout(6, 1));
        jpDTCenterRight2.add(jbDTVarchar);
        jpDTCenterRight2.add(jtfDTVarchar);
        jpDTCenterRight2.add(jcheckDTPrimaryKey);
        jpDTCenterRight2.add(jcheckDTDisallowNull);
        jpDTCenterRight2.add(jbDTDefaultValue);
        jpDTCenterRight2.add(jtfDTDefaultValue);
        jpDTCenterRight.add(jpDTCenterRight1);
        jpDTCenterRight.add(jpDTCenterRight2);
        jpDTCenter.add(jpDTCenterRight);
        jfDT.getContentPane().add(jpDTCenter, BorderLayout.CENTER);
        jfDT.validate();
    } //createDTScreen

    public void createDRScreen() {
        //create Define Relations screen
        jfDR = new JFrame(DEFINE_RELATIONS);
        jfDR.setSize(HORIZ_SIZE, VERT_SIZE);
        jfDR.setLocation(HORIZ_LOC, VERT_LOC);
        jfDR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jfDR.addWindowListener(controller.edgeWindowListener);
        jfDR.getContentPane().setLayout(new BorderLayout());

        //setup menubars and menus
        jmbDRMenuBar = new JMenuBar();
        jfDR.setJMenuBar(jmbDRMenuBar);
        jmDRFile = new JMenu("File");
        jmDRFile.setMnemonic(KeyEvent.VK_F);
        jmbDRMenuBar.add(jmDRFile);

        jmiDROpenFile = new JMenuItem("Open File...");
        jmiDROpenFile.setMnemonic(KeyEvent.VK_V);
        jmiDROpenFile.addActionListener(controller.menuListener);

        jmiDRExit = new JMenuItem("Exit");
        jmiDRExit.setMnemonic(KeyEvent.VK_X);
        jmiDRExit.addActionListener(controller.menuListener);
        jmDRFile.add(jmiDROpenFile);
        jmDRFile.add(jmiDRExit);

        jmDROptions = new JMenu("Options");
        jmDROptions.setMnemonic(KeyEvent.VK_O);
        jmbDRMenuBar.add(jmDROptions);


        jpDRCenter = new JPanel(new GridLayout(2, 2));
        jpDRCenter1 = new JPanel(new BorderLayout());
        jpDRCenter2 = new JPanel(new BorderLayout());
        jpDRCenter3 = new JPanel(new BorderLayout());
        jpDRCenter4 = new JPanel(new BorderLayout());

        dlmDRTablesRelations = new DefaultListModel();
        jlDRTablesRelations = new JList(dlmDRTablesRelations);
        jlDRTablesRelations.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        dlmDRFieldsTablesRelations = new DefaultListModel();
        jlDRFieldsTablesRelations = new JList(dlmDRFieldsTablesRelations);
        jlDRFieldsTablesRelations.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        dlmDRTablesRelatedTo = new DefaultListModel();
        jlDRTablesRelatedTo = new JList(dlmDRTablesRelatedTo);
        jlDRTablesRelatedTo.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        dlmDRFieldsTablesRelatedTo = new DefaultListModel();
        jlDRFieldsTablesRelatedTo = new JList(dlmDRFieldsTablesRelatedTo);
        jlDRFieldsTablesRelatedTo.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jspDRTablesRelations = new JScrollPane(jlDRTablesRelations);
        jspDRFieldsTablesRelations = new JScrollPane(jlDRFieldsTablesRelations);
        jspDRTablesRelatedTo = new JScrollPane(jlDRTablesRelatedTo);
        jspDRFieldsTablesRelatedTo = new JScrollPane(jlDRFieldsTablesRelatedTo);
        jlabDRTablesRelations = new JLabel("Tables With Relations", SwingConstants.CENTER);
        jlabDRFieldsTablesRelations = new JLabel("Fields in Tables with Relations", SwingConstants.CENTER);
        jlabDRTablesRelatedTo = new JLabel("Related Tables", SwingConstants.CENTER);
        jlabDRFieldsTablesRelatedTo = new JLabel("Fields in Related Tables", SwingConstants.CENTER);
        jpDRCenter1.add(jlabDRTablesRelations, BorderLayout.NORTH);
        jpDRCenter2.add(jlabDRFieldsTablesRelations, BorderLayout.NORTH);
        jpDRCenter3.add(jlabDRTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter4.add(jlabDRFieldsTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter1.add(jspDRTablesRelations, BorderLayout.CENTER);
        jpDRCenter2.add(jspDRFieldsTablesRelations, BorderLayout.CENTER);
        jpDRCenter3.add(jspDRTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter4.add(jspDRFieldsTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter.add(jpDRCenter1);
        jpDRCenter.add(jpDRCenter2);
        jpDRCenter.add(jpDRCenter3);
        jpDRCenter.add(jpDRCenter4);
        jfDR.getContentPane().add(jpDRCenter, BorderLayout.CENTER);
        jpDRBottom = new JPanel(new GridLayout(1, 3));

        jbDRDefineTables = new JButton(DEFINE_TABLES);
        jbDRDefineTables.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jbDRBindRelation = new JButton("Bind/Unbind Relation");
        jbDRBindRelation.setEnabled(false);
        jbDRBindRelation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }

        });

        jbDRCreateDDL = new JButton("Create DDL");
        jbDRCreateDDL.setEnabled(false);

        jpDRBottom.add(jbDRDefineTables);
        jpDRBottom.add(jbDRBindRelation);
        jpDRBottom.add(jbDRCreateDDL);
        jfDR.getContentPane().add(jpDRBottom, BorderLayout.SOUTH);
    } //createDRScreen
} // EdgeConverterGUI
