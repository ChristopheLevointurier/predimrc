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

import java.awt.Color;

/**
 * Representation of a basic 3D triangle. A
 * <code>JGL_3DTriangle</code> stores 3 points (
 * <code>JGL_3DVector</code> objects) and one color (
 * <code>java.awt.Color</code> object).
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_3DTriangle {

    /**
     * First point
     */
    public JGL_3DVector point1;
    /**
     * Second point
     */
    public JGL_3DVector point2;
    /**
     * Third point
     */
    public JGL_3DVector point3;
    /**
     * Display color
     */
    public Color color;

    /**
     * Constructs a white triangle with the specified 3 points.
     *
     * @param p1 : the first point
     * @param p2 : the second point
     * @param p3 : the third point
     */
    public JGL_3DTriangle(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3) {
        this(p1, p2, p3, new Color(255, 255, 255));
    }

    /**
     * Constructs a triangle with the specified 3 points and color.
     *
     * @param p1 : the first point
     * @param p2 : the second point
     * @param p3 : the third point
     * @param c : the display color
     */
    public JGL_3DTriangle(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3, Color c) {
        point1 = p1;
        point2 = p2;
        point3 = p3;

        color = c;
    }

    /**
     * Constructs a triangle with same values than the specified triangle.
     *
     * @param arg : the triangle to copy
     */
    public JGL_3DTriangle(JGL_3DTriangle arg) {
        point1 = arg.point1.clone();
        point2 = arg.point2.clone();
        point3 = arg.point3.clone();

        color = new Color(arg.color.getRGB());
    }

    /**
     * Assigns values of the specified 3 points to this triangle.
     *
     * @param p1 : the first point
     * @param p2 : the second point
     * @param p3 : the third point
     */
    public void assign(JGL_3DVector p1, JGL_3DVector p2, JGL_3DVector p3) {
        point1.assign(p1);
        point2.assign(p2);
        point3.assign(p3);
    }

    /**
     * Returns if the triangle has a valid shape.
     *
     * @return if the triangle has a valid shape
     */
    public boolean isValid() {
        return !(point1.eq(point2) || point2.eq(point3) || point1.eq(point3));
    }

    /**
     * Displays the triangle.
     */
    public void display() {

        JGL.setColor(color);
        JGL.displayTriangle(point1.x, point1.y, point1.z,
                point2.x, point2.y, point2.z,
                point3.x, point3.y, point3.z);

    }

    public JGL_3DTriangle clone() {
        return new JGL_3DTriangle(this);
    }
}
