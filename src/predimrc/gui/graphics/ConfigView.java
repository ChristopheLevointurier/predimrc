/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphics;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import predimrc.PredimRC;

/**
 *
 * @author Christophe Levointurier, 2 déc. 2012
 * @version
 * @see
 * @since
 */
public class ConfigView extends JPanel {

    public ConfigView() {
        super();
    }

    public void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draftConfigView.png")));
    }
}
