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
package predimrc.gui.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import predimrc.PredimRC;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.gui.widget.MegaLabel;
import predimrc.gui.graphic.drawable.panel.LeftPanel;
import predimrc.gui.graphic.drawable.panel.FrontPanel;
import predimrc.gui.graphic.drawable.panel.TopPanel;
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
public class MainView extends JPanel implements MouseMotionListener, IModelListener {

    private MegaLabel modelTitle;
    private FrontPanel diedrepanel;
    private LeftPanel calagepanel;
    private TopPanel toppanel;

    public MainView() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        modelTitle = new MegaLabel("Model name (Enter to validate):", "undefined", true);
        modelTitle.addKeyListener("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceModel().setName(modelTitle.getValue());
                modelTitle.setDefaultColor();
            }
        });

        modelTitle.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modelTitle.setValueBackground(new Color(200, 150, 150));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modelTitle.setValueBackground(new Color(200, 150, 150));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });


        add(modelTitle);
        JPanel topDraw = new JPanel();
        diedrepanel = new FrontPanel();
        calagepanel = new LeftPanel();
        toppanel = new TopPanel();
        ModelController.addModelListener(diedrepanel);
        ModelController.addModelListener(calagepanel);
        ModelController.addModelListener(toppanel);
        topDraw.add(diedrepanel, BorderLayout.WEST);
        topDraw.add(calagepanel, BorderLayout.EAST);
        add(topDraw);
        add(toppanel, BorderLayout.SOUTH);
        addMouseMotionListener(this);
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
        modelTitle.setDefaultColor();

    }

    @Override
    public void updateModel() {
        changeModel(PredimRC.getInstanceModel());
    }
}
