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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import predimrc.PredimRC;
import predimrc.common.StreamProcessReader;
import predimrc.common.Utils;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.frame.subframe.ReynoldsConfig;

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilInvoker implements Runnable {

    private static final String filenameTag = "FILENAME_TO_MERGE";
    private static final String ncritTag = "NCRIT_TO_MERGE";
    private static final String xtrTopTag = "XTR_TOP_TO_MERGE";
    private static final String xtrBotTag = "XTR_BOT_TO_MERGE";
    private static final String reynoldsTag = "REYNOLDS_TO_MERGE";
    private static final String filenameOutTag = "FILENAME_OUT_TO_MERGE";
    private PolarKey k;

    public XFoilInvoker(PolarKey _k) {
        k = _k;
    }

    @Override
    public void run() {
        try {
            String temp = loadfile().toString();
            temp = temp.replace(filenameTag, "../../AirFoils/" + k.getFoilName());
            temp = temp.replace(ncritTag, "" + k.getNcrit());
            temp = temp.replace(xtrTopTag, "" + (float) (k.getXtrt() / (float) 100));
            temp = temp.replace(xtrBotTag, "" + (float) (k.getXtrb() / (float) 100));
            temp = temp.replace(reynoldsTag, "" + ReynoldsConfig.reyValue.get(k.getReynoldsIndex()));
            temp = temp.replace(filenameOutTag, "../../Polars/" + k.getFile());
//call to xfoil
            String cmd = "";
            switch (Utils.getOs()) {
                case WINDOWS: {
                    PredimRC.logDebug("****Xfoil calls for " + k.getFoilName());
                    writeFile(temp, "windows/" + k.getFile());
                    cmd = "cmd /c make.bat " + k.getFile();
                    PredimRC.logDebugln(" cmd launched:" + cmd);
                    break;
                }
                default: {
                    PredimRC.logln("Xfoil calls only for windows in v1.0");
                    break;
                }
            }
            Process p = Runtime.getRuntime().exec(cmd, null, new File(PredimRC.appRep + "externalApp/Windows/"));
            new StreamProcessReader(p.getErrorStream(), true).start();
            new StreamProcessReader(p.getInputStream(), false).start();
            p.waitFor();
        } catch (InterruptedException | IOException ex) {
            PredimRC.logln("Error creating txt file for Xfoil:" + ex.getLocalizedMessage());
        }
        if (XFoil_Frame.initDone) {
            XFoil_Frame.getInstance().addPolar(PolarDataBase.getPolar(k, false));
        }
    }

    private void writeFile(String content, String rep) throws IOException {
        File out = new File(PredimRC.appRep + "externalApp/" + rep);
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)))) {
            br.write(content);
            Utils.closeStream(br);
        }
        out.deleteOnExit();
    }

    private StringBuilder loadfile() throws IOException {
        File in = new File(PredimRC.appRep + "externalApp/CMD.txt");
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