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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import predimrc.PredimRC;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ReynoldsConfig extends JPanel {

    private JCheckBox reynolds_25k_check = new JCheckBox("25 k", true);
    private JCheckBox reynolds_50k_check = new JCheckBox("50 k", true);
    private JCheckBox reynolds_100k_check = new JCheckBox("100 k", true);
    private JCheckBox reynolds_250k_check = new JCheckBox("250 k", true);
    private JCheckBox reynolds_750k_check = new JCheckBox("750 k", true);
    private JCheckBox reynolds_1500k_check = new JCheckBox("1500 k", true);

    public ReynoldsConfig() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Selected Reynolds"));

        JButton drawPanel = new JButton(PredimRC.getImageIcon("legende.png"));

        JPanel reynolds_panel = new JPanel();
        reynolds_panel.setLayout(new BoxLayout(reynolds_panel, BoxLayout.Y_AXIS));
        reynolds_panel.add(reynolds_25k_check);
        reynolds_panel.add(reynolds_50k_check);
        reynolds_panel.add(reynolds_100k_check);
        reynolds_panel.add(reynolds_250k_check);
        reynolds_panel.add(reynolds_750k_check);
        reynolds_panel.add(reynolds_1500k_check);


        add(drawPanel);
        add(reynolds_panel);
    }

    public JCheckBox getReynolds_25k_check() {
        return reynolds_25k_check;
    }

    public JCheckBox getReynolds_50k_check() {
        return reynolds_50k_check;
    }

    public JCheckBox getReynolds_100k_check() {
        return reynolds_100k_check;
    }

    public JCheckBox getReynolds_250k_check() {
        return reynolds_250k_check;
    }

    public JCheckBox getReynolds_750k_check() {
        return reynolds_750k_check;
    }

    public JCheckBox getReynolds_1500k_check() {
        return reynolds_1500k_check;
    }
}
