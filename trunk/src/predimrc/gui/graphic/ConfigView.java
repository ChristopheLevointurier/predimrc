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
package predimrc.gui.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import predimrc.PredimRC;
import predimrc.controller.IModelListener;
import predimrc.gui.widget.MegaCombo;
import predimrc.gui.widget.MegaLabel;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.VlmStab_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.model.Model;

/**
 *
 * @author Christophe Levointurier, 2 déc. 2012
 * @version
 * @see
 * @since
 */
public final class ConfigView extends JPanel implements IModelListener {

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
    private MegaLabel wingcorde_label = makeLabel("corde moyenne");
    private MegaLabel wingAlpha0_label = makeLabel("Alpha0");
    private MegaLabel wingCm0_label = makeLabel("Cm0");
    private MegaLabel wingCalage_label = makeLabel("Calage aile");
    private MegaLabel wingCzCalage_label = makeLabel("Cz Calage aile");
    private MegaLabel wingIncidence_label = makeLabel("Incidence aile");
    private MegaLabel wingDiedre_label = makeLabel("Diedre aile");
    /**
     * Labels for tail data
     */
    private MegaLabel stabspan_label = makeLabel("stab span");
    private MegaLabel stabarea_label = makeLabel("stab area");
    private MegaLabel stabFoil_label = makeLabel("stab airFoil");
    private MegaLabel stabratio_label = makeLabel("stab aspect ratio");
    private MegaLabel stabcorde_label = makeLabel("stab corde moyenne");
    private MegaLabel stablevier_label = makeLabel("Bras de levier");
    private MegaLabel stabouverture_label = makeLabel("stab Ouverture");
    private MegaLabel vstab_label = makeLabel("vstab ");
    /**
     * widgets for structure config
     */
    private JPanel structure_panel = new JPanel();
    private JCheckBox tailCheck = new JCheckBox("Tail", true);
    private MegaCombo wingCombo = new MegaCombo("Number of wing section:", true, "1", "2", "3", "4", "5");
    private MegaCombo tailCombo = new MegaCombo("Number of horizontal tail section:", true, "0", "1", "2", "3");

    public ConfigView() {
        super();



        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));




        structure_panel.setLayout(new BoxLayout(structure_panel, BoxLayout.Y_AXIS));
        structure_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Structure:"));
        structure_panel.add(tailCheck);
        structure_panel.add(wingCombo);
        structure_panel.add(tailCombo);
        add(structure_panel);

        
        mainWing.setLayout(new BoxLayout(mainWing, BoxLayout.Y_AXIS));
        mainWingButtons.setLayout(new BoxLayout(mainWingButtons, BoxLayout.X_AXIS));
        mainWing.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Main wing:"));
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
        tail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Stabilisateur:"));
        tail.add(stabspan_label);
        tail.add(stabarea_label);
        tail.add(stabFoil_label);
        tail.add(stabratio_label);
        tail.add(stabcorde_label);
        tail.add(stablevier_label);
        tail.add(stabouverture_label);
        tail.add(vstab_label);
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




        wingCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstance().getModel().setWingSectionNumber(Integer.parseInt(wingCombo.getValue()));
            }
        });


        tailCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstance().getModel().getTail().setExist(tailCheck.isSelected());
            }
        });

        tailCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstance().getModel().getTail().setTailWingNumber(Integer.parseInt(tailCombo.getValue()));
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
    public void changeModel(Model m) {
        tailCheck.setSelected(m.getTail().isExist());
        wingCombo.setValue("" + m.getWings().size());
        tailCombo.setEnabled(m.getTail().isExist());
        tailCombo.setValue("" + m.getTail().getHorizontal().size());
    }

    @Override
    public void updateModel() {
       changeModel(PredimRC.getInstance().getModel());
    }
}
