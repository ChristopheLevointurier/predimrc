/*This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.frame.subframe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import predimrc.PredimRC;
import predimrc.controller.ModelController;
import predimrc.gui.widget.MegaCombo;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilSelectionConfigPanel extends JPanel {

    static ArrayList<Color> colors = new ArrayList();
    static String[] fileList = new File(predimrc.PredimRC.appRep + "/AirFoils").list();
    static String[] xtr = {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "100"};
    private MegaCombo airfoil_combo, xtrTop, xtrBot;
    private JRadioButton crit3 = new JRadioButton("3", true);
    private JRadioButton crit6 = new JRadioButton("6", false);
    private JRadioButton crit9 = new JRadioButton("9", false);
    private JRadioButton crit12 = new JRadioButton("12", false);
    private int index = 0;
    private FoilSelectionConfigPanel instance;
    private ActionListener configAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            PredimRC.getInstanceDrawableModel().getXfoilConfig().setFoilConfig(index, instance);
            ModelController.applyChange();
        }
    };

    public FoilSelectionConfigPanel(int i, String selected, int crits, int _xtrBot, int _xtrTop) {
        airfoil_combo = new MegaCombo("Foil", true, fileList);
        airfoil_combo.addItem(" ");
        index = i;
        xtrTop = new MegaCombo("XTRtop", true, xtr);
        xtrBot = new MegaCombo("XTRbottom", true, xtr);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(airfoil_combo);

        ButtonGroup b = new ButtonGroup();
        b.add(crit3);
        b.add(crit6);
        b.add(crit9);
        b.add(crit12);

        JPanel crit = new JPanel();
        crit.setLayout(new BoxLayout(crit, BoxLayout.X_AXIS));
        crit.add(new JLabel("Ncrit:"));
        crit.add(crit3);
        crit.add(crit6);
        crit.add(crit9);
        crit.add(crit12);
        add(crit);

        JPanel xt = new JPanel();
        xt.setLayout(new BoxLayout(xt, BoxLayout.X_AXIS));
        xt.add(xtrTop);
        xt.add(xtrBot);
        add(xt);
        setConfig(selected, crits, _xtrBot, _xtrTop);

        xtrTop.addActionListener(configAction);
        xtrBot.addActionListener(configAction);
        crit3.addActionListener(configAction);
        crit6.addActionListener(configAction);
        crit9.addActionListener(configAction);
        crit12.addActionListener(configAction);
        airfoil_combo.addActionListener(configAction);
        instance = this;
    }

    public final void setConfig(String selected, int crits, int _xtrBot, int _xtrTop) {
        //    System.out.println("set:" + selected + crits + _xtrBot + _xtrTop);
        xtrBot.setValue("" + _xtrBot, false);
        xtrTop.setValue("" + _xtrTop, false);
        switch (crits) {
            case (3): {
                crit3.setSelected(true);
                break;
            }
            case (6): {
                crit6.setSelected(true);
                break;
            }
            case (9): {
                crit9.setSelected(true);
                break;
            }
            case (12): {
                crit12.setSelected(true);
                break;
            }
            default:
                break;
        }
        airfoil_combo.setValue(selected, false);
    }

    public String getSelectedFoil() {
        return airfoil_combo.getValue();
    }

    public int getCrit() {
        int ret = 3;
        if (crit6.isSelected()) {
            ret = 6;
        }
        if (crit9.isSelected()) {
            ret = 9;
        }
        if (crit12.isSelected()) {
            ret = 12;
        }
        return ret;
    }

    public int getXtrTop() {
        return Integer.parseInt(xtrTop.getValue());
    }

    public int getXtrBot() {
        return Integer.parseInt(xtrBot.getValue());
    }
}
