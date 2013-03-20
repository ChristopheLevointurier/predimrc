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

/**
 *
 * @author Christophe Levointurier, 20 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolarDataBase {

    private static HashMap<String, PolarData> foilsDataBase = new HashMap<>();

    public static PolarData getData(String key) {
        if (!foilsDataBase.containsKey(key)) {  //TODO
            return new PolarData("FAD18", 9, 100, 100, 1500);
            //call to xfoil
            //new PolarData
            //put new key
        }
        return foilsDataBase.get(key);
    }

    public static void addData(String key, PolarData value) {
        foilsDataBase.put(key, value);
    }

    public static void addData(String key) {
        foilsDataBase.put(key, new PolarData(key));
    }
}
