/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import predimrc.PredimRC;
import predimrc.gui.frame.Compare_Frame;
import predimrc.model.Model;

/**
 *
 * This class is a super calss for creating external frames.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public abstract class ExternalFrame extends JFrame {

    protected String title = "unknown";
    protected Image icon = null;
    protected int x = predimrc.PredimRC.DEFAULT_X_FRAME;
    protected int y = predimrc.PredimRC.DEFAULT_Y_FRAME;
    protected AbstractButton caller;
    protected Model model;

    public ExternalFrame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super();
        caller = _caller;
        icon = _icon;
        model = PredimRC.getInstance().getModel();
        x = _x;
        y = _y;
        setSize(x, y);
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                caller.setSelected(false);
                save();
                dispose();
                setAlwaysOnTop(false);
            }
        });



        /**
         * TODO * addKeyListener(new KeyListener() {
         *
         * @Override public void keyTyped(KeyEvent e) { }
         *
         * @Override public void keyPressed(KeyEvent keyEvent) { if
         * (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) { closer.doClick(); } }
         *
         * @Override public void keyReleased(KeyEvent e) { } });
         *
         */
    }

    public abstract void save();
}
