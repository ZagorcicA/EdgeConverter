package controller;

import dto.Field;
import view.EdgeConvertGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EdgeRadioButtonListener implements ActionListener {
    Controller controller;

    public EdgeRadioButtonListener(Controller controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent ae) {
        for (int i = 0; i < EdgeConvertGUI.jrbDataType.length; i++) {
            if (EdgeConvertGUI.jrbDataType[i].isSelected()) {
                controller.currentDTField.setDataType(i);
                break;
            }
        }
        if (EdgeConvertGUI.jrbDataType[0].isSelected()) {
            EdgeConvertGUI.jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
            EdgeConvertGUI.jbDTVarchar.setEnabled(true);
        } else {
            EdgeConvertGUI.jtfDTVarchar.setText("");
            EdgeConvertGUI.jbDTVarchar.setEnabled(false);
        }

        EdgeConvertGUI.jtfDTDefaultValue.setText("");
        controller.currentDTField.setDefaultValue("");
        controller.savedData = false;
    }
}
