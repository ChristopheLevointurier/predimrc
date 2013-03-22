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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import predimrc.PredimRC;
import predimrc.common.Utils;

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilInvoker {

    private static final String filenameTag = "FILENAME_TO_MERGE";
    private static final String ncritTag = "NCRIT_TO_MERGE";
    private static final String xtrTopTag = "XTR_TOP_TO_MERGE";
    private static final String xtrBotTag = "XTR_BOT_TO_MERGE";
    private static final String reynoldsTag = "REYNOLDS_TO_MERGE";
    private static final String filenameOutTag = "FILENAME_OUT_TO_MERGE";

  //  public static void main(String[] uiobg) {
 //       new XFoilInvoker("bla", "blo", 4, 85, 75, 15000);
  //  }

    public XFoilInvoker(String fileIn, String fileOut, int ncrit, int xtrTop, int xtrBot, int reynolds) {
        try {
            String temp = loadfile().toString();
            temp = temp.replace(filenameTag, "../../AirFoils/"+fileIn);
            temp = temp.replace(ncritTag, "" + ncrit);
            temp = temp.replace(xtrTopTag, "" + (float) (xtrTop / (float) 100));
            temp = temp.replace(xtrBotTag, "" + (float) (xtrBot / (float) 100));
            temp = temp.replace(reynoldsTag, "" + reynolds);
            temp = temp.replace(filenameOutTag,"../../Polars/"+ fileOut);

//call to xfoil
            Runtime r = Runtime.getRuntime();
            switch (Utils.getOs()) {
                case WINDOWS: {
                    System.out.println("xfoil call in windows mode");
                    writeFile(temp, "windows/");
                    r.exec("cmd /c start make", null, new File(PredimRC.appRep + "externalApp/Windows/"));
                    break;
                }
                default: {
                    PredimRC.log("Xfoil calls only for windows in v1.0");
                    break;
                }
            }
        } catch (IOException ex) {
            PredimRC.logln("Error creating txt file for Xfoil");
        }
    }

    private void writeFile(String content, String rep) throws IOException {
        File out = new File(PredimRC.appRep + "externalApp/" + rep + "CMD.txt");
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)))) {
            br.write(content);
            Utils.closeStream(br);
        }
    }

    private StringBuilder loadfile() throws IOException {
        File in = new File(predimrc.PredimRC.getResourceUrl("externalApp/CMD.txt").getFile());
        StringBuilder ret = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in)))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ret.append(ligne).append(System.getProperty("line.separator"));
            }
            Utils.closeStream(br);
        }
        return ret;
    }
}
