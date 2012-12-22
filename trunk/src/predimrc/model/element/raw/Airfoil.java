/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.model.element.raw;

import jglcore.JGL_3DMesh;
import jglcore.JGL_3DVector;

/**
 * This class is used to load foils from .dat files
 *
 * @author Christophe Levointurier, 21 déc. 2012
 * @version
 * @see
 * @since
 */
public class Airfoil extends RawElement {

    public Airfoil(String file) {
        super("AirFoils//" + file);
    }

    public JGL_3DMesh getWingPart(float width, float height, int r, int g, int b) {
        JGL_3DMesh mesh = new JGL_3DMesh();

        for (int i = 0; i < vertices.size() - 1; i++) {
            JGL_3DVector p1 = vertices.get(i);
            JGL_3DVector p2 = vertices.get(i + 1);
            JGL_3DVector p3 = new JGL_3DVector(p2.x, p2.y, width);
            JGL_3DVector p4 = new JGL_3DVector(p1.x, p1.y, width);
            mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, r, g, b).getFaces());
        }

        JGL_3DVector p1 = vertices.get(vertices.size()-1);
        JGL_3DVector p2 = vertices.get(0);
        JGL_3DVector p3 = new JGL_3DVector(p2.x, p2.y, width);
        JGL_3DVector p4 = new JGL_3DVector(p1.x, p1.y, width);
        mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, r, g, b).getFaces());
        return mesh;
    }
}
