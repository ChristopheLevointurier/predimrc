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
package predimrc.model.element.loader;

import javagl.jglcore.JGL_3DMesh;

/**
 * This class is used to load fuselages from .dat files
 *
 * @author Christophe Levointurier, 21 déc. 2012
 * @version
 * @see
 * @since
 */
public class FuselageLoader extends RawElementLoader {

    public FuselageLoader(String file, int r, int b, int g) {
        super("Fuselages/" + file + ".obj", r, b, g);
    }

    public JGL_3DMesh getFuselage(float length, float widthY, float widthZ) {
        //   JGL_3DMesh mesh = new JGL_3DMesh(_AppletLogo.class.getClassLoader().getResource("Fuselages/" + file + ".obj"), JGL_Data3D.OBJ).mesh));
        //  data = new JGL_3DMovable(new JGL_3DBsp(new JGL_Data3D(JGL_AppletLogo.class.getClassLoader().getResource(filename), JGL_Data3D.OBJ).mesh));
//TODOOOOOOOOOOOOOOOO
        /**
         * if (vertices.isEmpty()) { PredimRC.logln("vertices empty in fuselage!
         * " + file); return mesh; } for (int i = 0; i < vertices.size() - 1;
         * i++) { JGL_3DVector p1 = vertices.get(i); JGL_3DVector p2 =
         * vertices.get(i + 1); JGL_3DVector p3 = new JGL_3DVector(p2.x, p2.y,
         * length); JGL_3DVector p4 = new JGL_3DVector(p1.x, p1.y, length);
         * mesh.addFaces(Utils.getRectangle(p1, p2, p3, p4, color.getRed(),
         * color.getGreen(), color.getBlue()).getFaces()); }
         *
         * JGL_3DVector p1 = vertices.get(vertices.size() - 1); JGL_3DVector p2
         * = vertices.get(0); JGL_3DVector p3 = new JGL_3DVector(p2.x, p2.y,
         * length); JGL_3DVector p4 = new JGL_3DVector(p1.x, p1.y, length);
         * mesh.addFaces(Utils.getRectangle(p1, p2, p3, p4, color.getRed(),
         * color.getGreen(), color.getBlue()).getFaces()); *
         */
        return mesh;
    }
}
