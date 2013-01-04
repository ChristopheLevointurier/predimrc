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
package predimrc.model;

import java.awt.Dimension;
import java.io.Serializable;
import jglcore.JGL_3DVector;
import predimrc.controller.ModelController;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public abstract class ModelElement implements Serializable {

    float xPos, yPos, zPos;

    public void setPos(float _xPos, float _Ypox, float _Zpos) {
        xPos = _xPos;
        yPos = _Ypox;
        zPos = _Zpos;
        ModelController.applyChange();
    }

    public void setPosXY(float _xPos, float _Ypox) {
        xPos = _xPos;
        yPos = _Ypox;
        ModelController.applyChange();
    }

    public void setPosXY(Dimension _XYpox) {
        xPos = (float) _XYpox.getWidth();
        yPos = (float) _XYpox.getHeight();
        ModelController.applyChange();
    }

    public void setPosZ(float _Zpos) {
        zPos = _Zpos;
        ModelController.applyChange();
    }

    public JGL_3DVector getPosition() {
        return new JGL_3DVector(xPos, yPos, zPos);
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

    abstract public void computePositions();
}
