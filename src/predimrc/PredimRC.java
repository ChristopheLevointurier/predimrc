/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DTriangle;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
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
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import predimrc.common.About;
import predimrc.common.Extractor;
import predimrc.common.UserConfig;
import predimrc.common.Utils;
import predimrc.controller.ModelController;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Note_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.Perfo_Frame;
import predimrc.gui.frame.The3D_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.MainView;
import predimrc.gui.graphic.config.ConfigView;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.model.Model;
import predimrc.model.ModelVersion;

/**
 * @author Christophe Levointurier 11/2012
 * @version
 * @see
 * @since
 */
public class PredimRC extends JFrame {

    /**
     * consts
     */
    private static final String externalRefDoc = "https://code.google.com/p/predimrc/";
    private static final String DEFAULT_KEY_VALUE = "Unknown Key - Old version file problem";
    private static final String FILE_EXTENSION = "predimodel";
    private final static float dash1[] = {10.0f};
    public final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    private static final String VERSION = "Alpha 0.982";
    private static final long serialVersionUID = -2615396482200960443L;    // private final static String saveFileName = "links.txt";
    public static final String appRep = System.getProperty("user.home") + "\\PredimRCFiles\\";
    private static final boolean DEBUG_MODE = false;
    private static final String configFile = "config.cfg";
    public static final String defaultLabelContent = "xx";
    public static final Image icon;
    public static final ImageIcon imageIcon;
    /**
     * flag
     */
    public static boolean initDone = false;
    /**
     * menu
     */
    private static JMenuItem savetarget, opentarget, save;
    private static JCheckBoxMenuItem viewNeutralPoints, viewCG, viewRefPoint, manipFuse, manipWing, manipFin, manipStab;
    private static JMenuItem quit, openAppRep;
    private JToggleButton vlmBut = new JToggleButton("VLM");
    private JToggleButton optimBut = new JToggleButton("Optim");
    private JMenuItem xFoilBut = new JMenuItem(getImageIcon("xfoil.png"));
    private JMenuItem perfoBut = new JMenuItem("Perfos");
    private JMenuItem modelNoteBut = new JMenuItem(getImageIcon("note.png"));
    private JMenuItem the3DViewButton = new JMenuItem(getImageIcon("3d.png"));
    private JToggleButton compareBut = new JToggleButton("Compare Models");
    private JToggleButton engineBut = new JToggleButton("Engine");
    private static PredimRC instance;
    private static StringBuffer log = new StringBuffer();
    private DrawableModel drawableModel;
    /**
     *
     * components of the view
     */
    private MainView mainView;
    private ConfigView configView;

    static {
        icon = getImage("predimrc.jpg");
        imageIcon = new ImageIcon(icon);
    }

