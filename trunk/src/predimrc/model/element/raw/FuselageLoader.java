/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.model.element.raw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DVector;
import predimrc.PredimRC;

/**
 * This class is used to load fuselages from .dat files
 *
 * @author Christophe Levointurier, 21 déc. 2012
 * @version
 * @see
 * @since
 */
public class FuselageLoader extends RawElementLoader {

    protected ArrayList<JGL_3DVector> vertices_prof = new ArrayList<>();

    public FuselageLoader(String file, int r, int b, int g) {
        super("Fuselages/" + file + "_left.dat", r, b, g);
        vertices_prof = loadVertices("Fuselages/" + file + "_top.dat");
    }

    public JGL_3DMesh getFuselage(float length) {
        JGL_3DMesh mesh = new JGL_3DMesh();
        JGL_3DVector p1, p2, p3, p4;
        if (vertices.size() == 0) {
            PredimRC.logln("vertices empty in fuselage");
            return mesh;
        }
        if (vertices.size() != vertices_prof.size()) {
            PredimRC.logln("vertices sizes are different in fuselage:" + vertices.size() + "," + vertices_prof.size());
            return mesh;
        }
        for (int i = 0; i < vertices_prof.size() - 1; i++) {
            vertices_prof.get(i).z = vertices_prof.get(i).y;
            vertices_prof.get(i).y = 0;
        }

        for (int i = 0; i < vertices.size() - 1; i++) {
            p1 = vertices.get(i);
            p2 = vertices.get(i + 1);
            p3 = vertices_prof.get(i);
            p4 = vertices_prof.get(i + 1);
            mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());
        }

        p1 = vertices.get(vertices.size() - 1);
        p2 = vertices.get(0);
        p3 = vertices_prof.get(vertices_prof.size() - 1);
        p4 = vertices_prof.get(0);
        mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());



        for (int i = 0; i < vertices.size() - 1; i++) {
            p3 = vertices.get(i);
            p4 = vertices.get(i + 1);
            p1 = vertices_prof.get(i);
            p2 = vertices_prof.get(i + 1);
            mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());
        }

        p3 = vertices.get(vertices.size() - 1);
        p4 = vertices.get(0);
        p1 = vertices_prof.get(vertices_prof.size() - 1);
        p2 = vertices_prof.get(0);
        mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());



        for (int i = 0; i < vertices.size() - 1; i++) {
            p1 = vertices.get(i);
            p2 = vertices.get(i + 1);
            p3 = vertices_prof.get(vertices_prof.size() - i - 1);
            p4 = vertices_prof.get(vertices_prof.size() - i - 2);
            mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());
        }

        //i have headeache
        /**
         * p1 = vertices.get(vertices.size() - 1); p2 = vertices.get(0); p3 =
         * vertices_prof.get(vertices_prof.size() - 1); p4 =
         * vertices_prof.get(0);
         * mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4,
         * color.getRed(), color.getGreen(), color.getBlue()).getFaces());
         *
         */
        for (int i = 0; i < vertices.size() - 1; i++) {
            p3 = vertices.get(vertices.size() - i - 1);
            p4 = vertices.get(vertices.size() - i - 2);
            p1 = vertices_prof.get(i);
            p2 = vertices_prof.get(i + 1);
            mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4, color.getRed(), color.getGreen(), color.getBlue()).getFaces());
        }

        /**
         * p3 = vertices.get(vertices.size() - 1); p4 = vertices.get(0); p1 =
         * vertices_prof.get(vertices_prof.size() - 1); p2 =
         * vertices_prof.get(0);
         * mesh.addFaces(predimrc.PredimRC.getRectangle(p1, p2, p3, p4,
         * color.getRed(), color.getGreen(), color.getBlue()).getFaces());
         *
         */
        return mesh;
    }
}
