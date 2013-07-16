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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 17 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilResultPanel extends JPanel {

    private MegaLabel cm0 = new MegaLabel("Cm0");
    private MegaLabel alpha0 = new MegaLabel("Alpha0");
    private String title = "none";

    public XFoilResultPanel(double cm, double alpha, String _title, Color c) {
        super();
        title = _title;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        cm0.setValueBackground(c);
        alpha0.setValueBackground(c);
        add(cm0);
        add(alpha0);
        update("" + cm, "" + alpha, title);
    }

    public final void update(String cm, String alpha, String _title) {
        title = _title;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), title));
        cm0.setValue("" + cm);
        alpha0.setValue("" + alpha);
    }

    public void clear() {
        update("", "", "none");
    }

    public double getCm0Value() {
        if (cm0.getValue().equals("")) {
            return 0;
        }
        return new Double(cm0.getValue());
    }

    public double getAlpha0Value() {
        if (alpha0.getValue().equals("")) {
            return 0;
        }
        return new Double(alpha0.getValue());
    }
}
