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

    protected float widthY, widthZ;
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
    private DrawablePoint upPointTopView;
    private DrawablePoint downPointTopView;
    /**
     * *
     * Left view points
     */
    private DrawablePoint upFrontPointLeftView;
    private DrawablePoint downFrontPointLeftView;
    private DrawablePoint upBackPointLeftView;
    private DrawablePoint downBackPointLeftView;

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
        widthZ = 45;
        used_for = Utils.USED_FOR.FUSELAGE;
        setPosXYZ(Utils.defaultFuselageNose, false);
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
            frontPointFrontView = DrawablePoint.makePointForFrontView(getPositionDimension3D(), false, this);
            upPointFrontView = new DrawablePoint(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() - widthZ / 2, false, this);
            downPointFrontView = new DrawablePoint(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() + widthZ / 2, false, this);
            /**
             * Top points*
             */
            frontPointTopView = DrawablePoint.makePointForTopView(getPositionDimension3D(), true, this);
            backPointTopView = new DrawablePoint(frontPointTopView.getX(), frontPointTopView.getY() + width, true, this);
            upPointTopView = new DrawablePoint(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + width / 5, true, this);
            downPointTopView = new DrawablePoint(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + 2 * width / 5, true, this);
            /**
             * *
             * Left view points
             */
            frontPointLeftView = DrawablePoint.makePointForLeftView(getPositionDimension3D(), true, this);
            backPointLeftView = new DrawablePoint(frontPointLeftView.getX() + width, frontPointLeftView.getY(), true, this);
            upFrontPointLeftView = new DrawablePoint(frontPointLeftView.getX() + width / 5, frontPointLeftView.getY() - widthZ / 2, false, this);
            downFrontPointLeftView = new DrawablePoint(frontPointLeftView.getX() + width / 5, frontPointLeftView.getY() + widthZ / 2, true, this);
            upBackPointLeftView = new DrawablePoint(frontPointLeftView.getX() + 2 * width / 5, frontPointLeftView.getY() - widthZ / 2, false, this);
            downBackPointLeftView = new DrawablePoint(frontPointLeftView.getX() + 2 * width / 5, frontPointLeftView.getY() + widthZ / 2, true, this);
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
            upPointTopView.setLocation(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + width / 5);
            downPointTopView.setLocation(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + 2 * width / 5);
            /**
             * *
             * Left view points
             */
            frontPointLeftView.setLocation(xPos, zPos);
            backPointLeftView.setLocation(frontPointLeftView.getX() + width, frontPointLeftView.getY());
            upFrontPointLeftView.setLocation(frontPointLeftView.getX() + width / 5, frontPointLeftView.getY() - widthZ / 2);
            downFrontPointLeftView.setLocation(frontPointLeftView.getX() + width / 5, frontPointLeftView.getY() + widthZ / 2);
            upBackPointLeftView.setLocation(frontPointLeftView.getX() + 2 * width / 5, frontPointLeftView.getY() - widthZ / 2);
            downBackPointLeftView.setLocation(frontPointLeftView.getX() + 2 * width / 5, frontPointLeftView.getY() + widthZ / 2);
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
        return new Fuselage(filename, width, widthY, widthZ);
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
                ret.add(upPointTopView);
                ret.add(downPointTopView);
                break;
            }
            case LEFT_VIEW: {
                ret.add(frontPointLeftView);
                ret.add(backPointLeftView);
                ret.add(upFrontPointLeftView);
                ret.add(downFrontPointLeftView);
                ret.add(upBackPointLeftView);
                ret.add(downBackPointLeftView);
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
        switch (view) {
            case FRONT_VIEW: {
                g.setColor(used_for.getColor());
                Utils.drawline(frontPointFrontView, upPointFrontView, g);
                Utils.drawline(frontPointFrontView, downPointFrontView, g);
                Utils.drawRect(upPointFrontView, downPointFrontView, g, view);
                frontPointFrontView.draw(g);
                upPointFrontView.draw(g);
                downPointFrontView.draw(g);
                break;
            }

            case TOP_VIEW: {
                frontPointTopView.draw(g);
                backPointTopView.draw(g);
                downPointTopView.draw(g);
                upPointTopView.draw(g);
                g.setColor(used_for.getColor());
                Utils.drawline(frontPointTopView, upPointTopView, g);
                Utils.drawline(upPointTopView, downPointTopView, g);
                Utils.drawline(downPointTopView, backPointTopView, g);
                Utils.drawline(frontPointTopView, upPointTopView.getMirror(view), g);
                Utils.drawline(upPointTopView.getMirror(view), downPointTopView.getMirror(view), g);
                Utils.drawline(downPointTopView.getMirror(view), backPointTopView, g);
                break;
            }
            case LEFT_VIEW: {
                g.setColor(used_for.getColor());
                Utils.drawline(frontPointLeftView, upFrontPointLeftView, g);
                Utils.drawline(frontPointLeftView, downFrontPointLeftView, g);
                Utils.drawline(upFrontPointLeftView, upBackPointLeftView, g);
                Utils.drawline(downFrontPointLeftView, downBackPointLeftView, g);
                Utils.drawline(upBackPointLeftView, backPointLeftView, g);
                Utils.drawline(downBackPointLeftView, backPointLeftView, g);
                frontPointLeftView.draw(g);
                backPointLeftView.draw(g);
                upFrontPointLeftView.draw(g);
                downFrontPointLeftView.draw(g);
                upBackPointLeftView.draw(g);
                downBackPointLeftView.draw(g);
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
        return (p.equals(upPointTopView)
                || p.equals(downPointTopView));
    }

    public boolean isWidthZPoint(DrawablePoint p) {
        return (p.equals(upFrontPointLeftView)
                || p.equals(downFrontPointLeftView)
                || p.equals(upBackPointLeftView)
                || p.equals(downBackPointLeftView));
    }

    @Override
    public DrawableModel getBelongsTo() {
        return (DrawableModel) belongsTo;
    }

    @Override
    public String toString() {
        return "DrawableFuselage " + filename + getPositionDimension3D() + ",widthX=" + width + ",widthY=" + widthY + ",widthZ=" + widthZ;
    }

    @Override
    public String toInfoString() {
        return "Fuselage";
    }
}
