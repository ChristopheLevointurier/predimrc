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

    private AbstractDrawableWing previous;
    /**
     * Top view Points
     */
    protected DrawablePoint backPointTopView;
    protected DrawablePoint frontPointTopView;
    /**
     * Front view Points
     */
    private DrawablePoint frontPointFrontView = new DrawablePoint(0, 0);
    /**
     * datas
     */
    private float viewableLength, viewableWidth;
    //diedre and calageAngulaire are in degree.
    private float diedre, calageAngulaire;
    private float width, lenght, fleche;

    /**
     * Constructor used for copying a wingsection
     *
     * @param wingConnection
     * @param _belongsTo
     */
    public DrawableWingSection(DrawableWingSection copy, DrawableWing _belongsTo) {
        this(copy.getDiedre(), copy.getFleche(), copy.getWidth(), copy.getLenght(), _belongsTo);
    }

    public DrawableWingSection(Dimension3D wingConnection, DrawableWing _belongsTo) {
        super(wingConnection, _belongsTo);
        switch (((DrawableWing) belongsTo).getUsedFor()) {
            case VERTICAL_PLAN: {
                setValues(90, -20, 50, 50);
                break;
            }
            case HORIZONTAL_PLAN: {
                setValues(1, -10, 10, 20);
                break;
            }
            default:
            case MAIN_WING: {
                setValues(2f, 0, 55, 60);
                break;
            }
        }
    }

    /**
     * Constructors
     */
    public DrawableWingSection(Dimension3D wingConnection, float _diedre, float _fleche, float _width, float _lenght, DrawableWing _belongsTo) {
        super(wingConnection, _belongsTo);
        setValues(_diedre, _fleche, _width, _lenght);
    }

    public DrawableWingSection(float _diedre, float _fleche, float _width, float _lenght, DrawableWing _belongsTo) {
        super(_belongsTo);
        setValues(_diedre, _fleche, _width, _lenght);
    }

    /**
     * Constructor from real model
     */
    public DrawableWingSection(WingSection _in, DrawableWing _belongsTo) {
        super(_in.getPositionDimension3D(), _belongsTo);
        setValues(_in.getDiedre(), _in.getFleche(), _in.getWidth(), _in.getLenght());
    }

    /**
     * getters and setters
     */
    private void setValues(float _diedre, float _fleche, float _width, float _lenght) {
        diedre = _diedre;
        fleche = _fleche;
        width = _width;
        lenght = _lenght;

    }

    public float getDiedre() {
        return diedre;
    }

    public float getWidth() {
        return width;
    }

    public float getLenght() {
        return lenght;
    }

    @Override
    public void setDiedre(float _diedre) {
        if (((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.HORIZONTAL_PLAN)) {
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

    @Override
    public void setWidth(float _width) {
        width = _width;
        apply();
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

    public float getViewableLength() {
        return viewableLength;
    }

    /**
     * *
     * Getters for points
     */
    @Override
    public DrawablePoint getFrontPointTopView() {
        return frontPointTopView;
    }

    @Override
    public DrawablePoint getBackPointTopView() {
        return backPointTopView;
    }

    @Override
    public DrawablePoint getFrontPointFrontView() {
        return frontPointFrontView;
    }

    public DrawablePoint getPreviousFrontPointTopView() {
        return previous.getFrontPointTopView();
    }

    public DrawablePoint getPreviousBackPointTopView() {
        return previous.getBackPointTopView();
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
        int index = ((DrawableWing) belongsTo).getIndexInArray(this);
        if (index > 0) {
            previous = ((DrawableWing) belongsTo).get(index - 1);
            setyPos(previous.getFrontPointFrontView().getFloatX());
            setzPos(previous.getFrontPointFrontView().getFloatY());
        } else {
            previous = (AbstractDrawableWing) belongsTo;
            setPosXYZ(belongsTo.getPositionDimension3D(), true);
        }


        viewableLength = (float) (lenght * (Math.cos(Math.toRadians(diedre))));
        //   viewableWidth = (float) (distance valeur absolue de widthAtConnection * (Math.cos(Math.toRadians(calageAngulaire))));

        if (!pointsCalculed) {
            frontPointTopView = new DrawablePoint(previous.getFrontPointTopView().getFloatX() - viewableLength, previous.getFrontPointTopView().getFloatY() - fleche, this);
            //   frontPointTopView = new DrawablePoint(Utils.getCoordOnCircle(previous.getFrontPointTopView(), fleche, viewableLength), this);
            backPointTopView = new DrawablePoint(frontPointTopView.getFloatX(), frontPointTopView.getIntY() + width, this);
            frontPointFrontView = new DrawablePoint(Utils.getCoordOnCircle(DrawablePoint.makePointForFrontView(getPositionDimension3D()), diedre + 180, lenght), !((DrawableWing) belongsTo).getUsedFor().equals(Utils.USED_FOR.HORIZONTAL_PLAN), this);
            pointsCalculed = true;
        } else {
            frontPointTopView.setLocation(previous.getFrontPointTopView().getFloatX() - viewableLength, previous.getFrontPointTopView().getFloatY() - fleche);
            backPointTopView.setFloatLocation(frontPointTopView.getFloatX(), frontPointTopView.getIntY() + width);
            frontPointFrontView.setLocation(Utils.getCoordOnCircle(DrawablePoint.makePointForFrontView(getPositionDimension3D()), diedre + 180, lenght));
        }
        setxPos(frontPointTopView.getFloatY());
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
        g.setColor(Color.GRAY.brighter());


        switch (view) {
            case FRONT_VIEW: {
                g.drawLine((int) getyPos(), (int) getzPos(), frontPointFrontView.getIntX(), frontPointFrontView.getIntY());
                frontPointFrontView.draw(g);
                break;
            }

            case TOP_VIEW: {
                Utils.drawline(frontPointTopView, previous.getFrontPointTopView(), g);
                Utils.drawline(backPointTopView, previous.getBackPointTopView(), g);

                Utils.drawline(frontPointTopView, backPointTopView, g);
                Utils.drawline(frontPointTopView.getMirrorTop(), previous.getFrontPointTopView().getMirrorTop(), g);
                Utils.drawline(backPointTopView.getMirrorTop(), previous.getBackPointTopView().getMirrorTop(), g);
                Utils.drawline(frontPointTopView.getMirrorTop(), backPointTopView.getMirrorTop(), g);
                frontPointTopView.draw(g);
                backPointTopView.draw(g);
                break;
            }
            case LEFT_VIEW:
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
                ret.add(frontPointTopView);
                ret.add(backPointTopView);
                break;
            }
            case LEFT_VIEW: {
                break;
            }
        }
        return ret;
    }

    public WingSection generateModel() {
        return new WingSection(getPositionDimension3D(), diedre, fleche, width, lenght);
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

    public float getAngle() {
        return calageAngulaire;
    }
}
