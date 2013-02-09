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
import predimrc.gui.graphic.drawable.model.DrawableFuselage;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 30 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigFuselage_PopUp extends ConfigPopUp {

    private DrawableFuselage drawableBelongsTo;
    private MegaLabel fileLabel = new MegaLabel("Filename:", true);
    private MegaLabel widthLabel = new MegaLabel("Width:", true);
    private MegaLabel margeLabel = new MegaLabel("margeStatiques:", true);

    public ConfigFuselage_PopUp(DrawableModelElement _drawableBelongsTo, TYPE_MODIF _usedfor) {
        super("WingSection Configuration", _usedfor);
        drawableBelongsTo = (DrawableFuselage) _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up for " + drawableBelongsTo + " usedFor:" + usedFor);

        switch (usedFor) {
            case FRONT_POINT: {
                fileLabel.setValue("" + drawableBelongsTo.getFilename());
                Dimension3D pos = Utils.getRefPos(drawableBelongsTo.getPositionDimension3D());
                xposLabel.setValue("" + pos.getX());
                yposLabel.setValue("" + pos.getY());
                zposLabel.setValue("" + pos.getZ());
                margeLabel.setValue("" + drawableBelongsTo.getBelongsTo().getMargeStatiqueDeCentrage());


                widgets.add(makePanelPos());
                widgets.add(fileLabel);
                widgets.add(margeLabel);

                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        drawableBelongsTo.setFilename(fileLabel.getValue());
                        drawableBelongsTo.setPosXYZ(Utils.getWorldPos(new Dimension3D(xposLabel.getFloatValue(), yposLabel.getFloatValue(), zposLabel.getFloatValue())), true);
                        drawableBelongsTo.getBelongsTo().setMargeStatiqueDeCentrage(margeLabel.getFloatValue());
                        ModelController.applyChange();
                        dispose();
                    }
                });
                break;
            }
            case BACK_POINT: {
                widthLabel.setValue("" + drawableBelongsTo.getWidth());
                widgets.add(widthLabel);
                okBut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        drawableBelongsTo.setWidth(widthLabel.getFloatValue());
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