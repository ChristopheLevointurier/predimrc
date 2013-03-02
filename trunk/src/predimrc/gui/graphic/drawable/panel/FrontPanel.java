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
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.popup.SimplePopUp;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class FrontPanel extends DrawablePanel {

    private float currentDiedre;

    public FrontPanel() {
        super();
        view = VIEW_TYPE.FRONT_VIEW;
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getClickCount() == 2) || (SwingUtilities.isRightMouseButton(e))) {
                    try {
                        currentDiedre = Float.parseFloat(SimplePopUp.MakePopup("" + currentDiedre));
                        applyDiedre();
                    } catch (java.lang.NumberFormatException | NullPointerException exxx) {
                        PredimRC.logln("Invalid diedre value typed");
                    }
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selectedElement instanceof DrawableWingSection) {
                        int index = ((DrawableWingSection) selectedElement).getIndexInBelongsTo();
                        currentDiedre = Utils.calcAngle(((DrawableWing) selectedPoint.getBelongsTo().getBelongsTo()).getPreviousPointForDiedre(index), getXcur(e), getYcur(e));
                        applyDiedre();
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) //Pan
                {
                    if (startPanY == 0) {
                        startPanY = e.getX();
                    } else {
                        panY = oldPanY + e.getX() - startPanY;
                    }
                    if (startPanZ == 0) {
                        startPanZ = e.getY();
                    } else {
                        panZ = oldPanZ + e.getY() - startPanZ;
                    }
                    PredimRC.repaintDrawPanels();
                    getGraphics().drawLine((int) startPanY, (int) startPanZ, e.getX(), e.getY());
                }
            }
        });

        //     backgroundImage = PredimRC.getImage("front.png");
    }

    private void applyDiedre() {
        switch (selectedElement.getUsedFor()) {
            case HORIZONTAL_PLAN: {
                currentDiedre = currentDiedre > 60 ? 60 : currentDiedre;
                currentDiedre = currentDiedre < -60 ? -60 : currentDiedre;
                break;
            }
            case MAIN_WING: {
                currentDiedre = currentDiedre > 30 ? 30 : currentDiedre;
                currentDiedre = currentDiedre < -30 ? -30 : currentDiedre;
                break;
            }
            case VERTICAL_PLAN: {

                boolean negat = currentDiedre < 0;
                float absCurrentDiedre = Math.abs(currentDiedre);
                absCurrentDiedre = absCurrentDiedre > 110 ? 110 : absCurrentDiedre;
                absCurrentDiedre = absCurrentDiedre < 70 ? 70 : absCurrentDiedre;
                currentDiedre = negat ? -absCurrentDiedre : absCurrentDiedre;
                break;
            }
        }

        ((DrawableWingSection) selectedPoint.getBelongsTo()).setDihedral(currentDiedre);
        info.setDetailedInfo(" diedre : " + ((DrawableWingSection) selectedPoint.getBelongsTo()).getDihedral());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Utils.FRONT_SCREEN_X, Utils.FRONT_SCREEN_Y);
    }
}
