/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.IModelReact;
import predimrc.gui.MegaLabel;
import predimrc.gui.graphic.drawable.CalagePanel;
import predimrc.gui.graphic.drawable.DiedrePanel;
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
public class MainView extends JPanel implements MouseMotionListener, IModelReact {

    private MegaLabel modelTitle;

    public MainView() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        modelTitle = new MegaLabel("Model name:", "undefined", true);

        add(modelTitle);
        JPanel topDraw = new JPanel();

        topDraw.add(new DiedrePanel(), BorderLayout.WEST);
        topDraw.add(new CalagePanel(), BorderLayout.EAST);

        add(topDraw);
        addMouseMotionListener(this);
        showDraft();
    }

    private void showDraft() {
        add(new JButton(PredimRC.getImageIcon("draftMainView.png")));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //  PredimRC.logDebugln("mousemdrag in main");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //  PredimRC.logDebugln("mousemmvoe in main");
    }

    @Override
    public void changeModel(Model m) {
        modelTitle.setValue(m.getName());
    }
}
