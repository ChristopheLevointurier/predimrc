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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.config.ConfigBasicView;
import predimrc.gui.graphic.drawable.panel.FrontPanel;
import predimrc.gui.graphic.drawable.panel.LeftPanel;
import predimrc.gui.graphic.drawable.panel.TopPanel;

/**
 *
 * This is the main view from the main frame.
 *
 * @author Christophe Levointurier 2 déc. 2012
 * @version
 * @see
 * @since
 */
public class MainView extends JPanel implements MouseMotionListener {

    private FrontPanel diedrepanel;
    private LeftPanel calagepanel;
    private TopPanel toppanel;
    private ConfigBasicView basicConfig;

    public MainView() {
        super();
        basicConfig = new ConfigBasicView();
        this.setLayout(new BorderLayout());
        add(basicConfig, BorderLayout.NORTH);
        ModelController.addModelListener(basicConfig);
        diedrepanel = new FrontPanel();
        calagepanel = new LeftPanel();
        toppanel = new TopPanel();
        ModelController.addModelListener(diedrepanel);
        ModelController.addModelListener(calagepanel);
        ModelController.addModelListener(toppanel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, diedrepanel, calagepanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, toppanel);
        topSplitPane.setOneTouchExpandable(true);
        topSplitPane.setResizeWeight(0.25);
        add(topSplitPane, BorderLayout.CENTER);
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

    public void repaintDrawPanels() {
        diedrepanel.repaint();
        calagepanel.repaint();
        toppanel.repaint();
    }
}
