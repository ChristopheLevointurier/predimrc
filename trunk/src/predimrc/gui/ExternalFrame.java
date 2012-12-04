/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * This class is a super calss for creating external frames.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since 
 */
public class ExternalFrame extends JFrame {

    protected String title = "unknown";
    protected Image icon = null;
    protected int x = predimrc.PredimRC.DEFAULT_X_FRAME;
    protected int y = predimrc.PredimRC.DEFAULT_Y_FRAME;
    protected AbstractButton caller;

 
    public ExternalFrame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super();
        caller = _caller;
        icon = _icon;
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
                dispose();
                setAlwaysOnTop(false);
            }
        });
    }
}
