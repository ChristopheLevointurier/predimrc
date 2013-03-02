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
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 11 fev. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigModel_PopUp extends ConfigPopUp {

    private DrawableModel drawableBelongsTo;
    private MegaLabel margeLabel = new MegaLabel("Static Margin(%):", true);

    public ConfigModel_PopUp(DrawableModelElement _drawableBelongsTo, TYPE_MODIF _usedfor) {
        super("Model Configuration", _usedfor);
        drawableBelongsTo = (DrawableModel) _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up for " + drawableBelongsTo + " usedFor:" + usedFor);

        switch (usedFor) {
            case CG_POINT: {
                margeLabel.setValue("" + drawableBelongsTo.getBelongsTo().getStaticMarginRatio()*100);
                widgets.add(margeLabel);

                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        drawableBelongsTo.setStaticMarginRatio(margeLabel.getFloatValue()/100);
                        ModelController.applyChange();
                        dispose();
                    }
                });
                break;
            }
            default:
                break;

        }
        finish();
    }
}