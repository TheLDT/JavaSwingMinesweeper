package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import minesweepergui.Colors;
import minesweepergui.Field;
import minesweepergui.Settings;
import static minesweepergui.MinesweeperGUI.DARK_MODE;

public class myJFrame extends JFrame implements ActionListener, MouseListener {

    //Sizes
    private int BUTTON_TEXT_SIZE;
    //texts
    private final String BOMBS_LEFT = "";
    //icon names
    private final String FLAG_ICON_NAME = "icons/flag.png";
    private final String MINE_ICON_NAME = "icons/mine.png";
    private final String TIME_ICON_NAME = "icons/time.png";
    private final String WIN_ICON_NAME = "icons/win.png";
    private final String LOSS_ICON_NAME = "icons/loss.png";
    private final String QUESTION_ICON_NAME = "icons/question.png";
    //icon variables, of adjustable sizes
    private ImageIcon FLAG_ICON;
    private ImageIcon MINE_ICON;
    private ImageIcon TIME_ICON;
    private ImageIcon QUESTION_ICON;
    private ImageIcon WIN_ICON;
    private ImageIcon LOSS_ICON;
    private final Image IMAGE = new ImageIcon(MINE_ICON_NAME).getImage();
    //Variables initialized in setup but used in other places.
    private Field field;
    private JButton[][] buttons;
    private JPanel labelsPanel, buttonsPanel;
    private Timer timer;
    private final ArrayList<String> choice_array = new ArrayList<>();

    //exportable
    //Constructor
    public myJFrame(boolean dark_mode) {
        DARK_MODE = dark_mode;
        setupMenuBar();
        menu();
        Colors.changeLightMode(field, buttons, labelsPanel, buttonsPanel, this.getJMenuBar());
    }

    public myJFrame(int size, int bombs, boolean dark_mode) {
        //Bypass the menu
        DARK_MODE = dark_mode;
        setupMenuBar();
        setup(size, bombs);
        Colors.changeLightMode(field, buttons, labelsPanel, buttonsPanel, this.getJMenuBar());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Dark Mode")) {
            DARK_MODE = !DARK_MODE;
            Settings.updateSettings("dark_mode", DARK_MODE + "");
            Colors.changeLightMode(field, buttons, labelsPanel, buttonsPanel, this.getJMenuBar());
            return;
        } else if (e.getActionCommand().equals("Restart")) {
            restart(true);
            return;
        }

        JButton source = ((JButton) (e.getSource()));
        String[] data = source.getToolTipText().split(",");
        int row = Integer.parseInt(data[0]);
        int column = Integer.parseInt(data[1]);

        if (field.getMinefield()[row][column].isFlagged() || field.getMinefield()[row][column].isPressed()) {
            return;//Don't press if the cell is flagged or already pressed.
        }

        field.getMinefield()[row][column].press();
        field.setPressed_cells(field.getPressed_cells() + 1);

        String visual = field.getMinefield()[row][column].getVisual() + "";
        source.setText(visual);
        buttons[row][column].setBackground(Colors.getButtonColor(DARK_MODE));
        buttons[row][column].setForeground(Colors.getNeighoursColor(visual));
        buttons[row][column].setIcon(null);

        if (field.getMinefield()[row][column].isEmpty()) {
            field.find_other_empty(row, column, 0);
            for (int i = 0; i < field.getSize(); i++) {
                for (int j = 0; j < field.getSize(); j++) {
                    if (field.getMinefield()[i][j].isPressed()) {
                        visual = field.getMinefield()[i][j].getVisual() + "";
                        buttons[i][j].setForeground(Colors.getNeighoursColor(visual));
                        buttons[i][j].setBackground(Colors.getButtonColor(DARK_MODE));
                        buttons[i][j].setText(visual);
                    }
                }
            }
        }

        if (field.getMinefield()[row][column].isBomb()) {
            end_of_game(false);
        }

