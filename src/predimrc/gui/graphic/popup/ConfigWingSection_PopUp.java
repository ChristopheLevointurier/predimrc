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

import javax.swing.JOptionPane;

/**
 *
 * @author Christophe Levointurier, 30 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ConfigWingSection_PopUp {

    private TYPE_MODIF usedFor;
    private String input;

    public static String MakePopup(TYPE_MODIF _usedFor, String _input) {
        ConfigWingSection_PopUp pop = new ConfigWingSection_PopUp(_usedFor, _input);
        return pop.getValue();
    }

    private ConfigWingSection_PopUp(TYPE_MODIF _usedFor, String _input) {
        usedFor = _usedFor;
        input = _input;
    }

    private String getValue() {

        switch (usedFor) {
            case LENGTH_AND_FLECHE: {
                predimrc.PredimRC.logln("Pop up " + usedFor.name());
                break;
            }
            case DIEDRE: {
                predimrc.PredimRC.logln("Pop up " + usedFor.name());
                return JOptionPane.showInputDialog(null, "Type exact diedre value here:", "x");
            }
            case WIDTH1: {
                predimrc.PredimRC.logln("Pop up " + usedFor.name());
                return JOptionPane.showInputDialog(null, "Type exact width1 value here:", "x");
            }
            case WIDTH2: {
                predimrc.PredimRC.logln("Pop up " + usedFor.name());
                return JOptionPane.showInputDialog(null, "Type exact width2 value here:", "x");
            }
        }
        return "";
    }

    public static enum TYPE_MODIF {

        DIEDRE, LENGTH_AND_FLECHE, WIDTH1, WIDTH2;
    }
}
