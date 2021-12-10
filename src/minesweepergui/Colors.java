package minesweepergui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import static minesweepergui.MinesweeperGUI.DARK_MODE;

public class Colors {

    public static Color getButtonColor(boolean DARK_MODE) {
        return DARK_MODE ? Color.BLACK : Color.WHITE;
    }

    public static Color getTextColor(boolean DARK_MODE) {
        return DARK_MODE ? Color.WHITE : Color.BLACK;
    }

    public static Color getCustomColor(boolean DARK_MODE, Color dark, Color light) {
        return DARK_MODE ? dark : light;
    }

    public static Color getNeighoursColor(String visual) {
        switch (visual) {
            case "0":
                return Colors.getButtonColor(DARK_MODE);
            case "1":
                return Colors.getCustomColor(DARK_MODE, new Color(67, 75, 237), Color.BLUE);
            case "2":
                return Color.GREEN;
            case "3":
                return Color.RED;
            case "4":
                return Color.MAGENTA;
            case "5":
                return Color.ORANGE;
            case "6":
                return Color.CYAN;
            case "7":
                return Color.PINK;
            case "8":
                return Color.LIGHT_GRAY;
            default:
                return Color.BLACK;
        }
    }

    public static void changeLightMode(Field field, JButton[][] buttons, JPanel labelsPanel, JPanel buttonsPanel, JMenuBar menubar) {
        String visual;
        //minefield
        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (field.getMinefield()[i][j].isPressed()) {
                    visual = field.getMinefield()[i][j].getVisual() + "";
                    buttons[i][j].setForeground(Colors.getNeighoursColor(visual));
                    buttons[i][j].setBackground(Colors.getButtonColor(DARK_MODE));
                } else {
                    buttons[i][j].setBackground(Colors.getCustomColor(DARK_MODE, Color.DARK_GRAY, Color.LIGHT_GRAY));
                }
            }
        }
        //labels
        if (((JLabel) (labelsPanel.getComponent(0))).getForeground() != Color.RED) {
            ((JLabel) (labelsPanel.getComponent(0))).setForeground(Colors.getTextColor(DARK_MODE));
        }
        ((JLabel) (labelsPanel.getComponent(1))).setForeground(Colors.getTextColor(DARK_MODE));

        labelsPanel.setBackground(Colors.getButtonColor(DARK_MODE));

        buttonsPanel.setBackground(Colors.getButtonColor(DARK_MODE));
        //menu
        menubar.setBackground(Colors.getCustomColor(DARK_MODE, Color.DARK_GRAY, Color.LIGHT_GRAY));
        for (java.awt.Component c : menubar.getComponents()) {
            c.setForeground(Colors.getTextColor(DARK_MODE));
            c.setBackground(Colors.getButtonColor(DARK_MODE));
        }
    }
}
