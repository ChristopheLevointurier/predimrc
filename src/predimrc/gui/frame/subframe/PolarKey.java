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

import java.util.StringTokenizer;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolarKey {

    private String file = "fail";
    private String foilName = "fail";
    private int reynoldsIndex = 0;
    private int ncrit = 0;
    private int xtrt = 0;
    private int xtrb = 0;
    private int colIndex = 0;

    public PolarKey(String _foilName, int _cIndex, int _ncrit, int _xtrt, int _xtrb, int _reynoldsIndex) {
        foilName = _foilName;
        reynoldsIndex = _reynoldsIndex;
        ncrit = _ncrit;
        xtrt = _xtrt;
        xtrb = _xtrb;
        colIndex = _cIndex;
        makeFile();
    }

    public PolarKey(String key) {
        StringTokenizer tok = new StringTokenizer(key, XfoilConfig.DELIM);

        foilName = tok.nextToken();
        try {
            colIndex = Integer.parseInt(tok.nextToken());
            ncrit = Integer.parseInt(tok.nextToken());
            xtrt = Integer.parseInt(tok.nextToken());
            xtrb = Integer.parseInt(tok.nextToken());
            reynoldsIndex = Integer.parseInt(tok.nextToken());
        } catch (java.lang.NumberFormatException e) {
            predimrc.PredimRC.logln("NumberFormatException:" + foilName + ":" + colIndex + ":" + ncrit + ":" + xtrt + ":" + xtrb + ":" + reynoldsIndex);
        }
        makeFile();
    }

    private void makeFile() {
        file = "Polars/" + foilName.substring(0, foilName.lastIndexOf(".")) + "N" + ncrit + "X" + xtrt + "-" + xtrb + "R" + ReynoldsConfig.reyValue.get(reynoldsIndex) + ".txt";
    }

    public String getFile() {
        return file;
    }

    public String getFoilName() {
        return foilName;
    }

    public int getReynoldsIndex() {
        return reynoldsIndex;
    }

    public int getNcrit() {
        return ncrit;
    }

    public int getXtrt() {
        return xtrt;
    }

    public int getXtrb() {
        return xtrb;
    }

    public int getColIndex() {
        return colIndex;
    }
}
