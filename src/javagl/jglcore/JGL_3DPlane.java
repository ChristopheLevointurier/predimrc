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
 * Representation of a plane in a 3D space.
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_3DPlane {

    /**
     * Normal vector
     */
    public JGL_3DVector normal;
    /**
     * Position constant
     */
    public float constant;
    /**
     * Point in the plan
     */
    public JGL_3DVector point;

    /**
     * Constructs a plane given 3 points. The plane's front side is the one
     * where the points are disposed in the counter clockwise order.
     *
     * @param p1 : the first point
     * @param p2 : the second point
     * @param p3 : the third point
     */
    public JGL_3DPlane(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3) {
        normal = new JGL_3DVector();
        point = new JGL_3DVector();
        assign(p1, p2, p3);
    }

    /**
     * Constructs a plane given its normal values.
     *
     * @param a : the normal vector's X value
     * @param b : the normal vector's Y value
     * @param c : the normal vector's Z value
     */
    public JGL_3DPlane(float a, float b, float c) {
        normal = new JGL_3DVector(a, b, c);
        normal.normalize();
        point = new JGL_3DVector();
        constant = 0f;
    }

    /**
     * Constructs a plane with same values than the specified plane.
     *
     * @param arg : the plane to copy
     */
    public JGL_3DPlane(JGL_3DPlane arg) {
        normal = arg.normal.clone();
        constant = arg.constant;
        point = arg.point.clone();
    }

    /**
     * Assigns a new plane defined by the specified 3 points.
     *
     * @param p1 : the first point
     * @param p2 : the second point
     * @param p3 : the third point
     */
    public void assign(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3) {

        float x1 = p2.x - p1.x;
        float y1 = p2.y - p1.y;
        float z1 = p2.z - p1.z;

        float x2 = p3.x - p1.x;
        float y2 = p3.y - p1.y;
        float z2 = p3.z - p1.z;

        normal.x = (y1 * z2) - (y2 * z1);
        normal.y = (z1 * x2) - (z2 * x1);
        normal.z = (x1 * y2) - (x2 * y1);
        normal.normalize();

        constant = -((normal.x * p1.x)
                + (normal.y * p1.y)
                + (normal.z * p1.z));
        point.assign(p1);
    }

    /**
     * Assigns values of the specified normal to this plane.
     *
     * @param a : the normal vector's X value
     * @param b : the normal vector's Y value
     * @param c : the normal vector's Z value
     */
    public void assign(float a, float b, float c) {
        normal.assign(a, b, c);
        constant = 0f;
        point.assign(0f, 0f, 0f);
    }

    /**
     * Assigns values of the specified plane to this plane.
     *
     * @param arg : the plane to assign
     */
    public void assign(JGL_3DPlane arg) {
        normal.assign(arg.normal);
        constant = arg.constant;
        point.assign(arg.point);
    }

    /**
     * Returns if the specified plane is equal to this plane.
     *
     * @param arg : the plane to test
     * @return if the specified plane is equal to this plane
     */
    public boolean eq(JGL_3DPlane arg) {
        return normal.eq(arg.normal) && Math.abs(constant - arg.constant) < JGL_Math.EPSILON;
    }

    /**
     * Returns the distance between the specified point and the plane.
     *
     * @param p : the point
     * @return the distance between the specified point and the plane
     */
    public float distance(JGL_3DVector p) {
        return (normal.x * p.x) + (normal.y * p.y) + (normal.z * p.z) + constant;
    }

    /**
     * Returns if the specified point is before the plane.
     *
     * @param p : the point
     * @return true if the point is before the plane (distance
     * GT <code>JGL_Math.EPSILON</code>), false otherwise
     */
    public boolean before(JGL_3DVector p) {
        return distance(p) >= JGL_Math.EPSILON;
    }

    /**
     * Returns if the specified point is inside the plane.
     *
     * @param p : the point
     * @return true if the point is inside the plane (absolute value of the
     * distance LT -<code>JGL_Math.EPSILON</code>), false otherwise
     */
    public boolean inside(JGL_3DVector p) {
        return Math.abs(distance(p)) < JGL_Math.EPSILON;
    }

    /**
     * Returns if the specified point is behind the plane.
     *
     * @param p : the point
     * @return true if the point is before the plane (distance LT
     * -<code>JGL_Math.EPSILON</code>), false otherwise
     */
    public boolean behind(JGL_3DVector p) {
        return distance(p) <= -JGL_Math.EPSILON;
    }

    public JGL_3DPlane clone() {
        return new JGL_3DPlane(this);
    }
}
