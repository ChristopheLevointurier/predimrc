/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui;

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

    private String title = "unknown";
    private ImageIcon icon = null;
    private int x = 800;
    private int y = 600;
    private AbstractButton caller;

    public ExternalFrame(AbstractButton _caller, String _title, ImageIcon _icon) {
        this(_caller, _title, _icon, 800, 600);
    }

    public ExternalFrame(AbstractButton _caller, String _title, ImageIcon _icon, int _x, int _y) {
        super(_title);
        caller = _caller;
        title = _title;
        icon = _icon;
        x = _x;
        y = _y;
        setSize(x, y);
        setResizable(true);
        setLocationRelativeTo(null);
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
