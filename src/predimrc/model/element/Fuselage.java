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

import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class Fuselage extends ModelElement {

    private float length;
    private String filename = "not yet defined";

    public Fuselage(String _filename, float _length) {
        filename = _filename;
        length = _length;
    }

    public float getLength() {
        return length;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return "Fuselage " + filename + getPositionDimension3D() + ",length=" + length;
    }
}
