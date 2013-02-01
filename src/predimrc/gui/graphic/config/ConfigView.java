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
import predimrc.controller.IModelListener;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.VlmStab_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.DrawableWingSection;
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
    private JPanel mainWing = new JPanel();
    private JPanel tail = new JPanel();
    private JPanel mainWingButtons = new JPanel();
    private JPanel fuseButtons = new JPanel();
    private JToggleButton vlmBut = new JToggleButton("VLM");
    private JToggleButton optimBut = new JToggleButton("Optim");
    private JToggleButton xFoilBut = new JToggleButton("xFoil");
    /**
     * Buttons
     */
    private JToggleButton vrillageBut = new JToggleButton("vrillage");
    private JToggleButton vlmStabBut = new JToggleButton("VLM");
    private JToggleButton engineBut = new JToggleButton("Engine");
    private JToggleButton rzBut = new JToggleButton("Rz cste");
    private JToggleButton vxBut = new JToggleButton("Vx cste");
    private JToggleButton compareBut = new JToggleButton("Compare Models");
    private JCheckBox fuseCheck = new JCheckBox("Fuse");
    /**
     * Labels for wing data
     */
    private MegaLabel wingspan_label = makeLabel("wing span");
    private MegaLabel wingarea_label = makeLabel("wing area");
    private MegaLabel airFoil_label = makeLabel("airFoil");
    private MegaLabel wingratio_label = makeLabel("aspect ratio");
    private MegaLabel wingcorde_label = makeLabel("mean cord");
    private MegaLabel wingDiedre_label = makeLabel("Diedre wing");
    private MegaLabel wingAlpha0_label = makeLabel("Alpha0");
    private MegaLabel wingCm0_label = makeLabel("Cm0");
    private MegaLabel wingCalage_label = makeLabel("Calage aile");
    private MegaLabel wingCzCalage_label = makeLabel("Cz Calage wing");
    private MegaLabel wingIncidence_label = makeLabel("Incidence wing");
    /**
     * Labels for tail data
     */
    private MegaLabel stabspan_label = makeLabel("stab span");
    private MegaLabel stabarea_label = makeLabel("stab area");
    private MegaLabel stabFoil_label = makeLabel("stab airFoil");
    private MegaLabel stabratio_label = makeLabel("stab aspect ratio");
    private MegaLabel stabcorde_label = makeLabel("stab mean cord");
    private MegaLabel stablevier_label = makeLabel("Bras de levier");
    private MegaLabel stabouverture_label = makeLabel("stab Ouverture");
    private MegaLabel vstab_label = makeLabel("vstab ");
    private MegaLabel tailDiedre_label = makeLabel("Diedre tail");

    /**
     * widgets for structure config
     */
    public ConfigView() {
        super();

        df.setMaximumFractionDigits(2);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));




        mainWing.setLayout(new BoxLayout(mainWing, BoxLayout.Y_AXIS));
        mainWingButtons.setLayout(new BoxLayout(mainWingButtons, BoxLayout.X_AXIS));
        mainWing.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Main wing specs:"));
        mainWing.add(wingspan_label);
        mainWing.add(wingarea_label);
        mainWing.add(airFoil_label);
        mainWing.add(wingratio_label);
        mainWing.add(wingcorde_label);
        mainWing.add(wingAlpha0_label);
        mainWing.add(wingCm0_label);
        mainWing.add(wingCalage_label);
        mainWing.add(wingCzCalage_label);
        mainWing.add(wingIncidence_label);
        mainWing.add(wingDiedre_label);
        mainWingButtons.add(vlmBut);
        mainWingButtons.add(optimBut);
        mainWingButtons.add(xFoilBut);
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
        tail.add(stabouverture_label);
        tail.add(vstab_label);
        tail.add(tailDiedre_label);
        tail.add(vlmStabBut);
        add(tail);



        fuseButtons.setLayout(new BoxLayout(fuseButtons, BoxLayout.X_AXIS));
        fuseButtons.add(engineBut);
        // fuseButtons.add(rzBut);
        //  fuseButtons.add(vxBut);
        fuseButtons.add(compareBut);
        add(fuseButtons);

        compareBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Compare_Frame(compareBut);
            }
        });


        engineBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Engine_Frame(engineBut);
            }
        });


        vlmBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Vlm_Frame(vlmBut);
            }
        });
        vlmStabBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VlmStab_Frame(vlmStabBut);
            }
        });


        optimBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Optim_Frame(optimBut);
            }
        });


        xFoilBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new XFoil_Frame(xFoilBut);
            }
        });


    }

    private MegaLabel makeLabel(String str) {
        return new MegaLabel(str, PredimRC.defaultLabelContent, false);
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

    public MegaLabel getWingCalage_label() {
        return wingCalage_label;
    }

    public MegaLabel getWingCzCalage_label() {
        return wingCzCalage_label;
    }

    public MegaLabel getWingIncidence_label() {
        return wingIncidence_label;
    }

    public MegaLabel getWingDiedre_label() {
        return wingDiedre_label;
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

    public MegaLabel getStabouverture_label() {
        return stabouverture_label;
    }

    @Override
    public void updateModel(DrawableModel m) {

        String diedre = "";
        for (DrawableWingSection w : m.getWings().get(0)) {
            diedre += "#" + df.format(w.getDiedre());
        }
        wingDiedre_label.setValue(diedre);

        diedre = "";
        for (DrawableWingSection w : m.getTail().get(0)) {
            diedre += "#" + df.format(w.getDiedre());
        }
        tailDiedre_label.setValue(diedre);

        wingspan_label.setValue("" + m.getWings().get(0).getSpan());
        wingarea_label.setValue("" + m.getWings().get(0).getArea());
        airFoil_label.setValue("" + m.getWings().get(0).getFilename());
        wingratio_label.setValue("" + m.getWings().get(0).getAspectRatio());
        wingcorde_label.setValue("" + m.getWings().get(0).getMeanCord());


        stabspan_label.setValue("" + m.getTail().get(0).getSpan());
        stabarea_label.setValue("" + m.getTail().get(0).getArea());
        stabFoil_label.setValue("" + m.getTail().get(0).getFilename());
        stabratio_label.setValue("" + m.getTail().get(0).getAspectRatio());
        stabcorde_label.setValue("" + m.getTail().get(0).getMeanCord());
    }
}
