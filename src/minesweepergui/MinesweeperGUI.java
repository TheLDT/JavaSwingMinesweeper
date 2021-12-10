package minesweepergui;

import classes.myJFrame;

public class MinesweeperGUI {

    public static boolean DARK_MODE = false;

    public static void main(String[] args) {
        //load the dark mode from a file
        Settings.readSettings();
        myJFrame frame = new myJFrame(DARK_MODE);
    }
}
