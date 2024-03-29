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
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DTriangle;
import javagl.jglcore.JGL_3DVector;
import predimrc.PredimRC;
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
    public static Dimension3D REF_POINT = new Dimension3D(0, TOP_SCREEN_X / 2, 0);
    public static final String FAKE_FILENAME = "EMPTY";
    public static final int DEFAULT_X_MINI_FRAME = 300;
    public static final int DEFAULT_Y_MINI_FRAME = 220;
    public static final int MAX_THREAD = Runtime.getRuntime().availableProcessors();

    public static Point2D.Float getCoordOnCircle(DrawablePoint center, float deg, float radius) {
        double angleRad = Math.toRadians(deg);
        double x = center.getX() + radius * Math.cos(angleRad);
        double y = center.getY() + radius * Math.sin(angleRad);
        return new Point2D.Float((float) x, (float) y);
    }

    /**
     * return the point on circle with y value on the west side
     *
     * @param center
     * @param yValue
     * @param radius
     * @return
     */
    public static double getCoordXOnCircleWithY(DrawablePoint center, float yValue, float radius) {
        if (radius == 0) {
            return center.getX();
        }
        double deg = 90 * (yValue / radius);
        double x = center.getX() - radius * Math.cos(Math.toRadians(deg));
        return (float) x;
    }

    public static float calcAngle(Point2D.Float ref, float x, float y) {
        float d = (float) (Math.atan2(y - ref.getY(), x - ref.getX()) * 180.0 / Math.PI) - 180;
        d = d < 0 ? d + 360 : d;
        d = d > 180 ? d - 360 : d;
        return d;
    }

    public static float calcAngle(DrawablePoint ref, float x, float y) {
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

    public static double distance(DrawablePoint p1, float x, float y) {
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

    public static void extractFile(File fileIn, File fileOut, boolean overwrite) {
        FileInputStream reader = null;
        FileOutputStream writer = null;
        try {
            PredimRC.logDebug("Writing :" + fileIn.getName() + " to " + fileOut + "...");
            reader = new FileInputStream(fileIn);
            if (fileOut.exists() && !overwrite) {
                return;
            }
            fileOut.getParentFile().mkdirs();
            writer = new FileOutputStream(fileOut);
            final byte[] buf;
            int i = 0;
            buf = new byte[32768];
            while ((i = reader.read(buf)) != -1) {
                writer.write(buf, 0, i);
            }
            PredimRC.logDebugln(" ok");
        } catch (IOException ex) {
            PredimRC.logln("Error copying :" + fileIn.getName() + " to " + fileOut);
        } finally {
            closeStream(reader);
            closeStream(writer);
        }
    }

    public static void closeStream(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (final IOException ex) {
                predimrc.PredimRC.logln(ex.getLocalizedMessage());
            }
        }
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

        FRONT_VIEW, TOP_VIEW, LEFT_VIEW, GRAPH;
    }

    public static enum OS {

        WINDOWS, UNIX, MAC, OTHERS;
    }

    public static void drawline(DrawablePoint a, DrawablePoint b, Graphics g) {
        g.drawLine(a.getDrawCoordX(), a.getDrawCoordY(), b.getDrawCoordX(), b.getDrawCoordY());
    }

    public static void drawLine(int x, int y, DrawablePoint b, Graphics g, VIEW_TYPE view) {
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

    public static ArrayList<DrawablePoint> loadDrawablePoints(String file, VIEW_TYPE view, boolean scalling) {
        ArrayList<DrawablePoint> points = new ArrayList<>();
        float maxX = 0.0000001f, maxY = 0.000000001f, minX = 1f, minY = 1f;

        if (file.toLowerCase().endsWith(".dat")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(predimrc.PredimRC.getDataResourceUrl(file).openStream()));
                String line;
                //  System.out.println("stream ok:" + file);
                try {
                    PredimRC.logDebugln("opening " + file + ":" + reader.readLine());
                    while ((line = reader.readLine()) != null) {
                        float f1 = 0f, f2 = 0f;
                        int cpt = 0;
                        String[] data = line.split("\\s+");
                        for (String d : data) {
                            if (d.length() > 0) {
                                f1 = cpt == 0 ? Float.parseFloat(d) : f1;
                                f2 = cpt == 1 ? Float.parseFloat(d) : f2;
                                if (cpt > 1) {
                                    PredimRC.logDebugln("dropped string:" + d);
                                }
                                minX = f1 < minX ? f1 : minX;
                                minY = f2 < minY ? f2 : minY;
                                maxX = f1 > maxX ? f1 : maxX;
                                maxY = f2 > maxY ? f2 : maxY;
                                cpt++;
                            }
                        }
                        if (cpt > 1) {
                            PredimRC.logDebugln("new point:(" + f1 + "," + f2);
                            points.add(new DrawablePoint(f1, f2, view));
                        }
                    }
                    if (scalling) {
                        //moving to 0,0
                        for (DrawablePoint p : points) {
                            p.setFloatX(p.getFloatX() - minX);
                            p.setFloatY(p.getFloatY() - minY);
                        }
                        maxX -= minX;
                        maxY -= minY;
                        //scalling to 1,1
                        for (DrawablePoint p : points) {
                            p.setFloatX(p.getFloatX() * (1 / maxX));
                            p.setFloatY(p.getFloatY() * (1 / maxY));
                        }
                    }
                    PredimRC.logDebugln("points amount:" + points.size());
                } catch (IOException ex) {
                    predimrc.PredimRC.logln("IOException:" + ex.getLocalizedMessage());
                } catch (NumberFormatException ex) {
                    predimrc.PredimRC.logln("NumberFormatException, non data line in file:" + ex.getLocalizedMessage());
                } finally {
                    reader.close();
                }
            } catch (IOException | NullPointerException ex) {
                predimrc.PredimRC.logln("loadDrawablePoints, IOException|NullPointerException while trying to read :" + file + System.getProperty("line.separator") + ex.getLocalizedMessage());
            }
        } else {
            predimrc.PredimRC.logln(file + " don't seems to be a data file.");
        }
        return points;
    }

    public static OS getOs() {
        Properties sys = System.getProperties();
        String os = sys.getProperty("os.name");
        if (os.endsWith("NT") || os.endsWith("2000") || os.endsWith("XP") || os.contains("Windows")) {
            return OS.WINDOWS;
        } else { //for now
            return OS.OTHERS;
        }
    }

    /**
     * get the nearest point from a list by x or y , from negatives or from
     * positives values compare to wanted point
     *
     * @param lst
     * @param wanted
     * @param byX
     * @param upside
     * @return
     */
    public static DrawablePoint getnearestPoint(ArrayList<DrawablePoint> lst, double wanted, boolean byX, boolean upside) {
        DrawablePoint ret = new DrawablePoint(Float.MAX_VALUE, Float.MAX_VALUE);

        for (DrawablePoint pt : lst) {
            if (byX && (!upside && (pt.getX() <= wanted) || upside && (pt.getX() > wanted))) {
                if (Math.abs(ret.getX() - wanted) > Math.abs(pt.getX() - wanted)) {
                    ret = pt;
                }

            }
            if (!byX && (!upside && (pt.getY() <= wanted) || upside && (pt.getY() > wanted))) {
                if (Math.abs(ret.getY() - wanted) > Math.abs(pt.getY() - wanted)) {
                    ret = pt;
                }
            }
            //     System.out.println(" pt=" + pt + "ret=" + ret + " wanted " + wanted + " byX " + byX + " upside " + upside);
        }
        //  System.out.println("EXIT  ret=" + ret + " wanted " + wanted);
        return ret;
    }
}
