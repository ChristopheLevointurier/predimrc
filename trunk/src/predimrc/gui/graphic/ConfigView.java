/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import predimrc.PredimRC;
import predimrc.gui.MegaLabel;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.VlmStab_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;

/**
 *
 * @author Christophe Levointurier, 2 déc. 2012
 * @version
 * @see
 * @since
 */
public final class ConfigView extends JPanel {

    private JPanel mainWing = new JPanel();
    private JPanel stab = new JPanel();
    private JPanel mainWingButtons = new JPanel();
    private JPanel fuselageButtons = new JPanel();
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
    private JCheckBox stabCheck = new JCheckBox("Stabilisateur");
    private JCheckBox fuselageCheck = new JCheckBox("Fuselage");
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
     * Labels for stab data
     */
    private MegaLabel stabspan_label = makeLabel("stab span");
    private MegaLabel stabarea_label = makeLabel("stab area");
    private MegaLabel stabFoil_label = makeLabel("stab airFoil");
    private MegaLabel stabratio_label = makeLabel("stab aspect ratio");
    private MegaLabel stabcorde_label = makeLabel("stab corde moyenne");
    private MegaLabel stablevier_label = makeLabel("Bras de levier");
    private MegaLabel stabouverture_label = makeLabel("stab Ouverture");
    private MegaLabel vstab_label = makeLabel("vstab ");

    public ConfigView() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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






        stab.setLayout(new BoxLayout(stab, BoxLayout.Y_AXIS));
        stab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Stabilisateur:"));
        stab.add(stabspan_label);
        stab.add(stabarea_label);
        stab.add(stabFoil_label);
        stab.add(stabratio_label);
        stab.add(stabcorde_label);
        stab.add(stablevier_label);
        stab.add(stabouverture_label);
        stab.add(vstab_label);
        stab.add(vlmStabBut);
        add(stab);




        fuselageButtons.setLayout(new BoxLayout(fuselageButtons, BoxLayout.X_AXIS));
        fuselageButtons.add(engineBut);
        // fuselageButtons.add(rzBut);
        //  fuselageButtons.add(vxBut);
        fuselageButtons.add(compareBut);
        add(fuselageButtons);



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
}
