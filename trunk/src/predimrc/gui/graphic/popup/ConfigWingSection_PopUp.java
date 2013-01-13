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
        predimrc.PredimRC.logln("Pop up " + usedFor.name());
        switch (usedFor) {
            case DIEDRE:
            case WIDTH:
            case ANGLE: {
                return JOptionPane.showInputDialog(null, "Type exact " + usedFor.name() + " value here:", input);
            }

        }
        return "0";
    }

    public static enum TYPE_MODIF {

        DIEDRE, WIDTH, ANGLE;
    }
}