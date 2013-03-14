/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
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
import predimrc.gui.frame.subframe.FoilSelectionConfigPanel;
import predimrc.gui.frame.subframe.ReynoldsConfig;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {

    private JTabbedPane foilSelect = new JTabbedPane();
    private FoilSelectionConfigPanel foil1 = new FoilSelectionConfigPanel(1, this);
    private FoilSelectionConfigPanel foil2 = new FoilSelectionConfigPanel(2, this);
    private FoilSelectionConfigPanel foil3 = new FoilSelectionConfigPanel(3, this);
    private JButton modif = new JButton("edit a foil");
    private JButton create = new JButton("create a foil");
    private JButton del = new JButton("delete a foil");
    private JButton calc = new JButton("compute");

    public XFoil_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));
        JPanel zone3 = new JPanel();
        zone3.setLayout(new BoxLayout(zone3, BoxLayout.Y_AXIS));
        zone3.add(new JButton(PredimRC.getImageIcon("xfoil4.png")));


        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));

        foilSelect.add(foil1);
        foilSelect.setForegroundAt(0, Color.red);
        foilSelect.add(foil2);
        foilSelect.setForegroundAt(1, Color.blue);
        foilSelect.add(foil3);
        foilSelect.setForegroundAt(2, Color.green.darker());
        user_panel.add(new ReynoldsConfig());
        user_panel.add(foilSelect);

        zone3.add(user_panel);
        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil1.png")));
        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil2.png")));
        mainPanel.add(zone3);
        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil3.png")));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        updateData();



        JMenuBar menu = new JMenuBar();
        menu.add(create);
        menu.add(modif);
        menu.add(del);
        menu.add(calc);

        setJMenuBar(menu);

    }

    public final void updateData() {
        foilSelect.setTitleAt(0, foil1.getSelectedFoil());
        foilSelect.setTitleAt(1, foil2.getSelectedFoil());
        foilSelect.setTitleAt(2, foil3.getSelectedFoil());

    }

    @Override
    public void save() {
        predimrc.PredimRC.logln("Save from " + title);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }
}