    private static PredimRC getInstance() {
        if (null == instance) {
            instance = new PredimRC();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        getInstance();
        Extractor.extract();
        loadConfiguration();
        getInstance().setUpAndFillComponents();
        initDone = true;
        logln("-- PredimRC " + VERSION + " started. --");
    }

    private PredimRC() {
        super("PredimRC");
        log = new StringBuffer();
        drawableModel = new DrawableModel();
        mainView = new MainView();
        configView = new ConfigView();
        xFoilBut.setToolTipText("Foils performances");
        perfoBut.setToolTipText("Model performances");
        the3DViewButton.setToolTipText("3 Dimension View");
        modelNoteBut.setToolTipText("Model's notes");
        save = new JMenuItem("Save model");
        savetarget = new JMenuItem("Save model...");
        opentarget = new JMenuItem("Open model");
        quit = new JMenuItem("Quit");
        openAppRep = new JMenuItem("Open PredimRC dir.");
        JMenu view = new JMenu("View special points:");

        viewNeutralPoints = new JCheckBoxMenuItem("neutral points", UserConfig.viewNeutralPoints);
        viewCG = new JCheckBoxMenuItem("gravity center", UserConfig.viewCG);
        viewRefPoint = new JCheckBoxMenuItem("reference point for X, Y and Z dimensions", UserConfig.viewRefPoint);
        view.add(viewNeutralPoints);
        view.add(viewCG);
        view.add(viewRefPoint);


        JMenu manip = new JMenu("Show points of");
        manipFuse = new JCheckBoxMenuItem("Fuse", UserConfig.manipFuse);
        manipWing = new JCheckBoxMenuItem("Wing", UserConfig.manipWing);
        manipStab = new JCheckBoxMenuItem("Stab", UserConfig.manipStab);
        manipFin = new JCheckBoxMenuItem("Fin(s)", UserConfig.manipFin);

        manip.add(manipFuse);
        manip.add(manipWing);
        manip.add(manipStab);
        manip.add(manipFin);

        JMenuBar menu = new JMenuBar();
        JMenu filemenu = new JMenu();
        filemenu.setIcon(getImageIcon("file.png"));
        JMenu editmenu = new JMenu();
        editmenu.setIcon(getImageIcon("config.png"));
        JMenuItem savebut = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.saveModel();
            }
        });
        savebut.setIcon(getImageIcon("disk.png"));
        savebut.setToolTipText("Save on disk");
        JMenu helpMenu = new JMenu();
        helpMenu.setIcon(getImageIcon("help.png"));
        helpMenu.setToolTipText("Help");

        JMenuItem help = new JMenuItem("Open Documentation from internet");
        helpMenu.add(help);
        JMenuItem logbut = new JMenuItem("log");
        helpMenu.add(logbut);
        JMenuItem about = new JMenuItem("About PredimRC...");
        helpMenu.add(about);


        modelNoteBut.setMaximumSize(new Dimension(50, 75));
        xFoilBut.setMaximumSize(new Dimension(100, 75));
        perfoBut.setMaximumSize(new Dimension(100, 75));
        savebut.setMaximumSize(new Dimension(50, 75));

        //  JMenu perfmenu = new JMenu("Performance");

        /**
         * register new listener of the model*
         */
        ModelController.addModelListener(configView);
        filemenu.add(opentarget);
        filemenu.add(save);
        filemenu.add(savetarget);
        filemenu.addSeparator();
        filemenu.add(openAppRep);
        filemenu.add(quit);

        editmenu.add(view);
        editmenu.add(manip);
        editmenu.addSeparator();


        // perfmenu.add(vlmBut);
        //  perfmenu.add(xFoilBut);
        //  perfmenu.add(optimBut);
        //  perfmenu.add(engineBut);
        //  perfmenu.add(compareBut);


        menu.add(filemenu);
        menu.add(editmenu);
        //  menu.add(perfmenu);
        menu.add(modelNoteBut);
        menu.add(xFoilBut);
        menu.add(perfoBut);
        // menu.add(the3DViewButton);
        menu.add(savebut);
        menu.add(helpMenu);

        setJMenuBar(menu);




        compareBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Compare_Frame(compareBut);
            }
        });


        engineBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Engine_Frame(engineBut);
            }
        });


        vlmBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Vlm_Frame(vlmBut);
            }
        });

        optimBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Optim_Frame(optimBut);
            }
        });


        xFoilBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(XFoil_Frame.maketInstance(xFoilBut, getInstanceDrawableModel().getXfoilConfig()));
            }
        });


        perfoBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(Perfo_Frame.maketInstance(perfoBut));
            }
        });


        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + externalRefDoc);
                } catch (IOException ex) {
                    logln(" Fail to access to doc :" + externalRefDoc + " :" + ex.getLocalizedMessage());
                }
            }
        });


        the3DViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (the3DViewButton.isEnabled()) {
                    new The3D_Frame(the3DViewButton);
                }
            }
        });




        modelNoteBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Note_Frame(modelNoteBut);
            }
        });






        opentarget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.loadModelWithChooser();
            }
        });



        savetarget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.saveModelWithChooser();
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.saveModel();
            }
        });





        viewNeutralPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.viewNeutralPoints = viewNeutralPoints.isSelected();
                ModelController.applyChange();
            }
        });
        viewRefPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.viewRefPoint = viewRefPoint.isSelected();
                ModelController.applyChange();
            }
        });
        viewCG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.viewCG = viewCG.isSelected();
                ModelController.applyChange();
            }
        });




        manipFin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.manipFin = manipFin.isSelected();
                ModelController.applyChange();
                repaintDrawPanels();
            }
        });
        manipFuse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.manipFuse = manipFuse.isSelected();
                ModelController.applyChange();
                repaintDrawPanels();
            }
        });
        manipWing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.manipWing = manipWing.isSelected();
                ModelController.applyChange();
                repaintDrawPanels();
            }
        });
        manipStab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserConfig.manipStab = manipStab.isSelected();
                ModelController.applyChange();
                repaintDrawPanels();
            }
        });


        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new About();
                //   Object[] options = {"OK"};
                //   int ret = JOptionPane.showOptionDialog(null, "PredimRC project\n  V " + VERSION + "\n\nhttps://code.google.com/p/predimrc/", "About",
                //          JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                //          null, options, options[0]);
            }
        });


        logbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                d.requestFocusInWindow();
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
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            d.dispose();
                        }
                    }
                });

                clearr.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            log = new StringBuffer();
                            tf.setText(log.toString());
                        }
                    }
                });

                refr.addActionListener(new ActionListener() {
                    @Override
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
                        d.dispose();

                    }
                });
            }
        });




        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });



        openAppRep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.OPEN)) {
                            desktop.open(new File(appRep));
                            return;
                        }
                    }
                } catch (Exception ex) {
                }
                logln(" failed to open:" + appRep);
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

        Object[] options = {"Quit", "Save&Quit", "Nooo"};
        int ret = JOptionPane.showOptionDialog(null, "Quit to windows?", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (ret == 2) {
            return;
        }
        if (ret == 1) {
            PredimRC.saveModel();
        }
        PredimRC.saveConfiguration();
        System.exit(0);
    }

    private void setUpAndFillComponents() {

        logln("set up components...");
        try {
            if (UserConfig.filename.equals("")) {
                resetModel();
            } else {
                loadModel();
            }
        } catch (IOException ex) {
        }
        logln("filling components...");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainView, BorderLayout.CENTER);
        getContentPane().add(configView, BorderLayout.EAST);
        setIconImage(icon);
        setVisible(true);
        //pack();
        setSize(Utils.MAIN_FRAME_SIZE_X, Utils.MAIN_FRAME_SIZE_Y);
        setLocationRelativeTo(null);
        // setAlwaysOnTop(true);

        Action quitActionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PredimRC.quit();
            }
        };

        Action saveActionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PredimRC.saveModel();
            }
        };

        validate();

        InputMap inputMap = ((JPanel) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "SAVE");
        ((JPanel) getContentPane()).getActionMap().put("CLOSE", quitActionListener);
        ((JPanel) getContentPane()).getActionMap().put("SAVE", saveActionListener);
    }

    public void changeDir() {
        final JFrame f = new JFrame();
        final JFileChooser chooser = new JFileChooser(UserConfig.airfoilsDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select a Directory to load airfoils.");
        //f.setAlwaysOnTop(true);
        f.setVisible(true);
        final int returnVal = chooser.showOpenDialog(f);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            UserConfig.airfoilsDirectory = chooser.getSelectedFile().getAbsolutePath();
            logDebugln(" Selected rep:" + UserConfig.airfoilsDirectory);
        }
        f.dispose();
    }

    public static void log(String t) {
        if (null == log) {
            System.out.println(t);
        } else {
            log.append(t);
        }
    }

    public static void logln(String t) {
        log(t + System.getProperty("line.separator"));
    }

    public static void logDebugln(String t) {
        if (DEBUG_MODE) {
            log("DEBUG:" + t + System.getProperty("line.separator"));
        }
    }

    public static void logDebug(String t) {
        if (DEBUG_MODE) {
            log("DEBUG:" + t);
        }
    }

    public static DrawableModel getInstanceDrawableModel() {
        return getInstance().getDrawableModel();
    }

    public static void clearLog() {
        log = new StringBuffer();
    }

    public static boolean loadConfiguration() {
        boolean ok = true;
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(appRep + configFile));
            PredimRC.getInstance().setFilename(loadConfig(config, "FILENAME"));
            UserConfig.airfoilsDirectory = loadConfig(config, "AIRFOILS");
            UserConfig.warnClosePopup = Boolean.parseBoolean(loadConfig(config, "WARNPOPUP"));
            UserConfig.viewCG = Boolean.parseBoolean(loadConfig(config, "VIEWCG"));
            UserConfig.viewNeutralPoints = Boolean.parseBoolean(loadConfig(config, "VIEWNP"));
            UserConfig.viewRefPoint = Boolean.parseBoolean(loadConfig(config, "VIEWRP"));
            UserConfig.manipFin = Boolean.parseBoolean(loadConfig(config, "MFIN"));
            UserConfig.manipFuse = Boolean.parseBoolean(loadConfig(config, "MFUSE"));
            UserConfig.manipStab = Boolean.parseBoolean(loadConfig(config, "MSTAB"));
            UserConfig.manipWing = Boolean.parseBoolean(loadConfig(config, "MWING"));

            UserConfig.rotX = Boolean.parseBoolean(loadConfig(config, "ROTX"));
            UserConfig.rotY = Boolean.parseBoolean(loadConfig(config, "ROTY"));
            UserConfig.rotZ = Boolean.parseBoolean(loadConfig(config, "ROTZ"));
            UserConfig.cull = Boolean.parseBoolean(loadConfig(config, "CULL"));
            UserConfig.light = Boolean.parseBoolean(loadConfig(config, "LIGHT"));
            UserConfig.solid = Boolean.parseBoolean(loadConfig(config, "SOLID"));
            viewNeutralPoints.setSelected(UserConfig.viewNeutralPoints);
            viewCG.setSelected(UserConfig.viewCG);
            viewRefPoint.setSelected(UserConfig.viewRefPoint);

            manipFin.setSelected(UserConfig.manipFin);
            manipFuse.setSelected(UserConfig.manipFuse);
            manipStab.setSelected(UserConfig.manipStab);
            manipWing.setSelected(UserConfig.manipWing);


            /**
             */
            logln("config loaded from properties file: " + appRep + configFile + " ok...");
        } catch (final Throwable t) {
            logln("IOException while attempting to load File " + appRep + configFile + "..." + t.getLocalizedMessage());
            ok = false;
        }
        return ok;
    }

    private static String loadConfig(Properties p, String key) {
        String prop = p.getProperty(key, DEFAULT_KEY_VALUE);
        logDebugln("config load : " + key + " = " + prop);
        return prop;
    }

    /**
     * Save on properties File the configuration .
     */
    public static void saveConfiguration() {
        logln(System.getProperty("line.separator") + "*******************************************" + System.getProperty("line.separator") + "*** Saving  configuration... ****");
        Properties config = new Properties();
        config.setProperty("AIRFOILS", "" + UserConfig.airfoilsDirectory);
        config.setProperty("FILENAME", "" + UserConfig.filename);
        config.setProperty("WARNPOPUP", "" + UserConfig.warnClosePopup);
        config.setProperty("VIEWCG", "" + UserConfig.viewCG);
        config.setProperty("VIEWNP", "" + UserConfig.viewNeutralPoints);
        config.setProperty("VIEWRP", "" + UserConfig.viewRefPoint);
        config.setProperty("MFIN", "" + UserConfig.manipFin);
        config.setProperty("MFUSE", "" + UserConfig.manipFuse);
        config.setProperty("MSTAB", "" + UserConfig.manipStab);
        config.setProperty("MWING", "" + UserConfig.manipWing);


        config.setProperty("ROTX", "" + UserConfig.rotX);
        config.setProperty("ROTY", "" + UserConfig.rotY);
        config.setProperty("ROTZ", "" + UserConfig.rotZ);

        config.setProperty("CULL", "" + UserConfig.cull);
        config.setProperty("SOLID", "" + UserConfig.solid);
        config.setProperty("LIGHT", "" + UserConfig.light);

        try {
            File fout = new File(appRep);
            if (!fout.exists()) {
                fout.mkdirs();
            }
            config.store(new FileOutputStream(appRep + configFile), "Properties");
            logln(System.getProperty("line.separator") + "**** Saving  configuration ok ****" + System.getProperty("line.separator") + "*******************************************");
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

    public static URL getDataResourceUrl(String file) {
        URL ret = getResourceUrl(file);
        try {
            ret = new URL("file:" + appRep + file);
            PredimRC.logDebugln(appRep + file + " resource  found!");
        } catch (MalformedURLException ex) {
            PredimRC.logln(appRep + file + " resource not found!");
        }
        return ret;
    }

    public static URL getResourceUrl(String path) {
        path = "resource/" + path;
        URL u = ClassLoader.getSystemResource(path);
        if (null == u) {
            u = ClassLoader.getSystemResource(path);
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
        logDebugln("load " + path + " image ok");
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

    public DrawableModel getDrawableModel() {
        return drawableModel;
    }

    public static JGL_3DMesh mergeMesh(JGL_3DMesh m1, JGL_3DMesh m2) {
        JGL_3DMesh mesh = new JGL_3DMesh();
        Enumeration e1 = m1.getFaces().elements();
        while (e1.hasMoreElements()) {
            mesh.addFace((JGL_3DTriangle) e1.nextElement());
        }
        Enumeration e2 = m2.getFaces().elements();
        while (e2.hasMoreElements()) {
            mesh.addFace((JGL_3DTriangle) e2.nextElement());
        }
        return mesh;
    }

    public static void loadModel() throws FileNotFoundException {
        PredimRC.log("load of :" + UserConfig.filename);
        String versionInfile = "unknown";
        FileInputStream in_pute = new FileInputStream(UserConfig.filename);
        try {
            ObjectInputStream p = new ObjectInputStream(in_pute);
            versionInfile = ((ModelVersion) p.readObject()).VERSION_MODEL;
            PredimRC.log(", version in file to load:" + versionInfile);
            if (versionInfile.equals((new ModelVersion()).VERSION_MODEL)) {
                PredimRC.getInstance().drawableModel = new DrawableModel((Model) p.readObject());
                PredimRC.getInstance().setTitle("PredimRC  --  " + UserConfig.filename);
                PredimRC.logln(" success.");
                ModelController.applyChange();
            } else {
                throw new IOException();
            }
        } catch (IOException | ClassNotFoundException p) {
            PredimRC.logln(" failed!");
            JOptionPane.showMessageDialog(null, "error while opening file " + UserConfig.filename + ",  model version:" + versionInfile + ". I can only open version " + (new ModelVersion()).VERSION_MODEL + ".", null, JOptionPane.ERROR_MESSAGE);
            PredimRC.resetModel();
        } finally {
            try {
                in_pute.close();
            } catch (IOException ex) {
                Logger.getLogger(PredimRC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void loadModelWithChooser() {
        checkModelDir();
        JFileChooser chooser = new JFileChooser(appRep + "models/");
        chooser.setFileFilter(new FileNameExtensionFilter("PredimRC model.", FILE_EXTENSION));
        chooser.setDialogTitle("Load PredimRC model from disk");

        int valid = chooser.showOpenDialog(new JPanel());

        if (valid == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if ((selectedFile != null) && (!selectedFile.isDirectory())) {
                String fich = selectedFile.getName();
                String extension = "";
                int i = fich.lastIndexOf('.');
                extension = fich.substring(i + 1).toLowerCase();
                if (extension.equals(FILE_EXTENSION)) {
                    UserConfig.filename = selectedFile.getAbsolutePath();
                    try {
                        loadModel();
                    } catch (HeadlessException | IOException p) {
                        PredimRC.logln(" failed!");
                        JOptionPane.showMessageDialog(null, "error while opening file", null, JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selected file is not a PredimRC model", null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void saveModel() {
        if (UserConfig.filename.equals("")) {
            saveModelWithChooser();
            return;
        }
        File fichier = new File(UserConfig.filename);
        PredimRC.log("Save model to " + fichier.getAbsolutePath());
        try {
            try (FileOutputStream ostream = new FileOutputStream(fichier)) {
                ObjectOutputStream p = new ObjectOutputStream(ostream);
                p.writeObject(new ModelVersion());
                p.writeObject(PredimRC.getInstanceDrawableModel().generateModel());
                p.flush();
            }
            PredimRC.getInstance().setTitle("PredimRC  --  " + UserConfig.filename);
            PredimRC.logln(" success.");
        } catch (Exception p) {
            PredimRC.logln(" failed. error while trying to write file :" + fichier.getAbsolutePath() + ":" + p.toString());
        }
    }

    public static void saveModelWithChooser() {
        checkModelDir();
        JFileChooser chooser = new JFileChooser(appRep + "models/");
        chooser.setDialogTitle("Save PredimRC model to disk");

        if (chooser.showSaveDialog(PredimRC.getInstance()) == JFileChooser.APPROVE_OPTION) {
            //If they clicked yes call fileSaver method
            UserConfig.filename = chooser.getSelectedFile().getAbsolutePath();
            UserConfig.filename = UserConfig.filename.endsWith(FILE_EXTENSION) ? UserConfig.filename : UserConfig.filename + "." + FILE_EXTENSION;
            saveModel();
        } else {
            //Show cancelled message
            JOptionPane.showMessageDialog(PredimRC.getInstance(), "Save Cancelled.", "Action cancelled by user", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void checkModelDir() {
        try {
            File modeldir = new File(appRep + "models/");
            if (!modeldir.exists()) {
                modeldir.mkdirs();
            }
        } catch (Exception p) {
            PredimRC.logln("error while trying to create directory :" + appRep + "models/".toString());
        }

    }

    private void setFilename(String property) {
        UserConfig.filename = property;
        setTitle("PredimRC  --  " + UserConfig.filename);
    }

    public static void resetModel() {
        logDebugln("resetModel()");
        getInstance().drawableModel = DrawableModel.makeDefaultModel();
        getInstance().setFilename("");
        ModelController.applyChange();
    }

    public static void repaintDrawPanels() {
        getInstance().mainView.repaintDrawPanels();
    }
}