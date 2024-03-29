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
import predimrc.gui.frame.subframe.panel.FreeChartPanel;
import predimrc.data.PolarData;
import predimrc.data.PolarDataBase;
import predimrc.data.PolarKey;
import predimrc.gui.frame.subframe.ReynoldsConfig;
import predimrc.gui.frame.subframe.panel.XFoilResults;
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
    private JButton calc = new JButton("Recompute polars");
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
        if (!initDone) {
            PredimRC.logln("Xfoil frame call before init!");
        }
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


        cZAlphaPanel.fit(0.5, 0.5);
        cMcz.fit(0.5, 0.5);
        cXcZPanel.fit(0.5, 0.5);


        JSplitPane splitPaneTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cZAlphaPanel, cMcz);
        splitPaneTop.setOneTouchExpandable(true);
        splitPaneTop.setResizeWeight(0.5);
        // JSplitPane splitPaneBot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cXcZPanel, results);
        // splitPaneBot.setOneTouchExpandable(true);
        // splitPaneBot.setResizeWeight(0.75);
        //   JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, splitPaneBot);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, cXcZPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.25);

        getContentPane().add(splitPane);
        JMenuBar menu = new JMenuBar();

        menu.add(foilsBut);
        menu.add(viewFoilsBut);
        menu.add(reynoldsBut);
        //menu.add(create);
        // menu.add(modif);
        // menu.add(del);
        menu.add(calc);
        setJMenuBar(menu);
        pack();


        reynoldsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReynoldsConfig(reynoldsBut, instance);
            }
        });
        foilsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(new FoilSelectionConfigSubFrame(foilsBut, instance));
            }
        });
        viewFoilsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelController.addModelListener(new FoilRenderer(viewFoilsBut, instance));
            }
        });


        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePolars();
                updateModel(PredimRC.getInstanceDrawableModel());
            }
        });



        updateModel(PredimRC.getInstanceDrawableModel());
        foilsBut.doClick();  //opens the pane
    }

    public ArrayList<Boolean> getReynolds() {
        return xfoilconfig.getReynolds();
    }

    @Override
    public void save() {
        PredimRC.saveModel();
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
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        predimrc.PredimRC.logDebugln("XFoilFrame updateModel");
        PolarDataBase.getPolars(xfoilconfig.getConfigs());
        updateGraphsAndResults();
    }

    private void cleanGraphsAndResults() {
        cXcZPanel.clean();
        cZAlphaPanel.clean();
        cMcz.clean();
        results.clear();
    }

    public void updateGraphsAndResults() {
        cleanGraphsAndResults();
        predimrc.PredimRC.logDebugln("update polardatabase:" + xfoilconfig.getConfigsToDisplay());
        for (String key : xfoilconfig.getConfigsToDisplay()) {
            addPolar(PolarDataBase.getPolar(new PolarKey(key), true));
        }

        for (String key : xfoilconfig.getConfigsToCompute()) {
            predimrc.PredimRC.logDebugln("update polardatabase calc:" + key);
            PolarKey polkey = new PolarKey(key);
            PolarData p = PolarDataBase.getPolar(polkey, true);
            if (p != null) {
                results.set0(p.getColIndex(), p.getCmCzData(), p.getCzAlphaData());
                cZAlphaPanel.addPoint(results.getAlpha(p.getColIndex()), 0);
                cMcz.addPoint(0, results.getCm(p.getColIndex()));
            } else {
                results.set0Error(polkey.getColIndex());
            }
        }
    }

    private void removePolars() {
        for (String key : xfoilconfig.getConfigsToDisplay()) {
            predimrc.PredimRC.logDebugln("deleted polardatabase :" + key);
            PolarDataBase.removePolar(new PolarKey(key));
        }
    }

    public XfoilConfig getXfoilconfig() {
        return xfoilconfig;
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
