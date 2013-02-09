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
package predimrc.model.element;

import predimrc.common.Dimension3D;
import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class Fuselage extends ModelElement {

    private float widthX, widthY, widthZ, neutralPointRatio;

    public Fuselage(String _filename, Dimension3D xyz, float _length, float _widthY, float _widthZ, float _neutralPointRatio) {
        filename = _filename;
        xPos = xyz.getX();
        yPos = xyz.getY();
        zPos = xyz.getZ();
        widthX = _length;
        widthY = _widthY;
        widthZ = _widthZ;
        neutralPointRatio = _neutralPointRatio;
    }

    public float getLength() {
        return widthX;
    }

    public float getWidthY() {
        return widthY;
    }

    public float getWidthZ() {
        return widthZ;
    }

    public float getNeutralPointRatio() {
        return neutralPointRatio;
    }

    public void setNeutralPointRatio(float neutralPointRatio) {
        this.neutralPointRatio = neutralPointRatio;
    }

    @Override
    public String toString() {
        return "Fuselage " + super.toString() + ",widthX=" + widthX + ",widthY=" + widthY + ",widthZ=" + widthZ + " , neutralPointRatio=" + neutralPointRatio;
    }
}
