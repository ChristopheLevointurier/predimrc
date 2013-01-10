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
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class LeftPanel extends DrawablePanel {

    public LeftPanel() {
        view = Utils.VIEW_TYPE.LEFT_VIEW;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(getPreferredSize());
         addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                  /**  try {
                        currentDiedre = Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.DIEDRE, "" + currentDiedre));
                        applyDiedre();
                    } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                        PredimRC.logln("Invalid diedre value typed");
                    }
                    repaint();**/
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedElement instanceof DrawableWing) {
                    //move wingConnection
                    if (selectedPoint.equals(((DrawableWing) selectedElement).getFrontPointLeftView())) {
                        selectedElement.setPos(e.getX(), selectedElement.getyPos(), e.getY());
                        infoAction = " moved to : " + selectedElement.getPositionDimension3D();
                    }
                    //resize angle
                    if (selectedPoint.equals(((DrawableWing) selectedElement).getBackPointLeftView())) {
                     //   int newlenght = e.getY() - ((DrawableWing) selectedElement).getFrontPointTopView().getIntY();
                     //   if (newlenght > 1) {
                     //       ((DrawableWing) selectedElement).setWidth(newlenght);
                     //       infoAction = " Width=" + newlenght;
                     //   }
                    }
                }
            }
        });
        //    backgroundImage = PredimRC.getImage("pegleft.png");
        backgroundImage = PredimRC.getImage("left.png");
    }

    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(Utils.LEFT_SCREEN_X, Utils.LEFT_SCREEN_Y);
    }
}
