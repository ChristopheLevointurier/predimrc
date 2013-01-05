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

import predimrc.gui.graphic.drawable.model.OldDrawableWingPart;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
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
import predimrc.gui.graphic.drawable.Utils;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.model.Model;
import predimrc.model.element.Wing;
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
    private ArrayList<OldDrawableWingPart> wingParts = new ArrayList<>();
    //  private ArrayList<DrawablePoint> wingPoints2 = new ArrayList<>();
    //   private ArrayList<DrawablePoint> tailPoints = new ArrayList<>();
    //  private ArrayList<DrawablePoint> tailPoints2 = new ArrayList<>();
    private DrawablePoint wingConnection = new DrawablePoint(defaultWingConnection);
    private DrawablePoint tailConnection = new DrawablePoint(defaultTailConnection);
    private DrawablePoint selectedPoint = new DrawablePoint(0, 0);
    public static final Dimension defaultWingConnection = new Dimension(385, 125);
    public static final Dimension defaultTailConnection = new Dimension(MID_SCREEN_X, 350);
    private OldDrawableWingPart selectedwing;
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
                    info += selectedwing.isOntail() ? (indexWing - PredimRC.getInstanceModel().getWings().get(0).getSize() + 1) : (indexWing + 1);
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
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH1, "" + (selectedwing.getPreviousBackPoint().getFloatY() - selectedwing.getPreviousFrontPoint().getFloatY()));
                    }
                    if (selectedPoint.equals(selectedwing.getBackPoint())) {//resize width2
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH2, "" + (selectedwing.getBackPoint().getFloatY() - selectedwing.getFrontPoint().getFloatY()));
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

                    if (selectedPoint.equals(selectedwing.getFrontPoint())) {//resize length and fleche
                        int newlenght = selectedwing.getPreviousFrontPoint().getIntX() - e.getX();
                        float newFleche = Utils.calcAngle(selectedwing.getPreviousFrontPoint(), new DrawablePoint(e.getX(), e.getY()));
                        selectedwing.getSection().setFleche(newFleche);
                        if (newlenght > 1) {
                            selectedwing.getSection().setLenght(newlenght);
                        }
                        infoDetail = " Lenght=" + newlenght + ", Fleche=" + (e.getY() - selectedwing.getPreviousFrontPoint().getFloatY());

                    }
                    if (selectedPoint.equals(selectedwing.getBackPoint())) {//resize width2
                        int newlenght = e.getY() - selectedwing.getFrontPoint().getIntY();
                        if (newlenght > 1) {
                            selectedwing.getSection().setWidth_2(newlenght);
                            infoDetail = " Width2=" + newlenght;
                        }
                    }

                    if (selectedPoint.equals(selectedwing.getPreviousBackPoint())) {//resize width1
                        int newlenght = e.getY() - selectedwing.getPreviousFrontPoint().getIntY();
                        if (newlenght > 1) {
                            selectedwing.getSection().setWidth_1(newlenght);
                            infoDetail = " Width1=" + newlenght;
                        }
                    }

                } else {
                    if (wingConnection.isSelected()) {
                        int xpos = e.getX() > MID_SCREEN_X ? MID_SCREEN_X : e.getX();
                        wingConnection.setFloatLocation(xpos, e.getY());
                        PredimRC.getInstanceModel().getWings().get(0).setPosXY(xpos, e.getY());
                        info = "wingConnection: (" + e.getY() + "," + xpos + ")";
                        infoDetail = "";
                    }
                    if (tailConnection.isSelected()) {
                        System.out.println(" poifpsoijf psoidjf sopidfj sopidfjsopqij fspodij ");
                        int xpos = e.getX() > MID_SCREEN_X ? MID_SCREEN_X : e.getX();
                        tailConnection.setFloatLocation(xpos, e.getY());
                        PredimRC.getInstanceModel().getTail().get(0).setPosXY(xpos, e.getY());
                        info = "tailConnection: (" + e.getY() + "," + xpos + ")";
                        infoDetail = "";
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
        System.out.println("--");
        for (OldDrawableWingPart p : wingParts) {
            // System.out.println("drawTop of :"+p);
            p.drawTop((Graphics2D) g);
            System.out.println(p);
        }
        g.setColor(Color.GRAY);
    }

    @Override
    public void changeModel(Model m) {

        System.out.println("call change model");
        wingParts = new ArrayList<>();
        OldDrawableWingPart previous = OldDrawableWingPart.makeRoot(wingConnection, m.getWings().get(0).get(0)); //root of the wing
        wingConnection.setFloatLocation(m.getWings().get(0).getxPos(), m.getWings().get(0).getyPos());
        for (WingSection w : m.getWings().get(0)) {
            OldDrawableWingPart d = new OldDrawableWingPart(w, previous, false);
            wingParts.add(d);
            previous = d;
        }

        if (m.getTail().get(0).getSize() > 0) {
            previous = OldDrawableWingPart.makeRoot(tailConnection, m.getTail().get(0).get(0));
            tailConnection.setFloatLocation(m.getTail().get(0).getxPos(), m.getTail().get(0).getyPos());
            for (WingSection w : m.getTail().get(0)) {
                OldDrawableWingPart d = new OldDrawableWingPart(w, previous, true);
                wingParts.add(d);
                previous = d;
            }
        }
    }

    private void getNearestPoint(int x, int y) {
        dist = Integer.MAX_VALUE;

        checkDist(null, wingConnection, x, y);
        checkDist(wingParts.get(0), wingParts.get(0).getPreviousBackPoint(), x, y);
        if (PredimRC.getInstanceModel().getTail().get(0).getSize() > 0) {//there is tail wings
            checkDist(null, tailConnection, x, y);
            checkDist(wingParts.get(PredimRC.getInstanceModel().getWings().get(0).getSize()), wingParts.get(PredimRC.getInstanceModel().getWings().get(0).getSize()).getPreviousBackPoint(), x, y);
        }
        for (OldDrawableWingPart d : wingParts) {
            checkDist(d, d.getFrontPoint(), x, y);
            checkDist(d, d.getBackPoint(), x, y);
        }
    }

    private void checkDist(OldDrawableWingPart d, DrawablePoint p, int x, int y) {
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