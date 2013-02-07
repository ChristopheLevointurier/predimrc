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
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 30 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigWingSection_PopUp extends ConfigPopUp {

    private DrawableWingSection drawableBelongsTo;
    private MegaLabel angleLabel = new MegaLabel("Angle:", true);
    private MegaLabel flecheLabel = new MegaLabel("Fleche:", true);
    private MegaLabel lengthLabel = new MegaLabel("Length:", true);
    private MegaLabel widthLabel = new MegaLabel("Width:", true);

    public ConfigWingSection_PopUp(DrawableModelElement _drawableBelongsTo, TYPE_MODIF _usedfor) {
        super("WingSection Configuration", _usedfor);
        drawableBelongsTo = (DrawableWingSection) _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up for " + drawableBelongsTo + " usedFor:" + usedFor);



        switch (usedFor) {
            case FRONT_POINT: {
                flecheLabel.setValue("" + drawableBelongsTo.getSweep());
                lengthLabel.setValue("" + drawableBelongsTo.getLenght());
                widgets.add(flecheLabel);
                widgets.add(lengthLabel);
                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        drawableBelongsTo.setSweep(flecheLabel.getFloatValue());
                        drawableBelongsTo.setLenght(lengthLabel.getFloatValue());
                        ModelController.applyChange();
                        dispose();
                    }
                });

                break;
            }
            case BACK_POINT: {
                angleLabel.setValue("" + drawableBelongsTo.getAngle());
                widthLabel.setValue("" + drawableBelongsTo.getWidth());
                widgets.add(angleLabel);
                widgets.add(widthLabel);
                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        drawableBelongsTo.setWidth(widthLabel.getFloatValue());
                        drawableBelongsTo.setAngle(angleLabel.getFloatValue());
                        ModelController.applyChange();
                        dispose();
                    }
                });
                break;
            }
        }
        finish();
    }
}