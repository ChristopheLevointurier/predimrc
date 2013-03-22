/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

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
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.controller.ModelController;
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.subframe.FoilRenderer;
import predimrc.gui.frame.subframe.FoilSelectionConfigSubFrame;
import predimrc.gui.frame.subframe.FreeChartPanel;
import predimrc.gui.frame.subframe.PolarData;
import predimrc.gui.frame.subframe.PolarDataBase;
import predimrc.gui.frame.subframe.ReynoldsConfig;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {

    private JButton modif = new JButton("Edit a foil");
    private JButton create = new JButton("Import a foil");
    private JButton del = new JButton("Delete a foil");
    private JButton calc = new JButton("Recaculate");
    /**
     *
     */
    private JMenuItem reynoldsBut = new JMenuItem("Select reynolds");
    private static JMenuItem foilsBut = new JMenuItem("Select foils");
    private JMenuItem viewFoilsBut = new JMenuItem("View foils");
    private XfoilConfig xfoilconfig;
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
        foilsBut.doClick();
        return instance;
    }

    private XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y, XfoilConfig _xfoilconfig) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        xfoilconfig = _xfoilconfig;
        //setconfig
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));

        mainPanel.add(cZAlphaPanel);
        mainPanel.add(cXcZPanel);
        mainPanel.add(cMcz);
        mainPanel.add(new FreeChartPanel("", "bla", "tructruc"));

        getContentPane().add(mainPanel);
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
        updateModel(predimrc.PredimRC.getInstanceDrawableModel());
    }

    public ArrayList<Boolean> getReynolds() {
        return xfoilconfig.getReynolds();
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

    @Override
    public void updateModel(DrawableModel m) {
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
}
