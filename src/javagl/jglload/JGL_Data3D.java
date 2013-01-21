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

import java.net.URL;
import java.util.Vector;
import javagl.jglcore.JGL_3DMesh;
import javagl.jglcore.JGL_Util;


/**
 * Structure containing 3D data loaded from a file :<br> - a 3D mesh<br> - a
 * list of 3D sub-meshes<br> - an animated skeleton<br> - a mapping list to link
 * the mesh vertexes to skeleton bones (if skeleton is found)<br>
 *
 * @author Nicolas Devere
 *
 */
public class JGL_Data3D {

    /**
     * Milkshape ASCII file type
     */
    public static int MILKSHAPE_ASCII = 0;
    /**
     * OBJ file type
     */
    public static int OBJ = 1;
    public static int WRML = 2;
    /**
     * The global mesh contained in the file
     */
    public JGL_3DMesh mesh;
    /**
     * All the simple meshes contained in the file
     */
    public Vector subMeshes;
    /**
     * the points/bones mapping list (for animations) contained in the file
     */
    public Vector map_points_bones;
    /**
     * Flag showing if the loading is OK
     */
    public boolean is_loaded;

    /**
     * Constructs and fills new data according to the specified URL and file
     * type (ex : JGL_Data3D.MILKSHAPE_ASCII).
     *
     * @param path : the URL of the file to load
     * @param fileType : the file type (ex : JGL_Data3D.MILKSHAPE_ASCII)
     */
    public JGL_Data3D(URL path, int fileType) {
        try {
            reset();
            JGL_Reader reader = null;
        /**    if (fileType == WRML) {
                reader = new JGL_ReaderVRML(path);
            }**/
            if (fileType == MILKSHAPE_ASCII) {
                reader = new JGL_ReaderMilkshapeAscii(path);
            }
            if (fileType == OBJ) {
                reader = new JGL_ReaderObj(path);
            }

            if (reader != null) {
                reader.getData(this);
                is_loaded = true;
            } else {
                is_loaded = false;
            }
        } catch (Exception ex) {
            reset();
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Resets the data (sets features to null)
     *
     */
    private void reset() {
        mesh = JGL_Util.getCube(1f, 1f, 1f, 255, 255, 255);
        subMeshes = new Vector();
        subMeshes.add(mesh.clone());
        map_points_bones = new Vector();
        is_loaded = false;
    }
}
