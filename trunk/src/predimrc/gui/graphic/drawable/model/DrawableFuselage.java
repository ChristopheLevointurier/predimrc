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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import predimrc.PredimRC;
import predimrc.common.UserConfig;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.graphic.drawable.tool.DrawableNeutralPoint;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
import predimrc.model.element.Fuselage;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableFuselage extends DrawableModelElement {

    private float widthY, widthZ;
    private float area = 0;
    private float kSf = 0.68f, kSMf = 0.68f, neutralPointRatio = 19f; //default value, defined in fuse.txt
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
    private ArrayList<DrawablePoint> shapeTop;
    private ArrayList<DrawablePoint> shapeLeft;
    private ArrayList<DrawablePoint> shapeFront;
    private ArrayList<DrawablePoint> scalledShapeTop;
    private ArrayList<DrawablePoint> scalledShapeLeft;
    private ArrayList<DrawablePoint> scalledShapeFront;

    public DrawableFuselage(Fuselage f, DrawableModel _belongsTo) {
        super(f.getPositionDimension3D(), _belongsTo);
        width = f.getLength();
        widthY = f.getWidthY();
        widthZ = f.getWidthZ();
        setFilename(f.getFilename());
        used_for = Utils.USED_FOR.FUSELAGE;
        neutralPointRatio = f.getNeutralPointRatio();
    }

    public DrawableFuselage(DrawableModel _belongsTo) {
        super(_belongsTo);
        width = 430;
        widthY = 30;
        widthZ = 26;
        used_for = Utils.USED_FOR.FUSELAGE;
        setPosXYZ(Utils.defaultFuselageNose, false);
        setFilename("fuse");
    }

    public static DrawableFuselage makeFake() {
        return new DrawableFuselage();
    }

    private DrawableFuselage() {
        width = 0;
        widthY = 0;
        widthZ = 0;
        used_for = Utils.USED_FOR.FUSELAGE;
        setPosXYZ(Utils.defaultFuselageNose, false);
        fake = true;
        setFilename(Utils.FAKE_FILENAME);
    }

    /**
     * getters and setters
     *
     * @return
     */
    @Override
    public void computePositions() {
        if (fake) {
            return;
        }


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
            neutralPoint = new DrawableNeutralPoint(this);  //foyer
            pointsCalculed = true;
        } else {
            /**
             * Front points*
             */
            frontPointFrontView.setLocation(getyPos(), getzPos());
            upPointFrontView.setLocation(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() - widthZ / 2);
            downPointFrontView.setLocation(frontPointFrontView.getX() - widthY / 2, frontPointFrontView.getY() + widthZ / 2);
            /**
             * Top points*
             */
            frontPointTopView.setLocation(getyPos(), getxPos());
            backPointTopView.setLocation(frontPointTopView.getX(), frontPointTopView.getY() + width);
            sidePointTopView.setLocation(frontPointTopView.getX() - widthY / 2, frontPointTopView.getY() + width / 2);
            /**
             * *
             * Left view points
             */
            frontPointLeftView.setLocation(getxPos(), getzPos());
            backPointLeftView.setLocation(frontPointLeftView.getX() + width, frontPointLeftView.getY());
            sidePointLeftView.setLocation(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() + widthZ / 2);
            mirrorSidePointLeftView.setLocation(frontPointLeftView.getX() + width / 2, frontPointLeftView.getY() - widthZ / 2);
        }
        neutralPoint.setLocation(Utils.TOP_SCREEN_X / 2, getWidth() * (getNeutralPointRatio() / 100) + getxPos()); //foyer



        if (shapeTop.size() > 0) {
            scalledShapeTop = new ArrayList<>();
            for (DrawablePoint p : shapeTop) {
                scalledShapeTop.add(new DrawablePoint(p.getFloatY() * widthY + getyPos() - widthY / 2, p.getFloatX() * width + getxPos(), VIEW_TYPE.TOP_VIEW));
            }
        }

        if (shapeLeft.size() > 0) {
            scalledShapeLeft = new ArrayList<>();
            for (DrawablePoint p : shapeLeft) {
                scalledShapeLeft.add(new DrawablePoint(p.getFloatX() * width + getxPos(), Math.abs(1 - p.getFloatY()) * widthZ + getzPos() - widthZ / 2, VIEW_TYPE.LEFT_VIEW));
            }
        }
        if (shapeFront.size() > 0) {
            scalledShapeFront = new ArrayList<>();
            for (DrawablePoint p : shapeFront) {
                scalledShapeFront.add(new DrawablePoint(p.getFloatX() * widthY + getyPos() - widthY / 2, Math.abs(1 - p.getFloatY()) * widthZ + getzPos() - widthZ / 2, VIEW_TYPE.FRONT_VIEW));
            }
        }
        area = widthY * width * kSf;
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
        if (view.equals(VIEW_TYPE.TOP_VIEW)) {
            ret.add(neutralPoint);
        }
        if (fake || !UserConfig.manipFuse) {
            return ret;
        }
        switch (view) {
            case FRONT_VIEW: {
                ret.add(frontPointFrontView);
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
        if (!pointsCalculed || fake) {
            return;
        }

        for (DrawablePoint p : getPoints(view)) {
            p.draw(g);
        }
        g.setColor(used_for.getColor());

        switch (view) {
            case FRONT_VIEW: {
                g.setStroke(new BasicStroke(2));
                shapeRender(g, scalledShapeFront);
                g.setStroke(predimrc.PredimRC.dashed);
                Utils.drawline(frontPointFrontView, upPointFrontView, g);
                Utils.drawline(frontPointFrontView, downPointFrontView, g);
                Utils.drawRect(upPointFrontView, downPointFrontView, g);
                Utils.drawline(frontPointFrontView, upPointFrontView.getMirror(), g);
                Utils.drawline(frontPointFrontView, downPointFrontView.getMirror(), g);
                break;
            }

            case TOP_VIEW: {
                g.setStroke(new BasicStroke(2));
                shapeRender(g, scalledShapeTop);
                g.setStroke(predimrc.PredimRC.dashed);
                g.drawLine(sidePointTopView.getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), frontPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getDrawCoordX(), backPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getMirror().getDrawCoordX(), frontPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), backPointTopView.getDrawCoordY());
                g.drawLine(sidePointTopView.getDrawCoordX(), backPointTopView.getDrawCoordY(), sidePointTopView.getMirror().getDrawCoordX(), backPointTopView.getDrawCoordY());
                break;
            }
            case LEFT_VIEW: {
                g.setStroke(new BasicStroke(2));
                shapeRender(g, scalledShapeLeft);
                g.setStroke(predimrc.PredimRC.dashed);
                g.drawLine(frontPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), frontPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(frontPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(backPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), mirrorSidePointLeftView.getDrawCoordY());
                g.drawLine(frontPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY(), backPointLeftView.getDrawCoordX(), sidePointLeftView.getDrawCoordY());
                break;
            }
        }
    }

    private void shapeRender(Graphics2D g, ArrayList<DrawablePoint> scalledShape) {
        if (scalledShape.size() > 0) {
            DrawablePoint temp = scalledShape.get(scalledShape.size() - 1);
            for (DrawablePoint p : scalledShape) {
                g.drawLine(temp.getDrawCoordX(), temp.getDrawCoordY(), p.getDrawCoordX(), p.getDrawCoordY());
                temp = p;
            }
        }
    }

    @Override
    public final void setFilename(String file) {
        filename = file;
        if (filename.equals(Utils.FAKE_FILENAME)) {
            fake = true;
            shapeTop = new ArrayList<>();
            shapeLeft = new ArrayList<>();
            shapeFront = new ArrayList<>();
            kSf = 0.68f;
            kSMf = 0.68f;
            neutralPointRatio = 19f;
        } else {
            shapeTop = Utils.loadDrawablePoints("Fuselages/" + filename + "_top.dat", VIEW_TYPE.TOP_VIEW);
            shapeLeft = Utils.loadDrawablePoints("Fuselages/" + filename + "_left.dat", VIEW_TYPE.LEFT_VIEW);
            shapeFront = Utils.loadDrawablePoints("Fuselages/" + filename + "_front.dat", VIEW_TYPE.FRONT_VIEW);
            Properties config = new Properties();
            try {
                config.load(predimrc.PredimRC.getResourceUrl("Fuselages/" + filename + "_coeff.txt").openStream());
                kSf = Float.parseFloat(config.getProperty("kSf", "0.65"));
                kSMf = Float.parseFloat(config.getProperty("kSMf", "0.65"));
                neutralPointRatio = Float.parseFloat(config.getProperty("xFf", "0.20"));
                PredimRC.logDebugln("fuse values:kSf " + kSf + ", kSMf " + kSMf + " , neutralPointRatio " + neutralPointRatio);
            } catch (final IOException | NumberFormatException t) {
                PredimRC.logln("IOException while attempting to load fuse File " + filename + "_coeff.txt \n" + t.getLocalizedMessage());
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
        widthY = Utils.round(_width);
        apply();
    }

    public void setWidthZ(float _width) {
        widthZ = Utils.round(_width);
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
        neutralPointRatio = Utils.round(_neutralPointRatio);
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

    public float getArea() {
        return area;
    }

    public float getkSf() {
        return kSf;
    }

    public float getkSMf() {
        return kSMf;
    }
}
