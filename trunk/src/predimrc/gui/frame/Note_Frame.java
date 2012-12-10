/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import predimrc.PredimRC;
import predimrc.gui.ExternalFrame;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class Note_Frame extends ExternalFrame {

    private JTextArea area;

    public Note_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, predimrc.PredimRC.DEFAULT_X_FRAME, predimrc.PredimRC.DEFAULT_Y_FRAME);
    }

    public Note_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "Note";
        setTitle(title);
        area = new JTextArea(model.getNote(), 10, 50);
        JPanel todos = new JPanel();
        todos.add(area);
        setLayout(new BorderLayout());
        getContentPane().add(todos, BorderLayout.CENTER);
        pack();
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
    }

    @Override
    public void save() {
        predimrc.PredimRC.logDebugln("Save de " + title);
        model.setNote(area.getText());
        caller.setSelected(false);
        dispose();
     }
}
