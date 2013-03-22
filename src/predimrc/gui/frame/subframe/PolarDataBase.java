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
import predimrc.controller.Refresher;

/**
 *
 * @author Christophe Levointurier, 20 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolarDataBase {

    private static HashMap<String, PolarData> foilsDataBase = new HashMap<>();

    public static PolarData getPolar(String key) {
        if (!foilsDataBase.containsKey(key)) {
            PolarKey k = new PolarKey(key);
            try {
                foilsDataBase.put(key, new PolarData(k));
            } catch (MissingPolarDataException ex) {
                predimrc.PredimRC.logDebugln("Missing file:" + key);
                XFoilInvoker invoker = new XFoilInvoker(k);  //call to xfoil
                //Refresher.add(invoker);
                Refresher.waitFor(invoker);
                try {
                    foilsDataBase.put(key, new PolarData(k));  //new PolarData
                } catch (MissingPolarDataException ex2) {
                    predimrc.PredimRC.logDebugln("Failed to add new polar:" + key);
                }
            }
         }

        return foilsDataBase.get(key);
    }

    public static void putPolar(String key, PolarData value) {
        foilsDataBase.put(key, value);
    }
}
