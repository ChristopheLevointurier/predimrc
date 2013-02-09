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
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.element.Fuselage;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableFuselage extends DrawableModelElement {

    protected float widthY, widthZ, neutralPointRatio = 20;
    /**
     * *
     * Front view points
     */
    private DrawablePoint upPointFrontView;
    private DrawablePoint downPointFrontView;
    /**
     * *
     * Top view points
     */
    private DrawablePoint sidePointTopView;
    /**
     * *
     * Left view points
     */
    private DrawablePoint sidePointLeftView;
    private DrawablePoint mirrorSidePointLeftView;

    public DrawableFuselage(Fuselage f, DrawableModel _belongsTo) {
        super(f.getPositionDimension3D(), _belongsTo);
        width = f.getLength();
        widthY = f.getWidthY();
        widthZ = f.getWidthZ();
        filename = f.getFilename();
        used_for = Utils.USED_FOR.FUSELAGE;
    }

    public DrawableFuselage(DrawableModel _belongsTo) {
        super(_belongsTo);
        width = 430;
        widthY = 30;
        widthZ = 26;
        used_for = Utils.USED_FOR.FUSELAGE;
        setPosXYZ(Utils.defaultFuselageNose, false);
        filename = "fuse";
    }

    /**
     * getters adn setters
     *
     * @return
     */
    @Override
    public void computePositions() {
        if (!pointsCalculed) {
            /**
             * Front points*
             */
            frontPointFrontView = DrawablePoint.makePointForFrontView(getPositionDimension3D(), false, this, VIEW_TYPE.FRONT_VIEW);
            upPointFrontView = new DrawablePoint(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() - widthZ / 2, false, this, VIEW_TYPE.FRONT_VIEW);
            downPointFrontView = new DrawablePoint(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() + widthZ / 2, false, this, VIEW_TYPE.FRONT_VIEW);
            /**
             * Top points*
             */
            frontPointTopView = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this, VIEW_TYPE.TOP_VIEW);
            backPointTopView = new DrawablePoint(frontPointTopView.getX(), frontPointTopView.getY() + width, true, this, VIEW_TYPE.TOP_VIEW);
            sidePointTopView = new DrawablePoint(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + width / 2, true, this, VIEW_TYPE.TOP_VIEW);
            /**
             * *
             * Left view points
             */
            frontPointLeftView = DrawablePoint.makePointForLeftView(getPositionDimension3D(), true, this, VIEW_TYPE.LEFT_VIEW);
            backPointLeftView = new DrawablePoint(frontPointLeftView.getX() + width, frontPointLeftView.getY(), true, this, VIEW_TYPE.LEFT_VIEW);
            sidePointLeftView = new DrawablePoint(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() + widthZ / 2, true, this, VIEW_TYPE.LEFT_VIEW);
            mirrorSidePointLeftView = new DrawablePoint(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() - widthZ / 2, false, this, VIEW_TYPE.LEFT_VIEW);
            pointsCalculed = true;
        } else {
            /**
             * Front points*
             */
            frontPointFrontView.setLocation(yPos, zPos);
            upPointFrontView.setLocation(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() - widthZ / 2);
            downPointFrontView.setLocation(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() + widthZ / 2);
            /**
             * Top points*
             */
            frontPointTopView.setLocation(yPos, xPos);
            backPointTopView.setLocation(frontPointTopView.getX(), frontPointTopView.getY() + width);
            sidePointTopView.setLocation(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + width / 2);
            /**
             * *
             * Left view points
             */
            frontPointLeftView.setLocation(xPos, zPos);
            backPointLeftView.setLocation(frontPointLeftView.getX() + width, frontPointLeftView.getY());
            sidePointLeftView.setLocation(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() + widthZ / 2);
            mirrorSidePointLeftView.setLocation(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() - widthZ / 2);
        }
    }

    @Override
    public int getIndexInBelongsTo() {
        return 0; //only one fuselage in model for now.
    }

    /**
     * paint methods
     *
     * @param g
     */
    public Fuselage generateModel() {
        return new Fuselage(filename, getPositionDimension3D(), width, widthY, widthZ, neutralPointRatio);
    }

    @Override
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        switch (view) {
            case FRONT_VIEW: {
                //    ret.add(frontPointFrontView);
                break;
            }
            case TOP_VIEW: {
                ret.add(frontPointTopView);
                ret.add(backPointTopView);
                ret.add(sidePointTopView);
                break;
            }
            case LEFT_VIEW: {
                ret.add(frontPointLeftView);
                ret.add(backPointLeftView);
                ret.add(sidePointLeftView);
                break;
            }
        }
        return ret;
    }

    /**
     * Paint method
     *
     * @param g
     */
    @Override
    public void draw(Graphics2D g, VIEW_TYPE view) {
        if (!pointsCalculed) {
            return;
        }
        g.setColor(used_for.getColor());
        switch (view) {
            case FRONT_VIEW: {
                Utils.drawline(frontPointFrontView, upPointFrontView, g);
                Utils.drawline(frontPointFrontView, downPointFrontView, g);
                Utils.drawRect(upPointFrontView, downPointFrontView, g);
                Utils.drawline(frontPointFrontView, upPointFrontView.getMirror(), g);
                Utils.drawline(frontPointFrontView, downPointFrontView.getMirror(), g);
                frontPointFrontView.draw(g);
                break;
            }

            case TOP_VIEW: {
                g.setStroke(new BasicStroke(2));
                g.drawLine(sidePointTopView.getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), frontPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getDrawCoordX(), backPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getMirror().getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), backPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getDrawCoordX(), backPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), backPointTopView.getDrawCoordY());
                frontPointTopView.draw(g);
                backPointTopView.draw(g);
                sidePointTopView.draw(g);
                break;
            }
            case LEFT_VIEW: {
                g.setStroke(new BasicStroke(2));
                g.drawLine(frontPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), frontPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(frontPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(backPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(frontPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY());
                frontPointLeftView.draw(g);
                backPointLeftView.draw(g);
                sidePointLeftView.draw(g);
                break;
            }
        }
    }

    public float getWidthY() {
        return widthY;
    }

    public float getWidthZ() {
        return widthZ;
    }

    public void setWidthY(float _width) {
        widthY = _width;
        apply();
    }

    public void setWidthZ(float _width) {
        widthZ = _width;
        apply();
    }

    public boolean isWidthYPoint(DrawablePoint p) {
        return p.equals(sidePointTopView);
    }

    public boolean isWidthZPoint(DrawablePoint p) {
        return p.equals(sidePointLeftView);
    }

    public float getNeutralPointRatio() {
        return neutralPointRatio;
    }

    public void setNeutralPointRatio(float _neutralPointRatio) {
        neutralPointRatio = _neutralPointRatio;
    }

    @Override
    public DrawableModel getBelongsTo() {
        return (DrawableModel) belongsTo;
    }

    @Override
    public String toString() {
        return "DrawableFuselage " + filename + getPositionDimension3D() + ",widthX=" + width + ",widthY=" + widthY + ",widthZ=" + widthZ + " ,neutralPointRatio=" + neutralPointRatio;
    }

    @Override
    public String toInfoString() {
        return "Fuselage";
    }
}
