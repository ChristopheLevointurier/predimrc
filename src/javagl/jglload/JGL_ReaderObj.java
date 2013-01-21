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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.Color;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_3DTriangle;
import javagl.jglcore.JGL_3DVector;


/**
 * OBJ Files reader.
 *
 * @author Nicolas Devere
 *
 */
class JGL_ReaderObj implements JGL_Reader {

    private URL url;
    private BufferedReader br;
    private Vector points;
    private Vector materials;
    private JGL_3DMesh mesh;
    private Vector subMeshes;

    /**
     * Class representing a diffuse color material.
     *
     * @author Nicolas Devere
     *
     */
    class JGL_ObjMaterial {

        public String name;
        public Color color;

        /**
         * Constructs a new color material.
         *
         * @param id : the material name
         * @param red : the red value in a [0-1] range
         * @param green : the green value in a [0-1] range
         * @param blue : the blue value in a [0-1] range
         */
        public JGL_ObjMaterial(String id, float red, float green, float blue) {
            name = id;
            color = new Color(red, green, blue);
        }
    }

    /**
     * Constructs a reader with the specified URL.
     *
     * @param path : the OBJ file URL
     */
    public JGL_ReaderObj(URL path) {

        url = path;

        points = new Vector();
        materials = new Vector();

        mesh = new JGL_3DMesh();
        subMeshes = new Vector();
    }

    public void getData(JGL_Data3D data) throws Exception {
        try {

            String line = "";
            StringTokenizer st;
            StringTokenizer st2;
            br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

            JGL_ObjMaterial curMat = null;
            JGL_3DMesh curMesh = new JGL_3DMesh();

            while ((line = br.readLine()) != null) {

                st = new StringTokenizer(line);
                if (st.hasMoreElements()) {
                    String tag = st.nextToken();
                    if (tag.equals("mtllib")) {
                        getMaterials(st.nextToken());
                    }
                    if (tag.equals("v")) {
                        points.add(new JGL_3DVector(Float.parseFloat(st.nextToken()),
                                Float.parseFloat(st.nextToken()),
                                Float.parseFloat(st.nextToken())));
                    }
                    if (tag.equals("g") || tag.equals("o")) {
                        if (curMesh != null) {
                            subMeshes.add(curMesh);
                        }
                        curMesh = new JGL_3DMesh();
                    }
                    if (tag.equals("usemtl")) {
                        curMat = searchMaterial(st.nextToken());
                    }
                    if (tag.equals("f")) {
                        st2 = new StringTokenizer(st.nextToken(), "/");
                        int p1 = Integer.parseInt(st2.nextToken());
                        st2 = new StringTokenizer(st.nextToken(), "/");
                        int p2 = Integer.parseInt(st2.nextToken());
                        st2 = new StringTokenizer(st.nextToken(), "/");
                        int p3 = Integer.parseInt(st2.nextToken());
                        JGL_3DTriangle tr = new JGL_3DTriangle((JGL_3DVector) points.get(p1 - 1),
                                (JGL_3DVector) points.get(p2 - 1),
                                (JGL_3DVector) points.get(p3 - 1));
                        if (curMat != null) {
                            tr.color = curMat.color;
                        }
                        mesh.addFace(tr);
                        curMesh.addFace(tr);
                    }
                }
            }
            br.close();

            data.mesh = mesh;
            data.subMeshes = subMeshes;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Loads all the materials from the associated file .mtl.
     *
     * @throws Exception
     */
    private void getMaterials(String file) throws Exception {

        try {
            String line = "";

            String objfile = "";
            StringTokenizer urlst = new StringTokenizer(url.getPath(), "/");
            while (urlst.hasMoreTokens()) {
                objfile = urlst.nextToken();
            }

            URL urlm = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath().replace(objfile, file));
            BufferedReader brm = new BufferedReader(new InputStreamReader(urlm.openConnection().getInputStream()));

            String curName = "";

            while ((line = brm.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreElements()) {
                    String tag = st.nextToken();
                    if (tag.equals("newmtl")) {
                        curName = st.nextToken();
                    }
                    if (tag.equals("Kd")) {
                        materials.add(new JGL_ObjMaterial(curName, Float.parseFloat(st.nextToken()),
                                Float.parseFloat(st.nextToken()),
                                Float.parseFloat(st.nextToken())));
                    }
                }
            }
            brm.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return;
        }
    }

    /**
     * Returns the material stored under the specified name, or null if not
     * found.
     *
     * @param name : the material name
     * @return the material, or null
     */
    private JGL_ObjMaterial searchMaterial(String name) {

        JGL_ObjMaterial mat;
        for (int i = 0; i < materials.size(); i++) {
            mat = (JGL_ObjMaterial) materials.get(i);
            if (name.equals(mat.name)) {
                return mat;
            }
        }
        return null;
    }
}
