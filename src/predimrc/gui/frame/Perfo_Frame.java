/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.subframe.InterpolViewer;
import predimrc.gui.frame.subframe.panel.FreeChartPanel;
import predimrc.gui.frame.subframe.panel.PerfoResults;
import predimrc.gui.graphic.drawable.model.DrawableModel;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class Perfo_Frame extends ExternalFrame implements MouseListener {

    private JButton calc = new JButton("Recompute");
    /**
     *
     */
    private JMenuItem aeroContrib = new JMenuItem("Aeronautic factors");
    private JMenuItem airFactors = new JMenuItem("Air factors");
    private JMenuItem interpol = new JMenuItem("Interpol");
    private FreeChartPanel vzvPanel = new FreeChartPanel("", "V", "Vz", new XYSeriesCollection());
    private FreeChartPanel glideRatioPanel = new FreeChartPanel("", "V", "GlideRatio", new XYSeriesCollection());
    private FreeChartPanel czVPanel = new FreeChartPanel("", "V", "Cz", new XYSeriesCollection());
    private FreeChartPanel interpolPanel = new FreeChartPanel("", "", "Interpol", new XYSeriesCollection());
    private PerfoResults results = new PerfoResults();
    public static boolean initDone = false;
    /**
     *
     */
    private static Perfo_Frame instance;

    public static Perfo_Frame getInstance() {
        return instance;
    }

    public static Perfo_Frame maketInstance(AbstractButton _caller) {
        instance = new Perfo_Frame(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
        initDone = true;
        return instance;
    }

    private Perfo_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "Perfos";
        setTitle(title);

        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));

        vzvPanel.fit(1, 0.33);
        glideRatioPanel.fit(1, 0.33);
        czVPanel.fit(1, 0.33);


        JSplitPane splitPaneTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, glideRatioPanel, czVPanel);
        splitPaneTop.setOneTouchExpandable(true);
        splitPaneTop.setResizeWeight(0.33);
        // JSplitPane splitPaneBot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cXcZPanel, results);
        // splitPaneBot.setOneTouchExpandable(true);
        // splitPaneBot.setResizeWeight(0.75);
        //   JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, splitPaneBot);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, vzvPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.33);

        getContentPane().add(splitPane);
        JMenuBar menu = new JMenuBar();

        menu.add(airFactors);
        menu.add(aeroContrib);
        menu.add(interpol);
        //menu.add(create);
        // menu.add(modif);
        // menu.add(del);
        menu.add(calc);
        setJMenuBar(menu);
        pack();


        interpol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  new InterpolViewer(interpol, instance);
            }
        });

        aeroContrib.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  new ReynoldsConfig(aeroContrib, instance);
            }
        });
        airFactors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //   ModelController.addModelListener(new FoilSelectionConfigSubFrame(airFactors, instance));
            }
        });


        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel(PredimRC.getInstanceDrawableModel());
            }
        });
        updateModel(PredimRC.getInstanceDrawableModel());
    }

    @Override
    public void save() {
        //   PredimRC.saveModel();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    @Override
    public void updateModel(DrawableModel m) {
        // PolarDataBase.getPolars(xfoilconfig.getConfigs());
        updateGraphsAndResults();
    }

    private void cleanGraphsAndResults() {
        vzvPanel.clean();
        glideRatioPanel.clean();
        czVPanel.clean();
        results.clear();
    }

    public void updateGraphsAndResults() {
        cleanGraphsAndResults();
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
        glideRatioPanel.repaint();
        vzvPanel.repaint();
        czVPanel.repaint();
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
