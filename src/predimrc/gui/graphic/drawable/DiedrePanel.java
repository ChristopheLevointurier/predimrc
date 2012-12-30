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
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.model.Model;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class DiedrePanel extends DrawablePanel {

    private ArrayList<DrawablePoint> points = new ArrayList<>();
    private ArrayList<DrawablePoint> tailPoints = new ArrayList<>();
    private DrawablePoint connection = new DrawablePoint(380, 125);
    private DrawablePoint tailConnection = new DrawablePoint(400, 55);
    private DrawablePoint selected = new DrawablePoint(0, 0);
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

                double dist = getNearestPoint(Integer.MAX_VALUE, points, e.getX(), e.getY(), false);
                getNearestPoint(dist, tailPoints, e.getX(), e.getY(), true);
                currentDiedre = onTail ? PredimRC.getInstance().getModel().getTail().getHorizontal().get(indexWing).getDiedre() : PredimRC.getInstance().getModel().getWings().get(indexWing).getDiedre();
                info = onTail ? "Tail" : "Wing";
                info += "diedre, section:" + (indexWing + 1) + " : " + currentDiedre;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && indexWing > -1) {
                    try {
                        currentDiedre = Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.DIEDRE, ""));
                        applyDiedre();
                    } catch (java.lang.NumberFormatException exxx) {
                        PredimRC.logln("Invalid value typed");
                    }
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                //       System.out.println(e.getX() + ":" + e.getY());
            }

            public void mouseDragged(MouseEvent e) {

                DrawablePoint ref = onTail ? tailConnection : connection;
                if (indexWing > 0) {
                    ref = onTail ? tailPoints.get(indexWing - 1) : points.get(indexWing - 1);
                }

                currentDiedre = Utils.calcAngle(ref, new DrawablePoint(e.getX(), e.getY()));
                applyDiedre();
            }
        });

        backgroundImage = PredimRC.getImage("front.png");
    }

    private void applyDiedre() {
        if (onTail) {
            currentDiedre = currentDiedre > 60 ? 60 : currentDiedre;
            currentDiedre = currentDiedre < -60 ? -60 : currentDiedre;
        } else {
            currentDiedre = currentDiedre > 30 ? 30 : currentDiedre;
            currentDiedre = currentDiedre < -30 ? -30 : currentDiedre;
        }
        if (onTail) {
            PredimRC.getInstance().getModel().getTail().setDiedre(currentDiedre);
            info = "Tail diedre : " + currentDiedre;
        } else {
            PredimRC.getInstance().getModel().getWings().get(indexWing).setDiedre(currentDiedre);
            //   movePoint(Utils.getCoordOnCircle(ref, currentDiedre, PredimRC.getInstance().getModel().getWings().get(indexWing).getLenght()));
            info = "Wing diedre , section:" + (indexWing + 1) + ": " + currentDiedre;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        g.drawString(info, 10, 20);
        g.setColor(Color.GRAY.brighter());
        ((Graphics2D) g).setStroke(new BasicStroke(5));
        DrawablePoint previous = connection;
        for (DrawablePoint p : points) {
            g.drawLine((int) previous.getIntX(), (int) previous.getIntY(), p.getIntX(), p.getIntY());
            previous = p;
        }

        previous = tailConnection;
        for (DrawablePoint p : tailPoints) {
            g.drawLine((int) previous.getIntX(), (int) previous.getIntY(), p.getIntX(), p.getIntY());
            previous = p;
        }

        g.setColor(Color.BLUE);
        for (DrawablePoint p : points) {
            p.draw((Graphics2D) g);
        }
        for (DrawablePoint p : tailPoints) {
            p.draw((Graphics2D) g);
        }
        g.setColor(Color.GRAY);
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 125, 390, 125);
    }

    @Override
    public void changeModel(Model m) {
        points = new ArrayList<>();
        DrawablePoint previous = connection;
        for (WingSection w : m.getWings()) {
            DrawablePoint newpoint = Utils.getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
            points.add(newpoint);
            previous = newpoint;
        }

        tailPoints = new ArrayList<>();
        previous = tailConnection;
        for (WingSection w : m.getTail().getHorizontal()) {
            DrawablePoint newpoint = Utils.getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
            previous = newpoint;
        }
        tailPoints.add(previous);
        repaint();
    }

    private double getNearestPoint(double dist, ArrayList<DrawablePoint> list, int x, int y, boolean _onTail) {
        int index = 0;
        for (DrawablePoint p : list) {
            double temp = PredimRC.distance(p, x, y);
            if (temp < dist) {
                dist = temp;
                onTail = _onTail;
                indexWing = index;
                selected.setSelected(false);
                selected = p;
                selected.setSelected(true);
            }
            index++;
        }
        return dist;
    }
}
