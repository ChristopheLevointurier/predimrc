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
package predimrc.gui.graphic.drawable;

import java.awt.Point;

/**
 *
 * @author Christophe Levointurier, 29 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Utils {

    public static Point getCoordOnCircle(Point center, float deg, float radius) {
        double angleRad = Math.toRadians(deg + 180);
        double x = center.getX() + radius * Math.cos(angleRad);
        double y = center.getY() + radius * Math.sin(angleRad);
        return new Point((int) x, (int) y);
    }

    public static float calcAngle(Point ref, Point point) {
        float d = (float) (Math.atan2(point.y - ref.y, point.x - ref.x) * 180.0 / Math.PI) - 180;
        d = d < 0 ? d + 360 : d;
        d = d > 180 ? d - 360 : d;
        return d;
    }
}
