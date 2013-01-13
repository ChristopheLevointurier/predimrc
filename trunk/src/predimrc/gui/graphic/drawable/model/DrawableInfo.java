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
package predimrc.gui.graphic.drawable.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Christophe Levointurier, 13 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableInfo {

    private String info = "Select an object";
    private String detailedInfo = " to modify its value.";
    boolean dirty = true;

    public DrawableInfo() {
    }

    public String getDetailedInfo() {
        return detailedInfo;
    }

    public void setInfo(String _info) {
        if (!info.equals(_info)) {
            dirty = true;
        }
        info = _info;
    }

    public void setDetailedInfo(String string) {
        if (!detailedInfo.equals(string)) {
            dirty = true;
        }
        detailedInfo = string;
    }

    public void draw(Graphics g) {
        if (dirty) {
            g.setColor(new Color(225, 225, 255));
            g.fillRect(1, 1, 350, 35);
            g.setColor(Color.blue);
            g.drawString(info + detailedInfo, 10, 20);
            dirty = false;
        }
    }

    public void undraw(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(1, 1, 350, 35);
        dirty = true;

    }
}
