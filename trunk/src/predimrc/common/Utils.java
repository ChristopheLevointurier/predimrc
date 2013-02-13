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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DTriangle;
import javagl.jglcore.JGL_3DVector;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

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
    private static final Color DEFAULT_FUSELAGE_COLOR = Color.GRAY;
    private static final Color DEFAULT_MAIN_WING_COLOR = Color.PINK;
    private static final Color DEFAULT_TAIL_WING_COLOR = Color.ORANGE;
    private static final Color DEFAULT_DERIVE_WING_COLOR = Color.GREEN;
    public static final float DEFAULT_MAIN_WING_WIDTH_VALUE = 100;
    public static final float DEFAULT_TAIL_WING_WIDTH_VALUE = 55;
    public static final float DEFAULT_DERIVE_WING_WIDTH_VALUE = 35;
    public static final int LEFT_SCREEN_X = 450;
    public static final int TOP_SCREEN_Y = 460;
    public static final int LEFT_SCREEN_Y = 200;
    public static final int FRONT_SCREEN_X = 460;
    public static final int FRONT_SCREEN_Y = 200;
    public static final int TOP_SCREEN_X = 920;
    public static final Dimension3D defaultFuselageNose = new Dimension3D(5, TOP_SCREEN_X / 2, 125);
    public static final Dimension3D defaultWingConnection = new Dimension3D(145, TOP_SCREEN_X / 2 - 15, 112);
    public static final Dimension3D defaultTailConnection = new Dimension3D(390, TOP_SCREEN_X / 2, 60);
    public static final Dimension3D defaultDeriveConnection = new Dimension3D(370, TOP_SCREEN_X / 2, 125);
    public static final String defaultWingFoil = "fad05.dat";
    public static final String defaultTailFoil = "s8064.dat";
    public static final String defaultDeriveFoil = "naca0006";
    public static final int DEFAULT_Y_FRAME = 600;
    public static final int MAIN_FRAME_SIZE_X = 1144;
    public static final int MAIN_FRAME_SIZE_Y = 780;
    public static final int DEFAULT_X_FRAME = 800;
    public static Dimension3D REF_POINT = new Dimension3D();

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

    public static JGL_3DMesh getRectangle(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3, JGL_3DVector p4, Color color) {
        JGL_3DMesh mesh = new JGL_3DMesh();
        mesh.addFace(new JGL_3DTriangle(p1, p2, p3, color));
        mesh.addFace(new JGL_3DTriangle(p3, p4, p1, color));
        return mesh;
    }

    public static Dimension3D getRefPos(Dimension3D in) {
        return in.sub(REF_POINT);
    }

    public static Dimension3D getWorldPos(Dimension3D in) {
        return in.add(REF_POINT);
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

        FUSELAGE(Utils.DEFAULT_FUSELAGE_COLOR, "Fuselage"),
        MAIN_WING(Utils.DEFAULT_MAIN_WING_COLOR, "Wing"),
        VERTICAL_PLAN(Utils.DEFAULT_DERIVE_WING_COLOR, "Derive"),
        HORIZONTAL_PLAN(Utils.DEFAULT_TAIL_WING_COLOR, "Tail"),
        DEFAULT(Color.BLACK, "default");
        private Color color;
        private String desc;

        private USED_FOR(Color c, String descr) {
            color = c;
            desc = descr;
        }

        public String getDesc() {
            return desc;
        }

        public Color getColor() {
            return color;
        }
    }

    public static enum VIEW_TYPE {

        FRONT_VIEW, TOP_VIEW, LEFT_VIEW;
    }

    public static void drawline(DrawablePoint a, DrawablePoint b, Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawLine(a.getDrawCoordX(), a.getDrawCoordY(), b.getDrawCoordX(), b.getDrawCoordY());
    }

    public static void drawLine(int x, int y, DrawablePoint b, Graphics g, VIEW_TYPE view) {
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawLine(x, y, b.getDrawCoordX(), b.getDrawCoordY());
    }

    public static void drawRect(DrawablePoint p1, DrawablePoint p2, DrawablePoint p3, DrawablePoint p4, Graphics2D g, boolean miror) {
        Utils.drawline(p1, p3, g);
        Utils.drawline(p2, p4, g);
        Utils.drawline(p1, p2, g);
        if (miror) {
            Utils.drawline(p1.getMirror(), p3.getMirror(), g);
            Utils.drawline(p2.getMirror(), p4.getMirror(), g);
            Utils.drawline(p1.getMirror(), p2.getMirror(), g);
        }
    }

    public static void drawRect(DrawablePoint p1, DrawablePoint p2, Graphics2D g) {
        Utils.drawline(p1, p2, g);
        Utils.drawline(p1, p1.getMirror(), g);
        Utils.drawline(p1.getMirror(), p2.getMirror(), g);
        Utils.drawline(p2.getMirror(), p2, g);
    }

    public static double round(double in) {
        return Math.round(in * 100.0) / 100.0;
    }

    public static float round(float in) {
        return (float) (Math.round(in * 100.0) / 100.0);
    }
}
