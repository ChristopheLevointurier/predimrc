/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.model.element.loader;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import jglcore.JGL_3DVector;
import predimrc.PredimRC;

/**
 * This super class is used to load meshes from data files
 *
 * @author Christophe Levointurier, 21 déc. 2012
 * @version
 * @see
 * @since
 */
public class RawElementLoader {

    protected ArrayList<JGL_3DVector> vertices;
    protected String title;
    protected String file;
    protected Color color = Color.WHITE;

    public RawElementLoader(String file, int r, int b, int g) {
        this(file);
        setRGB(r, g, b);
    }

    public RawElementLoader(String file) {
        vertices = loadVertices(file);
    }

    /**
     * This method loads the data point fro the file to an array list of
     * vertices
     *
     * @param file
     * @return
     */
     protected ArrayList<JGL_3DVector> loadVertices(String file) {
        ArrayList<JGL_3DVector> vertices_temp = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(predimrc.PredimRC.getResourceUrl(file).openStream()));
            String line;
            try {
                title = reader.readLine();
                PredimRC.logln("opening" + file + ":" + title);
                while ((line = reader.readLine()) != null) {
                    float f1 = 0f, f2 = 0f, f3 = 0f;
                    int cpt = 0;
                    String[] data = line.split(" ");
                    for (String d : data) {
                        if (d.length() > 0) {
                            f1 = cpt == 0 ? Float.parseFloat(d) : f1;
                            f2 = cpt == 1 ? Float.parseFloat(d) : f2;
                            f3 = cpt == 2 ? Float.parseFloat(d) : f3;
                            cpt++;
                        }
                    }
                    if (cpt > 1) {
                        PredimRC.logDebugln("new point:(" + f1 + "," + f2 + "," + f3);
                        vertices_temp.add(new JGL_3DVector(f1, f2, f3));
                    }
                }
            } catch (IOException ex) {
                predimrc.PredimRC.log("IOException:" + ex.getLocalizedMessage());
            } catch (NumberFormatException ex) {
                predimrc.PredimRC.log("NumberFormatException, non data line in file:" + ex.getLocalizedMessage());
            } finally {
                reader.close();
            }
        } catch (IOException | NullPointerException ex) {
            predimrc.PredimRC.log("IOException|NullPointerException:" + ex.getLocalizedMessage());
        }
        return vertices_temp;
    }

    final public void setRGB(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public ArrayList<JGL_3DVector> getVertices() {
        return vertices;
    }

    final public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public Color getColor() {
        return color;
    }
}
