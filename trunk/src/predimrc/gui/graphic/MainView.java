/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import predimrc.PredimRC;

/**
 *
 * This is the top view from the main frame.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public class MainView extends JPanel {

    public MainView() {
        super();
     }

    public void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draftMainView.png")));
    }
}
