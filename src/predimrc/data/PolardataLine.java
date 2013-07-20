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

/**
 * A line of data from polars.dat
 *
 * @author Christophe Levointurier, 19 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolardataLine {

    private float alpha = 0f;
    private float Cz = 0f;     //CL   
    private float Cx = 0f;    //CD 
    private float CDp = 0f;
    private float Cm = 0f;
    private float Top_Xtr = 0f;
    private float Bot_Xtr = 0f;

    public PolardataLine(String alpha, String CZ, String Cx, String CDp, String CM, String Top_Xtr, String Bot_Xtr) {
        try {
            this.alpha = Float.parseFloat(alpha);
            this.Cz = Float.parseFloat(CZ);
            this.Cx = Float.parseFloat(Cx);
            this.CDp = Float.parseFloat(CDp);
            this.Cm = Float.parseFloat(CM);
            this.Top_Xtr = Float.parseFloat(Top_Xtr);
            this.Bot_Xtr = Float.parseFloat(Bot_Xtr);
        } catch (java.lang.NumberFormatException e) {
            predimrc.PredimRC.logln("NumberFormatException:" + alpha + ":" + CZ + ":" + Cx + ":" + CDp + ":" + CM + ":" + Top_Xtr + ":" + Bot_Xtr);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public float getCz() {
        return Cz;
    }

    public float getCx() {
        return Cx;
    }

    public float getCDp() {
        return CDp;
    }

    public float getCm() {
        return Cm;
    }

    public float getTop_Xtr() {
        return Top_Xtr;
    }

    public float getBot_Xtr() {
        return Bot_Xtr;
    }
}
