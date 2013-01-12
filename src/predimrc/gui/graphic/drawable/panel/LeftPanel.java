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
import predimrc.gui.graphic.popup.ConfigWingSection_PopUp;

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
                    if (selectedPoint.equals(((DrawableWing) selectedElement).getFrontPointLeftView())) {//moveXYZ
                    }

                    if (selectedPoint.equals(((DrawableWing) selectedElement).getBackPointLeftView())) {//change calage angulaire

                        try {
                            currentAngle = Float.parseFloat(ConfigWingSection_PopUp.MakePopup(ConfigWingSection_PopUp.TYPE_MODIF.ANGLE, "" + ((DrawableWing) selectedElement).getAngle()));
                            applyAngle();
                        } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                            PredimRC.logln("Invalid angle value typed");
                        }
                    }
                    repaint();
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
                        currentAngle = 180 - Utils.calcAngle(((DrawableWing) selectedElement).getFrontPointLeftView(), e.getX(), e.getY());
                        applyAngle();
                    }


                }
            }
        });
        //    backgroundImage = PredimRC.getImage("pegleft.png");
        backgroundImage = PredimRC.getImage("left.png");
    }

    private void applyAngle() {
        currentAngle = currentAngle > 180 ? currentAngle - 360 : currentAngle;
        currentAngle = currentAngle > 20 ? 20 : currentAngle;
        currentAngle = currentAngle < -20 ? -20 : currentAngle;
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
        infoAction = " angle : " + currentAngle;
    }

    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(Utils.LEFT_SCREEN_X, Utils.LEFT_SCREEN_Y);
    }
}
