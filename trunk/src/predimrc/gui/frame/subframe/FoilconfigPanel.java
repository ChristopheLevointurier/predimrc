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
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.gui.widget.MegaCombo;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilConfigPanel extends JPanel {

    static ArrayList<Color> colors = new ArrayList();
    static String[] fileList = new File(".\\src\\resource\\AirFoils").list();

    static {
        colors.add(Color.red);
        colors.add(Color.BLUE);
        colors.add(Color.green);
        colors.add(Color.yellow);
        colors.add(Color.pink);
        colors.add(Color.black);
    }

    public FoilConfigPanel(int i) {
        super();
        MegaCombo airfoil1_combo = new MegaCombo("Foil", true, fileList);
        airfoil1_combo.getLabel().setForeground(colors.get(i - 1));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Foil " + i + " configuration."));
        add(airfoil1_combo);
    }
}
