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
import predimrc.common.Utils.VIEW_TYPE;
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
public class FrontPanel extends DrawablePanel {

    private float currentDiedre;

    public FrontPanel() {
        super();
        view = VIEW_TYPE.FRONT_VIEW;
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
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
            @Override
            public void mouseDragged(MouseEvent e) {
                // change diedre
                switch (((AbstractDrawableWing) selectedElement).getUsedFor()) {
                    case MAIN_WING: {
                        int index = ((DrawableWingSection) selectedPoint.getBelongsTo()).getIndexInBelongsTo();
                        currentDiedre = Utils.calcAngle(((DrawableWing) selectedPoint.getBelongsTo().getBelongsTo()).getPreviousPointForDiedre(index), e.getX(), e.getY());
                        break;
                    }
                    case HORIZONTAL_PLAN: {
                        currentDiedre = ((DrawableWing) selectedPoint.getBelongsTo()).get(0).getDiedre(); //all diedre are same in tail
                        currentDiedre = Utils.calcAngle(((DrawableWing) selectedPoint.getBelongsTo()).getPreviousPointForDiedre(0), e.getX(), e.getY());
                        break;
                    }
                    case VERTICAL_PLAN: { //TODO should not come here. See after for -90
                        currentDiedre = 90;
                        break;
                    }
                }
                ((AbstractDrawableWing) selectedPoint.getBelongsTo()).setDiedre(currentDiedre);
                applyDiedre();
            }
        });

        backgroundImage = PredimRC.getImage("front.png");
    }

    private void applyDiedre() {
        switch (((AbstractDrawableWing) selectedElement).getUsedFor()) {
            case HORIZONTAL_PLAN: {
                currentDiedre = currentDiedre > 60 ? 60 : currentDiedre;
                currentDiedre = currentDiedre < -60 ? -60 : currentDiedre;
                ((DrawableWing) selectedPoint.getBelongsTo()).setDiedre(currentDiedre);
                infoAction = " diedre : " + currentDiedre;
                break;
            }
            case MAIN_WING: {
                currentDiedre = currentDiedre > 30 ? 30 : currentDiedre;
                currentDiedre = currentDiedre < -30 ? -30 : currentDiedre;
                ((DrawableWingSection) selectedPoint.getBelongsTo()).setDiedre(currentDiedre);
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
        return new Dimension(Utils.FRONT_SCREEN_X, Utils.FRONT_SCREEN_Y);
    }
}
