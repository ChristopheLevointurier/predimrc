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
package predimrc.gui.graphic.drawable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Christophe Levointurier, 26 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawablePoint extends Point2D implements IDrawableObject {

    private boolean selected = false;
    private float x, y;

    public DrawablePoint(float _x, float _y) {
        x = _x;
        y = _y;
    }

    public DrawablePoint(double _x, double _y) {
        x = (float) _x;
        y = (float) _y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public float getFloatX() {
        return x;
    }

    public float getFloatY() {
        return y;
    }

    public int getIntX() {
        return (int) x;
    }

    public int getIntY() {
        return (int) y;
    }

    @Override
    public void setLocation(double _x, double _y) {
        x = (float) _x;
        y = (float) _y;
    }

    public void setFloatLocation(float _x, float _y) {
        x = _x;
        y = _y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE.brighter());
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(getIntX(), getIntY(), 2, 2);
            g.setColor(Color.BLUE.brighter());

        } else {
            g.drawOval(getIntX(), getIntY(), 2, 2);
        }
    }
}
