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
import java.io.IOException;

/**
 *
 * @author Christophe Levointurier, 21 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class About {

    //    public static void main(String[] uiobg) {        new About();    }
    public About() {
        File aboutFile = new File(predimrc.PredimRC.getResourceUrl("about.html").getFile());
        try {
            File fileOut = File.createTempFile("" + System.currentTimeMillis(), ".html");
            predimrc.PredimRC.logDebug("Writing :" + aboutFile.getName());
            fileOut.deleteOnExit();

            Utils.extractFile(aboutFile, fileOut, true);
            ///   System.out.println(os);
            Runtime r = Runtime.getRuntime();
            switch (Utils.getOs()) {
                case WINDOWS: {
                    r.exec("cmd /c start " + fileOut.getAbsolutePath());
                    break;
                }
                default: {
                    r.exec("start " + fileOut.getAbsolutePath());
                    break;
                }
            }
        } catch (IOException ex) {
            predimrc.PredimRC.logln("Error extracting :" + aboutFile.getName());
        }
    }
}
