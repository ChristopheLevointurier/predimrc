//	Copyright 2008 - 2010 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
package javagl.jglload;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DTriangle;
import javagl.jglcore.JGL_3DVector;

/**
 * VRML 97 Files reader.
 *
 * @author Nicolas Devere
 *
 */
class JGL_ReaderVRML implements JGL_Reader {

    private static String DEF = "DEF";
    private static String USE = "USE";
    private URL url;
    private BufferedReader br;
    private Vector materialNames;
    private Vector<Color> materialColors;
    private Color currentMaterial;

    /**
     * Constructs a reader with the specified URL.
     *
     * @param path : the VRML file URL
     */
    public JGL_ReaderVRML(URL url) {
        try {
            br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        } catch (Exception i) {
            return;
        }
        materialNames = new Vector();
        materialColors = new Vector();
        currentMaterial = new Color(255, 255, 255);
    }

    public void getData(JGL_Data3D data) throws Exception {

        try {

            data.mesh = new JGL_3DMesh();
            data.subMeshes = new Vector();

            String word = "";
            JGL_AsciiFile file = new JGL_AsciiFile(br);

            while (!(word = file.lireChaine()).equals(JGL_AsciiFile.EOF)) {
                if (word.equals(DEF)) {
                    file.lireChaine();
                    String type = file.lireChaine();
                    if (type.equals("Shape")) {
                        getMesh(file, data);
                    }
                }
            }

            file.fermer();
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void getMesh(JGL_AsciiFile file, JGL_Data3D data) {

        try {
            getCurrentMaterial(file);

            JGL_3DMesh mesh = new JGL_3DMesh();

            String word = "";
            word.length();

            Vector points = new Vector();
            while (!(word = file.lireChaine()).equals("point")) {
            }

            while (!(word = file.lireChaine()).equals("]")) {
                points.add(new JGL_3DVector(file.lireNombre(), file.lireNombre(), file.lireNombre()));
            }

            Vector triangles = new Vector();
            while (!(word = file.lireChaine()).equals("coordIndex")) {
            }

            while (!(word = file.lireChaine()).equals("]")) {
                int indexA = (int) file.lireNombre();
                int indexB = (int) file.lireNombre();
                int indexC = (int) file.lireNombre();
                file.lireNombre();

                triangles.add(new JGL_3DTriangle((JGL_3DVector) points.get(indexA),
                        (JGL_3DVector) points.get(indexB),
                        (JGL_3DVector) points.get(indexC),
                        currentMaterial));
            }

            for (int i = 0; i < triangles.size(); i++) {
                JGL_3DTriangle t = (JGL_3DTriangle) triangles.get(i);
                data.mesh.addFace(t);
                mesh.addFace(t);
            }
            data.subMeshes.add(mesh);
        } catch (Exception ex) {
            return;
        }
    }

    private void getCurrentMaterial(JGL_AsciiFile file) {

        try {
            String word = file.lireChaine();
            while (!word.equals(DEF) && !word.equals(USE)) {
                word = file.lireChaine();
            }

            if (word.equals(DEF)) {
                String name = file.lireChaine();
                String word2 = file.lireChaine();
                while (!word2.equals("diffuseColor")) {
                    word2 = file.lireChaine();
                }
                int r = (int) (file.lireNombre() * 255f);
                int g = (int) (file.lireNombre() * 255f);
                int b = (int) (file.lireNombre() * 255f);
                Color color = new Color(r, g, b);
                materialNames.add(name);
                materialColors.add(color);
                currentMaterial = color;
            }

            if (word.equals(USE)) {
                String name = file.lireChaine();
                for (int i = 0; i < materialNames.size(); i++) {
                    if (name.equals((String) materialNames.get(i))) {
                        currentMaterial = materialColors.get(i);
                    }
                }
            }


        } catch (Exception ex) {
            return;
        }
    }
}
