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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class extract files from jar to user dir
 *
 * @author Christophe Levointurier, 13 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Extractor {

    private static boolean overwrite = false;
    //public static void main(String[] uiobg) {        extract();    }

    public static void extract() {
        String[] files = new File(predimrc.PredimRC.getResourceUrl("").getFile()).list();
        for (String f : files) {
            if (!f.equals("Images") && !f.equals("Sounds") && !f.equals("about.html")) {
                extractDirectory(f);
            }
        }
    }

    private static void extractDirectory(String dir) {
        String[] files = new File(predimrc.PredimRC.getResourceUrl(dir).getFile()).list();
        for (String f : files) {
            File fileToExtract = new File(predimrc.PredimRC.getResourceUrl(dir + "/" + f).getFile());
            if (fileToExtract.isFile()) {
                extractFile(fileToExtract, dir + "/" + f);
            }
            if (fileToExtract.isDirectory()) {
                extractDirectory(dir + "/" + f);
            }
        }
    }

    private static void extractFile(File fileInZip, String fileOutZipName) {
        FileInputStream reader = null;
        FileOutputStream writer = null;
        try {
            predimrc.PredimRC.logDebug("Writing :" + fileInZip.getName() + " to " + fileOutZipName + "...");
            reader = new FileInputStream(fileInZip);
            File out = new File(predimrc.PredimRC.appRep + fileOutZipName);
            if (out.exists() && !overwrite) {
                return;
            }
            out.getParentFile().mkdirs();
            writer = new FileOutputStream(out);

            final byte[] buf;
            int i = 0;

            buf = new byte[32768];
            while ((i = reader.read(buf)) != -1) {
                writer.write(buf, 0, i);
            }
            predimrc.PredimRC.logDebugln(" ok");
        } catch (IOException ex) {
            predimrc.PredimRC.logln("Error extracting :" + fileInZip.getName() + " to " + fileOutZipName);
        } finally {
            close(reader);
            close(writer);
        }
    }

    private static void close(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (final IOException ex) {
                predimrc.PredimRC.logln(ex.getLocalizedMessage());
            }
        }
    }
}
