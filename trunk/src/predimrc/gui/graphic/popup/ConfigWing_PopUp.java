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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawableWing;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
import predimrc.gui.graphic.drawable.model.abstractClasses.AbstractDrawableWing;
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
public class ConfigWing_PopUp extends JFrame {

    private DrawableModelElement drawableBelongsTo;
    private MegaCombo nbrCombo = new MegaCombo("Number of section:", true, "1", "2", "3", "4", "5");
    private MegaLabel xposLabel = new MegaLabel("Xpos:", true);
    private MegaLabel yposLabel = new MegaLabel("Ypos:", true);
    private MegaLabel zposLabel = new MegaLabel("Zpos:", true);
    private MegaLabel angleLabel = new MegaLabel("Angle:", true);
    private MegaLabel flecheLabel = new MegaLabel("Fleche:", true);
    private MegaLabel lengthLabel = new MegaLabel("Length:", true);
    private JButton okBut = new JButton("Ok");
    private JButton cancelBut = new JButton("Cancel");

    public static ConfigWing_PopUp MakePopup(DrawableModelElement _drawableBelongsTo) {
        return new ConfigWing_PopUp(_drawableBelongsTo);
    }

    private ConfigWing_PopUp(DrawableModelElement _drawableBelongsTo) {
        super("Structure and position Configuration");
        drawableBelongsTo = _drawableBelongsTo;
        predimrc.PredimRC.logln("Pop up for " + drawableBelongsTo);
        JPanel widgets = new JPanel();
        widgets.setLayout(new BoxLayout(widgets, BoxLayout.Y_AXIS));
        angleLabel.setValue("" + ((AbstractDrawableWing) drawableBelongsTo).getAngle());

        if (drawableBelongsTo instanceof DrawableWing) {//config wing 
            JPanel pos = new JPanel();
            pos.setLayout(new BoxLayout(pos, BoxLayout.Y_AXIS));
            pos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Position:"));
            xposLabel.setValue("" + drawableBelongsTo.getxPos());
            yposLabel.setValue("" + drawableBelongsTo.getyPos());
            zposLabel.setValue("" + drawableBelongsTo.getzPos());
            nbrCombo.setSelectedValue(((DrawableWing) drawableBelongsTo).getSize(), false);
            pos.add(xposLabel);
            pos.add(yposLabel);
            pos.add(zposLabel);

            widgets.add(nbrCombo);
            widgets.add(pos);


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
                    drawableBelongsTo.setPos(xposLabel.getFloatValue(), yposLabel.getFloatValue(), zposLabel.getFloatValue());
                    ((DrawableWing) drawableBelongsTo).setAngle(angleLabel.getFloatValue());
                    ModelController.applyChange();
                    dispose();
                }
            });



        } else // config WingSection
        {
            flecheLabel.setValue("" + ((DrawableWingSection) drawableBelongsTo).getFleche());
            lengthLabel.setValue("" + +((DrawableWingSection) drawableBelongsTo).getLenght());


            widgets.add(flecheLabel);
            widgets.add(lengthLabel);

            okBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((DrawableWingSection) drawableBelongsTo).setFleche(flecheLabel.getFloatValue());
                    ((DrawableWingSection) drawableBelongsTo).setLenght(lengthLabel.getFloatValue());
                    ((DrawableWingSection) drawableBelongsTo).setAngle(angleLabel.getFloatValue());
                    ModelController.applyChange();
                    dispose();
                }
            });

        }



        cancelBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.logln("Action cancelled by user.");
                dispose();
            }
        });


        JPanel but = new JPanel();
        but.setLayout(new BoxLayout(but, BoxLayout.X_AXIS));
        but.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder()));
        but.add(okBut);
        but.add(cancelBut);
        widgets.add(angleLabel);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(widgets);
        getContentPane().add(but);



        pack();
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(predimrc.PredimRC.icon);
        setVisible(true);


    }
}
