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
package predimrc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import predimrc.common.Utils;
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
    public static Semaphore sem;
    private static List<PolarKey> failed = new ArrayList<>();

    public static void getPolars(List<String> keys) {
        sem = new Semaphore(Utils.MAX_THREAD);
        for (String key : keys) {
            PolarKey polkey = new PolarKey(key);
            if (!foilsDataBase.containsKey(polkey)) {
                try {
                    new PolarData(polkey);
                } catch (MissingPolarDataException ex) {
                    predimrc.PredimRC.logln("Missing file:" + polkey + " :" + ex);
                    launchInvoker(polkey);
                }
            }
        }
        waitForAll();
        predimrc.PredimRC.logln(failed.size() + " fails ");
        for (PolarKey polkey : failed) {
            predimrc.PredimRC.logDebugln("retrying " + polkey);
            launchInvoker(polkey);
        }
        waitForAll();
    }

    private static void waitForAll() {
        try {
            sem.acquire(Utils.MAX_THREAD);
            sem.release(Utils.MAX_THREAD);
        } catch (InterruptedException ex) {
            predimrc.PredimRC.logln("InterruptedException in acquire all ");
        }
    }

    public static PolarData getPolar(PolarKey key, boolean firstPass) {
        if (!foilsDataBase.containsKey(key)) {
            try {
                foilsDataBase.put(key, new PolarData(key));
            } catch (MissingPolarDataException ex) {
                predimrc.PredimRC.logln("Missing file:" + key + " :" + ex);//+ "firstPass="+firstPass);
                if (firstPass) {
                    new Thread(new XFoilInvoker(key)).start();  //call to xfoil
                }
            }
        }
        return foilsDataBase.get(key);
    }

    public static void removePolar(PolarKey key) {
        if (foilsDataBase.containsKey(key)) {
            predimrc.PredimRC.logDebugln("trash " + key);
            foilsDataBase.get(key).trashDataFile();
            foilsDataBase.remove(key);
        }
    }

    public static void putPolar(PolarKey key, PolarData value) {
        foilsDataBase.put(key, value);
    }

    public static List<PolarKey> getFailed() {
        return failed;
    }

    private static void launchInvoker(PolarKey polkey) {
            predimrc.PredimRC.logDebugln("launchInvoker "+polkey);
        try {
            sem.acquire();
            predimrc.PredimRC.logDebugln("launchInvoker, sem.acquired"+polkey);
            new Thread(new XFoilInvoker(polkey)).start();  //call to xfoil
        } catch (InterruptedException ex1) {
            predimrc.PredimRC.logln("InterruptedException");
        }

    }
}