        if (field.getPressed_cells() >= field.getTotal_cells() - field.getBomb_count()) {
            end_of_game(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
            JButton source = ((JButton) (e.getSource()));

            String[] data = source.getToolTipText().split(",");
            int row = Integer.parseInt(data[0]);
            int column = Integer.parseInt(data[1]);

            JLabel bomb_label = ((JLabel) (labelsPanel.getComponent(0)));

            int currentBombs = Integer.parseInt(bomb_label.getText().split(" ")[0]);
            //middle button
            if (e.getButton() == MouseEvent.BUTTON2) {
                if (source.getIcon() == null) {
                    source.setIcon(QUESTION_ICON);
                } else if (source.getIcon() == FLAG_ICON) {
                    currentBombs++;
                    bomb_label.setText(currentBombs + BOMBS_LEFT);
                    source.setIcon(QUESTION_ICON);
                } else if (source.getIcon() == QUESTION_ICON) {
                    source.setIcon(null);
                    field.getMinefield()[row][column].setFlagged(false);
                }
                return;
            }
            //right button
            if (source.getIcon() == FLAG_ICON) {
                currentBombs++;
                bomb_label.setText(currentBombs + BOMBS_LEFT);
                source.setIcon(QUESTION_ICON);
                field.getMinefield()[row][column].setFlagged(false);
            } else if (source.getIcon() == QUESTION_ICON) {
                source.setIcon(null);
                field.getMinefield()[row][column].setFlagged(false);
            } else {
                if (!field.getMinefield()[row][column].isPressed()) {
                    currentBombs--;
                    bomb_label.setText(currentBombs + BOMBS_LEFT);
                    source.setIcon(FLAG_ICON);
                    field.getMinefield()[row][column].setFlagged(true);
                }
            }

            if (currentBombs < 0) {
                bomb_label.setForeground(Color.RED);
            } else {
                bomb_label.setForeground(Colors.getTextColor(DARK_MODE));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void restart(boolean play_again) {
        this.dispose();
        if (play_again) {
            myJFrame myJFrame = new myJFrame(field.getSize(), field.getBomb_count(), DARK_MODE);
        } else {
            myJFrame myJFrame = new myJFrame(DARK_MODE);
        }
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Settings");
        JMenuItem[] menu1items = new JMenuItem[2];

        for (int i = 0; i < menu1items.length; i++) {
            menu1items[i] = new JMenuItem();
            menu1items[i].addActionListener(this);
            menu1.add(menu1items[i]);
        }

        menu1items[0].setText("Dark Mode");
        menu1items[1].setText("Restart");

        menuBar.add(menu1);
        menuBar.setBackground(Colors.getCustomColor(DARK_MODE, Color.DARK_GRAY, Color.LIGHT_GRAY));
        menu1.setForeground(Colors.getTextColor(DARK_MODE));
        menuBar.setBorderPainted(false);
        setJMenuBar(menuBar);
    }

    private void setup(int size, int bombs) {
        setSize(800, 600);
        getContentPane().setBackground(Colors.getButtonColor(DARK_MODE));
        setIconImage(IMAGE);
        setLocationRelativeTo(null);
        setVisible(true);

        if (size == 0) {
            boolean wrong_number;
            int[] result;
            do {
                try {
                    result = option_pane_show("input", false);
                    size = result[0];
                    bombs = result[1];
                    wrong_number = false;
                } catch (java.lang.NumberFormatException exc) {
                    wrong_number = true;
                }
            } while (wrong_number);
        }

        field = new Field(size, bombs);
        field.setup_field();

        resize(size);

        buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new java.awt.GridLayout(size, size));
        buttonsPanel.setCursor(new java.awt.Cursor(HAND_CURSOR));

        buttons = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton(field.getMinefield()[i][j].getVisual() + "");
                buttons[i][j].setToolTipText((i + "," + j));
                buttons[i][j].addActionListener(this);
                buttons[i][j].addMouseListener(this);
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setFont(new Font(null, Font.BOLD, BUTTON_TEXT_SIZE));
                buttons[i][j].setIconTextGap(0);
                buttons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                buttonsPanel.add(buttons[i][j]);
            }
        }

        JLabel bombsLeftLabel = new JLabel(field.getBomb_count() + BOMBS_LEFT);
        bombsLeftLabel.setIcon(MINE_ICON);
        bombsLeftLabel.setFont(new Font(null, Font.BOLD, BUTTON_TEXT_SIZE));

        JLabel timerLabel = new JLabel("0");
        timerLabel.setIcon(TIME_ICON);
        timerLabel.setFont(new Font(null, Font.BOLD, BUTTON_TEXT_SIZE));

        ActionListener updateClockAction;
        updateClockAction = (ActionEvent e) -> {
            int currentBombs = Integer.parseInt(timerLabel.getText()) + 1;
            timerLabel.setText(currentBombs + "");
        };

        this.timer = new Timer(1000, updateClockAction);
        this.timer.start();

        labelsPanel = new JPanel();
        labelsPanel.setMaximumSize(new Dimension(2000, 30));
        labelsPanel.add(bombsLeftLabel);
        labelsPanel.add(timerLabel);

        JPanel allPanels = new JPanel();
        allPanels.setLayout(new BoxLayout(allPanels, BoxLayout.Y_AXIS));

        JScrollPane buttonScrollPane = new JScrollPane(buttonsPanel);
        buttonScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        allPanels.add(labelsPanel);
        allPanels.add(buttonScrollPane);

        setLocationRelativeTo(null);
        setTitle("Minesweeper");
        add(allPanels);
        setMinimumSize(new Dimension(650, 650));
        setSize(size * 50, size * 50);
        setVisible(true);
        addWindowListener(new RedX());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void menu() {
        option_pane_show("menu", false);
    }

    private void resize(int size) {
        BUTTON_TEXT_SIZE = (int) (((double) 14 / size) * 20) + size / 2;
        int icon_size = (int) (((double) 14 / size) * 20) + size / 2;

        FLAG_ICON = new ImageIcon(getScaledImage(new ImageIcon(FLAG_ICON_NAME, "flag").getImage(), icon_size, icon_size));
        MINE_ICON = new ImageIcon(getScaledImage(new ImageIcon(MINE_ICON_NAME, "mine").getImage(), icon_size, icon_size));
        TIME_ICON = new ImageIcon(getScaledImage(new ImageIcon(TIME_ICON_NAME, "time").getImage(), icon_size, icon_size));
        QUESTION_ICON = new ImageIcon(getScaledImage(new ImageIcon(QUESTION_ICON_NAME, "time").getImage(), icon_size, icon_size));
    }

    private void showMines(boolean win) {
        Color background;
        background = win ? Color.GREEN : Color.RED;
        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (field.getMinefield()[i][j].isBomb()) {
                    buttons[i][j].setBackground(background);
                    buttons[i][j].setIcon(MINE_ICON);
                }
            }
        }
    }

    private int[] option_pane_show(String type, boolean win) {
        myVerticalDialog dialog = new myVerticalDialog(this, "Select:", true);
        dialog.setIconImage(IMAGE);
        JPanel optionPanel;
        int[] toReturn = new int[2];
        String choice;
        String[] options;
        switch (type) {
            case "menu":
                options = new String[]{"Easy [8x8 with 8 bombs]",
                    "Medium [10x10 with 20 bombs]",
                    "Hard [16x16 with 48 bombs]",
                    "Custom"};
                optionPanel = setupDialogButtons(options, dialog);
                optionPanel.setLayout(new GridLayout(options.length, 1));

                dialog.setTitle("Welcome to Minesweeper");
                dialog.add(optionPanel);
                dialog.pack();
                dialog.setVisible(true);
                //The name is set in the setup Dialog buttons and is the return of what button was pressed
                choice = choice_array.get(0);

                choice_array.clear();
                switch (choice) {
                    case "0":
                        setup(8, 8 * 1);
                        break;
                    case "1":
                        setup(10, 10 * 2);
                        break;
                    case "2":
                        setup(16, 16 * 3);
                        break;
                    default:
                        setup(0, 0);
                        break;
                }
                break;
            case "input":
                int default_size = 14,
                 default_bombs = (int) (14 * 1.75);
                JTextField size_tf = new JTextField(default_size + "");//default 14
                size_tf.setFont(new Font(null, Font.BOLD, 20));
                JTextField bombs_tf = new JTextField(default_bombs + "");// default int)(14*1.75)
                bombs_tf.setFont(new Font(null, Font.BOLD, 20));
                JLabel size_label = new JLabel("Size:");
                size_label.setFont(new Font(null, Font.BOLD, 20));
                String percent = String.format("%.2f", (double) (default_bombs) / (default_size * default_size));
                JLabel bombs_label = new JLabel("Bombs: (" + percent + "%)");
                System.out.println((double) (default_bombs) / (default_size * default_size));
                bombs_label.setFont(new Font(null, Font.BOLD, 20));
                JPanel optionPanePanel = new JPanel();
                optionPanePanel.setLayout(new GridLayout(2, 2));
                optionPanePanel.add(size_label);
                optionPanePanel.add(size_tf);
                optionPanePanel.add(bombs_label);
                optionPanePanel.add(bombs_tf);
                options = new String[]{"Done",
                    "Exit"};
                optionPanel = setupDialogButtons(options, dialog);
                optionPanel.setLayout(new GridLayout(options.length, 1));

                //give percent of bombs
                size_tf.getDocument().addDocumentListener((myDocumentListener) (DocumentEvent e) -> {
                    try {
                        int size = Integer.parseInt(size_tf.getText());
                        int bombs = Integer.parseInt(bombs_tf.getText());
                        String percent2 = String.format("%.2f", (double) (bombs) / (size * size));
                        bombs_label.setText("Bombs: (" + percent2 + "%)");
                    } catch (java.lang.NumberFormatException exc) {
                        System.out.println("exc");
                    }
                });

                bombs_tf.getDocument().addDocumentListener((myDocumentListener) (DocumentEvent e) -> {
                    try {
                        int size = Integer.parseInt(size_tf.getText());
                        int bombs = Integer.parseInt(bombs_tf.getText());
                        String percent2 = String.format("%.2f", (double) (bombs) / (size * size));
                        bombs_label.setText("Bombs: (" + percent2 + "%)");
                    } catch (java.lang.NumberFormatException exc) {
                        System.out.println("exc");
                    }
                });

                /*size_tf.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Text=");
                        JTextField source = ((JTextField)(e.getSource()));
                        System.out.println("Text=" + source.getText());
                    }
                });
                size_tf.addActionListener((ActionEvent e) -> {
                    System.out.println("Action");
                    JButton source = ((JButton)(e.getSource()));
                    int size = Integer.parseInt(size_tf.getText());
                    int bombs = Integer.parseInt(bombs_tf.getText());
                    String percent2 = String.format("%.2f", (double)(bombs)/(size*size));
                    bombs_label.setText("Bombs: ("+percent2+"%)");
                });*/
                dialog.setTitle("Select:");
                dialog.setLocationRelativeTo(null);
                dialog.setLayout(new GridLayout(2, 1));
                dialog.add(optionPanePanel);
                dialog.add(optionPanel);
                dialog.setSize(500, 300);
                dialog.setVisible(true);
                //The name is set in the setup Dialog buttons and is the return of what button was pressed
                choice = choice_array.get(0);
                choice_array.clear();
                if (choice.equals("1")) {
                    System.exit(0);
                }

                toReturn[0] = Integer.parseInt(size_tf.getText());
                toReturn[1] = Integer.parseInt(bombs_tf.getText());

                return toReturn;
            case "end":
                String message = win ? "You Won! Do you want to Restart?" : "You Lost! Do you want to Restart?";
                String icon_name = win ? WIN_ICON_NAME : LOSS_ICON_NAME;
                ImageIcon victory_icon = new ImageIcon(getScaledImage(new ImageIcon(icon_name, "time").getImage(), 30, 30));
                ;

                String message2 = field.getBombPercentage() + "% of cells were bombs.";

                JLabel messageLabel = new JLabel(message);
                messageLabel.setAlignmentY(TOP_ALIGNMENT);
                messageLabel.setIcon(victory_icon);
                messageLabel.setForeground(Colors.getTextColor(DARK_MODE));

                JLabel messageLabel2 = new JLabel(message2);
                messageLabel2.setAlignmentY(TOP_ALIGNMENT);
                messageLabel2.setForeground(Colors.getTextColor(DARK_MODE));

                options = new String[]{"Play Again",
                    "Back to Menu",
                    "Exit"};
                optionPanel = setupDialogButtons(options, dialog);
                optionPanel.setLayout(new GridLayout(options.length, 1));

                JPanel all = new JPanel();
                all.add(messageLabel);
                all.add(messageLabel2);
                all.add(optionPanel);
                all.setBackground(Colors.getButtonColor(DARK_MODE));

                dialog.setTitle(win ? "Victory!" : "Defeat!"
                        + " (" + field.getSize() + "x" + field.getSize()
                        + ": " + field.getBomb_count() + " bombs)");
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
                dialog.add(all);
                dialog.setSize(300, 220);
                dialog.setVisible(true);
                choice = choice_array.get(0);
                choice_array.clear();

                switch (choice) {
                    case "0"://"Play Again" same setup
                        restart(true);
                        break;
                    case "1"://"Back to Menu" menu select
                        restart(false);
                        break;
                    case "2"://"Exit"
                        System.exit(0);
                }
                break;
        }
        return null;
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private JPanel setupDialogButtons(String[] options, myVerticalDialog dialog) {
        JButton[] optionButtons = new JButton[options.length];
        JPanel toReturn = new JPanel();

        for (int i = 0; i < options.length; i++) {
            optionButtons[i] = new JButton(options[i]);
            optionButtons[i].setName(i + "");
            optionButtons[i].setBackground(Colors.getButtonColor(DARK_MODE));
            optionButtons[i].setForeground(Colors.getTextColor(DARK_MODE));
            optionButtons[i].setFont(new Font(null, Font.BOLD, 20));
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].addActionListener((ActionEvent e) -> {
                JButton source = ((JButton) (e.getSource()));
                choice_array.add(source.getName());
                dialog.setVisible(false);
            });
            toReturn.add(optionButtons[i]);
        }
        return toReturn;
    }

    private void end_of_game(boolean win) {
        timer.stop();
        showMines(win);
        option_pane_show("end", win);
    }
}
