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
package predimrc.gui.graphic.drawable.tool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import predimrc.common.Dimension3D;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;

/**
 * 2D projection of a vertex of a IDrawableObject.
 *
 * @author Christophe Levointurier, 7 fév. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableNeutralPoint extends DrawablePoint {

    public DrawableNeutralPoint(DrawableModelElement _belongsTo) {
        super(0, 0, _belongsTo, VIEW_TYPE.TOP_VIEW);
    }

    public DrawableNeutralPoint(float _x, float _y, DrawableModelElement _belongsTo) {
        super(_x, _y, _belongsTo, VIEW_TYPE.TOP_VIEW);
    }

    @Override
    public DrawableNeutralPoint getMirror() {
        return this;
    }

    @Override
    public void draw(Graphics2D g) {

        if (selected) {
            g.setColor(Color.BLUE.brighter());
        } else {
            g.setColor(belongsTo.getUsedFor().getColor());
        }

        g.setStroke(new BasicStroke(1));
        int neutralYpos = (int) (getDrawCoordY());
        g.drawOval(getDrawCoordX() - 5, neutralYpos - 5, 10, 10);
        g.drawLine(getDrawCoordX() - 10, neutralYpos, getDrawCoordX() + 10, neutralYpos);
        g.drawLine(getDrawCoordX(), neutralYpos - 10, getDrawCoordX(), neutralYpos + 10);
    }

    @Override
    public String toInfoString() {
        Dimension3D pos = Utils.getRefPos(new Dimension3D(y, x, 0));
        return ": NeutralPoint(" + pos.getX() + "," + pos.getY() + ")";
    }

    @Override
    public String toStringAll() {
        return "DrawableNeutralPoint(" + x + "," + y + ")selectable=" + selectable + " selected=" + selected + " for=" + belongsTo + " for view:" + view.name();
    }
}
