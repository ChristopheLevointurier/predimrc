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
 * This class has Widgets for the 3D view configuration
 *
 * @author Christophe Levointurier, 19 déc. 2012
 * @version
 * @see
 * @since
 */
public final class Config3DView extends JPanel {

    private JPanel config3DPanel = new JPanel();
    private JCheckBox cull_check = new JCheckBox("CullFacing", true);
    private JCheckBox solid_check = new JCheckBox("Solid", true);
    private JCheckBox lighting_check = new JCheckBox("Lightnings", true);
    private JCheckBox rotX_check = new JCheckBox("RotationX", true);
    private JCheckBox rotY_check = new JCheckBox("RotationY", false);
    private JCheckBox rotZ_check = new JCheckBox("RotationZ", false);

    public Config3DView() {
        super();



        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



        config3DPanel.setLayout(new BoxLayout(config3DPanel, BoxLayout.Y_AXIS));
        config3DPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Config"));
        config3DPanel.add(cull_check);
        config3DPanel.add(solid_check);
        config3DPanel.add(lighting_check);
        config3DPanel.add(rotX_check);
        config3DPanel.add(rotY_check);
        config3DPanel.add(rotZ_check);
        add(config3DPanel);


    }

    public JCheckBox getCull_check() {
        return cull_check;
    }

    public JCheckBox getSolid_check() {
        return solid_check;
    }

    public JCheckBox getLighting_check() {
        return lighting_check;
    }

    public JCheckBox getRotX_check() {
        return rotX_check;
    }

    public JCheckBox getRotY_check() {
        return rotY_check;
    }

    public JCheckBox getRotZ_check() {
        return rotZ_check;
    }
}
