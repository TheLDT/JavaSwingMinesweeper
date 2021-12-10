package minesweepergui;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import static minesweepergui.MinesweeperGUI.DARK_MODE;

public class Settings {

    public static void readSettings() {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("settings.properties"));
            DARK_MODE = Boolean.valueOf(p.getProperty("dark_mode"));
        } catch (IOException ex) {
            System.out.println("No file");
        }
    }

    public static void updateSettings(String key, String value) {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("settings.properties"));
            p.setProperty(key, value);
            p.store(new FileWriter("settings.properties"), "");
        } catch (IOException ex) {
            System.out.println("No file");
        }
    }
}
