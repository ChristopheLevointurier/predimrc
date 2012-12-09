/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import predimrc.PredimRC;
import predimrc.gui.MegaLabel;
import predimrc.gui.frame.Optim_Frame;
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
    private JPanel mainWingButtons = new JPanel();
    private JToggleButton vlmBut = new JToggleButton("VLM");
    private JToggleButton optimBut = new JToggleButton("Optim");
    private JToggleButton xFoilBut = new JToggleButton("xFoil");

    public ConfigView() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        mainWing.setLayout(new BoxLayout(mainWing, BoxLayout.Y_AXIS));
        mainWingButtons.setLayout(new BoxLayout(mainWingButtons, BoxLayout.X_AXIS));
        mainWing.add(new JLabel("Main wing:"));
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




        vlmBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Vlm_Frame(vlmBut);
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


        add(mainWing);
        add(mainWingButtons);
        
        showDraft();

    }

    public void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draftConfigView.png")));
    }
}
