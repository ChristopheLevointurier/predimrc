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
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DVector;
import javagl.jglload.JGL_Data3D;
import predimrc.PredimRC;
import predimrc.common.Dimension3D;
import predimrc.common.Utils;

/**
 * This super class is used to load meshes from data files
 *
 * @author Christophe Levointurier, 21 déc. 2012
 * @version
 * @see
 * @since
 */
public class RawElementLoader {

    protected JGL_3DMesh mesh;
    protected String title;
    protected String file;
    protected Color color = Color.WHITE;

    public RawElementLoader(String file, int r, int b, int g) {
        this(file);
        setRGB(r, g, b);
    }

    public RawElementLoader(String file) {
        loadVertices(file);
    }

    /**
     * This method loads the data point for the file to an array list of
     * vertices
     *
     * @param file
     * @return
     */
    private void loadVertices(String file) {
        if (file.toLowerCase().endsWith(".dat")) {
            ArrayList<JGL_3DVector> vertices = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(predimrc.PredimRC.getDataResourceUrl(file).openStream()));
                String line;
                try {
                    title = reader.readLine();
                    PredimRC.logDebug("opening " + file + ":" + title);
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
                            vertices.add(new JGL_3DVector(f1, f2, f3));
                        }
                    }
                    mesh = new JGL_3DMesh();

                    PredimRC.logDebugln("vertices amount:" + vertices.size());

                    for (int i = 0; i < vertices.size(); i++) {
                        JGL_3DVector p1 = vertices.get(i);
                        JGL_3DVector p2 = vertices.get((i + 1) % vertices.size());
                        JGL_3DVector p3 = new JGL_3DVector(p2.x, p2.y, 1); //1 for length
                        JGL_3DVector p4 = new JGL_3DVector(p1.x, p1.y, 1);
                        mesh.addFaces(Utils.getRectangle(p1, p2, p3, p4, color).getFaces());
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
        }
        if (file.toLowerCase().endsWith(".obj")) {
            JGL_Data3D data = new JGL_Data3D(predimrc.PredimRC.getDataResourceUrl(file), JGL_Data3D.OBJ);
            mesh = data.mesh;
        }
    }

    final public void setRGB(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public ArrayList<JGL_3DVector> getVertices() {
        return new ArrayList<JGL_3DVector>(mesh.getPoints());
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

    public void setPosition(Dimension3D pos) {
        //TODO
    }
}
