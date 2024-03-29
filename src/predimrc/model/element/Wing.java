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

import java.util.LinkedList;
import predimrc.common.Utils.USED_FOR;
import predimrc.common.Dimension3D;
import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 29 déc. 2012
 * @version
 * @see
 * @since
 */
public class Wing extends ModelElement {

    //position x,y,z of the wing is the front first point.
    private LinkedList<WingSection> wingsSection;
    private USED_FOR used_for;
    private float width;
    private float calageAngulaire;

    public Wing(String _filename, USED_FOR _used_for, Dimension3D xyz, float _width, float _calageAngulaire, LinkedList<WingSection> _wingsSection) {
        used_for = _used_for;
        width = _width;
        xPos = xyz.getX();
        yPos = xyz.getY();
        zPos = xyz.getZ();
        calageAngulaire = _calageAngulaire;
        wingsSection = _wingsSection;
        filename = _filename;
    }

    /**
     * getters
     */
    public LinkedList<WingSection> getWingsSection() {
        return wingsSection;
    }

    public USED_FOR getUsed_for() {
        return used_for;
    }

    public float getWidth() {
        return width;
    }

    public float getCalageAngulaire() {
        return calageAngulaire;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("\nWing used_for:" + used_for.name() + ", " + super.toString() + " size:" + wingsSection.size());
        return ret.toString();
    }

    @Override
    public String toStringAll() {
        StringBuilder ret = new StringBuilder("\nWing used_for:" + used_for.name() + ", " + super.toString() + ".");
        for (WingSection w : wingsSection) {
            ret.append(w);
        }
        return ret.toString();
    }
}
