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
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DVector;
import predimrc.controller.ModelController;
import predimrc.gui.frame.Note_Frame;
import predimrc.gui.frame.The3D_Frame;
import predimrc.gui.graphic.ConfigView;
import predimrc.gui.graphic.MainView;
import predimrc.gui.graphic.drawable.DrawablePoint;
import predimrc.model.Model;

/**
 *
 * @author Christophe Levointurier 11/2012
 * @version
 * @see
 * @since
 */
public class PredimRC extends JFrame {

    /**
     * consts
     */
    private static final String externalRefDoc = "https://code.google.com/p/predimrc/downloads/detail?name=CDC_PredimRc.pdf&can=2&q=";
    private static final String DEFAULT_KEY_VALUE = "Unknown Key - Old version file problem";
    private static final String FILE_EXTENSION = "predimodel";
    final static float dash1[] = {10.0f};
    public final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    private static final String VERSION = "Alpha 0.1.5";
    private static final long serialVersionUID = -2615396482200960443L;    // private final static String saveFileName = "links.txt";
    public static final String appRep = System.getProperty("user.home") + "\\PredimRCFiles\\";
    public static final String modelRep = System.getProperty("user.home") + "\\PredimRCFiles\\models\\";
    public static final int DEFAULT_X_FRAME = 800;
    public static final int DEFAULT_Y_FRAME = 600;
    public static final int MAIN_FRAME_SIZE_X = 1120;
    public static final int MAIN_FRAME_SIZE_Y = 758;
    private static final boolean DEBUG_MODE = true;
    private static final String configFile = "config.cfg";
    public static final String defaultLabelContent = "xx";
    /**
     *
     */
    public static String airfoilsDirectory = System.getProperty("user.home") + "\\PredimRCFiles\\";
    public static String filename = "";
    private static JButton aboutbut, help, savebut;
    ;
    private static JMenuItem savetarget, opentarget;
    private static JMenuItem quit, openConfigRep;
    private static JToggleButton logbut, modelNoteBut, the3DViewButton;
    // public static NumSelect amountThread = new NumSelect(3, 10, false, 1, 99);
    //  public static long threadsCount = 0;
    private static PredimRC instance;
    private static StringBuffer log = new StringBuffer();
    public static Image icon;
    public static ImageIcon imageIcon;
    private static String[] tabNames = {"Model", "Airfoils", "Performances", "Motorization", "rudders", "Model comparison"};
    private static String[] tabTooltip = {"Model configuration", "Selection of the airfoil", "Dynamic performances of the model", "Allow to define motorization of the model", "Rudders definition", "Allow to compare several predimRC models"};
    private Model model;
    public static boolean warnClosePopup = true;
    /**
     *
     * componentns of the view
     */
    private MainView mainView;
    private ConfigView configView;

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
        getInstance();
        loadConfiguration();
        getInstance().setUpAndFillComponents();

    }

    private PredimRC() {
        super("PredimRC");
        log = new StringBuffer();
        model = new Model();
        mainView = new MainView();
        configView = new ConfigView();
        aboutbut = new JButton("About...");
        savebut = new JButton(getImageIcon("harddisk.jpg"));
        the3DViewButton = new JToggleButton("3D View");
        help = new JButton("Help!");
        logbut = new JToggleButton("log", false);
        modelNoteBut = new JToggleButton("Notes", false);
        savetarget = new JMenuItem("Save model...");
        opentarget = new JMenuItem("Open model");
        quit = new JMenuItem("Quit");

        JMenuBar menu = new JMenuBar();
        JMenu filemenu = new JMenu("File");

        /**
         * register new listener of the model*
         */
        ModelController.addModelListener(mainView);
        ModelController.addModelListener(configView);


        filemenu.add(opentarget);
        filemenu.add(savetarget);
        filemenu.addSeparator();
        filemenu.add(quit);

        menu.add(filemenu);
        menu.add(modelNoteBut);
        menu.add(the3DViewButton);
        menu.add(logbut);
        menu.add(savebut);
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
                if (the3DViewButton.isEnabled()) {
                    new The3D_Frame(the3DViewButton);
                }
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
                PredimRC.loadModelWithChooser();
            }
        });



        savetarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.saveModelWithChooser();
            }
        });


        savebut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.saveModel();
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
        try {
            loadModel();
        } catch (IOException ex) {
        }
        logln("filling components...");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainView, BorderLayout.CENTER);
        getContentPane().add(configView, BorderLayout.EAST);
        setIconImage(icon);
        setVisible(true);
        //pack();
        setSize(MAIN_FRAME_SIZE_X, MAIN_FRAME_SIZE_Y);
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
            PredimRC.getInstance().setFilename(config.getProperty("FILENAME", DEFAULT_KEY_VALUE));
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
        config.setProperty("FILENAME", "" + PredimRC.getInstance().filename);
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

    public Model getModel() {
        return model;
    }

    public static final JGL_3DMesh getRectangle(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3, JGL_3DVector p4, int r, int g, int b) {
        JGL_3DMesh mesh = new JGL_3DMesh();
        Color color = new Color(r, g, b);
        mesh.addFace(new JGL_3DTriangle(p1, p2, p3, color));
        mesh.addFace(new JGL_3DTriangle(p1, p3, p4, color));
        return mesh;
    }

    public static final JGL_3DMesh mergeMesh(JGL_3DMesh m1, JGL_3DMesh m2) {
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

    public static final JGL_3DVector getNearestVertex(JGL_3DMesh m, JGL_3DVector p) {
        JGL_3DVector temp = new JGL_3DVector(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Enumeration e = m.getPoints().elements();
        while (e.hasMoreElements()) {
            JGL_3DVector temp2 = (JGL_3DVector) e.nextElement();
            if (distance(p, temp2) < distance(p, temp)) {
                temp = temp2;
            }
        }
        return temp;
    }

    public static final double distance(JGL_3DVector p1, JGL_3DVector p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
                + (p1.y - p2.y) * (p1.y - p2.y)
                + (p1.z - p2.z) * (p1.z - p2.z));
    }

    public static final double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
                + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static final double distance(DrawablePoint p1, DrawablePoint p2) {
        return Math.sqrt((p1.getFloatX() - p2.getFloatX()) * (p1.getFloatX() - p2.getFloatX())
                + (p1.getFloatY() - p2.getFloatY()) * (p1.getFloatY() - p2.getFloatY()));
    }

    public static void loadModel() throws FileNotFoundException, IOException {
        PredimRC.log("load of :" + filename);
        FileInputStream in_pute = new FileInputStream(filename);
        try {
            ObjectInputStream p = new ObjectInputStream(in_pute);
            PredimRC.getInstance().model = ((Model) p.readObject());
            PredimRC.getInstance().setTitle("PredimRC  --  " + PredimRC.getInstance().filename);
            PredimRC.logln(" success.");
        } catch (IOException | ClassNotFoundException p) {
            PredimRC.logln(" failed!");
            JOptionPane.showMessageDialog(null, "error while opening file " + filename, null, JOptionPane.ERROR_MESSAGE);
        } finally {
            in_pute.close();
            ModelController.changeModel(getInstance().getModel());
        }

    }

    public static void loadModelWithChooser() {
        checkModelDir();
        JFileChooser chooser = new JFileChooser(modelRep);
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
                    filename = selectedFile.getAbsolutePath();
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
        File fichier = new File(filename);
        PredimRC.log("Save model to " + fichier.getAbsolutePath());
        try {
            FileOutputStream ostream = new FileOutputStream(fichier);
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(PredimRC.getInstance().model);
            p.flush();
            ostream.close();
            PredimRC.getInstance().setTitle("PredimRC  --  " + PredimRC.getInstance().filename);
            PredimRC.logln(" success.");
        } catch (Exception p) {
            PredimRC.logln(" failed. error while trying to write file :" + fichier.getAbsolutePath() + ":" + p.toString());
        }
    }

    public static void saveModelWithChooser() {
        checkModelDir();
        JFileChooser chooser = new JFileChooser(modelRep);
        chooser.setDialogTitle("Save PredimRC model to disk");

        if (chooser.showSaveDialog(PredimRC.getInstance()) == JFileChooser.APPROVE_OPTION) {
            //If they clicked yes call fileSaver method
            filename = chooser.getSelectedFile().getAbsolutePath();
            filename = filename.endsWith(FILE_EXTENSION) ? filename : filename + "." + FILE_EXTENSION;
            saveModel();
        } else {
            //Show cancelled message
            JOptionPane.showMessageDialog(PredimRC.getInstance(), "Save Cancelled.", "Action cancelled by user", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void checkModelDir() {
        try {
            File modeldir = new File(modelRep);
            if (!modeldir.exists()) {
                modeldir.mkdirs();
            }
        } catch (Exception p) {
            PredimRC.logln("error while trying to create directory :" + modelRep.toString());
        }

    }

    private void setFilename(String property) {
        filename = property;
    }
}