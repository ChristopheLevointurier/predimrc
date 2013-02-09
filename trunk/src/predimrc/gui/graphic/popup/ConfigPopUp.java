/*This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.graphic.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 29 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigPopUp extends JFrame {

    protected JButton okBut = new JButton("Ok");
    protected JButton cancelBut = new JButton("Cancel");
    protected TYPE_MODIF usedFor;
    protected JPanel widgets, but;
    protected MegaLabel xposLabel = new MegaLabel("Position in X axis:", true);
    protected MegaLabel yposLabel = new MegaLabel("Position in Y axis:", true);
    protected MegaLabel zposLabel = new MegaLabel("Position in Z axis:", true);

    public ConfigPopUp(String title, TYPE_MODIF _usedFor) {
        super(title);
        usedFor = _usedFor;

        widgets = new JPanel();
        widgets.setLayout(new BoxLayout(widgets, BoxLayout.Y_AXIS));

        cancelBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.logln("Action cancelled by user.");
                dispose();
            }
        });

        but = new JPanel();
        but.setLayout(new BoxLayout(but, BoxLayout.X_AXIS));
        but.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder()));
        but.add(okBut);
        but.add(cancelBut);
    }

    protected void finish() {

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(widgets);
        getContentPane().add(but);

        pack();
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(predimrc.PredimRC.icon);
        setVisible(true);
    }

    protected JPanel makePanelPos() {
        JPanel pos = new JPanel();
        pos.setLayout(new BoxLayout(pos, BoxLayout.Y_AXIS));
        pos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Position:"));
        pos.add(xposLabel);
        pos.add(yposLabel);
        pos.add(zposLabel);
        return pos;
    }

    public static enum TYPE_MODIF {

        FRONT_POINT, BACK_POINT;
    }
}
