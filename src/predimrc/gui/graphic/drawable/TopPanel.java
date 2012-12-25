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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.graphic.DrawablePanel;
import predimrc.model.Model;
import predimrc.model.element.Wing;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class TopPanel extends DrawablePanel {

    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Point> tailPoints = new ArrayList<>();
    private Point connection1 = new Point(380, 125);
    private Point connection2 = new Point(380, 225);
    private Point tailConnection1 = new Point(400, 355);
    private Point tailConnection2 = new Point(400, 405);
    private Point selectedPoint;
    private boolean onTail = false;
    /**
     * field used while interact with gui
     */
    private Dimension currentPos;
    private int indexWing = -1;

    public TopPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //detect nearest point;
                getNearestPoint(e.getX(), e.getY());
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
                movePoint(e.getX(), e.getY());
            }

            private void movePoint(int x, int y) {
                selectedPoint.setLocation(x, y);
            }
        });

        backgroundImage = PredimRC.getImage("top.png");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(820, 400);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        if (indexWing > -1) {
            g.drawString("Change Wing , section:" + (indexWing + 1) + ": " + currentPos, 10, 20);

        }
        g.setColor(Color.GRAY.brighter());
        Graphics2D g2 = (Graphics2D) g;
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
        g.drawLine(0, 175, 800, 175);
    }

    @Override
    public void changeModel(Model m) {
        points = new ArrayList<>();
        /**
         * Point previous = connection; for (Wing w : m.getWings()) { Point
         * newpoint = getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
         * points.add(newpoint); previous = newpoint;
        }*
         */
        tailPoints = new ArrayList<>();
        /**
         * previous = tailConnection; for (Wing w : m.getTail().getHorizontal())
         * { Point newpoint = getCoordOnCircle(previous, w.getDiedre(),
         * w.getLenght()); tailPoints.add(newpoint); previous = newpoint;
        }*
         */
        repaint();
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
                selectedPoint = p;
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
