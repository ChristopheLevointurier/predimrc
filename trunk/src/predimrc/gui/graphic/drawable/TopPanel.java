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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import predimrc.PredimRC;
import predimrc.gui.graphic.DrawablePanel;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.model.Model;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class TopPanel extends DrawablePanel {

    public static final int MID_SCREEN_X = 410;
    public static final int MID_SCREEN_Y = 150;
    private ArrayList<DrawableWingPart> wingParts = new ArrayList<>();
    //  private ArrayList<DrawablePoint> wingPoints2 = new ArrayList<>();
    //   private ArrayList<DrawablePoint> tailPoints = new ArrayList<>();
    //  private ArrayList<DrawablePoint> tailPoints2 = new ArrayList<>();
    private DrawablePoint wingConnection = new DrawablePoint(100, 100);
    private DrawablePoint tailConnection = new DrawablePoint(100, 100);
    private DrawablePoint selectedPoint = new DrawablePoint(0, 0);
    private DrawableWingPart selectedwing;
    //   private boolean onTail = false;
    /**
     * field used while interact with gui
     */
    double dist = Integer.MAX_VALUE;
    private int indexWing = -1;
    private String infoDetail = "";

    /**
     * Constructor
     */
    public TopPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //detect nearest point;
                getNearestPoint(e.getX(), e.getY());
                if (null != selectedwing) {
                    info = selectedwing.isOntail() ? "Tail" : "Wing";
                    info += " section:";
                    info += selectedwing.isOntail() ? (indexWing - PredimRC.getInstance().getModel().getWings().size() + 1) : (indexWing + 1);
                }
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && indexWing > -1) {
                    if (selectedPoint.equals(selectedwing.getFrontPoint())) {
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.LENGTH_AND_FLECHE, "");
                    }
                    if (selectedPoint.equals(selectedwing.getPreviousBackPoint())) {//resize width1
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH1, "" + (selectedwing.getPreviousBackPoint().getFloatY()-selectedwing.getPreviousFrontPoint().getFloatY()));
                    }
                    if (selectedPoint.equals(selectedwing.getBackPoint())) {//resize width2
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH2, "" + (selectedwing.getBackPoint().getFloatY()-selectedwing.getFrontPoint().getFloatY()));
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                //          System.out.println(e.getX() + ":" + e.getY());
            }

            public void mouseDragged(MouseEvent e) {

                if (indexWing > -1 && !tailConnection.isSelected()) {
                    ArrayList<WingSection> toManage = selectedwing.isOntail() ? PredimRC.getInstance().getModel().getTail().getHorizontal() : PredimRC.getInstance().getModel().getWings();  //This should be private members init in mousepressed
                    int indexForActing = selectedwing.isOntail() ? indexWing - PredimRC.getInstance().getModel().getWings().size() : indexWing; //This should be private members init in mousepressed


                    if (selectedPoint.equals(selectedwing.getFrontPoint())) {//resize length and fleche
                        int newlenght = selectedwing.getPreviousFrontPoint().getIntX() - e.getX();
                        float newFleche = Utils.calcAngle(selectedwing.getPreviousFrontPoint(), new DrawablePoint(e.getX(), e.getY()));
                        WingSection toModifiy = toManage.get(indexForActing);
                        toModifiy.setFleche(newFleche);
                        if (newlenght > 1) {
                            toModifiy.setLenght(newlenght);
                        }
                        infoDetail = " Lenght=" + newlenght + ", Fleche=" + (e.getY() - selectedwing.getPreviousFrontPoint().getFloatY());

                    }
                    if (selectedPoint.equals(selectedwing.getBackPoint())) {//resize width2
                        int newlenght = e.getY() - selectedwing.getFrontPoint().getIntY();
                        if (newlenght > 1) {
                            toManage.get(indexForActing).setWidth_2(newlenght);
                            infoDetail = " Width2=" + newlenght;
                        }
                    }

                    if (selectedPoint.equals(selectedwing.getPreviousBackPoint())) {//resize width1
                        int newlenght = e.getY() - selectedwing.getPreviousFrontPoint().getIntY();
                        if (newlenght > 1) {
                            toManage.get(indexForActing).setWidth_1(newlenght);
                            infoDetail = " Width1=" + newlenght;
                        }
                    }


                    repaint();
                } else {
                    if (wingConnection.isSelected()) {
                        int xpos = e.getX() > MID_SCREEN_X ? MID_SCREEN_X : e.getX();
                        wingConnection.setFloatLocation(xpos, e.getY());
                        PredimRC.getInstance().getModel().getWings().get(0).setPosXY(xpos, e.getY());
                        info = "wingConnection: (" + e.getY() + "," + xpos + ")";
                        infoDetail = "";
                        changeModel(PredimRC.getInstance().getModel());
                    }
                    if (tailConnection.isSelected()) {
                        int xpos = e.getX() > MID_SCREEN_X ? MID_SCREEN_X : e.getX();
                        tailConnection.setFloatLocation(xpos, e.getY());
                        PredimRC.getInstance().getModel().getTail().getHorizontal().get(0).setPosXY(xpos, e.getY());
                        info = "tailConnection: (" + e.getY() + "," + xpos + ")";
                        infoDetail = "";
                        changeModel(PredimRC.getInstance().getModel());
                    }
                }
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
        g.drawString(info + " " + infoDetail, 10, 20);
        for (DrawableWingPart p : wingParts) {
            p.draw((Graphics2D) g);
        }


        g.setColor(Color.GRAY);
        //   ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        //   g.drawLine(0, 175, 800, 175);
    }

    @Override
    public void changeModel(Model m) {
        wingParts = new ArrayList<>();
        DrawableWingPart previous = DrawableWingPart.makeRoot(wingConnection, m.getWings().get(0));
        wingConnection.setFloatLocation(m.getWings().get(0).getxPos(), m.getWings().get(0).getyPos());
        for (WingSection w : m.getWings()) {
            DrawableWingPart d = new DrawableWingPart(w, previous, false);
            wingParts.add(d);
            previous = d;
        }

        if (m.getTail().getHorizontal().size() > 0) {
            previous = DrawableWingPart.makeRoot(tailConnection, m.getTail().getHorizontal().get(0));
            tailConnection.setFloatLocation(m.getTail().getHorizontal().get(0).getxPos(), m.getTail().getHorizontal().get(0).getyPos());
            for (WingSection w : m.getTail().getHorizontal()) {
                DrawableWingPart d = new DrawableWingPart(w, previous, true);
                wingParts.add(d);
                previous = d;
            }
        }



        /**
         * tailPoints = new ArrayList<>(); previous = tailConnection; for
         * (WingSection w : m.getTail().getHorizontal()) { Point newpoint =
         * getCoordOnCircle(previous, w.getDiedre(), w.getLenght());
         * tailPoints.add(newpoint); previous = newpoint; }*
         */
        repaint();
    }

    private void getNearestPoint(int x, int y) {
        dist = Integer.MAX_VALUE;

        checkDist(null, wingConnection, x, y);
        checkDist(wingParts.get(0), wingParts.get(0).getPreviousBackPoint(), x, y);
        if (wingParts.size() > PredimRC.getInstance().getModel().getWings().size()) {
            checkDist(null, tailConnection, x, y);
            checkDist(wingParts.get(PredimRC.getInstance().getModel().getWings().size()), wingParts.get(PredimRC.getInstance().getModel().getWings().size()).getPreviousBackPoint(), x, y);
        }
        for (DrawableWingPart d : wingParts) {
            checkDist(d, d.getFrontPoint(), x, y);
            checkDist(d, d.getBackPoint(), x, y);
        }


        /**
         * *
         * index = 0; for (Point p : tailPoints) { double temp =
         * PredimRC.distance(p, new Point(x, y)); if (temp < dist) { dist =
         * temp; onTail = true; indexWing = index; } index++; }
         */
    }

    private void checkDist(DrawableWingPart d, DrawablePoint p, int x, int y) {
        double temp = PredimRC.distance(p, x, y);
        if (temp < dist) {
            dist = temp;
            if (null == d) {
                indexWing = -1;
                selectedwing = null;
            } else {
                indexWing = wingParts.indexOf(d);
                selectedwing = d;
            }
            selectedPoint.setSelected(false);
            selectedPoint = p;
            p.setSelected(true);
        } else {
            p.setSelected(false);
        }
    }
}