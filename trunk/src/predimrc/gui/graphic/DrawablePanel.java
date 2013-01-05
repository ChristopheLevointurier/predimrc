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
package predimrc.gui.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import predimrc.controller.IModelListener;

/**
 *
 * @author Christophe Levointurier, 23 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public abstract class DrawablePanel extends JPanel implements IModelListener {

    protected Image backgroundImage;
    protected String info = "Select an object."; //TODO change to StringBuffer in the future.

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(220, 220, 255));
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), this);
        }
    }

    /**
     * paint update each frame.
     */
    @Override
    public void updateModel() {
        changeModel(predimrc.PredimRC.getInstanceModel());
        repaint();
    }
}
