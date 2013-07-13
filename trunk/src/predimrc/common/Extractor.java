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
package predimrc.common;

import java.io.File;
import predimrc.PredimRC;

/**
 * This class extract files from jar to user dir
 *
 * @author Christophe Levointurier, 13 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Extractor {

    private static boolean overwrite = true;
    //public static void main(String[] uiobg) {        extract();    }

    public static void extract() {
        String[] files = new File(predimrc.PredimRC.getResourceUrl("").getFile()).list();
        if (null != files) {
            for (String f : files) {
                if (!f.equals("Images") && !f.equals("Sounds") && !f.equals("about.html")) {
                    extractDirectory(f);
                }
            }
        }
    }

    private static void extractDirectory(String dir) {
        String[] files = new File(predimrc.PredimRC.getResourceUrl(dir).getFile()).list();
        for (String f : files) {
            File fileToExtract = new File(predimrc.PredimRC.getResourceUrl(dir + "/" + f).getFile());
            if (fileToExtract.isFile()) {
                Utils.extractFile(fileToExtract, new File(PredimRC.appRep + dir + "/" + f), overwrite);
            }
            if (fileToExtract.isDirectory()) {
                extractDirectory(dir + "/" + f);
            }
        }
        if (files.length == 0) {
            new File(PredimRC.appRep + dir).mkdirs();
        }
    }
}
