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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import predimrc.common.Dimension3D;
import predimrc.common.Utils;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;

/**
 * 2D projection of a vertex of a IDrawableObject.
 *
 * @author Christophe Levointurier, 26 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawablePoint {

    private boolean selectable = true;
    private boolean selected = false;
    private DrawableModelElement belongsTo;
    private float x, y;

    public DrawablePoint(Point2D.Float xy) {
        selectable = false;
        x = (float) xy.getX();
        y = (float) xy.getY();
    }

    public DrawablePoint(int _x, int _y) {
        selectable = false;
        x = _x;
        y = _y;
    }

    public DrawablePoint(float _x, float _y) {
        x = _x;
        y = _y;
        selectable = false;
    }

    public static DrawablePoint makePointForTopView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo) {
        return new DrawablePoint(xy.getY(), xy.getX(), _selectable, _belongsTo);
    }

    public static DrawablePoint makePointForFrontView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo) {
        return new DrawablePoint(xy.getY(), xy.getZ(), _selectable, _belongsTo);
    }

    public static DrawablePoint makePointForLeftView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo) {
        return new DrawablePoint(xy.getX(), xy.getZ(), _selectable, _belongsTo);
    }

    public static DrawablePoint makePointForTopView(Dimension3D xy) {
        return new DrawablePoint(xy.getY(), xy.getX());
    }

    public static DrawablePoint makePointForFrontView(Dimension3D xy) {
        return new DrawablePoint(xy.getY(), xy.getZ());
    }

    public static DrawablePoint makePointForLeftView(Dimension3D xy) {
        return new DrawablePoint(xy.getX(), xy.getZ());
    }

    public DrawablePoint(Dimension3D xy, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = xy.getX();
        y = xy.getY();
    }

    public DrawablePoint(Point2D.Float xy, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = (float) xy.getX();
        y = (float) xy.getY();
    }

    public DrawablePoint(float _x, float _y, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = _x;
        y = _y;
    }

    public DrawablePoint(double _x, double _y, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = (float) _x;
        y = (float) _y;
    }

    public DrawablePoint(float _x, float _y, boolean _selectable, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = _x;
        y = _y;
        selectable = _selectable;
    }

    public DrawablePoint(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = xy.getX();
        y = xy.getY();
        selectable = _selectable;
    }

    public DrawablePoint(Point2D.Float xy, boolean _selectable, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = (float) xy.getX();
        y = (float) xy.getY();
        selectable = _selectable;
    }

    public DrawablePoint(double _x, double _y, boolean _selectable, DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
        x = (float) _x;
        y = (float) _y;
        selectable = _selectable;
    }

    public DrawablePoint getMirrorTop() {
        return new DrawablePoint(Utils.TOP_SCREEN_X - x, y);
    }

    /**
     * Getters
     *
     * @return
     */
    public double getX() {
        return x;
    }

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

    public void setLocation(Point2D _xy) {
        x = (float) _xy.getX();
        y = (float) _xy.getY();
    }

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

    public void setSelected(boolean _selected) {
        selected = _selected;
    }

    public void setSelectable(boolean _selectable) {
        selectable = _selectable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public DrawableModelElement getBelongsTo() {
        return (DrawableModelElement) belongsTo;
    }

    public DrawableModelElement getDrawableBelongsTo() {
        return belongsTo;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(10));
        if (selectable) {
            g.setColor(Color.BLUE.brighter());
        } else {
            g.setColor(Color.GRAY.brighter());
        }
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(getIntX(), getIntY(), 2, 2);
            g.setColor(Color.BLUE.brighter());
        } else {
            g.drawOval(getIntX(), getIntY(), 2, 2);
        }
    }

    @Override
    public String toString() {
        String delim = selectable ? " " : "°";
        delim = selected ? "*" : delim;
        return delim + "(" + x + delim + y + ")" + delim;
    }

    public String toStringAll() {
        return "(" + x + "," + y + ")selectable=" + selectable + " selected=" + selected + " for=" + belongsTo;
    }
}
