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
     *
     */
    private JToggleButton vrillageBut = new JToggleButton("vrillage");
    private JToggleButton vlmStabBut = new JToggleButton("VLM");
    private JToggleButton engineBut = new JToggleButton("Engine");
    private JToggleButton rzBut = new JToggleButton("Rz cste");
    private JToggleButton vxBut = new JToggleButton("Vx cste");
    private JToggleButton compareBut = new JToggleButton("Compare Models");
    private JCheckBox stabCheck = new JCheckBox("Stabilisateur");
    private JCheckBox fuselageCheck = new JCheckBox("Fuselage");

    public ConfigView() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        mainWing.setLayout(new BoxLayout(mainWing, BoxLayout.Y_AXIS));
        mainWingButtons.setLayout(new BoxLayout(mainWingButtons, BoxLayout.X_AXIS));
        mainWing.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Main wing:"));
        mainWing.add(new MegaLabel("wing span", "20", true));
        mainWing.add(new MegaLabel("wing area", "100", true));
        mainWing.add(new MegaLabel("airFoil", "trucmuch", true));
        mainWing.add(new MegaLabel("aspect ratio", "x", true));
        mainWing.add(new MegaLabel("corde moyenne", "xx", true));
        mainWing.add(new MegaLabel("Alpha0", "xx", true));
        mainWing.add(new MegaLabel("Cm0", "xx", true));
        mainWingButtons.add(vlmBut);
        mainWingButtons.add(optimBut);
        mainWingButtons.add(xFoilBut);
        mainWing.add(mainWingButtons);
        add(mainWing);






        stab.setLayout(new BoxLayout(stab, BoxLayout.Y_AXIS));
        stab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Stabilisateur:"));
        stab.add(new MegaLabel("stab span", "5", true));
        stab.add(new MegaLabel("stab area", "10", true));
        stab.add(new MegaLabel("airFoil", "trucmuch", true));
        stab.add(new MegaLabel("aspect ratio", "x", true));
        stab.add(new MegaLabel("corde moyenne", "xx", true));
        stab.add(new MegaLabel("Bras de levier", "xx", true));
        stab.add(new MegaLabel("Ouverture", "xx", true));
        stab.add(new MegaLabel("Vstab", "xx", true));
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
}
