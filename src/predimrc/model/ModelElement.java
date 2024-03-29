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

import java.io.Serializable;
import predimrc.common.Dimension3D;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public abstract class ModelElement implements Serializable {

    protected float xPos, yPos, zPos;
    protected String filename = "not yet defined";

    public String toStringAll() {
        return toString();
    }

    public String getFilename() {
        return filename;
    }

    public Dimension3D getPositionDimension3D() {
        return new Dimension3D(xPos, yPos, zPos);
    }

    @Override
    public String toString() {
        return getPositionDimension3D() + ",filename=" + filename;
    }
}
