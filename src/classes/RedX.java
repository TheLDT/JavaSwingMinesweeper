package classes;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

public class RedX extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        if (JOptionPane.showConfirmDialog(e.getWindow(), "Are you sure you want to quit?") == 0) {
            System.exit(0);
        }
    }
}
