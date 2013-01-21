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
package javagl.jglcore;

import java.util.Vector;

/**
 * Representation of a 3D basic mesh. It manages a list of faces and a list of
 * points.
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_3DMesh implements JGL_3DStruct {

    private Vector points;
    private Vector faces;

    /**
     * Constructs an empty mesh.
     */
    public JGL_3DMesh() {
        points = new Vector();
        faces = new Vector();
    }

    /**
     * Constructs a mesh with the specified list of faces.
     *
     * @param _faces : the list of faces (JGL_3DTriangle objects)
     */
    public JGL_3DMesh(Vector _faces) {
        this();
        for (int i = 0; i < _faces.size(); i++) {
            addFace((JGL_3DTriangle) _faces.get(i));
        }
    }

    /**
     * Constructs a mesh with same values than the specified mesh.
     *
     * @param arg : the mesh to copy
     */
    public JGL_3DMesh(JGL_3DMesh arg) {

        if (arg == null) {
            arg = new JGL_3DMesh();
        }

        int i;
        JGL_3DTriangle t;

        Vector _points = new Vector();
        Vector _faces = new Vector();

        Vector points_origin = arg.getPoints();
        for (i = 0; i < points_origin.size(); i++) {
            _points.add(((JGL_3DVector) points_origin.get(i)).clone());
        }

        Vector faces_origin = arg.getFaces();
        for (i = 0; i < faces_origin.size(); i++) {
            t = (JGL_3DTriangle) faces_origin.get(i);
            _faces.add(new JGL_3DTriangle((JGL_3DVector) _points.get(points_origin.indexOf(t.point1)),
                    (JGL_3DVector) _points.get(points_origin.indexOf(t.point2)),
                    (JGL_3DVector) _points.get(points_origin.indexOf(t.point3)),
                    t.color));
        }
        points = _points;
        faces = _faces;
    }

    /**
     * Adds the faces to the mesh.
     *
     * @param _faces : the faces to add
     */
    public void addFaces(Vector _faces) {
        for (int i = 0; i < _faces.size(); i++) {
            addFace((JGL_3DTriangle) _faces.get(i));
        }
    }

    /**
     * Adds a face to the mesh.
     *
     * @param face : the face to add
     */
    public void addFace(JGL_3DTriangle face) {

        if (!points.contains(face.point1)) {
            points.add(face.point1);
        }

        if (!points.contains(face.point2)) {
            points.add(face.point2);
        }

        if (!points.contains(face.point3)) {
            points.add(face.point3);
        }

        faces.add(face);
    }

    /**
     * Returns if a point with the same coordinates than the specified one is
     * already in the mesh.
     *
     * @param point : the point to test
     * @return : if there's already a similar point
     */
    public boolean isPointDuplicated(JGL_3DVector point) {

        JGL_3DVector p;
        for (int i = 0; i < points.size(); i++) {
            p = (JGL_3DVector) points.get(i);
            if (p != point && p.eq(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the faces list in the mesh.
     *
     * @return Vector
     */
    public Vector getFaces() {
        return faces;
    }

    /**
     * Returns the points list in the mesh.
     *
     * @return Vector
     */
    public Vector getPoints() {
        return points;
    }

    /**
     * Returns a new list containing the unique points, without the instances
     * with same coordinates than the ones already added.
     *
     * @return the unique points
     */
    public Vector getUniquePoints() {

        int i, j;
        JGL_3DVector p1;
        JGL_3DVector p2;
        boolean alreadyIn;
        Vector result = new Vector();

        for (i = 0; i < points.size(); i++) {
            alreadyIn = false;
            p1 = (JGL_3DVector) points.get(i);
            for (j = 0; j < result.size() && !alreadyIn; j++) {
                p2 = (JGL_3DVector) result.get(j);
                if (p1.eq(p2)) {
                    alreadyIn = true;
                }
            }
            if (!alreadyIn) {
                result.add(p1);
            }
        }

        return result;
    }

    /**
     * Removes all the faces and points from the mesh.
     */
    public void clear() {
        points.clear();
        faces.clear();
    }

    /**
     * Displays the mesh.
     */
    public void display() {
        for (int i = 0; i < faces.size(); i++) {
            ((JGL_3DTriangle) faces.get(i)).display();
        }
    }

    public void display(JGL_3DVector eye) {
        display();
    }

    public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
        display();
    }

    public JGL_3DMesh clone() {
        return new JGL_3DMesh(this);
    }
}
