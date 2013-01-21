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

/**
 * Class Providing methods to move a JGL_3DMesh object. It is possible, after
 * moving the mesh, to get the list of moved points or moved faces (different
 * instances than the original mesh).
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_MeshMover {

    private JGL_3DMesh m1;
    private JGL_3DMesh m2;
    private JGL_3DMatrix mat;
    private boolean changed;

    /**
     * Constructs a mesh mover with the specified mesh.
     *
     * @param mesh : the mesh we can move
     */
    public JGL_MeshMover(JGL_3DMesh mesh) {

        mat = new JGL_3DMatrix();
        setMesh(mesh);
    }

    /**
     * Sets the original mesh to be moved.
     *
     * @param mesh : the original mesh to be moved
     */
    public void setMesh(JGL_3DMesh mesh) {
        m1 = mesh;
        m2 = m1.clone();
        changed = false;
    }

    /**
     * Returns the original mesh.
     *
     * @return the original mesh
     */
    public JGL_3DMesh getMesh1() {
        return m1;
    }

    /**
     * Returns the moved mesh (different instance than original mesh).
     *
     * @return the moved mesh
     */
    public JGL_3DMesh getMesh2() {

        JGL_3DVector p1, p2;

        if (changed) {
            for (int i = 0; i < m1.getPoints().size(); i++) {
                p1 = (JGL_3DVector) (m1.getPoints().get(i));
                p2 = (JGL_3DVector) (m2.getPoints().get(i));

                p2.x = (mat.m11 * p1.x) + (mat.m12 * p1.y) + (mat.m13 * p1.z) + mat.m14;
                p2.y = (mat.m21 * p1.x) + (mat.m22 * p1.y) + (mat.m23 * p1.z) + mat.m24;
                p2.z = (mat.m31 * p1.x) + (mat.m32 * p1.y) + (mat.m33 * p1.z) + mat.m34;
            }
            changed = false;
        }
        return m2;
    }

    /**
     * Sets the mesh mover to identity (no move).
     *
     * @return this mesh mover object
     */
    public JGL_MeshMover identity() {
        mat.identity();
        changed = true;
        return this;
    }

    /**
     * Applies a translation to the mesh mover.
     *
     * @param x : the X axis translation value
     * @param y : the Y axis translation value
     * @param z : the Z axis translation value
     * @return this mesh mover object
     */
    public JGL_MeshMover translate(float x, float y, float z) {
        mat.translate(x, y, z);
        changed = true;
        return this;
    }

    /**
     * Applies a rotation (in degrees) to the mesh mover around the specified
     * axis.
     *
     * @param angle : the rotation angle (in degrees)
     * @param x : the rotation axis's X value
     * @param y : the rotation axis's Y value
     * @param z : the rotation axis's Z value
     * @param normalizedAxis : sets if the axis is normalized (witch accelerates
     * compute), or not
     * @return this mesh mover object
     */
    public JGL_MeshMover rotate(float angle, float x, float y, float z, boolean normalizedAxis) {
        mat.rotate(angle, x, y, z, normalizedAxis);
        changed = true;
        return this;
    }

    /**
     * Applies a rotation to the mesh mover with the specified Euler angles, in
     * the chosen order (the angles order is given by constants from the
     * <code>jglcore.JGL</code> class. Ex :
     * <code>JGL.ZXY</code> means that the matrix is rotated around Z, then X,
     * then Y).
     *
     * @param angleX : the rotation angle around the X axis
     * @param angleY : the rotation angle around the Y axis
     * @param angleZ : the rotation angle around the Z axis
     * @param order : the 3 axis rotations order
     * @return this mesh mover object
     */
    public JGL_MeshMover rotate(float angleX, float angleY, float angleZ, int order) {
        mat.rotate(angleX, angleY, angleZ, order);
        changed = true;
        return this;
    }

    /**
     * Applies a scale transformation on 3 axis to the mesh mover.
     *
     * @param x : the X axis scale value
     * @param y : the Y axis scale value
     * @param z : the Z axis scale value
     * @return this mesh mover object
     */
    public JGL_MeshMover scale(float x, float y, float z) {
        mat.scale(x, y, z);
        changed = true;
        return this;
    }
}
