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

    /**
     * Top view Points
     */
    private DrawablePoint frontPoint = new DrawablePoint(0, 0);
    private DrawablePoint backPoint = new DrawablePoint(0, 0);
    /**
     * Front view Points
     */
    private DrawablePoint diedrePoint = new DrawablePoint(0, 0);
    /**
     * datas
     */
    private float viewableLength;
    //diedre & fleche are in degree.
    private float diedre, fleche;
    private float width, lenght;

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
        switch (((DrawableWing) belongsTo).getUsed_for()) {
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
    public void setDiedre(float diedre) {
        this.diedre = diedre;
        apply();
    }

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
    public DrawablePoint getFrontPoint() {
        return frontPoint;
    }

    @Override
    public DrawablePoint getBackPoint() {
        return backPoint;
    }

    @Override
    public DrawablePoint getDiedrePoint() {
        return diedrePoint;
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
        int index = ((DrawableWing) belongsTo).getIndexInArray(this);
        if (index > 0) {
            DrawableWingSection previous = ((DrawableWing) belongsTo).get(index - 1);
            setyPos(previous.getDiedrePoint().getFloatX());
            setzPos(previous.getDiedrePoint().getFloatY());
        }
        viewableLength = (float) (lenght * (Math.cos(Math.toRadians(diedre))));
        frontPoint = new DrawablePoint(Utils.getCoordOnCircle(((DrawableWing) belongsTo).getFrontPoint(), fleche, viewableLength), this);
        backPoint = new DrawablePoint(frontPoint.getFloatX(), frontPoint.getIntY() + width, this);
        diedrePoint = new DrawablePoint(Utils.getCoordOnCircle(DrawablePoint.makePointForFrontView(getPositionDimension3D()), diedre, lenght), !((DrawableWing) belongsTo).getUsed_for().equals(Utils.USED_FOR.HORIZONTAL_PLAN), this);
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
     * paint methods
     */
    @Override
    public void drawTop(Graphics2D g) {
        //   System.out.println("drawTop:" + previousFrontPoint.getIntX() + "," + previousFrontPoint.getIntY() + " - " + frontPoint.getIntX() + "," + frontPoint.getIntY());
        //will do this in Wing()
    }

    @Override
    public void drawLeft(Graphics2D g) {
        System.out.println("drawLeft " + this);
    }

    @Override
    public void drawFront(Graphics2D g) {
        g.setStroke(new BasicStroke(4));
        g.setColor(Color.GRAY.brighter());
        g.drawLine((int) getyPos(), (int) getzPos(), diedrePoint.getIntX(), diedrePoint.getIntY());
        diedrePoint.draw(g);
    }

    @Override
    public ArrayList<DrawablePoint> getFrontPoints() {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        ret.add(diedrePoint);
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

    public WingSection generateModel() {
        return new WingSection(getPositionDimension3D(), diedre, fleche, width, lenght);
    }
}
