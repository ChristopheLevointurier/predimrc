/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.graphic.drawable.model.abstractClasses;

import java.awt.Graphics2D;
import java.util.ArrayList;
import javagl.jglcore.JGL_3DVector;
import predimrc.PredimRC;
import predimrc.common.Dimension3D;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 06 janv. 2013
 * @version
 * @see
 * @since
 */
public abstract class DrawableModelElement {

    /**
     * Front view Point
     */
    protected DrawablePoint frontPointFrontView;
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
    /**
     * position coord
     */
    protected float xPos, yPos, zPos;
    protected DrawableModelElement belongsTo;
    protected boolean pointsCalculed = false;
    protected String filename = "notYetDefined";
    protected float width;
    protected Utils.USED_FOR used_for;

    public DrawableModelElement(Dimension3D d, DrawableModelElement _belongsTo) {
        setPosXYZ(d, true);
        belongsTo = _belongsTo;
    }

    public DrawableModelElement(DrawableModelElement _belongsTo) {
        belongsTo = _belongsTo;
    }

    public DrawableModelElement() {
    }

    public String toStringAll() {
        return toString();
    }

    public void setPos(float _xPos, float _Ypox, float _Zpos) {
        xPos = _xPos;
        yPos = _Ypox;
        zPos = _Zpos;
        apply();
    }

    public final void setPosXYZ(Dimension3D _XYZpos, boolean silent) {
        xPos = _XYZpos.getX();
        yPos = _XYZpos.getY();
        zPos = _XYZpos.getZ();
        if (!silent) {
            apply();
        }
    }

    public Utils.USED_FOR getUsedFor() {
        return used_for;
    }

    public JGL_3DVector getPosition3DVector() {
        return new JGL_3DVector(xPos, yPos, zPos);
    }

    public Dimension3D getPositionDimension3D() {
        return new Dimension3D(xPos, yPos, zPos);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float _width) {
        width = _width;
        apply();
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getzPos() {
        return zPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public void setzPos(float zPos) {
        this.zPos = zPos;
    }

    public DrawableModelElement getBelongsTo() {
        return belongsTo;
    }

    public void apply() {
        if (PredimRC.initDone) {
            ModelController.applyChange();
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DrawablePoint getFrontPointTopView() {
        return frontPointTopView;
    }

    public DrawablePoint getBackPointTopView() {
        return backPointTopView;
    }

    public DrawablePoint getBackPointLeftView() {
        return backPointLeftView;
    }

    public DrawablePoint getFrontPointLeftView() {
        return frontPointLeftView;
    }

    public DrawablePoint getFrontPointFrontView() {
        return frontPointFrontView;
    }

    abstract public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view);

    abstract public void computePositions();

    abstract public int getIndexInBelongsTo();

    abstract public void draw(Graphics2D g, VIEW_TYPE view);

    abstract public String toInfoString();
}
