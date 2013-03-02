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
import predimrc.common.UserConfig;
import predimrc.common.Utils;
import predimrc.common.Utils.USED_FOR;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.graphic.drawable.tool.DrawableNeutralPoint;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
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

    public static DrawableWing MakeEmptyWing(USED_FOR _used_for) {
        return new DrawableWing(_used_for);
    }
    private LinkedList<DrawableWingSection> drawableWingSection = new LinkedList<>();
    private float calageAngulaire = 0f;
    private float czCalage = 0f;
    /**
     * aerodynamics caracs
     */
    private double meanChord = 0;  //corde moyenne
    private double area = 0;  //surface
    private double span = 0; //envergure
    private double aspectRatio = 0;  //allongement

    private DrawableWing(USED_FOR _used_for) {
        used_for = _used_for;
        neutralPoint = new DrawableNeutralPoint(this);
        filename = Utils.FAKE_FILENAME;
        fake = true;
        drawableWingSection.add(new DrawableWingSection(0, 0, 0, 0, 0, this));
    }

    /**
     *
     * @param _used_for
     * @param m
     */
    public DrawableWing(USED_FOR _used_for, DrawableModel m) {
        super(m);
        used_for = _used_for;
        drawableWingSection = new LinkedList<>();
        switch (used_for) {
            case VERTICAL_PLAN: {
                setPosXYZ(Utils.defaultDeriveConnection, true);
                width = Utils.DEFAULT_DERIVE_WING_WIDTH_VALUE;
                calageAngulaire = 0;
                czCalage = 0;
                width = 55;
                filename = Utils.defaultDeriveFoil;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                break;
            }
            case HORIZONTAL_PLAN: {
                setPosXYZ(Utils.defaultTailConnection, true);
                width = Utils.DEFAULT_TAIL_WING_WIDTH_VALUE;
                calageAngulaire = -0.8f;
                czCalage = 0;
                filename = Utils.defaultTailFoil;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                break;
            }
            default:
            case MAIN_WING: {
                setPosXYZ(Utils.defaultWingConnection, true);
                width = Utils.DEFAULT_MAIN_WING_WIDTH_VALUE;
                calageAngulaire = 4;
                czCalage = 0;
                filename = Utils.defaultWingFoil;
                drawableWingSection.add(new DrawableWingSection(getPositionDimension3D(), this));
                drawableWingSection.add(new DrawableWingSection(4, -7.8f, 40, 200, -3.5f, this));
                drawableWingSection.add(new DrawableWingSection(6, 6f, 30, 80, -3f, this));
                break;
            }
        }
    }

    public DrawableWing(Wing w, DrawableModel m) {
        super(w.getPositionDimension3D(), m);
        used_for = w.getUsed_for();
        width = w.getWidth();
        drawableWingSection = new LinkedList<>();
        calageAngulaire = w.getCalageAngulaire();
        czCalage = w.getCzAdjustment();
        filename = w.getFilename();
        for (WingSection ws : w.getWingsSection()) {
            drawableWingSection.add(new DrawableWingSection(ws.getPositionDimension3D(), ws.getDihedral(), ws.getFleche(), ws.getWidth(), ws.getLenght(), ws.getCalageAngulaire(), this));
        }
        neutralPoint = new DrawableNeutralPoint(this);
        if (filename.equals(Utils.FAKE_FILENAME)) {
            fake = true;
        }
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        if (fake) {
            return;
        }
        /**
         * **
         * Points *
         */
        if (!pointsCalculed) {
            frontPointFrontView = DrawablePoint.makePointForFrontView(getPositionDimension3D(), false, this, VIEW_TYPE.FRONT_VIEW);
            frontPointTopView = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this, VIEW_TYPE.TOP_VIEW);

            backPointTopView = new DrawablePoint(frontPointTopView.getFloatX(), frontPointTopView.getFloatY() + getWidth() * Math.cos(Math.toRadians(calageAngulaire)), true, this, VIEW_TYPE.TOP_VIEW);
            frontPointLeftView = DrawablePoint.makePointForLeftView(getPositionDimension3D(), true, this, VIEW_TYPE.LEFT_VIEW);

            backPointLeftView = new DrawablePoint(Utils.getCoordOnCircle(DrawablePoint.makePointForLeftView(getPositionDimension3D()), calageAngulaire, width), true, this, VIEW_TYPE.LEFT_VIEW);
            neutralPoint = new DrawableNeutralPoint(this);  //foyer
            pointsCalculed = true;
        } else {
            frontPointFrontView.setFloatLocation(getyPos(), getzPos());
            frontPointTopView.setFloatLocation(getyPos(), getxPos());
            backPointTopView.setFloatLocation(frontPointTopView.getFloatX(), frontPointTopView.getFloatY() + (float) (getWidth() * Math.cos(Math.toRadians(calageAngulaire))));
            frontPointLeftView.setFloatLocation(getxPos(), getzPos());
            backPointLeftView.setLocation(Utils.getCoordOnCircle(DrawablePoint.makePointForLeftView(getPositionDimension3D()), calageAngulaire, width));
        }

        for (DrawableWingSection ds : drawableWingSection) {
            ds.computePositions();
            if (!used_for.equals(USED_FOR.MAIN_WING)) {
                ds.getFrontPointFrontView().setSelectable(ds.equals(drawableWingSection.getLast()));
            }
        }


        /**
         * specs
         */
        double areaTemp = 0;
        double meanChordTempCalc = 0;
        double meanChordTempI;
        float previousCord = getWidth();
        float previousSweep = 0;
        double areaI;
        double xFI;
        double xFTempCalc = 0;

        span = 0;
        for (DrawableWingSection ws : drawableWingSection) {
            areaI = (previousCord + ws.getWidth()) * ws.getLenght() / 2;
            areaTemp += areaI;
            meanChordTempI = ((double) 2 / (double) 3) * ((double) (previousCord * previousCord + previousCord * ws.getWidth() + ws.getWidth() * ws.getWidth())) / ((double) (previousCord + ws.getWidth()));
            xFI = ((double) (previousSweep + (ws.getSweep() / 3) * (previousCord + 2 * ws.getWidth()) / (previousCord + ws.getWidth())) + 0.25f * meanChordTempI);
            meanChordTempCalc += areaI * meanChordTempI;
            xFTempCalc += areaI * xFI;
            previousCord = ws.getWidth();
            previousSweep += ws.getSweep();
            span += 2 * ws.getLenght(); //envergure
        }

        area = 2 * areaTemp;  //surface
        meanChord = 2 * meanChordTempCalc / area;  //corde moyenne
        aspectRatio = span * span / area;  //allongement
        XF = 2 * xFTempCalc / area + getxPos();
        neutralPoint.setLocation(Utils.TOP_SCREEN_X / 2, XF); //foyer
    }

    public Point2D.Float getPreviousPointForDiedre(int index) {
        switch (used_for) {
            case VERTICAL_PLAN:
            case HORIZONTAL_PLAN: {
                return new Point2D.Float(getyPos(), getzPos());
            }
            default:
            case MAIN_WING: {

                if (index == 0) {
                    return new Point2D.Float(getyPos(), getzPos());
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

    /**
     * Getters and setters
     *
     */
    public int getSize() {
        return drawableWingSection.size();
    }

    @Override
    public void setDihedral(float _diedre) {
        for (DrawableWingSection w : (LinkedList<DrawableWingSection>) drawableWingSection.clone()) {
            w.setDiedre(Utils.round(_diedre), true);
        }
        apply();
    }

    @Override
    public void setFilename(String _file) {
        for (DrawableWingSection w : (LinkedList<DrawableWingSection>) drawableWingSection.clone()) {
            w.setFilename(_file);
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

        if (!pointsCalculed || fake) {
            return;
        }

        for (DrawablePoint p : getPoints(view)) {
            p.draw(g);
        }
        if (view.equals(VIEW_TYPE.LEFT_VIEW)) {
            g.setColor(used_for.getColor());
            Utils.drawline(frontPointLeftView, backPointLeftView, g);
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
        if (view.equals(VIEW_TYPE.TOP_VIEW)) {
            ret.add(neutralPoint);
        }
        if (fake
                || (used_for.equals(USED_FOR.MAIN_WING) && !UserConfig.manipWing)
                || (used_for.equals(USED_FOR.HORIZONTAL_PLAN) && !UserConfig.manipStab)
                || (used_for.equals(USED_FOR.VERTICAL_PLAN) && !UserConfig.manipFin)) {
            return ret;
        }

        for (DrawableWingSection ws : this) {
            ret.addAll(ws.getPoints(view));
        }

        switch (view) {
            case FRONT_VIEW: {
                ret.add(frontPointFrontView);
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
        return new Wing(filename, used_for, getPositionDimension3D(), width, calageAngulaire, czCalage, wsl);
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
        calageAngulaire = Utils.round(angle);
        apply();
    }

    @Override
    public float getAngle() {
        return calageAngulaire;
    }

    public float getCzCalage() {
        return czCalage;
    }

    public void setCzCalage(float czCalage) {
        this.czCalage = czCalage;
    }

    @Override
    public DrawablePoint getFrontPointFrontView() {
        return drawableWingSection.getLast().getFrontPointFrontView();
    }

    public double getMeanCord() {
        return meanChord;
    }

    public double getArea() {
        return area;
    }

    public double getSpan() {
        return span;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public float getSweep() {
        return 0;
    }
}
