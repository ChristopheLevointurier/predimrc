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
import javax.swing.SwingUtilities;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawableFuselage;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.popup.ConfigFuselage_PopUp;
import predimrc.gui.graphic.popup.ConfigPopUp;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.gui.graphic.popup.ConfigWing_PopUp;
import predimrc.gui.graphic.popup.SimplePopUp;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class TopPanel extends DrawablePanel {

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

                    if (selectedElement instanceof DrawableWing) {
                        if (selectedPoint.equals(selectedElement.getFrontPointTopView())) {// length and pos
                            new ConfigWing_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                        if (selectedPoint.equals(selectedPoint.getBelongsTo().getBackPointTopView())) {//resize width 
                            new ConfigWing_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.BACK_POINT);
                        }
                    }
                    if (selectedElement instanceof DrawableWingSection) {
                        if (selectedPoint.equals(selectedElement.getFrontPointTopView())) {//fleche length 
                            new ConfigWingSection_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                        if (selectedPoint.equals(selectedPoint.getBelongsTo().getBackPointTopView())) {//resize width 
                            new ConfigWingSection_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.BACK_POINT);
                        }
                    }

                    if (selectedElement instanceof DrawableFuselage) {
                        if (((DrawableFuselage) selectedElement).isWidthYPoint(selectedPoint)) {
                            try {
                                ((DrawableFuselage) selectedElement).setWidthY(Float.parseFloat(SimplePopUp.MakePopup("" + (((DrawableFuselage) selectedElement).getWidthY()))));
                            } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                                PredimRC.logln("Invalid value typed");
                            }

                        }
                        if (selectedPoint.equals(selectedElement.getFrontPointTopView())) { //pos
                            new ConfigFuselage_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                        if (selectedPoint.equals(selectedPoint.getBelongsTo().getBackPointTopView())) {//resize width 
                            new ConfigFuselage_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.BACK_POINT);
                        }

                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {

                    if (selectedElement instanceof DrawableWing || selectedElement instanceof DrawableFuselage) {
                        //move wingConnection
                        if (selectedPoint.equals(selectedElement.getFrontPointTopView())) {
                            float xpos = getXcur(e) > Utils.TOP_SCREEN_X / 2 ? Utils.TOP_SCREEN_X / 2 : getXcur(e);
                            xpos = selectedElement instanceof DrawableFuselage ? selectedElement.getyPos() : xpos;
                            selectedElement.setPos(getYcur(e), xpos, selectedElement.getzPos());
                            info.setDetailedInfo(" moved to : " + selectedElement.getPositionDimension3D());
                        }
                        //resize width
                        if (selectedPoint.equals(selectedElement.getBackPointTopView())) {
                            float newlenght = getYcur(e) - selectedElement.getFrontPointTopView().getFloatY();
                            if (newlenght > 1) {
                                selectedElement.setWidth(newlenght);
                                info.setDetailedInfo(" Width=" + newlenght);
                            }
                        }

                        if ((selectedElement instanceof DrawableFuselage) && (((DrawableFuselage) selectedElement).isWidthYPoint(selectedPoint))) {
                            float newlenght = (selectedElement.getFrontPointTopView().getFloatX() - getXcur(e)) * 2;
                            if (newlenght > 1) {
                                ((DrawableFuselage) selectedElement).setWidthY(newlenght);
                                info.setDetailedInfo(" Width Y=" + newlenght);
                            }
                        }


                    }
                    if (selectedElement instanceof DrawableWingSection) {
                        //change  length & fleche
                        if (selectedPoint.equals(((DrawableWingSection) selectedElement).getFrontPointTopView())) {
                            float newlenght = (float) Utils.distance(((DrawableWingSection) selectedElement).getPreviousFrontPointTopView(), new DrawablePoint(getXcur(e), getYcur(e), Utils.VIEW_TYPE.TOP_VIEW));
                            float newFleche = (float) (((DrawableWingSection) selectedElement).getPreviousFrontPointTopView().getY() - getYcur(e));
                            ((DrawableWingSection) selectedElement).setFleche(newFleche);
                            if (newlenght > 1) {
                                ((DrawableWingSection) selectedElement).setLenght(newlenght);
                            }
                            info.setDetailedInfo(" Lenght=" + newlenght + ", Fleche=" + (getYcur(e) - ((DrawableWingSection) selectedElement).getPreviousFrontPointTopView().getFloatY()));
                        }
                        if (selectedPoint.equals(selectedElement.getBackPointTopView())) {
                            float newlenght = getYcur(e) - selectedElement.getFrontPointTopView().getFloatY();
                            if (newlenght > 1) {
                                selectedElement.setWidth(newlenght);
                                info.setDetailedInfo(" Width=" + newlenght);
                            }
                        }

                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) //Pan
                {
                    if (startPanY == 0) {
                        startPanY = e.getX();
                    } else {
                        panY = oldPanY + e.getX() - startPanY;
                    }
                    if (startPanX == 0) {
                        startPanX = e.getY();
                    } else {
                        panX = oldPanX + e.getY() - startPanX;
                    }
                    PredimRC.logDebugln("Panx=" + (oldPanX + panX) + " Pany=" + (oldPanY + panY) + " PanZ=" + (oldPanZ + panZ));
                    PredimRC.repaintDrawPanels();
                    getGraphics().drawLine(startPanY, startPanX, e.getX(), e.getY());
                }
            }
        });
        //   backgroundImage = PredimRC.getImage("top.png");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Utils.TOP_SCREEN_X, Utils.TOP_SCREEN_Y);
    }
}