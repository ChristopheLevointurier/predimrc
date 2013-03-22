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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.ExternalFrame;
import predimrc.gui.frame.XFoil_Frame;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ReynoldsConfig extends ExternalFrame {

    private static final int[] reyIntValue = {25, 50, 100, 200, 750, 1500, 5000};
    public static final ArrayList<Integer> reyValue = new ArrayList<>();
    private static ArrayList<JCheckBox> reynolds_check = new ArrayList<>();
    private static JPanel reynolds_panel = new JPanel();
    private static boolean init = false;
    private static ActionListener check = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            XFoil_Frame.getInstance().setReynolds(getConfig());
        }
    };

    public static void initReynolds() {
        if (init) {
            return;
        }
        for (int i : reyIntValue) {
            reyValue.add(i);
            JCheckBox temp = new JCheckBox(i + " k", PredimRC.getInstanceDrawableModel().getXfoilConfig().getReynolds().get(reynolds_check.size()));
            temp.addActionListener(check);
            reynolds_check.add(temp);
            reynolds_panel.add(temp);
        }
        init = true;
    }

    public ReynoldsConfig(AbstractButton _caller) {
        super(_caller);
        title = "Reynolds Panels";
        setTitle(title);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        content.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Selected Reynolds"));
        JButton drawPanel = new JButton(PredimRC.getImageIcon("legende.png"));
        reynolds_panel.setLayout(new BoxLayout(reynolds_panel, BoxLayout.Y_AXIS));
        content.add(drawPanel);
        content.add(reynolds_panel);
        this.getContentPane().add(content);
        pack();
    }

    public static ArrayList<Boolean> getConfig() {
        ArrayList<Boolean> ret = new ArrayList<>();
        for (JCheckBox c : reynolds_check) {
            ret.add(c.isSelected());
        }
        return ret;
    }

    @Override
    public void save() {
        //nothing to do
    }
}
