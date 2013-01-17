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
import java.util.ArrayList;
import predimrc.common.Dimension3D;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableWingSection extends DrawableModelElement implements AbstractDrawableWing {

    private DrawableModelElement previous;
    /**
     * datas
     */
    //diedre and calageAngulaire are in degree.
    private float diedre, calageAngulaire;
    private float lenght, fleche;

    /**
     * Constructor used for copying a wingsection
     *
     * @param wingConnection
     * @param _belongsTo
     */
    public DrawableWingSection(DrawableWingSection copy, DrawableWing _belongsTo) {
        this(copy.getFilename(), copy.getDiedre(), copy.getFleche(), copy.getWidth(), copy.getLenght(), copy.getAngle(), _belongsTo);
    }

    public DrawableWingSection(Dimension3D wingConnection, DrawableWing _belongsTo) {
        super(wingConnection, _belongsTo);
        switch (((DrawableWing) belongsTo).getUsedFor()) {
            case VERTICAL_PLAN: {
                setValues(Utils.defaultDeriveFoil, 90, -20, 47, 66, 0);
                break;
            }
            case HORIZONTAL_PLAN: {
                setValues(Utils.defaultTailFoil, 1, -10, 35, 80, 0.8f);
                break;
            }
            default:
            case MAIN_WING: {
                setValues(Utils.defaultWingFoil, 2f, 0, 80, 60, -4.0f);
                break;
            }
        }
    }

    /**
     * Constructors
     */
    public DrawableWingSection(String _filename, Dimension3D wingConnection, float _diedre, float _fleche, float _width, float _lenght, float _calageAngulaire, DrawableWing _belongsTo) {
        super(wingConnection, _belongsTo);
        setValues(_filename, _diedre, _fleche, _width, _lenght, _calageAngulaire);
    }

    public DrawableWingSection(String _filename, float _diedre, float _fleche, float _width, float _lenght, float _calageAngulaire, DrawableWing _belongsTo) {
        super(_belongsTo);
        setValues(_filename, _diedre, _fleche, _width, _lenght, _calageAngulaire);
    }

    /**
     * Constructor from real model
     */
    public DrawableWingSection(WingSection _in, DrawableWing _belongsTo) {
        super(_in.getPositionDimension3D(), _belongsTo);
        setValues(_in.getFilename(), _in.getDiedre(), _in.getFleche(), _in.getWidth(), _in.getLenght(), _in.getCalageAngulaire());
    }

    private void setValues(String _filename, float _diedre, float _fleche, float _width, float _lenght, float _calageAngulaire) {
        diedre = _diedre;
        fleche = _fleche;
        width = _width;
        lenght = _lenght;
        calageAngulaire = _calageAngulaire;
        filename = _filename;
    }

    /**
     * Compute methods Calc each point with new params
     */
    @Override
    public void computePositions() {
        int index = ((DrawableWing) belongsTo).getIndexInArray(this);
        if (index > 0) {
            previous = ((DrawableWing) belongsTo).get(index - 1);
            setyPos(previous.getFrontPointFrontView().getFloatX());
            setzPos(previous.getFrontPointFrontView().getFloatY());
        } else {
            previous = belongsTo;
            setPosXYZ(belongsTo.getPositionDimension3D(), true);
        }

        float viewableLengthY = (float) (lenght * (Math.cos(Math.toRadians(diedre))));
        float viewableLengthZ = (float) (lenght * (Math.cos(Math.toRadians(diedre + 90))));
        float yref = belongsTo.getxPos();
        float viewableWidthCoef = (float) (Math.cos(Math.toRadians(calageAngulaire)));

        if (!pointsCalculed) {
            frontPointTopView = new DrawablePoint(previous.getFrontPointTopView().getFloatX() - viewableLengthY, previous.getFrontPointTopView().getFloatY() - fleche, !((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN), this);
            backPointTopView = new DrawablePoint(frontPointTopView.getFloatX(), frontPointTopView.getIntY() + width, !((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN), this);
            frontPointLeftView = new DrawablePoint(previous.getFrontPointLeftView().getFloatX() - fleche, previous.getFrontPointLeftView().getFloatY() + viewableLengthZ, ((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN), this);
            backPointLeftView = new DrawablePoint(Utils.getCoordOnCircle(frontPointLeftView, -calageAngulaire, width), ((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN), this);
            frontPointFrontView = new DrawablePoint(Utils.getCoordOnCircle(DrawablePoint.makePointForFrontView(getPositionDimension3D()), diedre + 180, lenght), ((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.MAIN_WING), this);
            //adjust with angle      
            applyAngle(frontPointTopView, yref, viewableWidthCoef);
            applyAngle(backPointTopView, yref, viewableWidthCoef);
            // applyAngle(frontPointFrontView, yref, viewableWidthCoef);
            // applyAngle(backPointLeftView, yref, viewableWidthCoef); TODO
            // applyAngle(frontPointFrontView, yref, viewableWidthCoef);
            pointsCalculed = true;
        } else {
            frontPointTopView.setLocation(previous.getFrontPointTopView().getFloatX() - viewableLengthY, previous.getFrontPointTopView().getFloatY() - fleche);
            backPointTopView.setFloatLocation(frontPointTopView.getFloatX(), frontPointTopView.getIntY() + width);
            frontPointLeftView.setFloatLocation(previous.getFrontPointLeftView().getFloatX() - fleche, previous.getFrontPointLeftView().getFloatY() + viewableLengthZ);
            backPointLeftView.setLocation(Utils.getCoordOnCircle(frontPointLeftView, -calageAngulaire, width));
            frontPointFrontView.setLocation(Utils.getCoordOnCircle(DrawablePoint.makePointForFrontView(getPositionDimension3D()), diedre + 180, lenght));
            //adjust with angle      
            applyAngle(frontPointTopView, yref, viewableWidthCoef);
            applyAngle(backPointTopView, yref, viewableWidthCoef);
            //  applyAngle(frontPointFrontView, yref, viewableWidthCoef);
            //  applyAngle(backPointLeftView, yref, viewableWidthCoef);  TODO
            //  applyAngle(frontPointFrontView, yref, viewableWidthCoef);
        }
        setxPos(frontPointTopView.getFloatY());
    }

    private void applyAngle(DrawablePoint p, float yref, float viewableWidthCoef) {
        p.setFloatY(yref + (p.getFloatY() - yref) * viewableWidthCoef);
    }

    @Override
    public int getIndexInBelongsTo() {
        return ((DrawableWing) belongsTo).getIndexInArray(this);
    }

    @Override
    public String toString() {
        return "\nDrawableWingSection " + getIndexInBelongsTo() + ": fleche=" + fleche + ", diedre=" + diedre + ", width=" + width + ", lenght=" + lenght;
    }

    @Override
    public String toStringAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * paint method
     */
    @Override
    public void draw(Graphics2D g, VIEW_TYPE view) {
        if (!pointsCalculed) {
            return;
        }

        g.setStroke(new BasicStroke(4));
        g.setColor(belongsTo.getUsedFor().getColor());

        switch (view) {
            case FRONT_VIEW: {
                Utils.drawLine((int) getyPos(), (int) getzPos(), frontPointFrontView, g);
                frontPointFrontView.draw(g);
                break;
            }

            case TOP_VIEW: {
                Utils.drawRect(frontPointTopView, backPointTopView, previous.getFrontPointTopView(), previous.getBackPointTopView(), g, true, view);
                frontPointTopView.draw(g);
                backPointTopView.draw(g);
                break;
            }
            case LEFT_VIEW:
                Utils.drawRect(frontPointLeftView, backPointLeftView, previous.getFrontPointLeftView(), previous.getBackPointLeftView(), g, false, view);
                frontPointLeftView.draw(g);
                backPointLeftView.draw(g);
                break;
        }
    }

    @Override
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        switch (view) {
            case FRONT_VIEW: {
                ret.add(frontPointFrontView);
                break;
            }
            case TOP_VIEW: {
                // if (!((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN)) {
                ret.add(frontPointTopView);
                ret.add(backPointTopView);
                //  }
                break;
            }
            case LEFT_VIEW: {
                //  if (((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.VERTICAL_PLAN)) {
                ret.add(frontPointLeftView);
                ret.add(backPointLeftView);
                //   }
                break;
            }
        }
        return ret;
    }

    public WingSection generateModel() {
        return new WingSection(getFilename(), getPositionDimension3D(), diedre, fleche, width, lenght, calageAngulaire);
    }

    /**
     * *
     * Getters and setters
     */
    public DrawablePoint getPreviousFrontPointTopView() {
        return previous.getFrontPointTopView();
    }

    public DrawablePoint getPreviousBackPointTopView() {
        return previous.getBackPointTopView();
    }

    public DrawablePoint getPreviousFrontPointLeftView() {
        return previous.getFrontPointLeftView();
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
        apply();
    }

    public float getFleche() {
        return fleche;
    }

    public void setFleche(float fleche) {
        this.fleche = fleche;
        apply();
    }

    public float getDiedre() {
        return diedre;
    }

    public float getLenght() {
        return lenght;
    }

    @Override
    public void setDiedre(float _diedre) {
        if (!((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.MAIN_WING)) {
            ((DrawableWing) belongsTo).setDiedre(_diedre);
            return;
        }
        diedre = _diedre;
        apply();
    }

    public void setDiedre(float _diedre, boolean silent) {
        if (silent) {
            diedre = _diedre;
        } else {
            setDiedre(diedre);
        }
    }

    public void setAngle(float _angle, boolean silent) {
        if (silent) {
            calageAngulaire = _angle;
        } else {
            setAngle(_angle);
        }
    }

    @Override
    public DrawableWing getBelongsTo() {
        return (DrawableWing) belongsTo;
    }

    @Override
    public Utils.USED_FOR getUsedFor() {
        return getBelongsTo().getUsedFor();
    }

    @Override
    public String toInfoString() {
        return belongsTo.toInfoString() + " section:" + (getIndexInBelongsTo() + 1);
    }

    @Override
    public void setAngle(float angle) {
        calageAngulaire = angle;
        apply();
    }

    @Override
    public float getAngle() {
        return calageAngulaire;
    }
}
