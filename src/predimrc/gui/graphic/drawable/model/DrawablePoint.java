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
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.DrawablePanel;
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

    protected boolean selectable = true;
    protected boolean selected = false;
    protected DrawableModelElement belongsTo;
    protected float x, y;
    protected VIEW_TYPE view;

    public DrawablePoint() {
        selectable = false;
        x = 0;
        y = 0;
        view = VIEW_TYPE.TOP_VIEW;
    }

    public DrawablePoint(Point2D.Float xy, VIEW_TYPE _view) {
        selectable = false;
        x = (float) xy.getX();
        y = (float) xy.getY();
        view = _view;
    }

    public DrawablePoint(int _x, int _y, VIEW_TYPE _view) {
        selectable = false;
        x = _x;
        y = _y;
        view = _view;
    }

    public DrawablePoint(float _x, float _y, VIEW_TYPE _view) {
        x = _x;
        y = _y;
        selectable = false;
        view = _view;
    }

    public static DrawablePoint makePointForTopView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        return new DrawablePoint(xy.getY(), xy.getX(), _selectable, _belongsTo, _view);
    }

    public static DrawablePoint makePointForFrontView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        return new DrawablePoint(xy.getY(), xy.getZ(), _selectable, _belongsTo, _view);
    }

    public static DrawablePoint makePointForLeftView(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        return new DrawablePoint(xy.getX(), xy.getZ(), _selectable, _belongsTo, _view);
    }

    public static DrawablePoint makePointForTopView(Dimension3D xy) {
        return new DrawablePoint(xy.getY(), xy.getX(), VIEW_TYPE.TOP_VIEW);
    }

    public static DrawablePoint makePointForFrontView(Dimension3D xy) {
        return new DrawablePoint(xy.getY(), xy.getZ(), VIEW_TYPE.FRONT_VIEW);
    }

    public static DrawablePoint makePointForLeftView(Dimension3D xy) {
        return new DrawablePoint(xy.getX(), xy.getZ(), VIEW_TYPE.LEFT_VIEW);
    }

    public DrawablePoint(Dimension3D xy, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = xy.getX();
        y = xy.getY();
        view = _view;
    }

    public DrawablePoint(Point2D.Float xy, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = (float) xy.getX();
        y = (float) xy.getY();
        view = _view;
    }

    public DrawablePoint(float _x, float _y, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = _x;
        y = _y;
        view = _view;
    }

    public DrawablePoint(double _x, double _y, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = (float) _x;
        y = (float) _y;
        view = _view;
    }

    public DrawablePoint(float _x, float _y, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = _x;
        y = _y;
        selectable = _selectable;
        view = _view;
    }

    public DrawablePoint(Dimension3D xy, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = xy.getX();
        y = xy.getY();
        selectable = _selectable;
        view = _view;
    }

    public DrawablePoint(Point2D.Float xy, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = (float) xy.getX();
        y = (float) xy.getY();
        selectable = _selectable;
        view = _view;
    }

    public DrawablePoint(double _x, double _y, boolean _selectable, DrawableModelElement _belongsTo, VIEW_TYPE _view) {
        belongsTo = _belongsTo;
        x = (float) _x;
        y = (float) _y;
        selectable = _selectable;
        view = _view;
    }

    public DrawablePoint getMirror() {
        switch (view) {
            case FRONT_VIEW:
                return new DrawablePoint(2 * Utils.FRONT_SCREEN_X - x, y, view);
            case TOP_VIEW:
                return new DrawablePoint(Utils.TOP_SCREEN_X - x, y, view);
            default:
            case LEFT_VIEW:
                return new DrawablePoint(x, y, false, belongsTo, view);
        }
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

    public void setFloatX(float _x) {
        x = _x;
    }

    public void setFloatY(float _y) {
        y = _y;
    }

    public int getDrawCoordX() {

        switch (view) {
            case FRONT_VIEW:
                return (int) x + DrawablePanel.panY;
            case LEFT_VIEW:
                return (int) x + DrawablePanel.panX;
            case TOP_VIEW:
                return (int) x + DrawablePanel.panY;
        }

        return (int) x;
    }

    public int getDrawCoordY() {

        switch (view) {
            case FRONT_VIEW:
                return (int) y + DrawablePanel.panZ;
            case LEFT_VIEW:
                return (int) y + DrawablePanel.panZ;
            case TOP_VIEW:
                return (int) y + DrawablePanel.panX;
        }

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
        if (selectable) {
            g.setColor(Color.BLUE.brighter());
        } else {
            if (null != belongsTo) {
                g.setColor(belongsTo.getUsedFor().getColor());
            } else {
                g.setColor(Utils.USED_FOR.DEFAULT.getColor());
            }
        }
        if (isSelected()) {
            //  g.setColor(Color.RED);
            g.setColor(Color.BLUE.brighter());
            g.setStroke(new BasicStroke(10));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setStroke(new BasicStroke(8));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setStroke(new BasicStroke(6));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setStroke(new BasicStroke(4));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
        } else {
            g.setStroke(new BasicStroke(10));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(8));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setStroke(new BasicStroke(6));
            g.drawOval(getDrawCoordX() - 1, getDrawCoordY(), 2, 2);
            g.setStroke(new BasicStroke(4));
        }
    }

    @Override
    public String toString() {
        String delim = selectable ? " " : "°";
        delim = selected ? "*" : delim;
        return delim + "(" + x + delim + y + ")" + delim;
    }

    public String toStringAll() {
        return "(" + x + "," + y + ")selectable=" + selectable + " selected=" + selected + " for=" + belongsTo + " for view:" + view.name();
    }

    public String toInfoString() {
        return "";
    }
}
