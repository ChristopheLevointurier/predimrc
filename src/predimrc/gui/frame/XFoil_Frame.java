/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.jfree.data.xy.XYSeriesCollection;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.controller.ModelController;
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.subframe.FoilRenderer;
import predimrc.gui.frame.subframe.FoilSelectionConfigSubFrame;
import predimrc.gui.frame.subframe.FreeChartPanel;
import predimrc.gui.frame.subframe.PolarData;
import predimrc.gui.frame.subframe.PolarDataBase;
import predimrc.gui.frame.subframe.PolarKey;
import predimrc.gui.frame.subframe.ReynoldsConfig;
import predimrc.gui.frame.subframe.XFoilResults;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame implements MouseListener {

    private JButton modif = new JButton("Edit a foil");
    private JButton create = new JButton("Import a foil");
    private JButton del = new JButton("Delete a foil");
    private JButton calc = new JButton("Recaculate");
    /**
     *
     */
    private JMenuItem reynoldsBut = new JMenuItem("Select reynolds");
    private JMenuItem foilsBut = new JMenuItem("Select foils");
    private JMenuItem viewFoilsBut = new JMenuItem("View foils");
    private XfoilConfig xfoilconfig;
    private FreeChartPanel cXcZPanel = new FreeChartPanel("", "Cx", "Cz", new XYSeriesCollection());
    private FreeChartPanel cZAlphaPanel = new FreeChartPanel("", "Alpha", "Cz", new XYSeriesCollection());
    private FreeChartPanel cMcz = new FreeChartPanel("", "Cz", "Cm", new XYSeriesCollection());
    private XFoilResults results = new XFoilResults();
    public static boolean initDone = false;
    /**
     *
     */
    private static XFoil_Frame instance;
    public static final Color[] listColor = {new Color(105, 50, 0), new Color(0, 0, 135), new Color(0, 195, 50)};
    private static final int[] alphas = {55, 88, 121, 154, 187, 220, 255};

    public static XFoil_Frame getInstance() {
        return instance;
    }

    public static XFoil_Frame maketInstance(AbstractButton _caller, XfoilConfig _xfoilconfig) {
        ReynoldsConfig.initReynolds();
        instance = new XFoil_Frame(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME, _xfoilconfig);
        initDone = true;
        return instance;
    }

    private XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y, XfoilConfig _xfoilconfig) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        xfoilconfig = _xfoilconfig;

        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));





        JSplitPane splitPaneTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cZAlphaPanel, cXcZPanel);
        splitPaneTop.setOneTouchExpandable(true);
        splitPaneTop.setResizeWeight(0.5);
        JSplitPane splitPaneBot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cMcz, results);
        splitPaneBot.setOneTouchExpandable(true);
        splitPaneBot.setResizeWeight(0.75);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, splitPaneBot);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.25);

        getContentPane().add(splitPane);
        JMenuBar menu = new JMenuBar();

        menu.add(foilsBut);
        menu.add(viewFoilsBut);
        menu.add(reynoldsBut);
        menu.add(create);
        menu.add(modif);
        menu.add(del);
        menu.add(calc);
        setJMenuBar(menu);
        pack();


        reynoldsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReynoldsConfig(reynoldsBut);
            }
        });
        foilsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(new FoilSelectionConfigSubFrame(foilsBut));
            }
        });
        viewFoilsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(new FoilRenderer(viewFoilsBut));
            }
        });


        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });



        updateModel(predimrc.PredimRC.getInstanceDrawableModel());
        foilsBut.doClick();
        //    splitPane.addMouseListener(this);
    }

    public ArrayList<Boolean> getReynolds() {
        return xfoilconfig.getReynolds();
    }

   @Override
    public void save() {
        //   PredimRC.saveModel();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    public static Color getAppropriateColor(int s, int r) {
        if (s >= 0 && r >= 0 && s < listColor.length && r < alphas.length) {
            Color col = listColor[s];
            return new Color(col.getRed() + r * 25, col.getGreen() + r * 10, col.getBlue() + r * 20, alphas[r]);
        } else {
            return Color.black;
        }
    }

    public void addPolar(PolarData p) {
        if (null != p) {
            Color col = getAppropriateColor(p.getColIndex(), p.getReynoldsIndex());
            cXcZPanel.addSeries(col, p.getCzCxData());
            cZAlphaPanel.addSeries(col, p.getCzAlphaData());
            cMcz.addSeries(col, p.getCmCzData());
            if (p.getReynoldsIndex() == ReynoldsConfig.reyRefForResults) {
                results.set0(p.getColIndex(), p.getCmCzData(), p.getCzAlphaData());
                cZAlphaPanel.addPoint(results.getAlpha(p.getColIndex()), 0);
                cMcz.addPoint(0, results.getCm(p.getColIndex()));
            }
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        cXcZPanel.clean();
        cZAlphaPanel.clean();
        cMcz.clean();
        results.clear();
        //PredimRC.logln("udpdate model:");
        for (String key : xfoilconfig.getConfigsToDisplay()) {
            predimrc.PredimRC.logDebugln("update xfoil:" + key);
            PolarData p = PolarDataBase.getPolar(new PolarKey(key), true);
            addPolar(p);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //    throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        cZAlphaPanel.repaint();
        cXcZPanel.repaint();
        cMcz.repaint();
        //  results.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //     throw new UnsupportedOperationException("Not supported yet.");
    }
}
