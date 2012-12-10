/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import predimrc.gui.frame.Note_Frame;
import predimrc.gui.frame.The3D_Frame;
import predimrc.gui.graphic.ConfigView;
import predimrc.gui.graphic.MainView;
import predimrc.model.Model;

/**
 *
 * @author Christophe Levointurier 11/2012
 * @version
 * @see
 * @since
 */
public class PredimRC extends JFrame implements KeyListener {

    private static final String externalRefDoc = "https://code.google.com/p/predimrc/downloads/detail?name=CDC_PredimRc.pdf&can=2&q=";
    private static final String DEFAULT_KEY_VALUE = "Unknown Key. Old version file problem";
    private static final String VERSION = "Alpha 0.0.4";
    private static final long serialVersionUID = -2615396482200960443L;    // private final static String saveFileName = "links.txt";
    private static final String configFile = "config.cfg";
    public static final String appRep = System.getProperty("user.home") + "\\PredimRCFiles\\";
    public static String airfoilsDirectory = System.getProperty("user.home") + "\\PredimRCFiles\\";
    private static JButton aboutbut, help;
    private static JMenuItem savetarget, opentarget;
    private static JMenuItem quit, openConfigRep;
    private static JToggleButton logbut, modelNoteBut, the3DViewButton;
    // public static NumSelect amountThread = new NumSelect(3, 10, false, 1, 99);
    //  public static long threadsCount = 0;
    private static PredimRC instance;
    private static final boolean DEBUG_MODE = true;
    private static StringBuffer log = new StringBuffer();
    public static Image icon;
    public static ImageIcon imageIcon;
    public static final int DEFAULT_X_FRAME = 800;
    public static final int DEFAULT_Y_FRAME = 600;
    private static String[] tabNames = {"Model", "Airfoils", "Performances", "Motorization", "rudders", "Model comparison"};
    private static String[] tabTooltip = {"Model configuration", "Selection of the airfoil", "Dynamic performances of the model", "Allow to define motorization of the model", "Rudders definition", "Allow to compare several predimRC models"};
    private Model model;
    public static boolean warnClosePopup = true;
    /**
     *
     * componentns of the main view
     */
    private MainView mainView = new MainView();
    private ConfigView configView = new ConfigView();

    static {
        icon = getImage("predimrc.jpg");
        imageIcon = new ImageIcon(icon);
    }

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
        PredimRC.getInstance().setUpAndFillComponents();

    }

    private PredimRC() {
        super("PredimRC");
        log = new StringBuffer();
        model = new Model();
        aboutbut = new JButton("About...");
        the3DViewButton = new JToggleButton("3Dimension View");
        help = new JButton("Help!");
        logbut = new JToggleButton("log", false);
        modelNoteBut = new JToggleButton("Notes", false);
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
        menu.add(modelNoteBut);
        menu.add(the3DViewButton);
        menu.add(logbut);
        menu.add(help);
        menu.add(aboutbut);

        setJMenuBar(menu);


        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + externalRefDoc);
                } catch (IOException ex) {
                    logln(" Fail to access to doc :" + externalRefDoc + " :" + ex.getLocalizedMessage());
                }
            }
        });


        the3DViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new The3D_Frame(the3DViewButton);
            }
        });




        modelNoteBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Note_Frame(modelNoteBut);
            }
        });






        logbut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!logbut.isSelected()) {
                    logbut.setSelected(true);
                    return;
                }
                // setAlwaysOnTop(false);
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
                //  d.setAlwaysOnTop(true);
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
                            //      setAlwaysOnTop(true);
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
                        //        setAlwaysOnTop(true);
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
                //  setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, VERSION, "PredimRC", JOptionPane.WARNING_MESSAGE, new ImageIcon(icon));
                //   setAlwaysOnTop(true);
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

    private void setUpAndFillComponents() {

        logln("set up components...");

        mainView.setModel(getModel());

        logln("filling components...");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainView, BorderLayout.CENTER);
        getContentPane().add(configView, BorderLayout.EAST);
        setIconImage(icon);
        setVisible(true);
        pack();
        //  setSize(1024, 632);
        setLocationRelativeTo(null);
        // setAlwaysOnTop(true);
        validate();

        logln("-- PredimRC " + VERSION + " started. --");
    }

    public void changeDir() {
        final JFrame f = new JFrame();
        final JFileChooser chooser = new JFileChooser(airfoilsDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select a Directory to load airfoils.");
        //f.setAlwaysOnTop(true);
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
            airfoilsDirectory = config.getProperty("AIRFOILS", DEFAULT_KEY_VALUE);
            PredimRC.getInstance().getModel().setNote(config.getProperty("NOTES", DEFAULT_KEY_VALUE));
            PredimRC.getInstance().getModel().setName(config.getProperty("NAME", DEFAULT_KEY_VALUE));
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
        config.setProperty("NOTES", "" + PredimRC.getInstance().getModel().getNote());
        config.setProperty("NAME", "" + PredimRC.getInstance().getModel().getName());
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
        URL u = getResourceUrl("Sounds/" + path);
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

    public static Image getImage(String path) {
        URL u = getResourceUrl("Images/" + path);
        if (null == u) {
            logln("fail to load " + path + " image");
            return java.awt.Toolkit.getDefaultToolkit().getImage("http://icdn.pro/images/fr/a/c/action-agt-fail-icone-4999-128.png");
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

    public Model getModel() {
        return model;
    }
}
