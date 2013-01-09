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
package predimrc.gui.graphic.drawable.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import predimrc.PredimRC;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.gui.graphic.popup.ConfigWing_PopUp;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class TopPanel extends DrawablePanel {

    //  private ArrayList<DrawablePoint> wingPoints2 = new ArrayList<>();
    //   private ArrayList<DrawablePoint> tailPoints = new ArrayList<>();
    //  private ArrayList<DrawablePoint> tailPoints2 = new ArrayList<>();
    //   private Point2D.Float wingConnection = new Point2D.Float(Utils.defaultWingConnection.getY(), Utils.defaultWingConnection.getX());
    //  private Point2D.Float tailConnection = new Point2D.Float(Utils.defaultTailConnection.getY(), Utils.defaultTailConnection.getX());
    /**
     * *
     */
    private DrawablePoint selectedPoint = new DrawablePoint(MID_TOP_SCREEN_X, MID_TOP_SCREEN_Y);
    //   private boolean onTail = false;
    /**
     * field used while interact with gui
     */
    double dist = Integer.MAX_VALUE;
    //   private int indexWing = -1;
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
                /**
                 * if (null != selectedwing) { info = selectedwing.isOntail() ?
                 * "Tail" : "Wing"; info += " section:"; info +=
                 * selectedwing.isOntail() ? (indexWing -
                 * PredimRC.getInstanceDrawableModel().getWings().get(0).getSize()
                 * + 1) : (indexWing + 1); }*
                 */
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (selectedPoint.equals(((AbstractDrawableWing) selectedPoint.getBelongsTo()).getFrontPoint())) {
                        ConfigWing_PopUp.MakePopup(selectedPoint.getDrawableBelongsTo());
                    }
                    if (selectedPoint.equals(((AbstractDrawableWing) selectedPoint.getBelongsTo()).getBackPoint())) {//resize width 
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH, "" + (((AbstractDrawableWing) selectedPoint.getBelongsTo()).getBackPoint().getFloatY() - ((AbstractDrawableWing) selectedPoint.getBelongsTo()).getFrontPoint().getFloatY()));
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                //          System.out.println(e.getX() + ":" + e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                /**
                 * if (selectedPoint.equals(selectedwing.getFrontPoint()))
                 * {//resize length and fleche int newlenght =
                 * selectedwing.getPreviousFrontPoint().getIntX() - e.getX();
                 * float newFleche = Utils.calcAngle(new
                 * Point2D.Float(selectedwing.getPreviousFrontPoint().getFloatX(),
                 * selectedwing.getPreviousFrontPoint().getFloatY()), e.getX(),
                 * e.getY()); selectedwing.getSection().setFleche(newFleche); if
                 * (newlenght > 1) {
                 * selectedwing.getSection().setLenght(newlenght); } infoDetail
                 * = " Lenght=" + newlenght + ", Fleche=" + (e.getY() -
                 * selectedwing.getPreviousFrontPoint().getFloatY());
                 *
                 * }
                 * if (selectedPoint.equals(selectedwing.getBackPoint()))
                 * {//resize width2 int newlenght = e.getY() -
                 * selectedwing.getFrontPoint().getIntY(); if (newlenght > 1) {
                 * selectedwing.getSection().setWidth_2(newlenght); infoDetail =
                 * " Width2=" + newlenght; } }
                 *
                 * if
                 * (selectedPoint.equals(selectedwing.getPreviousBackPoint()))
                 * {//resize width1 int newlenght = e.getY() -
                 * selectedwing.getPreviousFrontPoint().getIntY(); if (newlenght
                 * > 1) { selectedwing.getSection().setWidth_1(newlenght);
                 * infoDetail = " Width1=" + newlenght; } }
                 *
                 * //move wing if (drawablePointwingConnection.isSelected()) {
                 * int xpos = e.getX() > MID_TOP_SCREEN_X ? MID_TOP_SCREEN_X :
                 * e.getX(); drawablePointwingConnection.setFloatLocation(xpos,
                 * e.getY());
                 * PredimRC.getInstanceDrawableModel().getWings().get(0).setPosXY(xpos,
                 * e.getY()); info = "wingConnection: (" + e.getY() + "," + xpos
                 * + ")"; infoDetail = ""; }
                 *
                 * //move tail if (drawablePointtailConnection.isSelected()) {
                 * System.out.println(" poifpsoijf psoidjf sopidfj sopidfjsopqij
                 * fspodij "); int xpos = e.getX() > MID_TOP_SCREEN_X ?
                 * MID_TOP_SCREEN_X : e.getX();
                 * drawablePointtailConnection.setFloatLocation(xpos, e.getY());
                 * PredimRC.getInstanceDrawableModel().getTail().get(0).setPosXY(xpos,
                 * e.getY()); info = "tailConnection: (" + e.getY() + "," + xpos
                 * + ")"; infoDetail = ""; }
                 *
                 */
            }
        });

        backgroundImage = PredimRC.getImage("top.png");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(950, 400);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        g.drawString(info + " " + infoDetail, 10, 20);
        System.out.println("-topPanel paintComponent-");
        PredimRC.getInstanceDrawableModel().drawTop((Graphics2D) g);
        g.setColor(Color.GRAY);
    }

    private void getNearestPoint(int x, int y) {
        dist = Integer.MAX_VALUE;
        for (DrawablePoint p : PredimRC.getInstanceDrawableModel().getFrontPoints()) {
            checkDist(p, x, y);
        }
    }

    private void checkDist(DrawablePoint p, int x, int y) {
        double temp = PredimRC.distance(p, x, y);
        if (temp < dist) {
            dist = temp;
            selectedPoint.setSelected(false);
            selectedPoint = p;
            p.setSelected(true);
        } else {
            p.setSelected(false);
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        PredimRC.logDebugln("changeModel in TopPanel");
        //TODO
    }
}