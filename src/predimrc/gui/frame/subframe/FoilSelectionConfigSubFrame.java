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
import predimrc.gui.frame.XFoil_Frame;
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
    private FoilSelectionConfigPanel foil0 = new FoilSelectionConfigPanel(this, 0, "fad05.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil1 = new FoilSelectionConfigPanel(this, 1, "fad07.dat", 6, 100, 100);
    private FoilSelectionConfigPanel foil2 = new FoilSelectionConfigPanel(this, 2, "fad15.dat", 6, 100, 100);

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
        updateModelXfoilConfig();
        changeFoil();
        pack();
    }

    public void changeFoil() {
        String s0 = foil0.getSelectedFoil();
        String s1 = foil1.getSelectedFoil();
        String s2 = foil2.getSelectedFoil();
        //update tabs title
        foilSelect.setTitleAt(0, s0);
        foilSelect.setTitleAt(1, s1);
        foilSelect.setTitleAt(2, s2);
    }

    public final void updateModelXfoilConfig() {
        //update model config
        drawableModel.getXfoilConfig().setFoilConfig(0, foil0);
        drawableModel.getXfoilConfig().setFoilConfig(1, foil1);
        drawableModel.getXfoilConfig().setFoilConfig(2, foil2);
        XFoil_Frame.getInstance().refreshGraphs();
    }

    @Override
    public void save() {
        //nothing to do
    }
}
