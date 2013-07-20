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

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.frame.subframe.ReynoldsConfig;

/**
 *
 * @author Christophe Levointurier, 7 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Legende extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        int ind = 1;
        for (int r = 0; r < ReynoldsConfig.reyIntValue.length; r++) {
            for (int i = 0; i < XFoil_Frame.listColor.length; i++) {
                g.setColor(XFoil_Frame.getAppropriateColor(i, r));
                g.drawLine(0, ind * 5, 50, ind * 5);
                ind++;
            }
            ind++;
            ind++;
        }
    }
}
