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
package predimrc.gui.graphic.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.controller.IModelListener;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.VlmStab_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 2 déc. 2012
 * @version
 * @see
 * @since
 */
public final class ConfigView extends JPanel implements IModelListener {

    private static DecimalFormat df = new DecimalFormat();
    private JPanel model = new JPanel();
    private JPanel mainWing = new JPanel();
    private JPanel tail = new JPanel();
    private JPanel mainWingButtons = new JPanel();
     /**
     * Buttons
     */
    private JToggleButton vrillageBut = new JToggleButton("vrillage");
    /**
     * Labels for wing data
     */
    private MegaLabel wingspan_label = makeLabel("wing span");
    private MegaLabel wingarea_label = makeLabel("wing area");
    private MegaLabel airFoil_label = makeLabel("airFoil");
    private MegaLabel wingratio_label = makeLabel("aspect ratio");
    private MegaLabel wingcorde_label = makeLabel("mean chord");
    private MegaLabel wingAlpha0_label = makeLabel("Alpha0");
    private MegaLabel wingCm0_label = makeLabel("Cm0");
    /**
     * Labels for tail data
     */
    private MegaLabel stabspan_label = makeLabel("stab span");
    private MegaLabel stabarea_label = makeLabel("stab area");
    private MegaLabel stabFoil_label = makeLabel("stab airFoil");
    private MegaLabel stabratio_label = makeLabel("stab aspect ratio");
    private MegaLabel stabcorde_label = makeLabel("stab mean chord");
    private MegaLabel stablevier_label = makeLabel("stab lever");
    private MegaLabel vstab_label = makeLabel("vstab ");
    private MegaLabel stabRecommandedForCz_label = makeLabel("Stab ");
    /**
     * Labels for model data
     */
    private MegaLabel wingCzCalage_label = makeLabel("Cz adjustment");
    private MegaLabel staticMargin_label = makeLabel("Static margin(%)");
    private MegaLabel wingRecommandedForCz_label = makeLabel("Wing ");
    private MegaLabel stabCz_label = makeLabel("Stab Cz");

    /**
     * widgets for structure config
     */
    public ConfigView() {
        super();

        df.setMaximumFractionDigits(2);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        model.setLayout(new BoxLayout(model, BoxLayout.Y_AXIS));
        model.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Model specs:"));
        model.add(staticMargin_label);
        model.add(wingCzCalage_label);

        JPanel cz = new JPanel();
        cz.setLayout(new BoxLayout(cz, BoxLayout.Y_AXIS));
        cz.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "AngleOfAttack for Cz:"));
        cz.add(wingRecommandedForCz_label);
        cz.add(stabRecommandedForCz_label);
        model.add(cz);
        add(model);




        mainWing.setLayout(new BoxLayout(mainWing, BoxLayout.Y_AXIS));
        mainWingButtons.setLayout(new BoxLayout(mainWingButtons, BoxLayout.X_AXIS));
        mainWing.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Main wing specs:"));
        mainWing.add(wingspan_label);
        mainWing.add(wingarea_label);
        mainWing.add(airFoil_label);
        mainWing.add(wingAlpha0_label);
        mainWing.add(wingCm0_label);

        mainWing.add(wingratio_label);
        mainWing.add(wingcorde_label);
         mainWing.add(mainWingButtons);
        add(mainWing);


        tail.setLayout(new BoxLayout(tail, BoxLayout.Y_AXIS));
        tail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Horizontal tail specs:"));
        tail.add(stabspan_label);
        tail.add(stabarea_label);
        tail.add(stabFoil_label);
        tail.add(stabratio_label);
        tail.add(stabcorde_label);
        tail.add(stablevier_label);
        tail.add(vstab_label);
        tail.add(stabCz_label);
        add(tail);

    }

    private MegaLabel makeLabel(String str) {
        return new MegaLabel(str, PredimRC.defaultLabelContent);
    }

    public MegaLabel getWingspan_label() {
        return wingspan_label;
    }

    public MegaLabel getWingarea_label() {
        return wingarea_label;
    }

    public MegaLabel getAirFoil_label() {
        return airFoil_label;
    }

    public MegaLabel getWingratio_label() {
        return wingratio_label;
    }

    public MegaLabel getWingcorde_label() {
        return wingcorde_label;
    }

    public MegaLabel getWingAlpha0_label() {
        return wingAlpha0_label;
    }

    public MegaLabel getWingCm0_label() {
        return wingCm0_label;
    }

    public MegaLabel getWingCzCalage_label() {
        return wingCzCalage_label;
    }

    public MegaLabel getStaticMargin_label() {
        return staticMargin_label;
    }

    public MegaLabel getWingRecommandedForCz_label() {
        return wingRecommandedForCz_label;
    }

    public MegaLabel getStabRecommandedForCz_label() {
        return stabRecommandedForCz_label;
    }

    public MegaLabel getStabCz_label() {
        return stabCz_label;
    }

    public MegaLabel getStabspan_label() {
        return stabspan_label;
    }

    public MegaLabel getStabarea_label() {
        return stabarea_label;
    }

    public MegaLabel getStabFoil_label() {
        return stabFoil_label;
    }

    public MegaLabel getStabratio_label() {
        return stabratio_label;
    }

    public MegaLabel getStabcorde_label() {
        return stabcorde_label;
    }

    @Override
    public void updateModel(DrawableModel m) {

        wingspan_label.setValue("" + Utils.round(m.getWings().get(0).getSpan()));
        wingarea_label.setValue("" + Utils.round(m.getWings().get(0).getArea()));
        airFoil_label.setValue("" + m.getWings().get(0).getFilename());
        wingratio_label.setValue("" + Utils.round(m.getWings().get(0).getAspectRatio()));
        wingcorde_label.setValue("" + Utils.round(m.getWings().get(0).getMeanCord()));
        wingCzCalage_label.setValue("" + Utils.round(m.getCzAdjustment()));
        staticMargin_label.setValue("" + Utils.round(m.getStaticMarginRatio() * 100));
        wingRecommandedForCz_label.setValue("" + Utils.round(m.getAlphaWing()));
        stabRecommandedForCz_label.setValue("" + Utils.round(m.getAlphaStab()));
        wingAlpha0_label.setValue("" + Utils.round(m.getAlpha0a()));
        wingCm0_label.setValue("" + Utils.round(m.getCm0()));
        stabCz_label.setValue("" + Utils.round(m.getCzAStab()));
        stabspan_label.setValue("" + Utils.round(m.getTail().get(0).getSpan()));
        stabarea_label.setValue("" + Utils.round(m.getTail().get(0).getArea()));
        stabFoil_label.setValue("" + m.getTail().get(0).getFilename());
        stabratio_label.setValue("" + Utils.round(m.getTail().get(0).getAspectRatio()));
        stabcorde_label.setValue("" + Utils.round(m.getTail().get(0).getMeanCord()));
        vstab_label.setValue("" + Utils.round(m.getvStab()));
        stablevier_label.setValue("" + Utils.round(m.getStabLever()));
    }
}
