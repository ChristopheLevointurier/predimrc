/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.subframe.FoilRenderer;
import predimrc.gui.frame.subframe.FoilSelectionConfigPanel;
import predimrc.gui.frame.subframe.FreeChartPanel;
import predimrc.gui.frame.subframe.PolarData;
import predimrc.gui.frame.subframe.PolarDataBase;
import predimrc.gui.frame.subframe.ReynoldsConfig;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {

    private JTabbedPane foilSelect = new JTabbedPane();
    private FoilSelectionConfigPanel foil0 = new FoilSelectionConfigPanel(0, "fad05.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil1 = new FoilSelectionConfigPanel(1, "fad07.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil2 = new FoilSelectionConfigPanel(2, "fad15.dat", 6, 100, 100);
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
    private FoilRenderer foilRenderer;
    private FreeChartPanel cXcZPanel = new FreeChartPanel("", "Cx", "Cz");
    private FreeChartPanel cZAlphaPanel = new FreeChartPanel("", "Alpha", "Cz");
    private FreeChartPanel cMcz = new FreeChartPanel("", "Cz", "Cm");
    /**
     *
     */
    private static XFoil_Frame instance;

    public static XFoil_Frame getInstance() {
        return instance;
    }

    public static XFoil_Frame maketInstance(AbstractButton _caller, XfoilConfig _xfoilconfig) {
        ReynoldsConfig.initReynolds();
        instance = new XFoil_Frame(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME, _xfoilconfig);
        return instance;
    }

    private XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y, XfoilConfig _xfoilconfig) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        xfoilconfig = _xfoilconfig;
        foil0.setConfig(xfoilconfig.getFoilName(0), xfoilconfig.getCrit(0), xfoilconfig.getXtrBot(0), xfoilconfig.getXtrTop(0));
        foil1.setConfig(xfoilconfig.getFoilName(1), xfoilconfig.getCrit(1), xfoilconfig.getXtrBot(1), xfoilconfig.getXtrTop(1));
        foil2.setConfig(xfoilconfig.getFoilName(2), xfoilconfig.getCrit(2), xfoilconfig.getXtrBot(2), xfoilconfig.getXtrTop(2));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));

        foilSelect.add(foil0);
        foilSelect.setForegroundAt(0, Color.red);
        foilSelect.add(foil1);
        foilSelect.setForegroundAt(1, Color.blue);
        foilSelect.add(foil2);
        foilSelect.setForegroundAt(2, Color.green.darker());
        user_panel.add(foilSelect);


        mainPanel.add(cZAlphaPanel);
        mainPanel.add(cXcZPanel);
        mainPanel.add(cMcz);
        mainPanel.add(new FreeChartPanel("", "bla", "tructruc"));

        getContentPane().add(mainPanel);

        JMenuBar menu = new JMenuBar();

        menu.add(reynoldsBut);
        menu.add(foilsBut);
        menu.add(viewFoilsBut);
        menu.add(create);
        menu.add(modif);
        menu.add(del);
        menu.add(calc);
        setJMenuBar(menu);
        changeFoil();
        updateModelXfoilConfig();
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
                new ReynoldsConfig(reynoldsBut);
            }
        });
        viewFoilsBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foilRenderer = new FoilRenderer(viewFoilsBut, foil0.getSelectedFoil(), foil1.getSelectedFoil(), foil2.getSelectedFoil());
            }
        });
    }

    public final void updateModelXfoilConfig() {
        //update model config
        xfoilconfig.setFoilConfig(0, foil0);
        xfoilconfig.setFoilConfig(1, foil1);
        xfoilconfig.setFoilConfig(2, foil2);
        refreshGraphs();
    }

    private void refreshGraphs() {
        cXcZPanel.clean();
        cZAlphaPanel.clean();
        cMcz.clean();
        for (String key : xfoilconfig.getConfigsToDisplay()) {
            PolarData p = PolarDataBase.getData(key);
            if (null != p) {
                predimrc.PredimRC.logDebugln("update xfoil:" + key);
                cXcZPanel.addSeries(FoilRenderer.listColor.get(p.getColIndex()), p.getReynoldsIndex(), key, p.getCzCxData());
                cZAlphaPanel.addSeries(FoilRenderer.listColor.get(p.getColIndex()), p.getReynoldsIndex(), key, p.getCzAlphaData());
                cMcz.addSeries(FoilRenderer.listColor.get(p.getColIndex()), p.getReynoldsIndex(), key, p.getCmCzData());
            }
        }
    }

    public void setReynolds(ArrayList<Boolean> r) {
        xfoilconfig.setReynolds(r);
        refreshGraphs();
    }

    public ArrayList<Boolean> getReynolds() {
        return xfoilconfig.getReynolds();
    }

    public final void changeFoil() {
        String s0 = foil0.getSelectedFoil();
        String s1 = foil1.getSelectedFoil();
        String s2 = foil2.getSelectedFoil();
        //update tabs title
        foilSelect.setTitleAt(0, s0);
        foilSelect.setTitleAt(1, s1);
        foilSelect.setTitleAt(2, s2);
        //update foilRenderer
        if (null != foilRenderer) {
            foilRenderer.setS0(s0);
            foilRenderer.setS1(s1);
            foilRenderer.setS2(s2);
            foilRenderer.updateChart();
        }
    }

    @Override
    public void save() {
        predimrc.PredimRC.logln("Save from " + title);
        PredimRC.saveModel();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }
}
