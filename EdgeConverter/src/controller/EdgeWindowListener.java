package controller;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class EdgeWindowListener implements WindowListener {
    private final Controller controller;

    public EdgeWindowListener(Controller controller) {
        this.controller = controller;
    }

    public void windowActivated(WindowEvent we) {
    }

    public void windowClosed(WindowEvent we) {
    }

    public void windowDeactivated(WindowEvent we) {
    }

    public void windowDeiconified(WindowEvent we) {
    }

    public void windowIconified(WindowEvent we) {
    }

    public void windowOpened(WindowEvent we) {
    }

    public void windowClosing(WindowEvent we) {

        
        System.exit(0);
    }
}