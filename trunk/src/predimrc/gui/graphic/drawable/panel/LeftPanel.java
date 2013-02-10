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
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.popup.ConfigFuselage_PopUp;
import predimrc.gui.graphic.popup.ConfigPopUp;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.gui.graphic.popup.ConfigWing_PopUp;
import predimrc.gui.graphic.popup.SimplePopUp;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class LeftPanel extends DrawablePanel {

    private float currentAngle;

    public LeftPanel() {
        view = Utils.VIEW_TYPE.LEFT_VIEW;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(getPreferredSize());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getClickCount() == 2) || (SwingUtilities.isRightMouseButton(e))) {
                    if (selectedPoint.equals(selectedElement.getFrontPointLeftView())) {
                        if (selectedElement instanceof DrawableWing) {
                            new ConfigWing_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                        if (selectedElement instanceof DrawableWingSection) {
                            new ConfigWingSection_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                        if (selectedElement instanceof DrawableFuselage) {
                            new ConfigFuselage_PopUp(selectedElement, ConfigPopUp.TYPE_MODIF.FRONT_POINT);
                        }
                    }

                    if ((selectedElement instanceof DrawableFuselage) && (((DrawableFuselage) selectedElement).isWidthZPoint(selectedPoint))) {
                        try {
                            ((DrawableFuselage) selectedElement).setWidthZ(Float.parseFloat(SimplePopUp.MakePopup("" + (((DrawableFuselage) selectedElement).getWidthZ()))));
                        } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                            PredimRC.logln("Invalid value typed");
                        }

                    }
                    if (selectedPoint.equals(selectedElement.getBackPointLeftView())) {
                        switch (selectedElement.getUsedFor()) {
                            case HORIZONTAL_PLAN:
                            case MAIN_WING: //change calage angulaire
                            {
                                try {
                                    currentAngle = Float.parseFloat(SimplePopUp.MakePopup("" + ((DrawableWing) selectedElement).getAngle()));
                                    applyAngle();
                                } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                                    PredimRC.logln("Invalid angle value typed");
                                }
                                break;
                            }
                            case FUSELAGE:
                            case VERTICAL_PLAN: {
                                try {
                                    selectedElement.setWidth(Float.parseFloat(SimplePopUp.MakePopup("" + (selectedElement.getWidth()))));
                                } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                                    PredimRC.logln("Invalid value typed");
                                }
                                break;
                            }

                        }
                    }
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selectedPoint.equals(selectedElement.getFrontPointLeftView())) {
                        if (selectedElement instanceof DrawableWing || selectedElement instanceof DrawableFuselage) {
                            //move Connection
                            float zpos = selectedElement.equals(PredimRC.getInstanceDrawableModel().getWings().get(0)) ? selectedElement.getzPos() : getYcur(e);
                            selectedElement.setPos(getXcur(e), selectedElement.getyPos(), zpos);
                            info.setDetailedInfo(" moved to : " + Utils.getRefPos(selectedElement.getPositionDimension3D()));

                        }
                        if (selectedElement instanceof DrawableWingSection) {
                            //change  length & fleche
                            float newlenght = (float) Utils.distance(((DrawableWingSection) selectedElement).getPreviousFrontPointLeftView(), new DrawablePoint(getXcur(e), getYcur(e), Utils.VIEW_TYPE.LEFT_VIEW));
                            float newFleche = (float) (getXcur(e) - ((DrawableWingSection) selectedElement).getPreviousFrontPointLeftView().getX());

                            ((DrawableWingSection) selectedElement).setSweep(newFleche);
                            if (newlenght > 1) {
                                ((DrawableWingSection) selectedElement).setLenght(newlenght);
                            }
                            info.setDetailedInfo(" Lenght=" + ((DrawableWingSection) selectedElement).getLenght() + ", Fleche=" + ((DrawableWingSection) selectedElement).getSweep());
                        }
                    }



                    if (selectedPoint.equals(selectedElement.getBackPointLeftView())) {
                        switch (selectedElement.getUsedFor()) {
                            case HORIZONTAL_PLAN:
                            case MAIN_WING: //change calage angulaire
                            {
                                currentAngle = 180 - Utils.calcAngle(selectedElement.getFrontPointLeftView(), getXcur(e), getYcur(e));
                                applyAngle();
                                info.setDetailedInfo(" angle : " + ((DrawableWing) selectedPoint.getBelongsTo()).getAngle());
                                break;
                            }
                            case VERTICAL_PLAN:
                            case FUSELAGE: {
                                int newlenght = getXcur(e) - (int) selectedElement.getFrontPointLeftView().getX();
                                if (newlenght > 1) {
                                    selectedElement.setWidth(newlenght);
                                    info.setDetailedInfo(" Width=" + selectedElement.getWidth());
                                }
                                break;
                            }
                        }
                    }


                    if ((selectedElement instanceof DrawableFuselage) && (((DrawableFuselage) selectedElement).isWidthZPoint(selectedPoint))) {
                        float newlenght = (getYcur(e) - selectedElement.getFrontPointLeftView().getFloatY()) * 2;
                        if (newlenght > 1) {
                            ((DrawableFuselage) selectedElement).setWidthZ(newlenght);
                            info.setDetailedInfo(" Width Z=" + ((DrawableFuselage) selectedElement).getWidthZ());
                        }
                    }
                }

                if (SwingUtilities.isRightMouseButton(e)) //Pan
                {
                    if (startPanX == 0) {
                        startPanX = e.getX();
                    } else {
                        panX = oldPanX + e.getX() - startPanX;
                    }
                    if (startPanZ == 0) {
                        startPanZ = e.getY();
                    } else {
                        panZ = oldPanZ + e.getY() - startPanZ;
                    }
                    PredimRC.logDebugln("Panx=" + (oldPanX + panX) + " Pany=" + (oldPanY + panY) + " PanZ=" + (oldPanZ + panZ));
                    PredimRC.repaintDrawPanels();
                    getGraphics().drawLine(startPanX, startPanZ, e.getX(), e.getY());
                }
            }
        });
        //    backgroundImage = PredimRC.getImage("pegleft.png");
    }

    private void applyAngle() {
        currentAngle = currentAngle > 180 ? currentAngle - 360 : currentAngle;
        currentAngle = currentAngle > 10 ? 10 : currentAngle;
        currentAngle = currentAngle < -10 ? -10 : currentAngle;
        ((DrawableWing) selectedPoint.getBelongsTo()).setAngle(currentAngle);
    }

    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(Utils.LEFT_SCREEN_X, Utils.LEFT_SCREEN_Y);
    }
}
