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
package predimrc.common;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.DrawablePanel;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Dimension3D {

    private float x, y, z;

    public Dimension3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Dimension3D(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Dimension3D add(Dimension3D ret) {
        return new Dimension3D(x + ret.x, y + ret.y, z + ret.z);
    }

    public Dimension3D sub(Dimension3D ret) {
        return new Dimension3D(x - ret.x, y - ret.y, z - ret.z);
    }

    public void draw(Graphics2D g, VIEW_TYPE view, String toWrite) {
        
        g.setStroke(new BasicStroke(1));
        g.setColor(Utils.USED_FOR.DEFAULT.getColor());

        int xdrawPos = (int) getY() + DrawablePanel.panY;
        int ydrawPos = (int) getX() + DrawablePanel.panX;

        switch (view) {
            case FRONT_VIEW: {
                ydrawPos = (int) getZ() + DrawablePanel.panZ;
                break;
            }

            case TOP_VIEW: {
                break;
            }
            case LEFT_VIEW:
                xdrawPos = (int) x + DrawablePanel.panX;
                ydrawPos = (int) getZ() + DrawablePanel.panZ;
                break;
        }

        g.drawLine(xdrawPos, ydrawPos, xdrawPos - 25, ydrawPos - 25);
        g.drawLine(xdrawPos - 120, ydrawPos - 25, xdrawPos - 25, ydrawPos - 25);
        g.drawString(toWrite, xdrawPos - 120, ydrawPos - 27);
    }
}
