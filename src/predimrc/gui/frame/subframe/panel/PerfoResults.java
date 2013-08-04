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
package predimrc.gui.frame.subframe.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 7 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PerfoResults extends JPanel {

    private List<PerfoResultPanel> panels = new ArrayList<>();

    public PerfoResults() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Results:"));


        for (int i = 0; i < 3; i++) {
            PerfoResultPanel temp = new PerfoResultPanel(XFoil_Frame.getAppropriateColor(i, 6));
            panels.add(temp);
            add(temp);
        }
    }

    private double getInterpolated0(List<DrawablePoint> plist) {
        return -1;
    }

    public void clear() {
        for (PerfoResultPanel p : panels) {
            p.clear();
        }
    }
}
