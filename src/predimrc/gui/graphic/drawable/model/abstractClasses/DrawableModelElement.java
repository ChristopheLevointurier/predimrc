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
import jglcore.JGL_3DVector;
import predimrc.PredimRC;
import predimrc.common.Dimension3D;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 06 janv. 2013
 * @version
 * @see
 * @since
 */
public abstract class DrawableModelElement {

    protected float xPos, yPos, zPos;
    protected DrawableModelElement belongsTo;

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
        xPos = (float) _XYZpos.getX();
        yPos = (float) _XYZpos.getY();
        zPos = (float) _XYZpos.getZ();
        if (!silent) {
            apply();
        }
    }

    public JGL_3DVector getPosition3DVector() {
        return new JGL_3DVector(xPos, yPos, zPos);
    }

    public Dimension3D getPositionDimension3D() {
        return new Dimension3D(xPos, yPos, zPos);
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

    protected void apply() {
        if (PredimRC.initDone) {
            ModelController.applyChange();
        }
    }

    abstract public ArrayList<DrawablePoint> getFrontPoints();

    abstract public ArrayList<DrawablePoint> getBackPoints();

    abstract public ArrayList<DrawablePoint> getTopPoints();

    abstract public void computePositions();

    abstract public int getIndexInBelongsTo();

    abstract public void drawTop(Graphics2D g);

    abstract public void drawLeft(Graphics2D g);

    abstract public void drawFront(Graphics2D g);
}
