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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_3DVector;
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
    public static final float DEFAULT_MAIN_WING_WIDTH_VALUE = 100;
    public static final float DEFAULT_TAIL_WING_WIDTH_VALUE = 55;
    public static final float DEFAULT_DERIVE_WING_WIDTH_VALUE = 35;
    public static final int LEFT_SCREEN_X = 450;
    public static final int TOP_SCREEN_Y = 450;
    public static final int LEFT_SCREEN_Y = 200;
    public static final int FRONT_SCREEN_X = 450;
    public static final int FRONT_SCREEN_Y = 200;
    public static final int TOP_SCREEN_X = 920;
    public static final Dimension3D defaultWingConnection = new Dimension3D(145, TOP_SCREEN_X / 2 - 15, 125);
    public static final Dimension3D defaultTailConnection = new Dimension3D(350, TOP_SCREEN_X / 2, 85);
    public static final Dimension3D defaultDeriveConnection = new Dimension3D(350, TOP_SCREEN_X / 2, 135);
    public static final String defaultWingFoil = "naca2412";
    public static final String defaultTailFoil = "s8064.dat";
    public static final String defaultDeriveFoil = "fad05.dat";
    public static final int DEFAULT_Y_FRAME = 600;
    public static final int MAIN_FRAME_SIZE_X = 1144;
    public static final int MAIN_FRAME_SIZE_Y = 756;
    public static final int DEFAULT_X_FRAME = 800;

    public static Point2D.Float getCoordOnCircle(DrawablePoint center, float deg, float radius) {
        double angleRad = Math.toRadians(deg);
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

    public static JGL_3DVector getNearestVertex(JGL_3DMesh m, JGL_3DVector p) {
        JGL_3DVector temp = new JGL_3DVector(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Enumeration e = m.getPoints().elements();
        while (e.hasMoreElements()) {
            JGL_3DVector temp2 = (JGL_3DVector) e.nextElement();
            if (distance(p, temp2) < distance(p, temp)) {
                temp = temp2;
            }
        }
        return temp;
    }

    public static JGL_3DMesh getRectangle(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3, JGL_3DVector p4, int r, int g, int b) {
        JGL_3DMesh mesh = new JGL_3DMesh();
        Color color = new Color(r, g, b);
        mesh.addFace(new JGL_3DTriangle(p1, p2, p3, color));
        mesh.addFace(new JGL_3DTriangle(p1, p3, p4, color));
        return mesh;
    }

    public static double distance(DrawablePoint p1, int x, int y) {
        return Math.sqrt((p1.getFloatX() - x) * (p1.getFloatX() - x) + (p1.getFloatY() - y) * (p1.getFloatY() - y));
    }

    public static double distance(DrawablePoint p1, DrawablePoint p2) {
        return Math.sqrt((p1.getFloatX() - p2.getFloatX()) * (p1.getFloatX() - p2.getFloatX()) + (p1.getFloatY() - p2.getFloatY()) * (p1.getFloatY() - p2.getFloatY()));
    }

    public static double distance(Point2D.Float p1, Point2D.Float p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static double distance(JGL_3DVector p1, JGL_3DVector p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z));
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
