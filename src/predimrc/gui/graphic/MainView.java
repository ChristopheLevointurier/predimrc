/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.MegaLabel;
import predimrc.model.Model;

/**
 *
 * This is the main view from the main frame.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public class MainView extends JPanel {

    private MegaLabel modelTitle;

    public MainView() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        modelTitle = new MegaLabel("Model name:", "undefined");
        add(modelTitle);
        
    }

    public void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draftMainView.png")));
    }

    public void setModel(Model m) {
        modelTitle.setValue(m.getName());

    }
}
