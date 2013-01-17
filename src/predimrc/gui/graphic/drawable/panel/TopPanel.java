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

                    if (selectedElement instanceof AbstractDrawableWing) {
                        try {
                            if (selectedPoint.equals(selectedPoint.getBelongsTo().getFrontPointTopView())) {//fleche length and pos
                                ConfigWing_PopUp.MakePopup(selectedPoint.getDrawableBelongsTo());
                            }
                            if (selectedPoint.equals(selectedPoint.getBelongsTo().getBackPointTopView())) {//resize width 
                                selectedElement.setWidth(Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.WIDTH, "" + (selectedElement.getWidth()))));
                            }
                        } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                            PredimRC.logln("Invalid value typed");
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedElement instanceof DrawableWing || selectedElement instanceof DrawableFuselage) {
                    //move wingConnection
                    if (selectedPoint.equals(selectedElement.getFrontPointTopView())) {
                        float xpos = e.getX() > Utils.TOP_SCREEN_X / 2 ? Utils.TOP_SCREEN_X / 2 : e.getX();
                        xpos = selectedElement instanceof DrawableFuselage ? selectedElement.getyPos() : xpos;
                        selectedElement.setPos(e.getY(), xpos, selectedElement.getzPos());
                        info.setDetailedInfo(" moved to : " + selectedElement.getPositionDimension3D());
                    }
                    //resize width
                    if (selectedPoint.equals(selectedElement.getBackPointTopView())) {
                        int newlenght = e.getY() - selectedElement.getFrontPointTopView().getIntY();
                        if (newlenght > 1) {
                            selectedElement.setWidth(newlenght);
                            info.setDetailedInfo(" Width=" + newlenght);
                        }
                    }
                }
                if (selectedElement instanceof DrawableWingSection) {
                    //change  length & fleche
                    if (selectedPoint.equals(((DrawableWingSection) selectedElement).getFrontPointTopView())) {
                        float newlenght = (float) Utils.distance(((DrawableWingSection) selectedElement).getPreviousFrontPointTopView(), new DrawablePoint(e.getX(), e.getY()));
                        float newFleche = (float) (((DrawableWingSection) selectedElement).getPreviousFrontPointTopView().getY() - e.getY());
                        ((DrawableWingSection) selectedElement).setFleche(newFleche);
                        if (newlenght > 1) {
                            ((DrawableWingSection) selectedElement).setLenght(newlenght);
                        }
                        info.setDetailedInfo(" Lenght=" + newlenght + ", Fleche=" + (e.getY() - ((DrawableWingSection) selectedElement).getPreviousFrontPointTopView().getFloatY()));
                    }
                    if (selectedPoint.equals(selectedElement.getBackPointTopView())) {
                        int newlenght = e.getY() - selectedElement.getFrontPointTopView().getIntY();
                        if (newlenght > 1) {
                            selectedElement.setWidth(newlenght);
                            info.setDetailedInfo(" Width=" + newlenght);
                        }
                    }

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