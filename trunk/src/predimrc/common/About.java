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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Christophe Levointurier, 21 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class About {

    //  public static void main(String[] uiobg) {        new About();    }
    public About() {
        File aboutFile = new File(predimrc.PredimRC.getResourceUrl("about.html").getFile());
        File fileOut;
        FileInputStream reader = null;
        FileOutputStream writer = null;
        try {
            predimrc.PredimRC.logDebug("Writing :" + aboutFile.getName());
            fileOut = File.createTempFile("" + System.currentTimeMillis(), ".html");
            fileOut.deleteOnExit();
            reader = new FileInputStream(aboutFile);
            writer = new FileOutputStream(fileOut);
            final byte[] buf;
            int i = 0;
            buf = new byte[32768];
            while ((i = reader.read(buf)) != -1) {
                writer.write(buf, 0, i);
            }

            Properties sys = System.getProperties();
            String os = sys.getProperty("os.name");
            System.out.println(os);
            Runtime r = Runtime.getRuntime();

            if (os.endsWith("NT") || os.endsWith("2000") || os.endsWith("XP") || os.contains("Windows")) {
                r.exec("cmd /c start " + fileOut.getAbsolutePath());
            } else {
                r.exec("start " + fileOut.getAbsolutePath());
            }
        } catch (IOException ex) {
            predimrc.PredimRC.logln("Error extracting :" + aboutFile.getName());
        } finally {
            try {
                reader.close();
            } catch (NullPointerException | IOException ex) {
            }
            try {
                writer.close();
            } catch (NullPointerException | IOException ex) {
            }
        }
    }
}
