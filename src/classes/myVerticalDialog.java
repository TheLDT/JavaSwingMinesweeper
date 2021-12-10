package classes;

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class myVerticalDialog extends JDialog {

    public myVerticalDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.setSize(300, 100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new RedX());
    }
}
