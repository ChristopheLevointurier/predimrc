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

import java.util.HashMap;
import predimrc.common.exception.MissingPolarDataException;

/**
 *
 * @author Christophe Levointurier, 20 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolarDataBase {

    private static HashMap<PolarKey, PolarData> foilsDataBase = new HashMap<>();

    public static PolarData getPolar(PolarKey key, boolean firstPass) {
        if (!foilsDataBase.containsKey(key)) {
            try {
                foilsDataBase.put(key, new PolarData(key));
            } catch (MissingPolarDataException ex) {
                predimrc.PredimRC.logln("Missing file:" + key+ "firstPass="+firstPass);
                if (firstPass) {
                    new Thread(new XFoilInvoker(key)).start();  //call to xfoil
                }
            }
        }
        return foilsDataBase.get(key);
    }

    public static void putPolar(PolarKey key, PolarData value) {
        foilsDataBase.put(key, value);
    }
}
