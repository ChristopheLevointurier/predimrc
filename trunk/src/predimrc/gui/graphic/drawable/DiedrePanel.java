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
package predimrc.gui.graphic.drawable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import jglcore.JGL_3DVector;
import predimrc.PredimRC;
import predimrc.controller.IModelListener;
import predimrc.gui.graphic.DrawablePanel;
import predimrc.model.Model;
import predimrc.model.element.Wing;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class DiedrePanel extends DrawablePanel {

    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Point> tailPoints = new ArrayList<>();
    private Point connection = new Point(380, 125);
    private Point tailConnection = new Point(400, 55);
    private boolean onTail = false;
    /**
     * field used while interact with gui
     */
    private float currentDiedre;
    private int indexWing = -1;

    public DiedrePanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //detect nearest point;
                getNearestPoint(e.getX(), e.getY());
                currentDiedre = onTail ? PredimRC.getInstance().getModel().getTail().getHorizontal().get(indexWing).getDiedre() : PredimRC.getInstance().getModel().getWings().get(indexWing).getDiedre();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                //       System.out.println(e.getX() + ":" + e.getY());
            }

            public void mouseDragged(MouseEvent e) {

                Point ref = onTail ? tailConnection : connection;
                if (indexWing > 0) {
                    ref = onTail ? tailPoints.get(indexWing - 1) : points.get(indexWing - 1);
                }

                currentDiedre = calcDiedre(ref, new Point(e.getX(), e.getY()));
                if (onTail) {
                    movePoint(getCoordOnCircle(ref, currentDiedre, PredimRC.getInstance().getModel().getTail().getHorizontal().get(indexWing).getLenght()));
                } else {
                    movePoint(getCoordOnCircle(ref, currentDiedre, PredimRC.getInstance().getModel().getWings().get(indexWing).getLenght()));
                }

            }
        });

        backgroundImage = PredimRC.getImage("front.png");
    }

    private void movePoint(Point g) {
        if (onTail) {
            PredimRC.getInstance().getModel().getTail().getHorizontal().get(indexWing).setDiedre(currentDiedre);
        } else {
            PredimRC.getInstance().getModel().getWings().get(indexWing).setDiedre(currentDiedre);
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        if (indexWing > -1) {
            if (onTail) {
                g.drawString("Tail diedre, section:" + (indexWing + 1) + " : " + currentDiedre, 10, 20);
            } else {
                g.drawString("Wing diedre , section:" + (indexWing + 1) + ": " + currentDiedre, 10, 20);
            }
        }
        g.setColor(Color.GRAY.brighter());
        Graphics2D g2 = (Graphics2D) g;
        Point previous = connection;
        g2.setStroke(new BasicStroke(12));
        for (Point p : points) {
            g2.drawLine((int) previous.x, (int) previous.y, p.x, p.y);
            previous = p;
        }

        previous = tailConnection;
        for (Point p : tailPoints) {
            g2.drawLine((int) previous.x, (int) previous.y, p.x, p.y);
            previous = p;
        }

        int i = 0;
        g.setColor(Color.BLUE);
        for (Point p : points) {
            if (!onTail && i == indexWing) {
                g.setColor(Color.RED);
                g.drawOval(p.x, p.y, 2, 2);
                g.setColor(Color.BLUE.brighter());

            } else {
                g.drawOval(p.x, p.y, 2, 2);
            }
            i++;
        }
        i = 0;
        for (Point p : tailPoints) {
            if (onTail && i == indexWing) {
                g.setColor(Color.RED);
                g.drawOval(p.x, p.y, 2, 2);
                g.setColor(Color.BLUE.brighter());

            } else {
                g.drawOval(p.x, p.y, 2, 2);
            }
            i++;
        }
        g.setColor(Color.GRAY);
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 125, 390, 125);
    }

    @Override
    public void changeModel(Model m) {
        points = new ArrayList<>();
        Point previous = connection;
        for (Wing w : m.getWings()) {
            Point newpoint = getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
            points.add(newpoint);
            previous = newpoint;
        }

        tailPoints = new ArrayList<>();
        previous = tailConnection;
        for (Wing w : m.getTail().getHorizontal()) {
            Point newpoint = getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
            tailPoints.add(newpoint);
            previous = newpoint;
        }
        repaint();
    }

    private Point getCoordOnCircle(Point center, float deg, float radius) {
        double angleRad = Math.toRadians(deg + 180);
        double x = (center.getX() + radius * Math.cos(angleRad));
        double y = (center.getY() + radius * Math.sin(angleRad));
        return new Point((int) x, (int) y);
    }

    private float calcDiedre(Point ref, Point point) {
        float d = ((float) (Math.atan2(point.y - ref.y, point.x - ref.x) * 180.0d / Math.PI) - 180);
        d = d < 0 ? d + 360 : d;
        d = d > 180 ? d - 360 : d;
        return d;
    }

    private void getNearestPoint(int x, int y) {
        double dist = Integer.MAX_VALUE;
        int index = 0;
        for (Point p : points) {
            double temp = PredimRC.distance(p, new Point(x, y));
            if (temp < dist) {
                dist = temp;
                onTail = false;
                indexWing = index;
            }
            index++;
        }
        index = 0;
        for (Point p : tailPoints) {
            double temp = PredimRC.distance(p, new Point(x, y));
            if (temp < dist) {
                dist = temp;
                onTail = true;
                indexWing = index;
            }
            index++;
        }



    }
}
