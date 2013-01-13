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
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import predimrc.common.Utils;
import predimrc.common.Utils.USED_FOR;
import predimrc.common.Utils.VIEW_TYPE;
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

    private DrawablePoint connectionPointFrontView;
    private LinkedList<DrawableWingSection> drawableWingSection;
    private USED_FOR used_for;
    private float widthAtConnection;
    private float calageAngulaire;
    /**
     * Top view Points
     */
    protected DrawablePoint backPointTopView;
    protected DrawablePoint frontPointTopView;
    /**
     * Left view point
     */
    protected DrawablePoint backPointLeftView;
    protected DrawablePoint frontPointLeftView;

    public DrawableWing(USED_FOR _used_for, DrawableModel m) {
        super(m);
        used_for = _used_for;
        drawableWingSection = new LinkedList<>();
        switch (_used_for) {
            case VERTICAL_PLAN: {
                setPosXYZ(Utils.defaultDeriveConnection, true);
                widthAtConnection = Utils.DEFAULT_DERIVE_WING_WIDTH_VALUE;
                calageAngulaire = 0;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                break;
            }
            case HORIZONTAL_PLAN: {
                setPosXYZ(Utils.defaultTailConnection, true);
                widthAtConnection = Utils.DEFAULT_TAIL_WING_WIDTH_VALUE;
                calageAngulaire = -12;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                break;
            }
            default:
            case MAIN_WING: {
                setPosXYZ(Utils.defaultWingConnection, true);
                widthAtConnection = Utils.DEFAULT_MAIN_WING_WIDTH_VALUE;
                calageAngulaire = -8;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                drawableWingSection.add(new DrawableWingSection(Utils.defaultWingFoil, 3, -6, 40, 200, -11, this));
                drawableWingSection.add(new DrawableWingSection(Utils.defaultWingFoil, -5, -4, 30, 80, -12, this));
                break;
            }
        }
    }

    public DrawableWing(Wing w, DrawableModel m) {
        super(w.getPositionDimension3D(), m);
        used_for = w.getUsed_for();
        widthAtConnection = w.getWidth();
        drawableWingSection = new LinkedList<>();
        calageAngulaire = w.getCalageAngulaire();
        filename = w.getFilename();
        for (WingSection ws : w.getWingsSection()) {
            drawableWingSection.add(new DrawableWingSection(ws.getFilename(), ws.getPositionDimension3D(), ws.getDiedre(), ws.getFleche(), ws.getWidth(), ws.getLenght(), ws.getCalageAngulaire(), this));
        }
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        if (!pointsCalculed) {
            connectionPointFrontView = DrawablePoint.makePointForFrontView(getPositionDimension3D(), false, this);
            frontPointTopView = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this);

            backPointTopView = new DrawablePoint(frontPointTopView.getFloatX(), frontPointTopView.getFloatY() + getWidth() * Math.cos(Math.toRadians(calageAngulaire)), true, this);
            frontPointLeftView = DrawablePoint.makePointForLeftView(getPositionDimension3D(), true, this);

            backPointLeftView = new DrawablePoint(Utils.getCoordOnCircle(DrawablePoint.makePointForLeftView(getPositionDimension3D()), -calageAngulaire, widthAtConnection), true, this);
            pointsCalculed = true;
        } else {
            connectionPointFrontView.setFloatLocation(yPos, zPos);
            frontPointTopView.setFloatLocation(yPos, xPos);
            backPointTopView.setFloatLocation(frontPointTopView.getFloatX(), frontPointTopView.getFloatY() + (float) (getWidth() * Math.cos(Math.toRadians(calageAngulaire))));
            frontPointLeftView.setFloatLocation(xPos, zPos);
            backPointLeftView.setLocation(Utils.getCoordOnCircle(DrawablePoint.makePointForLeftView(getPositionDimension3D()), -calageAngulaire, widthAtConnection));
        }

        for (DrawableWingSection ds : drawableWingSection) {
            ds.computePositions();
            if (!used_for.equals(USED_FOR.MAIN_WING)) {
                ds.getFrontPointFrontView().setSelectable(ds.equals(drawableWingSection.getLast()));
            }
        }

    }

    public Point2D.Float getPreviousPointForDiedre(int index) {
        switch (used_for) {
            case VERTICAL_PLAN:
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
                wingsTemp.add(new DrawableWingSection(wingsTemp.get(wingsTemp.size() - 1), this));
                //  }
            }
        }
        drawableWingSection = wingsTemp;
        apply();
    }

    @Override
    public DrawablePoint getFrontPointTopView() {
        return frontPointTopView;
    }

    @Override
    public DrawablePoint getBackPointTopView() {
        return backPointTopView;
    }

    @Override
    public DrawablePoint getBackPointLeftView() {
        return backPointLeftView;
    }

    @Override
    public DrawablePoint getFrontPointLeftView() {
        return frontPointLeftView;
    }

    @Override
    public DrawablePoint getFrontPointFrontView() {
        return drawableWingSection.getLast().getFrontPointFrontView();
    }

    /**
     * Getters and setters
     *
     */
    @Override
    public float getWidth() {
        return widthAtConnection;
    }

    @Override
    public void setWidth(float widthAtConnection) {
        this.widthAtConnection = widthAtConnection;
        apply();
    }

    @Override
    public USED_FOR getUsedFor() {
        return used_for;
    }

    public int getSize() {
        return drawableWingSection.size();
    }

    @Override
    public void setDiedre(float _diedre) {
        for (DrawableWingSection w : (LinkedList<DrawableWingSection>) drawableWingSection.clone()) {
            w.setDiedre(_diedre, true);
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
     * Paint method
     *
     * @param g
     */
    @Override
    public void draw(Graphics2D g, VIEW_TYPE view) {
        for (DrawableWingSection d : this) {
            d.draw(g, view);
        }

        if (!pointsCalculed) {
            return;
        }
        switch (view) {
            case FRONT_VIEW: {
                connectionPointFrontView.draw(g);
                break;
            }

            case TOP_VIEW: {
                frontPointTopView.draw(g);
                backPointTopView.draw(g);
                break;
            }
            case LEFT_VIEW: {
                g.setColor(used_for.getColor());
                Utils.drawline(frontPointLeftView, backPointLeftView, g);
                frontPointLeftView.draw(g);
                backPointLeftView.draw(g);
                break;
            }
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
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (DrawableWingSection ws : this) {
            ret.addAll(ws.getPoints(view));
        }

        switch (view) {
            case FRONT_VIEW: {
                ret.add(connectionPointFrontView);
                break;
            }
            case TOP_VIEW: {
                ret.add(frontPointTopView);
                ret.add(backPointTopView);
                break;
            }
            case LEFT_VIEW: {
                ret.add(frontPointLeftView);
                ret.add(backPointLeftView);
                break;
            }
        }
        return ret;
    }

    public Wing generateModel() {
        LinkedList<WingSection> wsl = new LinkedList<>();
        for (DrawableWingSection ws : this) {
            wsl.add(ws.generateModel());
        }
        return new Wing("fad05.dat", used_for, getPositionDimension3D(), widthAtConnection, calageAngulaire, wsl);
    }

    @Override
    public DrawableModel getBelongsTo() {
        return (DrawableModel) belongsTo;
    }

    @Override
    public String toInfoString() {
        String ret = getUsedFor().getDesc();
        ret += (getIndexInBelongsTo() + 1);
        return ret;
    }

    @Override
    public void setAngle(float angle) {
        calageAngulaire = angle;
           for (DrawableWingSection w : (LinkedList<DrawableWingSection>) drawableWingSection.clone()) {
            w.setAngle(angle, true);
        }
        apply();
        
        apply();
    }

    @Override
    public float getAngle() {
        return calageAngulaire;
    }
    
    
}