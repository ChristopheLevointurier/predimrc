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
public class WingSection extends ModelElement {

    //position x,y,z of the wing is the front first point.
    //diedre & fleche are in degree.
    protected float diedre, fleche;
    protected float width, lenght;

    public WingSection() {
        fleche = 0f;
        diedre = 2f;
        width = 65f;
        lenght = 60f;
    }

    public WingSection(Dimension3D xyz, float _diedre, float _fleche, float _width, float _lenght) {
        diedre = _diedre;
        fleche = _fleche;
        width = _width;
        lenght = _lenght;
        xPos = xyz.getX();
        yPos = xyz.getY();
        zPos = xyz.getZ();
    }

    /**
     * getters
     */
    public float getDiedre() {
        return diedre;
    }

    public float getFleche() {
        return fleche;
    }

    public float getWidth() {
        return width;
    }

    public float getLenght() {
        return lenght;
    }

    @Override
    public String toString() {
        return "\nWingSection  fleche=" + fleche + ", diedre=" + diedre + ", width=" + width + ", lenght=" + lenght;
    }
}
