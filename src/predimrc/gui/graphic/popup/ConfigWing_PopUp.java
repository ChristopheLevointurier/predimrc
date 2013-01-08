/*This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.graphic.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.widget.MegaCombo;

/**
 * Pop up to modify a wing config: amount of section, positionXYZ
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigWing_PopUp {

    private DrawableModelElement drawableBelongsTo;
    private MegaCombo nbrCombo = new MegaCombo("Number of section:", true, "1", "2", "3", "4", "5");

    public static ConfigWing_PopUp MakePopup(DrawableModelElement _drawableBelongsTo) {
        return new ConfigWing_PopUp(_drawableBelongsTo);
    }

    private ConfigWing_PopUp(DrawableModelElement _drawableBelongsTo) {
        drawableBelongsTo = _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up " + drawableBelongsTo);

        //   setLayout(new BoxLayout(structure_panel, BoxLayout.Y_AXIS));
        //   add(nbrCombo);

        /**
         * *******---structure widgets----****
         */
        nbrCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //       PredimRC.getInstanceDrawableModel().getWings().get(0).setWingSectionNumber(Integer.parseInt(wingCombo.getValue()));
                //       ModelController.applyChange();
            }
        });


    }
}
