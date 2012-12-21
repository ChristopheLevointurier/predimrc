/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.model.element.raw;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        vertices = new ArrayList<JGL_3DVector>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(predimrc.PredimRC.getResourceUrl(file).openStream()));
            String line;
            try {
                title = reader.readLine();
                PredimRC.logln("opening" + file + ":" + title);
                while ((line = reader.readLine()) != null) {
                    float f1 = 0, f2 = 0, f3 = 0;
                    int cpt = 0;
                    String[] data = line.split(" ");
                    for (String d : data) {
                        if (d.length() > 0) {
                            f1 = cpt == 0 ? f1 : Float.parseFloat(d);
                            f2 = cpt == 1 ? f2 : Float.parseFloat(d);
                            f3 = cpt == 2 ? f3 : Float.parseFloat(d);
                            cpt++;
                        }

                    }
                    PredimRC.logDebugln("new point:(" + f1 + "," + f2 + "," + f3);
                    vertices.add(new JGL_3DVector(f1, f2, f2));
                }
            } catch (Exception ex) {
                predimrc.PredimRC.log(ex.getLocalizedMessage());
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
