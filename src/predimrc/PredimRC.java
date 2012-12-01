/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Chris
 */
public class PredimRC extends JFrame implements KeyListener {

    private static final String VERSION = "Alpha 0.1";
    private static final long serialVersionUID = -2615396482200960443L;    // private final static String saveFileName = "links.txt";
    private static final String configFile = "config.cfg";
    public static final String appRep = System.getProperty("user.home") + "\\PredimRCFiles\\";
    public static String airfoilsDirectory = System.getProperty("user.home") + "\\PredimRCFiles\\";
    private static JMenuItem aboutbut;
    private static JMenuItem savetarget, opentarget;
    private static JMenuItem quit, openConfigRep, configProxy;
    private static JToggleButton logbut, modelNote;
    // public static NumSelect amountThread = new NumSelect(3, 10, false, 1, 99);
    //  public static long threadsCount = 0;
    private static PredimRC instance;
    private static final boolean DEBUG_MODE = true;
    private static StringBuffer log = new StringBuffer();
    private static StringBuffer notes = new StringBuffer();

    static {
        //
    }
    private static String[] tabNames = {"Model", "Airfoils", "Performances", "Motorization", "rudders", "Model comparison"};
    private static String[] tabTooltip = {"Model configuration", "Selection of the airfoil", "Dynamic performances of the model", "Allow to define motorization of the model", "Rudders definition", "Allow to compare several predimRC models"};

