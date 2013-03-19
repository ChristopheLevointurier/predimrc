/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.subframe.FoilRenderer;
import predimrc.gui.frame.subframe.FoilSelectionConfigPanel;
import predimrc.gui.frame.subframe.FreeChartPanel;
import predimrc.gui.frame.subframe.PolarData;
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
    private FoilSelectionConfigPanel foil1 = new FoilSelectionConfigPanel(1, this, "fad05.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil2 = new FoilSelectionConfigPanel(2, this, "fad07.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil3 = new FoilSelectionConfigPanel(3, this, "fad15.dat", 6, 100, 100);
    private FoilRenderer foilRenderer;
    private ReynoldsConfig reynoldConfig;
    private JButton modif = new JButton("Edit a foil");
    private JButton create = new JButton("Import a foil");
    private JButton del = new JButton("Delete a foil");
    private JButton calc = new JButton("Recaculate");
    private XfoilConfig xfoilconfig;

    public XFoil_Frame(AbstractButton _caller, XfoilConfig _xfoilconfig) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME, _xfoilconfig);
    }

    public XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y, XfoilConfig _xfoilconfig) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        xfoilconfig = _xfoilconfig;
        foil1.setConfig(xfoilconfig.getFoilName(1), xfoilconfig.getCrit(1), xfoilconfig.getXtrBot(1), xfoilconfig.getXtrTop(1));
        foil2.setConfig(xfoilconfig.getFoilName(2), xfoilconfig.getCrit(2), xfoilconfig.getXtrBot(2), xfoilconfig.getXtrTop(2));
        foil3.setConfig(xfoilconfig.getFoilName(3), xfoilconfig.getCrit(3), xfoilconfig.getXtrBot(3), xfoilconfig.getXtrTop(3));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));
        JPanel zone3 = new JPanel();
        zone3.setLayout(new BoxLayout(zone3, BoxLayout.Y_AXIS));


        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));

        foilSelect.add(foil1);
        foilSelect.setForegroundAt(0, Color.red);
        foilSelect.add(foil2);
        foilSelect.setForegroundAt(1, Color.blue);
        foilSelect.add(foil3);
        foilSelect.setForegroundAt(2, Color.green.darker());
        reynoldConfig = new ReynoldsConfig(xfoilconfig.getReynolds());
        user_panel.add(reynoldConfig);
        user_panel.add(foilSelect);

        foilRenderer = new FoilRenderer(foil1.getSelectedFoil(), foil2.getSelectedFoil(), foil3.getSelectedFoil());
        zone3.add(user_panel);
        zone3.add(foilRenderer);

        FreeChartPanel cXcZPanel = new FreeChartPanel("", "Cx", "Cz");
        PolarData polData = new PolarData("FAD18", 1500000, 9);
        cXcZPanel.addSeries(Color.red, polData.getCxCzData());
        mainPanel.add(cXcZPanel);

        FreeChartPanel cZAlphaPanel = new FreeChartPanel("", "Alpha", "Cz");
        cZAlphaPanel.addSeries(Color.red, polData.getCzAlphaData());
        mainPanel.add(cZAlphaPanel);

        mainPanel.add(zone3);

        FreeChartPanel cMcz = new FreeChartPanel("", "Cz", "Cm");
        cMcz.addSeries(Color.red, polData.getCmCzData());
        mainPanel.add(cMcz);

        getContentPane().add(mainPanel);

        JMenuBar menu = new JMenuBar();
        menu.add(create);
        menu.add(modif);
        menu.add(del);
        menu.add(calc);
        setJMenuBar(menu);
        updateData();
        pack();
    }

    public final void updateData() {
        String s1 = foil1.getSelectedFoil();
        String s2 = foil2.getSelectedFoil();
        String s3 = foil3.getSelectedFoil();
        foilSelect.setTitleAt(0, s1);
        foilSelect.setTitleAt(1, s2);
        foilSelect.setTitleAt(2, s3);
        foilRenderer.setS1(s1);
        foilRenderer.setS2(s2);
        foilRenderer.setS3(s3);
        foilRenderer.updateChart();
    }

    @Override
    public void save() {
        predimrc.PredimRC.logln("Save from " + title);
        PredimRC.getInstanceDrawableModel().setXfoilConfig(getXFoilConfig());
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    private XfoilConfig getXFoilConfig() {
        xfoilconfig.setFoilConfig(1, foil1);
        xfoilconfig.setFoilConfig(2, foil2);
        xfoilconfig.setFoilConfig(3, foil3);
        xfoilconfig.setReynolds(reynoldConfig.getConfig());
        return xfoilconfig;
    }
}
