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
package predimrc.model.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import predimrc.gui.frame.subframe.FoilSelectionConfigPanel;

/**
 *
 * @author Christophe Levointurier, 16 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XfoilConfig implements Serializable {

    public static final String DELIM = "¤";
    private String reynolds = "true¤true¤true¤true¤true¤true";
    private String foil0Config = "fad15.dat¤2¤6¤100¤100";
    private String foil1Config = "fad05.dat¤0¤6¤100¤100";
    private String foil2Config = "fad07.dat¤1¤6¤100¤100";

    public XfoilConfig() {
    }

    public ArrayList<Boolean> getReynolds() {
        ArrayList<Boolean> ret = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(reynolds, DELIM);
        while (tok.hasMoreTokens()) {
            ret.add(Boolean.parseBoolean(tok.nextToken()));
        }
        return ret;
    }

    public void setReynolds(ArrayList<Boolean> r) {
        StringBuilder reynoldsBuilder = new StringBuilder();
        for (Boolean b : r) {
            reynoldsBuilder.append(b).append(DELIM);
        }
        reynolds = reynoldsBuilder.toString();
    }

    public String getFoilName(int i) {
        StringTokenizer tok = getTok(i);
        return tok.nextToken();
    }

    public int getCrit(int i) {
        StringTokenizer tok = getTok(i);
        tok.nextToken();
        return Integer.parseInt(tok.nextToken());
    }

    public int getXtrTop(int i) {
        StringTokenizer tok = getTok(i);
        tok.nextToken();
        tok.nextToken();
        return Integer.parseInt(tok.nextToken());
    }

    public int getXtrBot(int i) {
        StringTokenizer tok = getTok(i);
        tok.nextToken();
        tok.nextToken();
        tok.nextToken();
        return Integer.parseInt(tok.nextToken());
    }

    private StringTokenizer getTok(int i) {
        switch (i) {
            case 0:
                return new StringTokenizer(foil0Config, DELIM);
            case 1:
                return new StringTokenizer(foil1Config, DELIM);
            case 2:
                return new StringTokenizer(foil2Config, DELIM);
            default:
                return new StringTokenizer(foil0Config, DELIM);
        }
    }

    public final String setFoilConfig(String foil, int cindex, int crit, int xtrTop, int xtrBot) {
        return foil + DELIM + cindex + DELIM + crit + DELIM + xtrTop + DELIM + xtrBot + DELIM;
    }

    public void setFoilConfig(int i, FoilSelectionConfigPanel foilConf) {
        switch (i) {
            case 0:
                foil0Config = setFoilConfig(foilConf.getSelectedFoil(), 0, foilConf.getCrit(), foilConf.getXtrTop(), foilConf.getXtrBot());
                return;
            case 1:
                foil1Config = setFoilConfig(foilConf.getSelectedFoil(), 1, foilConf.getCrit(), foilConf.getXtrTop(), foilConf.getXtrBot());
                return;
            case 2:
                foil2Config = setFoilConfig(foilConf.getSelectedFoil(), 2, foilConf.getCrit(), foilConf.getXtrTop(), foilConf.getXtrBot());
            default:
        }
    }

    public ArrayList<String> getConfigsToDisplay() {
        ArrayList<String> ret = new ArrayList<>();
        boolean f0 = getFoilName(0).length() > 1;
        boolean f1 = getFoilName(1).length() > 1;
        boolean f2 = getFoilName(2).length() > 1;
        ArrayList<Boolean> r = getReynolds();
        for (int i = 0; i < r.size(); i++) {
            if (r.get(i)) {
                addfoil(ret, "" + i, f0, f1, f2);
            }
        }
        return ret;
    }

    private ArrayList<String> addfoil(ArrayList<String> in, String re, boolean f0, boolean f1, boolean f2) {
        if (f0) {
            in.add(foil0Config + re + DELIM);
        }
        if (f1) {
            in.add(foil1Config + re + DELIM);
        }
        if (f2) {
            in.add(foil2Config + re + DELIM);
        }
        return in;
    }
}