    public static PredimRC getInstance() {
        if (null == instance) {
            instance = new PredimRC();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // JokerParser.test();
        PredimRC.getInstance().addKeyListener(instance);
        loadConfiguration();
        PredimRC.getInstance().fillComponents();
    }

    private PredimRC() {
        super("PredimRC");
        log = new StringBuffer();
        aboutbut = new JMenuItem("...");
        logbut = new JToggleButton("log", false);
        modelNote = new JToggleButton("Notes", false);
        savetarget = new JMenuItem("Save model");
        opentarget = new JMenuItem("Open model");
        quit = new JMenuItem("Quit");

        JMenuBar menu = new JMenuBar();
        JMenu filemenu = new JMenu("File");

        /**
         * JPanel iuygiuyg = new JPanel(); iuygiuyg.setLayout(new
         * BoxLayout(iuygiuyg, BoxLayout.X_AXIS)); iuygiuyg.add(new JLabel("Save
         * directory : ")); iuygiuyg.add(targetDirectoryField);
         *
         * targetDirectoryField.setEditable(false); filemenu.add(iuygiuyg);*
         */
        //  JPanel subMenuButton = new JPanel();
        //  subMenuButton.setLayout(new BoxLayout(subMenuButton, BoxLayout.X_AXIS));
        filemenu.add(opentarget);
        filemenu.add(savetarget);
        filemenu.addSeparator();
        filemenu.add(quit);

        menu.add(filemenu);
        menu.add(modelNote);
        menu.add(logbut);
        menu.add(aboutbut);

        setJMenuBar(menu);






        modelNote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!modelNote.isSelected()) {
                    modelNote.setSelected(true);
                    return;
                }
                setAlwaysOnTop(false);
                final JFrame d = new JFrame("Notes");
                final JButton temp = new JButton("Save & Close");
                final JPanel todos = new JPanel();
                final JTextArea area = new JTextArea(notes.toString(), 10, 50);
                todos.add(area);
                d.setLayout(new BorderLayout());
                d.add(todos, BorderLayout.CENTER);
                d.add(temp, BorderLayout.SOUTH);
                d.pack();
                d.setResizable(true);
                d.setLocationRelativeTo(null);
                d.setVisible(true);
                d.setAlwaysOnTop(true);
                //  d.addKeyListener(todos);
                temp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        {
                            notes = new StringBuffer(area.getText());
                            modelNote.setSelected(false);
                            d.dispose();
                            setAlwaysOnTop(true);
                        }
                    }
                });

                d.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                /**
                 * d.addWindowListener(new WindowAdapter() {
                 *
                 * @Override public void windowClosing(final WindowEvent e) {
                 * todos.save(); todo.setSelected(false); d.dispose();
                 * setAlwaysOnTop(true); } });*
                 */
            }
        });






        logbut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!logbut.isSelected()) {
                    logbut.setSelected(true);
                    return;
                }
                setAlwaysOnTop(false);
                final JFrame d = new JFrame("Log de l'exécution");
                final JTextArea tf = new JTextArea(log.toString(), 45, 55);
                d.setLayout(new BorderLayout());
                JScrollPane ts = new JScrollPane(tf, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                d.add(ts, BorderLayout.NORTH);
                JPanel bout = new JPanel();
                bout.setLayout(new BorderLayout());
                JButton clearr = new JButton("Clear");
                JButton refr = new JButton("Refresh");
                final JButton temp = new JButton("Close");
                bout.add(clearr, BorderLayout.WEST);
                bout.add(temp, BorderLayout.CENTER);
                bout.add(refr, BorderLayout.EAST);
                d.add(bout, BorderLayout.SOUTH);
                d.pack();
                d.setResizable(false);
                ts.updateUI();
                d.setLocationRelativeTo(null);
                d.setVisible(true);
                d.setAlwaysOnTop(true);
                d.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            temp.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
                d.requestFocusInWindow();

                temp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        {
                            logbut.setSelected(false);
                            d.dispose();
                            setAlwaysOnTop(true);
                        }
                    }
                });

                clearr.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        {
                            log = new StringBuffer();
                            tf.setText(log.toString());
                        }
                    }
                });

                refr.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        {
                            tf.setText(log.toString());
                        }
                    }
                });


                d.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                d.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(final WindowEvent e) {
                        logbut.setSelected(false);
                        d.dispose();
                        setAlwaysOnTop(true);
                    }
                });
            }
        });

        opentarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.logln("load");
            }
        });







        savetarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.logln("save");
            }
        });

        aboutbut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, VERSION, "PredimRC", JOptionPane.WARNING_MESSAGE, new ImageIcon(getImage("predimrc.jpg")));
                setAlwaysOnTop(true);
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });




        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                quit();
            }
        });
        logln("-- PredimRC " + VERSION + " init. --");
    }

    public static void quit() {
        getInstance().saveConfiguration();
        // saveXMLConfig();
        System.exit(0);
    }

    private void fillComponents() {

        logln("filling component...");
        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent panel1 = new JPanel();
        panel1.add(new JTextField(tabTooltip[0]));
        tabbedPane.addTab(tabNames[0], null, panel1, tabTooltip[0]);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);


        JComponent panel2 = new JPanel();
        panel2.add(new JTextField(tabTooltip[1]));
        tabbedPane.addTab(tabNames[1], null, panel2, tabTooltip[1]);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = new JPanel();
        panel3.add(new JTextField(tabTooltip[2]));
        tabbedPane.addTab(tabNames[2], null, panel3, tabTooltip[2]);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = new JPanel();
        panel4.add(new JTextField(tabTooltip[3]));
        tabbedPane.addTab(tabNames[3], null, panel4, tabTooltip[3]);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);


        JComponent panel5 = new JPanel();
        panel5.add(new JTextField(tabTooltip[4]));
        tabbedPane.addTab(tabNames[4], null, panel5, tabTooltip[4]);
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

        JComponent panel6 = new JPanel();
        panel6.add(new JTextField(tabTooltip[5]));
        tabbedPane.addTab(tabNames[5], null, panel6, tabTooltip[5]);
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);

        getContentPane().add(tabbedPane);
        setIconImage(getImage("predimrc.jpg"));
        pack();
        //  setSize((int) getPreferredSize().getWidth() + 75, (int) getPreferredSize().getHeight());
        setSize(1024, 632);
        setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
        validate();
        logln("-- PredimRC " + VERSION + " started. --");
    }

    public void changeDir() {
        final JFrame f = new JFrame();
        final JFileChooser chooser = new JFileChooser(airfoilsDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select a Directory to load airfoils.");
        f.setAlwaysOnTop(true);
        f.setVisible(true);
        final int returnVal = chooser.showOpenDialog(f);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            airfoilsDirectory = chooser.getSelectedFile().getAbsolutePath();
            logln(" Selected rep:" + airfoilsDirectory);
        }
        f.dispose();
    }

    /**
     * Implementation of KeyListener.
     */
    public void keyPressed(final KeyEvent arg0) {
        // unused
        if (arg0.getKeyCode() == 86)// && ctrl)
        {
            logln("pres");
        }
    }

    /**
     * Implementation of KeyListener.
     */
    public void keyReleased(final KeyEvent arg0) {
        boolean ctrl = (arg0.getModifiers() == (KeyEvent.CTRL_MASK));
        if (arg0.getKeyCode() == 86 && ctrl) // if (arg0.getKeyCode() == KeyEvent.VK_V)
        {
            logln("add from clipboard");
            //tableModel.addVid(this.getClipboardContents());
        }

    }

    /**
     * Implementation of KeyListener.
     */
    public void keyTyped(final KeyEvent arg0) {
        logln("typ");
        // unused
    }

    public static void log(String t) {
        if (null == log) {
            System.out.println(t);
        } else {
            log.append(t);
        }
    }

    public static void logln(String t) {
        log(t + "\n");
    }

    public static void logDebugln(String t) {
        if (DEBUG_MODE) {
            log("DEBUG:" + t + "\n");
        }
    }

    public static void clearLog() {
        log = new StringBuffer();

    }

    public static boolean loadConfiguration() {
        boolean ok = true;
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(appRep + configFile));
            airfoilsDirectory = config.getProperty("AIRFOILS");
            notes = new StringBuffer(config.getProperty("NOTES"));
            logln("config loaded from properties file: " + appRep + configFile + " ok...");
        } catch (final Throwable t) {
            logln("IOException while attempting to load File " + appRep + configFile + "...\n" + t.getLocalizedMessage());
            ok = false;
        }
        return ok;
    }

    /**
     * Save on properties File the configuration .
     */
    public static void saveConfiguration() {
        logln("\n*******************************************\n**** Saving  configuration... ****");
        Properties config = new Properties();
        config.setProperty("AIRFOILS", "" + airfoilsDirectory);
        config.setProperty("NOTES", "" + notes);
        try {
            File fout = new File(appRep);
            if (!fout.exists()) {
                fout.mkdirs();
            }
            config.store(new FileOutputStream(appRep + configFile), "Properties");
            logln("\n**** Saving  configuration ok ****\n*******************************************\n");
        } catch (final IOException e) {
            logln("io error for saving configuration:" + e.getLocalizedMessage());
        }

    }

    public static void play(String path) {
        URL u = getResourceUrl("sounds/" + path);
        if (null != u) {
            Applet.newAudioClip(u).play();
        } else {
            log("play " + path + " fail");
        }
    }

    public static URL getResourceUrl(String path) {
        path = "resource/" + path;
        URL u = Thread.currentThread().getContextClassLoader().getSystemResource(path);
        if (null == u) {
            u = ClassLoader.getSystemClassLoader().getSystemResource(path);
        }
        if (null == u) {
            u = Thread.currentThread().getContextClassLoader().getResource(path);
        }
        if (null == u) {
            u = ClassLoader.getSystemClassLoader().getResource(path);
        }
        return u;
    }

    private static Image getImage(String path) {
        URL u = getResourceUrl("images/" + path);
        if (null == u) {
            return java.awt.Toolkit.getDefaultToolkit().getImage("http://icones.pro/action-agt-fail-image-png.html");
        }
        return java.awt.Toolkit.getDefaultToolkit().getImage(u);
    }

    public static ImageIcon getImageIcon(String path) {
        return new ImageIcon(getImage(path));
    }

    public static JButton getButton(String path) {
        return getButton(path, "jpg");
    }

    public static JButton getButton(String path, String extension) {
        return (JButton) imageBut(new JButton(), path, extension);
    }

    public static JToggleButton getToggleButton(String path) {
        return getToggleButton(path, "jpg");
    }

    public static JToggleButton getToggleButton(String path, String extension) {
        return (JToggleButton) imageBut(new JToggleButton(), path, extension);
    }

    public static AbstractButton imageBut(AbstractButton j, String path, String extension) {

        j.setIcon(getImageIcon(path + "." + extension));
        String pathext = path + "_click." + extension;
        j.setPressedIcon(getImageIcon(pathext));
        j.setSelectedIcon(getImageIcon(pathext));
        pathext = path + "_focus." + extension;
        j.setRolloverIcon(getImageIcon(pathext));
        j.setBorder(null);
        j.setMargin(null);
        return j;
    }
}
