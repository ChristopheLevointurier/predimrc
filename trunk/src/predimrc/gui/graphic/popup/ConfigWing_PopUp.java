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
import predimrc.common.Dimension3D;
import predimrc.common.Utils;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.widget.MegaCombo;
import predimrc.gui.widget.MegaLabel;

/**
 * Pop up to modify a wing config: amount of section, positionXYZ
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigWing_PopUp extends ConfigPopUp {

    private DrawableWing drawableBelongsTo;
    private MegaCombo nbrCombo = new MegaCombo("Number of section:", true, "1", "2", "3", "4", "5");
    private MegaLabel angleLabel = new MegaLabel("Angle:", true);
    private MegaLabel fileLabel = new MegaLabel("Filename:", true);
    private MegaLabel widthLabel = new MegaLabel("Width:", true);

    public ConfigWing_PopUp(DrawableModelElement _drawableBelongsTo, TYPE_MODIF _usedfor) {
        super("Wing Configuration", _usedfor);
        drawableBelongsTo = (DrawableWing) _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up for " + drawableBelongsTo + " usedFor:" + usedFor);

        switch (usedFor) {
            case FRONT_POINT: {
                nbrCombo.setSelectedValue(((DrawableWing) drawableBelongsTo).getSize(), false);
                fileLabel.setValue("" + drawableBelongsTo.getFilename());

                Dimension3D pos = Utils.getRefPos(drawableBelongsTo.getPositionDimension3D());
                xposLabel.setValue("" + pos.getX());
                yposLabel.setValue("" + pos.getY());
                zposLabel.setValue("" + pos.getZ());



                widgets.add(nbrCombo);
                widgets.add(makePanelPos());
                widgets.add(fileLabel);
                nbrCombo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((DrawableWing) drawableBelongsTo).setDrawableWingSectionNumber(Integer.parseInt(nbrCombo.getValue()));
                        ModelController.applyChange();
                    }
                });

                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((DrawableWing) drawableBelongsTo).setAngle(angleLabel.getFloatValue());
                        drawableBelongsTo.setFilename(fileLabel.getValue());
                        drawableBelongsTo.setPosXYZ(Utils.getWorldPos(new Dimension3D(xposLabel.getFloatValue(), yposLabel.getFloatValue(), zposLabel.getFloatValue())), true);
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
