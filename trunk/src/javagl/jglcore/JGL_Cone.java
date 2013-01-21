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
 * Static class managing a view cone.
 *
 * @author Nicolas Devere
 *
 */
public class JGL_Cone {

    private static JGL_3DMatrix mat = new JGL_3DMatrix();
    private static JGL_3DVector[] baseCone = new JGL_3DVector[4];
    private static JGL_3DVector[] resultCone = new JGL_3DVector[4];

    static {
        for (int i = 0; i < 4; i++) {
            baseCone[i] = new JGL_3DVector();
            resultCone[i] = new JGL_3DVector();
        }
    }

    /**
     * Computes the base view cone according to a depth and current JGL
     * settings.
     *
     * @param depth : the cone depth (must be a negative value)
     */
    public static void computeViewCone(float depth) {

        if (depth >= 0f) {
            depth = -1f;
        }

        float ratio = -depth / JGL.getFocal();
        float w = (JGL.getWidth() * 0.5f) * ratio;
        float h = (JGL.getHeight() * 0.5f) * ratio;

        baseCone[0].assign(-w, h, depth);
        baseCone[1].assign(-w, -h, depth);
        baseCone[2].assign(w, -h, depth);
        baseCone[3].assign(w, h, depth);
    }

    /**
     * Returns the current view cone according to the specified position and
     * orientation.
     *
     * @param position : the view cone position
     * @param orientation : the view cone XYZ orientation
     * @return the current view cone
     */
    public static JGL_3DVector[] getUpdatedViewCone(JGL_3DVector position, JGL_3DVector orientation) {

        mat.identity();
        mat.translate(position.x, position.y, position.z);
        mat.rotate(orientation.x, orientation.y, orientation.z, JGL.YXZ);

        for (int i = 0; i < 4; i++) {
            resultCone[i].x = (mat.m11 * baseCone[i].x) + (mat.m12 * baseCone[i].y) + (mat.m13 * baseCone[i].z) + (mat.m14);
            resultCone[i].y = (mat.m21 * baseCone[i].x) + (mat.m22 * baseCone[i].y) + (mat.m23 * baseCone[i].z) + (mat.m24);
            resultCone[i].z = (mat.m31 * baseCone[i].x) + (mat.m32 * baseCone[i].y) + (mat.m33 * baseCone[i].z) + (mat.m34);
        }
        return resultCone;
    }
}
