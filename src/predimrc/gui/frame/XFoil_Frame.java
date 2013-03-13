/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.widget.MegaCombo;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {

    public XFoil_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));


        String[] fileList;
        fileList = new File(".\\src\\resource\\AirFoils").list();
        MegaCombo airfoil1_combo = new MegaCombo("Foil 1", true, fileList);
        MegaCombo airfoil2_combo = new MegaCombo("Foil 2", true, fileList);
        MegaCombo airfoil3_combo = new MegaCombo("Foil 3", true, fileList);
        JCheckBox reynolds_25k_check = new JCheckBox("25 k", true);
        JCheckBox reynolds_50k_check = new JCheckBox("50 k", true);
        JCheckBox reynolds_100k_check = new JCheckBox("100 k", true);
        JCheckBox reynolds_250k_check = new JCheckBox("250 k", true);
        JCheckBox reynolds_750k_check = new JCheckBox("750 k", true);
        JCheckBox reynolds_1500k_check = new JCheckBox("1500 k", true);

        JPanel zone3 = new JPanel();
        zone3.setLayout(new BoxLayout(zone3, BoxLayout.Y_AXIS));
        zone3.add(new JButton(PredimRC.getImageIcon("xfoil4.png")));


        // Reynolds panel
        JPanel user_panel = new JPanel();
        user_panel.setLayout(new BoxLayout(user_panel, BoxLayout.X_AXIS));
        JPanel reynolds_panel = new JPanel();
        reynolds_panel.setLayout(new BoxLayout(reynolds_panel, BoxLayout.Y_AXIS));
        reynolds_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Selected Reynolds"));
        reynolds_panel.add(reynolds_25k_check);
        reynolds_panel.add(reynolds_50k_check);
        reynolds_panel.add(reynolds_100k_check);
        reynolds_panel.add(reynolds_250k_check);
        reynolds_panel.add(reynolds_750k_check);
        reynolds_panel.add(reynolds_1500k_check);

        airfoil1_combo.getLabel().setForeground(Color.RED);
        airfoil2_combo.getLabel().setForeground(Color.GREEN);
        airfoil3_combo.getLabel().setForeground(Color.BLUE);

        JPanel foilCombo_panel = new JPanel();
        foilCombo_panel.setLayout(new BoxLayout(foilCombo_panel, BoxLayout.Y_AXIS));
        foilCombo_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Selected Foils"));
        foilCombo_panel.add(airfoil1_combo);
        foilCombo_panel.add(airfoil2_combo);
        foilCombo_panel.add(airfoil3_combo);


        user_panel.add(reynolds_panel);
        user_panel.add(foilCombo_panel);

        zone3.add(user_panel);



        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil1.png")));
        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil2.png")));
        mainPanel.add(zone3);
        mainPanel.add(new JButton(PredimRC.getImageIcon("xfoil3.png")));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
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
