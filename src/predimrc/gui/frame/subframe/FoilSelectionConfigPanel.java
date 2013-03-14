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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.widget.MegaCombo;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilSelectionConfigPanel extends JPanel {

    static ArrayList<Color> colors = new ArrayList();
    static String[] fileList = new File(".\\src\\resource\\AirFoils").list();
    private MegaCombo airfoil1_combo;
    private XFoil_Frame from;
    private JRadioButton crit3 = new JRadioButton("3", true);
    private JRadioButton crit6 = new JRadioButton("6", false);
    private JRadioButton crit9 = new JRadioButton("9", false);
    private JRadioButton crit12 = new JRadioButton("12", false);

    public FoilSelectionConfigPanel(int i, XFoil_Frame _from) {
        super();
        from = _from;
        airfoil1_combo = new MegaCombo("Foil", true, fileList);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Foil " + i + " configuration."));
        add(airfoil1_combo);

        airfoil1_combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                from.updateData();
            }
        });

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
        add(new MegaLabel("XTRtop"));
        add(new MegaLabel("XTRbottom"));
    }

    public String getSelectedFoil() {
        return airfoil1_combo.getValue();
    }
}
