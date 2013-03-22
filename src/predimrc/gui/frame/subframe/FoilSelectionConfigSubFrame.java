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
package predimrc.gui.frame.subframe;

import java.awt.Color;
import javax.swing.AbstractButton;
import javax.swing.JTabbedPane;
import predimrc.gui.ExternalFrame;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilSelectionConfigSubFrame extends ExternalFrame {

    private JTabbedPane foilSelect = new JTabbedPane();
    private FoilSelectionConfigPanel foil0 = new FoilSelectionConfigPanel(0, "fad05.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil1 = new FoilSelectionConfigPanel(1, "fad07.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil2 = new FoilSelectionConfigPanel(2, "fad15.dat", 6, 100, 100);

    public FoilSelectionConfigSubFrame(AbstractButton _caller) {
        super(_caller);
        title = "Foil Selection Panels";
        setTitle(title);
        XfoilConfig xfoilconfig = drawableModel.getXfoilConfig();
        foil0.setConfig(xfoilconfig.getFoilName(0), xfoilconfig.getCrit(0), xfoilconfig.getXtrBot(0), xfoilconfig.getXtrTop(0));
        foil1.setConfig(xfoilconfig.getFoilName(1), xfoilconfig.getCrit(1), xfoilconfig.getXtrBot(1), xfoilconfig.getXtrTop(1));
        foil2.setConfig(xfoilconfig.getFoilName(2), xfoilconfig.getCrit(2), xfoilconfig.getXtrBot(2), xfoilconfig.getXtrTop(2));

        foilSelect.add(foil0);
        foilSelect.setForegroundAt(0, Color.red);
        foilSelect.add(foil1);
        foilSelect.setForegroundAt(1, Color.blue);
        foilSelect.add(foil2);
        foilSelect.setForegroundAt(2, Color.green.darker());
        getContentPane().add(foilSelect);
        setSize(350, 160);
        updateModel(predimrc.PredimRC.getInstanceDrawableModel());
    }

    @Override
    public void save() {
        //nothing to do
    }

    @Override
    public void updateModel(DrawableModel m) {
        //  System.out.println("update tabs title:" + m.getXfoilConfig().getFoilName(0) + m.getXfoilConfig().getFoilName(1) + m.getXfoilConfig().getFoilName(2));
        foilSelect.setTitleAt(0, m.getXfoilConfig().getFoilName(0));
        foilSelect.setTitleAt(1, m.getXfoilConfig().getFoilName(1));
        foilSelect.setTitleAt(2, m.getXfoilConfig().getFoilName(2));
    }
}
