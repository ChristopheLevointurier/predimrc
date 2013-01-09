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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawableWing;
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
    //   private boolean onTail = false;
    /**
     * field used while interact with gui
     */
    //   private int indexWing = -1;
    /**
     * Constructor
     */
    public TopPanel() {
        view = Utils.VIEW_TYPE.TOP_VIEW;
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (selectedPoint.equals(((AbstractDrawableWing) selectedPoint.getBelongsTo()).getFrontPointTopView())) {//fleche length and pos
                        ConfigWing_PopUp.MakePopup(selectedPoint.getDrawableBelongsTo());
                    }
                    if (selectedPoint.equals(((AbstractDrawableWing) selectedPoint.getBelongsTo()).getBackPointTopView())) {//resize width 
                        ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH, "" + (((AbstractDrawableWing) selectedPoint.getBelongsTo()).getBackPointTopView().getFloatY() - ((AbstractDrawableWing) selectedPoint.getBelongsTo()).getFrontPointTopView().getFloatY()));
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedElement instanceof DrawableWing) {
                 //   System.out.println("" + selectedElement);
                    if (selectedPoint.equals(((DrawableWing) selectedElement).getFrontPointTopView())) {
                        //move wingConnection
                        int xpos = e.getX() > MID_TOP_SCREEN_X ? MID_TOP_SCREEN_X : e.getX();
                        selectedElement.setPos(e.getY(), xpos, selectedElement.getzPos());
                        infoAction = " moved to : (" + selectedElement.getPositionDimension3D();
                    }
                }



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
        return new Dimension(930, 400);
    }
}