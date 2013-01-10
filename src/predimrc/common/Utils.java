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
package predimrc.common;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 29 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Utils {

    /**
     * datas
     */
    public static final float DEFAULT_MAIN_WING_WIDTH_VALUE = 55;
    public static final float DEFAULT_TAIL_WING_WIDTH_VALUE = 25;
    public static final float DEFAULT_DERIVE_WING_WIDTH_VALUE = 35;
    public static final int LEFT_SCREEN_X = 450;
    public static final int TOP_SCREEN_Y = 450;
    public static final int LEFT_SCREEN_Y = 200;
    public static final int FRONT_SCREEN_X = 450;
    public static final int FRONT_SCREEN_Y = 200;
    public static final int TOP_SCREEN_X = 900;
    public static final Dimension3D defaultWingConnection = new Dimension3D(125, TOP_SCREEN_X / 2 - 10, 125);
    public static final Dimension3D defaultTailConnection = new Dimension3D(350, TOP_SCREEN_X / 2, 85);
    public static final Dimension3D defaultDeriveConnection = new Dimension3D(350, TOP_SCREEN_X / 2, 135);

    public static Point2D.Float getCoordOnCircle(DrawablePoint center, float deg, float radius) {
        double angleRad = Math.toRadians(deg + 180);
        double x = center.getX() + radius * Math.cos(angleRad);
        double y = center.getY() + radius * Math.sin(angleRad);
        return new Point2D.Float((float) x, (float) y);
    }

    public static float calcAngle(Point2D.Float ref, int x, int y) {
        float d = (float) (Math.atan2(y - ref.getY(), x - ref.getX()) * 180.0 / Math.PI) - 180;
        d = d < 0 ? d + 360 : d;
        d = d > 180 ? d - 360 : d;
        return d;
    }

    public static float calcAngle(DrawablePoint ref, int x, int y) {
        float d = (float) (Math.atan2(y - ref.getY(), x - ref.getX()) * 180.0 / Math.PI) - 180;
        d = d < 0 ? d + 360 : d;
        d = d > 180 ? d - 360 : d;
        return d;
    }

    public static enum USED_FOR {

        MAIN_WING, VERTICAL_PLAN, HORIZONTAL_PLAN;
    }

    public static enum VIEW_TYPE {

        FRONT_VIEW, TOP_VIEW, LEFT_VIEW;
    }

    public static void drawline(DrawablePoint a, DrawablePoint b, Graphics g) {
        g.drawLine(a.getIntX(), a.getIntY(), b.getIntX(), b.getIntY());
    }
}
