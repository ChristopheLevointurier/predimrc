/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.DrawableModel;

/**
 *
 * This class is a super calss for creating mini external frames.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public abstract class MiniFrame extends JFrame implements IModelListener {

    protected String title = "unknown";
    protected int x = Utils.DEFAULT_X_MINI_FRAME;
    protected int y = Utils.DEFAULT_Y_MINI_FRAME;
    protected AbstractButton caller;
    protected DrawableModel drawableModel;

    public MiniFrame(AbstractButton _caller, JFrame _mother) {
        this(_caller, _mother, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public MiniFrame(AbstractButton _caller, JFrame _mother, int _x, int _y) {
        super();
        caller = _caller;
        caller.setEnabled(false);
        drawableModel = PredimRC.getInstanceDrawableModel();
        x = _x;
        y = _y;
        setSize(x, y);
        setResizable(true);
        setAlwaysOnTop(true);
        setLocationRelativeTo(_mother);
        getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        setUndecorated(true);
        //setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 32, 32));
        setOpacity(0.80f);
        setVisible(true);
        final MiniFrame frame = this;
        Action actionListener = new AbstractAction() {
            @Override
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
                caller.setEnabled(true);
                caller.setSelected(false);
                ModelController.removeModelListener((IModelListener) e.getWindow());
                dispose();
            }
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void updateModel(DrawableModel m) {
    }
}
