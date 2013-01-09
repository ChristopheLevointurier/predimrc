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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import predimrc.PredimRC;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.common.Utils;
import predimrc.common.Utils.USED_FOR;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class FrontPanel extends DrawablePanel {

    private ArrayList<DrawablePoint> points = new ArrayList<>();
    private DrawablePoint selected = new DrawablePoint(0, 0); //to avoid NPE
    //private DrawablePoint wingConnection = new DrawablePoint(380, 125);
    //  private DrawablePoint tailConnection = new DrawablePoint(400, 55);
    private USED_FOR usedFor;
    private float currentDiedre;

    public FrontPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //detect nearest point;
                getNearestPoint(e.getX(), e.getY());
                switch (usedFor) {
                    case MAIN_WING: {
                        currentDiedre = ((DrawableWingSection) selected.getBelongsTo()).getDiedre();
                        info = "Wing (" + ((DrawableWing) selected.getBelongsTo().getBelongsTo()).getIndexInBelongsTo() + ") section:" + selected.getBelongsTo().getIndexInBelongsTo();
                        break;
                    }
                    case HORIZONTAL_PLAN: {
                        currentDiedre = ((DrawableWing) selected.getBelongsTo().getBelongsTo()).get(0).getDiedre(); //all diedre are same in tail
                        info = "Tail (" + selected.getBelongsTo().getIndexInBelongsTo() + ") ";
                        break;
                    }
                    case VERTICAL_PLAN: { //should not come here
                        currentDiedre = 90;
                        info = "Derive";
                        break;
                    }
                }
                infoAction = " diedre:" + currentDiedre;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        currentDiedre = Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.DIEDRE, "" + currentDiedre));
                        applyDiedre();
                    } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                        PredimRC.logln("Invalid diedre value typed");
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

// change diedre
                switch (usedFor) {
                    case MAIN_WING: {
                        int index = ((DrawableWingSection) selected.getBelongsTo()).getIndexInBelongsTo();
                        currentDiedre = Utils.calcAngle(((DrawableWing) selected.getBelongsTo().getBelongsTo()).getPreviousPointForDiedre(index), e.getX(), e.getY());
                        break;
                    }
                    case HORIZONTAL_PLAN: {
                        currentDiedre = ((DrawableWing) selected.getBelongsTo()).get(0).getDiedre(); //all diedre are same in tail
                        currentDiedre = Utils.calcAngle(((DrawableWing) selected.getBelongsTo()).getPreviousPointForDiedre(0), e.getX(), e.getY());
                        break;
                    }
                    case VERTICAL_PLAN: { //TODO should not come here. See after for -90
                        currentDiedre = 90;
                        break;
                    }
                }
                ((AbstractDrawableWing) selected.getBelongsTo()).setDiedre(currentDiedre);
                applyDiedre();
            }
        });

        backgroundImage = PredimRC.getImage("front.png");
    }

    private void applyDiedre() {
        switch (usedFor) {
            case HORIZONTAL_PLAN: {
                currentDiedre = currentDiedre > 60 ? 60 : currentDiedre;
                currentDiedre = currentDiedre < -60 ? -60 : currentDiedre;
                ((DrawableWing) selected.getBelongsTo()).setDiedre(currentDiedre);
                infoAction = " diedre : " + currentDiedre;
                break;
            }
            case MAIN_WING: {
                currentDiedre = currentDiedre > 30 ? 30 : currentDiedre;
                currentDiedre = currentDiedre < -30 ? -30 : currentDiedre;
                ((DrawableWingSection) selected.getBelongsTo()).setDiedre(currentDiedre);
                //   movePoint(Utils.getCoordOnCircle(ref, currentDiedre, PredimRC.getInstance().getModel().getWings().get(indexWing).getLenght()));
                infoAction = " diedre : " + currentDiedre;
                break;
            }
            case VERTICAL_PLAN: {
                break;
            }
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
        g.drawString(info + infoAction, 10, 20);
        g.setColor(Color.GRAY.brighter());
        if (PredimRC.initDone) {
            PredimRC.getInstanceDrawableModel().drawFront((Graphics2D) g);
        }
        g.setColor(Color.GRAY);
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 125, 390, 125);
    }

    @Override
    public void updateModel(DrawableModel m) {
        PredimRC.logDebugln("changeModel in FrontPanel");
        points = PredimRC.getInstanceDrawableModel().getFrontPoints();
        repaint();
    }

    private double getNearestPoint(int x, int y) {
        double dist = Double.MAX_VALUE;
       // System.out.println("getNearestPoint:" + points.size());
        for (DrawablePoint p : points) {
            double temp = PredimRC.distance(p, x, y);
            if (p.isSelectable() && temp < dist) {
            //    System.out.println("selected:" + p.toStringAll());
                dist = temp;
                selected.setSelected(false);
                selected = p;
                selected.setSelected(true);
                usedFor = ((DrawableWing) (selected.getBelongsTo().getBelongsTo())).getUsed_for();
            }
        }
        return dist;
    }
}
