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

    private ArrayList<DrawableWingPart> wingParts = new ArrayList<>();
    //  private ArrayList<DrawablePoint> wingPoints2 = new ArrayList<>();
    //   private ArrayList<DrawablePoint> tailPoints = new ArrayList<>();
    //  private ArrayList<DrawablePoint> tailPoints2 = new ArrayList<>();
    private DrawablePoint wingConnection = new DrawablePoint(380, 125);
    //  private DrawablePoint tailConnection = new DrawablePoint(395, 355);
    private DrawablePoint selectedPoint = new DrawablePoint(0, 0);
    //   private boolean onTail = false;
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
                repaint();
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
        ((Graphics2D) g).setStroke(new BasicStroke(10));
         for (DrawableWingPart p : wingParts) {
            p.draw(g);
        }
      
         
        g.setColor(Color.GRAY);
     //   ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
     //   g.drawLine(0, 175, 800, 175);
    }

    @Override
    public void changeModel(Model m) {
        wingParts = new ArrayList<>();
        DrawableWingPart previous = DrawableWingPart.makeRoot(wingConnection, m.getWings().get(0));
        for (Wing w : m.getWings()) {
            DrawableWingPart d = new DrawableWingPart(w, previous);
            wingParts.add(d);
            previous = d;
        }


        /**
         * tailPoints = new ArrayList<>(); previous = tailConnection; for (Wing
         * w : m.getTail().getHorizontal()) { Point newpoint =
         * getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
         * tailPoints.add(newpoint); previous = newpoint; }*
         */
        repaint();
    }

    private void getNearestPoint(int x, int y) {
        double dist = Integer.MAX_VALUE;
        int index = 0;
        for (DrawableWingPart d : wingParts) {
            double temp = PredimRC.distance(d.getFrontPoint(), new DrawablePoint(x, y));
            if (temp < dist) {
                dist = temp;
                //    onTail = false;
                indexWing = index;
                selectedPoint.setSelected(false);
                selectedPoint = d.getFrontPoint();
                d.getFrontPoint().setSelected(true);
            } else {
                d.getFrontPoint().setSelected(false);
            }
            index++;
        }
        /**
         * *
         * index = 0; for (Point p : tailPoints) { double temp =
         * PredimRC.distance(p, new Point(x, y)); if (temp < dist) { dist =
         * temp; onTail = true; indexWing = index; } index++; }
         */
    }
}
