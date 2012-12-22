/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.model.element.raw;

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
public class RawElement {

    protected ArrayList<JGL_3DVector> vertices;
    protected String title;
    protected String file;

    public RawElement(String file) {
        vertices = new ArrayList<>();
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
                        vertices.add(new JGL_3DVector(f1, f2, f3));
                    }
                }
            } catch (IOException ex) {
                predimrc.PredimRC.log("IOException:" + ex.getLocalizedMessage());
            } catch (NumberFormatException ex) {
                predimrc.PredimRC.log("NumberFormatException, non data line in file:" + ex.getLocalizedMessage());
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            predimrc.PredimRC.log(ex.getLocalizedMessage());
        }
    }

    public ArrayList<JGL_3DVector> getVertices() {
        return vertices;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }
}