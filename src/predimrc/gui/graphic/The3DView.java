/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import predimrc.PredimRC;

/**
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public class The3DView extends JPanel {

    public The3DView() {
        super();
        showDraft();
    }

    private void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draft3DView.png")));
    }
}
