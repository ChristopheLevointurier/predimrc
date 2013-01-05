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
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier,  5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since 
 */
public class DrawableWingSection extends WingSection implements IDrawableObject{

    @Override
    public void drawTop(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawLeft(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawFront(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
