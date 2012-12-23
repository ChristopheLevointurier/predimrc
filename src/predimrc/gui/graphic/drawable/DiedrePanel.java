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
    private Point connection = new Point(300, 125);
    /**
     * field used while interact with gui
     */
    private Point tomove;
    private float currentDiedre;
    private int indexWing = 0;

    public DiedrePanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //detect nearest point;
                //indexWing=..
                tomove = points.get(indexWing);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //save new location;
                PredimRC.getInstance().getModel().getWings().get(indexWing).setDiedre(currentDiedre);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
         //       System.out.println(e.getX() + ":" + e.getY());
            }

            public void mouseDragged(MouseEvent e) {

                Point ref = connection;
                if (indexWing > 0) {
                    ref = points.get(indexWing - 1);
                }

                currentDiedre = calcDiedre(ref, new Point(e.getX(), e.getY()));
                movePoint(getCoordOnCircle(ref, currentDiedre, PredimRC.getInstance().getModel().getWings().get(indexWing).getLenght()));
            }
        });

        backgroundImage = PredimRC.getImage("front.png");
    }

    private void movePoint(Point g) {
        tomove.setLocation(g.x, g.y);
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
        g.drawString("Diedre draw " + indexWing + points.size() + PredimRC.getInstance().getModel().getWings().size() + " diedre:" + currentDiedre, 10, 20);
        g.setColor(Color.GRAY);
        Point previous = connection;
        for (Point p : points) {
            g.drawLine((int) previous.x, (int) previous.y, p.x, p.y);
            //   Line2D.Double l = new Line2D.Double();
        }
        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 80, 200, 80);
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
    }

    private Point getCoordOnCircle(Point center, float deg, float radius) {
        double angleRad = Math.toRadians(deg + 180);
        double x = (center.getX() + radius * Math.cos(angleRad));
        double y = (center.getY() + radius * Math.sin(angleRad));
        return new Point((int) x, (int) y);
    }

    private float calcDiedre(Point ref, Point point) {
        return ((float) (Math.atan2(point.y - ref.y, point.x - ref.x) * 180.0d / Math.PI) - 180);
    }
}
