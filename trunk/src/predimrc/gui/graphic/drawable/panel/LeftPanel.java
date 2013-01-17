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
import predimrc.gui.graphic.drawable.model.DrawableFuselage;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;
import predimrc.gui.graphic.popup.ConfigWing_PopUp;

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
                if (e.getClickCount() == 2) {
                    if (selectedPoint.equals(selectedElement.getFrontPointLeftView())) {
                        if (selectedElement instanceof DrawableWing) {
                            //move Connection XYZ & wing struct 
                            ConfigWing_PopUp.MakePopup(selectedPoint.getDrawableBelongsTo());
                        }
                        if (selectedElement instanceof DrawableWingSection) {
                            try {
                                ConfigWing_PopUp.MakePopup(selectedPoint.getDrawableBelongsTo());
                            } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                                PredimRC.logln("Invalid value typed");
                            }
                        }
                    }


                    if (selectedPoint.equals(selectedElement.getBackPointLeftView())) {
                        switch (selectedElement.getUsedFor()) {
                            case HORIZONTAL_PLAN:
                            case MAIN_WING: //change calage angulaire
                            {
                                try {
                                    currentAngle = Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.ANGLE, "" + ((DrawableWing) selectedElement).getAngle()));
                                    applyAngle();
                                } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                                    PredimRC.logln("Invalid angle value typed");
                                }
                                break;
                            }
                            case VERTICAL_PLAN: {
                                try {
                                    selectedElement.setWidth(Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH, "" + (selectedElement.getWidth()))));
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
                if (selectedPoint.equals(selectedElement.getFrontPointLeftView())) {
                    if (selectedElement instanceof DrawableWing || selectedElement instanceof DrawableFuselage) {
                        //move Connection
                        selectedElement.setPos(e.getX(), selectedElement.getyPos(), e.getY());
                        info.setDetailedInfo(" moved to : " + selectedElement.getPositionDimension3D());

                    }
                    if (selectedElement instanceof DrawableWingSection) {
                        //change  length & fleche
                        float newlenght = (float) Utils.distance(((DrawableWingSection) selectedElement).getPreviousFrontPointLeftView(), new DrawablePoint(e.getX(), e.getY()));
                        float newFleche = (float) (((DrawableWingSection) selectedElement).getPreviousFrontPointLeftView().getX() - e.getX());

                        ((DrawableWingSection) selectedElement).setFleche(newFleche);
                        if (newlenght > 1) {
                            ((DrawableWingSection) selectedElement).setLenght(newlenght);
                        }
                        info.setDetailedInfo(" Lenght=" + newlenght + ", Fleche=" + (e.getY() - ((DrawableWingSection) selectedElement).getPreviousFrontPointTopView().getFloatY()));
                    }

                }



                if (selectedPoint.equals(selectedElement.getBackPointLeftView())) {
                    switch (selectedElement.getUsedFor()) {
                        case HORIZONTAL_PLAN:
                        case MAIN_WING: //change calage angulaire
                        {
                            currentAngle = 180 - Utils.calcAngle(selectedElement.getFrontPointLeftView(), e.getX(), e.getY());
                            applyAngle();
                            break;
                        }
                        case VERTICAL_PLAN: {
                            int newlenght = e.getX() - selectedElement.getFrontPointLeftView().getIntX();
                            if (newlenght > 1) {
                                selectedElement.setWidth(newlenght);
                                info.setDetailedInfo(" Width=" + newlenght);
                            }
                            break;
                        }

                    }
                    //    }

                }
            }
        });
        //    backgroundImage = PredimRC.getImage("pegleft.png");
        //   backgroundImage = PredimRC.getImage("left.png");
    }

    private void applyAngle() {
        currentAngle = currentAngle > 180 ? currentAngle - 360 : currentAngle;
        currentAngle = currentAngle > 45 ? 45 : currentAngle;
        currentAngle = currentAngle < -45 ? -45 : currentAngle;
        /**
         * switch (((DrawableWing) selectedElement).getUsedFor()) { case
         * HORIZONTAL_PLAN: { currentDiedre = currentDiedre > 60 ? 60 :
         * currentDiedre; currentDiedre = currentDiedre < -60 ? -60 :
         * currentDiedre; break; } case MAIN_WING: { currentDiedre =
         * currentDiedre > 30 ? 30 : currentDiedre; currentDiedre =
         * currentDiedre < -30 ? -30 : currentDiedre; break; } case
         * VERTICAL_PLAN: { return; } }*
         */
        ((DrawableWing) selectedPoint.getBelongsTo()).setAngle(currentAngle);
        info.setDetailedInfo(" angle : " + currentAngle);
    }

    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(Utils.LEFT_SCREEN_X, Utils.LEFT_SCREEN_Y);
    }
}
