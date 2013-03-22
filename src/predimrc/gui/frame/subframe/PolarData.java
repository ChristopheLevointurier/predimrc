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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.common.exception.MissingPolarDataException;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

/**
 * This class contains data from polars.dat.
 *
 * @author Christophe Levointurier, 19 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PolarData {

    private PolarKey key;
    private ArrayList<PolardataLine> data;

    
    public PolarData(PolarKey _key) throws MissingPolarDataException {
        key = _key;
        loadPolarData();
    }

    private void loadPolarData() throws MissingPolarDataException {
        data = new ArrayList<>();
        if (key.getFile().toLowerCase().endsWith(".txt")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(predimrc.PredimRC.getDataResourceUrl("Polars/" + key.getFile()).openStream()));
                String line;
                //  System.out.println("stream ok:" + file);
                try {
                    PredimRC.logln("opening" + key.getFile() + ":" + reader.readLine());
                    int cpt = 0;
                    while ((line = reader.readLine()) != null) {
                        cpt++;
                        if (cpt >= 12) {
                            String[] datasInLine = line.split("\\s+");
                            data.add(new PolardataLine(datasInLine[1], datasInLine[2], datasInLine[3], datasInLine[4], datasInLine[5], datasInLine[6], datasInLine[7]));
                        }
                    }
                    PredimRC.logln("points amount:" + data.size());
                } catch (IOException ex) {
                    predimrc.PredimRC.logln("IOException:" + ex.getLocalizedMessage());
                } finally {
                    reader.close();
                }
            } catch (IOException | NullPointerException ex) {
                predimrc.PredimRC.logln("IOException|NullPointerException while trying to read :" + key.getFile() + System.getProperty("line.separator") + ex.getLocalizedMessage());
                throw new MissingPolarDataException();
            }
        } else {
            predimrc.PredimRC.logln(key.getFile() + " don't seems to be a data file.");
            throw new MissingPolarDataException();
        }
    }

    public ArrayList<DrawablePoint> getCzCxData() {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (PolardataLine p : data) {
            ret.add(new DrawablePoint(p.getCx(), p.getCz(), Utils.VIEW_TYPE.GRAPH));
        }
        return ret;
    }

    public ArrayList<DrawablePoint> getCzAlphaData() {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (PolardataLine p : data) {
            ret.add(new DrawablePoint(p.getAlpha(), p.getCz(), Utils.VIEW_TYPE.GRAPH));
        }
        return ret;
    }

    public ArrayList<DrawablePoint> getCmCzData() {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (PolardataLine p : data) {
            ret.add(new DrawablePoint(p.getCz(), p.getCm(), Utils.VIEW_TYPE.GRAPH));
        }
        return ret;
    }

    public int getColIndex() {
        return key.getColIndex();
    }

    public int getReynoldsIndex() {
        return key.getReynoldsIndex();
    }
}
