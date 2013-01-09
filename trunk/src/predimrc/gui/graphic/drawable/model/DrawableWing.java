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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import predimrc.common.Utils;
import predimrc.common.Utils.USED_FOR;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.element.Wing;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableWing extends DrawableModelElement implements Iterable<DrawableWingSection>, AbstractDrawableWing {

    /**
     * Top view Points
     */
    private DrawablePoint frontPoint;
    private DrawablePoint backPoint;
    private DrawablePoint connectionPoint;
    private LinkedList<DrawableWingSection> drawableWingSection;
    private USED_FOR used_for;
    private float widthAtConnection;

    public DrawableWing(USED_FOR _used_for, DrawableModel m) {
        super(m);
        used_for = _used_for;
        widthAtConnection = Utils.DEFAULT_MAIN_WING_WIDTH_VALUE;
        drawableWingSection = new LinkedList<>();
        switch (_used_for) {
            case VERTICAL_PLAN: {
                setPosXYZ(Utils.defaultDeriveConnection, true);
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                break;
            }
            case HORIZONTAL_PLAN: {
                setPosXYZ(Utils.defaultTailConnection, true);
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), 5, -10, 45, 45, this));
                break;
            }
            default:
            case MAIN_WING: {
                setPosXYZ(Utils.defaultWingConnection, true);
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), 6, 8, 70, 100, this));
                drawableWingSection.add(new DrawableWingSection(3, -6, 60, 140, this));
                drawableWingSection.add(new DrawableWingSection(-5, -4, 30, 80, this));
                break;
            }
        }
        frontPoint = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this);
        backPoint = new DrawablePoint(frontPoint.getFloatX(), frontPoint.getFloatY() + getWidthAtConnection(), true, this);
    }

    public DrawableWing(Wing w, DrawableModel m) {
        super(w.getPositionDimension3D(), m);
        used_for = w.getUsed_for();
        widthAtConnection = w.getWidth();
        drawableWingSection = new LinkedList<>();

        for (WingSection ws : w.getWingsSection()) {
            drawableWingSection.add(new DrawableWingSection(ws.getPositionDimension3D(), ws.getDiedre(), ws.getFleche(), ws.getWidth(), ws.getLenght(), this));
        }
        frontPoint = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this);
        backPoint = new DrawablePoint(frontPoint.getFloatX(), frontPoint.getFloatY() + getWidthAtConnection(), true, this);
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        connectionPoint = new DrawablePoint(getyPos(), getzPos(), false, belongsTo);
        for (DrawableWingSection ds : drawableWingSection) {
            ds.computePositions();
        }
    }

    public Point2D.Float getPreviousPointForDiedre(int index) {
        switch (used_for) {
            case VERTICAL_PLAN: { // TODO, return derive connection
                return new Point2D.Float(yPos, zPos);
            }
            case HORIZONTAL_PLAN: {
                return new Point2D.Float(yPos, zPos);
            }
            default:
            case MAIN_WING: {

                if (index == 0) {
                    return new Point2D.Float(yPos, zPos);
                } else {
                    return new Point2D.Float(get(index).getyPos(), get(index).getzPos());
                }
            }
        }
    }

    @Override
    public int getIndexInBelongsTo() {
        switch (used_for) {
            case VERTICAL_PLAN: {
                return ((DrawableModel) belongsTo).getDerive().indexOf(this);
            }
            case HORIZONTAL_PLAN: {
                return ((DrawableModel) belongsTo).getTail().indexOf(this);
            }
            default:
            case MAIN_WING: {
                return ((DrawableModel) belongsTo).getWings().indexOf(this);
            }
        }
    }

    public final void add(DrawableWingSection newSection) {
        drawableWingSection.add(newSection);
        apply();
    }

    public void setDrawableWingSectionNumber(int _i) {
        predimrc.PredimRC.logDebugln("setDrawableWingSectionNumber:" + _i);
        LinkedList<DrawableWingSection> wingsTemp = new LinkedList<>();
        for (int i = 0; i < _i; i++) {
            if (!drawableWingSection.isEmpty()) {
                wingsTemp.add(drawableWingSection.remove(0));
            } else {
                // if (wingsTemp.isEmpty()) {                    wingsTemp.add(new DrawableWingSection(0, 0, 10, 10, 10, this));                } else {
                wingsTemp.add(new DrawableWingSection(wingsTemp.get(wingsTemp.size()), this));
                //  }
            }
        }
        drawableWingSection = wingsTemp;
        apply();
    }

    @Override
    public DrawablePoint getFrontPoint() {
        return frontPoint;
    }

    @Override
    public DrawablePoint getBackPoint() {
        return backPoint;
    }

    @Override
    public DrawablePoint getDiedrePoint() {
        return drawableWingSection.getLast().getDiedrePoint();
    }

    /**
     * Getters and setters
     *
     */
    public final float getWidthAtConnection() {
        return widthAtConnection;
    }

    public void setWidthAtConnection(float widthAtConnection) {
        this.widthAtConnection = widthAtConnection;
    }

    public USED_FOR getUsed_for() {
        return used_for;
    }

    public int getSize() {
        return drawableWingSection.size();
    }

    @Override
    public void setDiedre(float diedre) {
        for (DrawableWingSection w : (ArrayList<DrawableWingSection>) drawableWingSection.clone()) {
            w.setDiedre(diedre);
        }
        apply();
    }

    public DrawableWingSection get(int index) {
        return drawableWingSection.get(index);
    }

    public int getIndexInArray(DrawableWingSection ws) {
        return drawableWingSection.indexOf(ws);
    }

    @Override
    public Iterator iterator() {
        return drawableWingSection.iterator();
    }

    /**
     * Paint methods
     *
     * @param g
     */
    @Override
    public void drawTop(Graphics2D g) {
        System.out.println("drawTop " + this);

        /**
         *
         *
         *
         * g.setStroke(new BasicStroke(6)); g.setColor(Color.GRAY.brighter());
         * g.drawLine(previousFrontPoint.getIntX(),
         * previousFrontPoint.getIntY(), frontPoint.getIntX(),
         * frontPoint.getIntY()); g.drawLine(frontPoint.getIntX(),
         * frontPoint.getIntY(), backPoint.getIntX(), backPoint.getIntY());
         * g.drawLine(previousBackPoint.getIntX(), previousBackPoint.getIntY(),
         * backPoint.getIntX(), backPoint.getIntY()); //miror
         * g.drawLine(DrawablePanel.MID_TOP_SCREEN_X * 2 -
         * previousFrontPoint.getIntX(), previousFrontPoint.getIntY(),
         * DrawablePanel.MID_TOP_SCREEN_X * 2 - frontPoint.getIntX(),
         * frontPoint.getIntY()); g.drawLine(DrawablePanel.MID_TOP_SCREEN_X * 2
         * - frontPoint.getIntX(), frontPoint.getIntY(),
         * DrawablePanel.MID_TOP_SCREEN_X * 2 - backPoint.getIntX(),
         * backPoint.getIntY()); g.drawLine(DrawablePanel.MID_TOP_SCREEN_X * 2 -
         * previousBackPoint.getIntX(), previousBackPoint.getIntY(),
         * DrawablePanel.MID_TOP_SCREEN_X * 2 - backPoint.getIntX(),
         * backPoint.getIntY()); frontPoint.draw(g); backPoint.draw(g);
         * previousFrontPoint.draw(g); previousBackPoint.draw(g);
         */
    }

    @Override
    public void drawLeft(Graphics2D g) {
        System.out.println("drawLeft " + this);
    }

    @Override
    public void drawFront(Graphics2D g) {
        //  System.out.println("drawFront IN WINGGGGGGGGGGGGGGGGGGGGG:"+PredimRC.initDone);
        //  connectionPoint.draw(g); TODO check this
        for (DrawableWingSection d : this) {
            d.drawFront(g);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("\nDrawableWing " + getIndexInBelongsTo() + " used_for:" + used_for.name() + ", " + getPositionDimension3D() + " size:" + drawableWingSection.size());
        return ret.toString();
    }

    @Override
    public String toStringAll() {
        StringBuilder ret = new StringBuilder("\nDrawableWing " + getIndexInBelongsTo() + " used_for:" + used_for.name() + ", " + getPositionDimension3D());
        for (DrawableWingSection w : drawableWingSection) {
            ret.append(w);
        }
        return ret.toString();
    }

    @Override
    public ArrayList<DrawablePoint> getFrontPoints() {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (DrawableWingSection ws : this) {
            ret.addAll(ws.getFrontPoints());
        }
        return ret;
    }

    @Override
    public ArrayList<DrawablePoint> getBackPoints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<DrawablePoint> getTopPoints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Wing generateModel() {
        LinkedList<WingSection> wsl = new LinkedList<>();
        for (DrawableWingSection ws : this) {
            wsl.add(ws.generateModel());
        }
        return new Wing(used_for, getPositionDimension3D(), widthAtConnection, wsl);
    }
}
