/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
        caller.setEnabled(false);
        icon = _icon;
        model = PredimRC.getInstance().getModel();
        x = _x;
        y = _y;
        setSize(x, y);
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(icon);
        setVisible(true);
        final ExternalFrame frame = this;
        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                PredimRC.logDebugln("Esc from" + title);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        };
        JPanel content = (JPanel) getContentPane();
        InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE");
        content.getActionMap().put("CLOSE", actionListener);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (PredimRC.warnClosePopup) {
                    Object[] options = {"OK", "OMG DONT TELL ME AGAIN -_-"};
                    int ret = JOptionPane.showOptionDialog(null, "Closing a pop-up automatically save its content", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    PredimRC.warnClosePopup = (ret != 1);

                }
                save();
                caller.setEnabled(true);
                caller.setSelected(false);
                dispose();
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

    public String getTitle() {
        return title;
    }
}
