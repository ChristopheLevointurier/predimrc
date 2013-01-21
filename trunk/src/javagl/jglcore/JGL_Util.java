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
import java.util.Random;
import java.util.Vector;

/**
 * Class gathering static utility methods for JavaGL user.
 *
 * @author Nicolas Devere
 *
 */
public class JGL_Util {

    /**
     * Computes and assigns to the meshes vector some colors given a light
     * position and a shininess factor.
     *
     * @param mesh : the meshes vector to modify
     * @param light : the light position
     * @param shininess : the shininess factor (0-100)
     * @param twoSides : true to light the reverse side as the front side, false
     * otherwise
     */
    public static final void setStaticLighting(Vector meshes, JGL_3DVector light, float shininess, boolean twoSides) {
        for (int i = 0; i < meshes.size(); i++) {
            setStaticLighting((JGL_3DMesh) meshes.get(i), light, shininess, twoSides);
        }
    }

    /**
     * Computes and assigns to the mesh some colors given a light position and a
     * shininess factor.
     *
     * @param mesh : the mesh to modify
     * @param light : the light position
     * @param shininess : the shininess factor (0-100)
     * @param twoSides : true to light the reverse side as the front side, false
     * otherwise
     */
    public static final void setStaticLighting(JGL_3DMesh mesh, JGL_3DVector light, float shininess, boolean twoSides) {
        Vector faces = mesh.getFaces();
        for (int i = 0; i < faces.size(); i++) {
            setStaticLighting((JGL_3DTriangle) faces.get(i), light, shininess, twoSides);
        }
    }

    /**
     * Computes and assigns to the triangle a color given a light position and a
     * shininess factor.
     *
     * @param t : the triangle to modify
     * @param light : the light position
     * @param shininess : the shininess factor (0-100)
     * @param twoSides : true to light the reverse side as the front side, false
     * otherwise
     */
    public static final void setStaticLighting(JGL_3DTriangle t, JGL_3DVector light, float shininess, boolean twoSides) {

        if (!t.isValid()) {
            return;
        }

        JGL_3DVector u = new JGL_3DVector();
        JGL_3DVector v = new JGL_3DVector();
        JGL_3DVector n = new JGL_3DVector();
        JGL_3DVector p = new JGL_3DVector();

        JGL_Math.vector_subtract(t.point2, t.point1, u);
        JGL_Math.vector_subtract(t.point3, t.point1, v);
        JGL_Math.vector_crossProduct(u, v, n);
        n.normalize();

        p.assign(light);
        JGL_Math.vector_multiply(p, 3f, p);
        JGL_Math.vector_subtract(p, t.point1, p);
        JGL_Math.vector_subtract(p, t.point2, p);
        JGL_Math.vector_subtract(p, t.point3, p);
        p.normalize();

        float scal = JGL_Math.vector_dotProduct(n, p);
        if (scal < 0f) {
            if (twoSides) {
                scal = -scal;
            } else {
                scal = 0f;
            }
        }
        float red = t.color.getRed();
        float green = t.color.getGreen();
        float blue = t.color.getBlue();

        shininess *= 0.01f;
        float ratio = (1f - shininess) + (shininess * scal);

        t.color = new Color((int) (red * ratio),
                (int) (green * ratio),
                (int) (blue * ratio));
    }

    /**
     * Returns a new cube with the specified offsets and color.
     *
     * @param widthOffset : the width offset
     * @param heightOffset : the height offset
     * @param depthOffset : the depth offset
     * @param r : the color red component (0 - 255)
     * @param g : the color green component (0 - 255)
     * @param b : the color blue component (0 - 255)
     * @return the new cube
     */
    public static final JGL_3DMesh getCube(float widthOffset, float heightOffset, float depthOffset, int r, int g, int b) {

        widthOffset = Math.abs(widthOffset);
        heightOffset = Math.abs(heightOffset);
        depthOffset = Math.abs(depthOffset);

        JGL_3DMesh mesh = new JGL_3DMesh();
        JGL_3DVector p1 = new JGL_3DVector(-widthOffset, -heightOffset, -depthOffset);
        JGL_3DVector p2 = new JGL_3DVector(-widthOffset, -heightOffset, depthOffset);
        JGL_3DVector p3 = new JGL_3DVector(widthOffset, -heightOffset, depthOffset);
        JGL_3DVector p4 = new JGL_3DVector(widthOffset, -heightOffset, -depthOffset);
        JGL_3DVector p5 = new JGL_3DVector(-widthOffset, heightOffset, -depthOffset);
        JGL_3DVector p6 = new JGL_3DVector(-widthOffset, heightOffset, depthOffset);
        JGL_3DVector p7 = new JGL_3DVector(widthOffset, heightOffset, depthOffset);
        JGL_3DVector p8 = new JGL_3DVector(widthOffset, heightOffset, -depthOffset);

        Color color = new Color(r, g, b);

        mesh.addFace(new JGL_3DTriangle(p1, p2, p6, color));
        mesh.addFace(new JGL_3DTriangle(p1, p6, p5, color));

        mesh.addFace(new JGL_3DTriangle(p2, p3, p7, color));
        mesh.addFace(new JGL_3DTriangle(p2, p7, p6, color));

        mesh.addFace(new JGL_3DTriangle(p3, p4, p8, color));
        mesh.addFace(new JGL_3DTriangle(p3, p8, p7, color));

        mesh.addFace(new JGL_3DTriangle(p4, p1, p5, color));
        mesh.addFace(new JGL_3DTriangle(p4, p5, p8, color));

        mesh.addFace(new JGL_3DTriangle(p4, p3, p2, color));
        mesh.addFace(new JGL_3DTriangle(p4, p2, p1, color));

        mesh.addFace(new JGL_3DTriangle(p5, p6, p7, color));
        mesh.addFace(new JGL_3DTriangle(p5, p7, p8, color));

        return mesh;
    }

    /**
     * Returns a new sphere with the specified parameters and color.
     *
     * @param radius : the sphere radius
     * @param slices : the number of vertical slices
     * @param stacks : the number of horizontal stacks
     * @param r : the color red component (0 - 255)
     * @param g : the color green component (0 - 255)
     * @param b : the color blue component (0 - 255)
     * @return the new sphere
     */
    public static final JGL_3DMesh getSphere(float radius, int slices, int stacks, int r, int g, int b) {

        int i, j;

        radius = Math.abs(radius);
        if (slices < 3) {
            slices = 3;
        }
        if (stacks < 2) {
            stacks = 2;
        }

        float wGap = 360f / (float) slices;
        float hGap = 180f / (float) stacks;

        JGL_3DMesh mesh = new JGL_3DMesh();

        Color color = new Color(r, g, b);

        JGL_3DVector up = new JGL_3DVector(0f, radius, 0f);
        JGL_3DVector dn = new JGL_3DVector(0f, -radius, 0f);
        JGL_3DVector[][] mid = new JGL_3DVector[slices][stacks - 1];
        for (i = 0; i < slices; i++) {
            for (j = 0; j < stacks - 1; j++) {
                mid[i][j] = new JGL_3DVector();
                JGL_Math.vector_fastYXrotate(up, (j + 1) * hGap, -i * wGap, mid[i][j]);
            }
        }

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(up, mid[i][0], mid[i + 1][0], color));
        }
        mesh.addFace(new JGL_3DTriangle(up, mid[slices - 1][0], mid[0][0], color));

        for (j = 0; j < stacks - 2; j++) {
            for (i = 0; i < slices - 1; i++) {
                mesh.addFace(new JGL_3DTriangle(mid[i][j], mid[i][j + 1], mid[i + 1][j + 1], color));
                mesh.addFace(new JGL_3DTriangle(mid[i][j], mid[i + 1][j + 1], mid[i + 1][j], color));
            }
            mesh.addFace(new JGL_3DTriangle(mid[slices - 1][j], mid[slices - 1][j + 1], mid[0][j + 1], color));
            mesh.addFace(new JGL_3DTriangle(mid[slices - 1][j], mid[0][j + 1], mid[0][j], color));
        }

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(mid[i][stacks - 2], dn, mid[i + 1][stacks - 2], color));
        }
        mesh.addFace(new JGL_3DTriangle(mid[slices - 1][stacks - 2], dn, mid[0][stacks - 2], color));

        return mesh;
    }

    /**
     * Returns a new vertical cylinder with the specified parameters and color.
     *
     * @param height : the cylinder vertical radius
     * @param radius : the cylinder horizontal radius
     * @param slices : the number of vertical slices
     * @param stacks : the number of horizontal stacks
     * @param r : the color red component (0 - 255)
     * @param g : the color green component (0 - 255)
     * @param b : the color blue component (0 - 255)
     * @return the new vertical cylinder
     */
    public static final JGL_3DMesh getCylinder(float height, float radius, int slices, int stacks, int r, int g, int b) {

        int i, j;

        height = Math.abs(height);
        radius = Math.abs(radius);
        if (slices < 3) {
            slices = 3;
        }
        if (stacks < 1) {
            stacks = 1;
        }

        float wGap = 360f / (float) slices;
        float hGap = (height * 2f) / (float) stacks;

        JGL_3DMesh mesh = new JGL_3DMesh();

        Color color = new Color(r, g, b);

        JGL_3DVector up = new JGL_3DVector(0f, height, 0f);
        JGL_3DVector dn = new JGL_3DVector(0f, -height, 0f);
        JGL_3DVector ed = new JGL_3DVector(0f, height, radius);

        JGL_3DVector[][] mid = new JGL_3DVector[slices][stacks + 1];
        for (j = 0; j < stacks + 1; j++) {
            for (i = 0; i < slices; i++) {
                mid[i][j] = new JGL_3DVector();
                JGL_Math.vector_fastYXrotate(ed, 0f, -i * wGap, mid[i][j]);
                mid[i][j].y -= j * hGap;
            }
        }

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(up, mid[i][0], mid[i + 1][0], color));
        }
        mesh.addFace(new JGL_3DTriangle(up, mid[slices - 1][0], mid[0][0], color));

        for (j = 0; j < stacks; j++) {
            for (i = 0; i < slices - 1; i++) {
                mesh.addFace(new JGL_3DTriangle(mid[i][j], mid[i][j + 1], mid[i + 1][j + 1], color));
                mesh.addFace(new JGL_3DTriangle(mid[i][j], mid[i + 1][j + 1], mid[i + 1][j], color));
            }
            mesh.addFace(new JGL_3DTriangle(mid[slices - 1][j], mid[slices - 1][j + 1], mid[0][j + 1], color));
            mesh.addFace(new JGL_3DTriangle(mid[slices - 1][j], mid[0][j + 1], mid[0][j], color));
        }

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(mid[i][stacks], dn, mid[i + 1][stacks], color));
        }
        mesh.addFace(new JGL_3DTriangle(mid[slices - 1][stacks], dn, mid[0][stacks], color));

        return mesh;
    }

    /**
     * Returns a new geosphere with the specified parameters and color.
     *
     * @param radius : the geosphere radius
     * @param depth : the tesselation depth
     * @param r : the color red component (0 - 255)
     * @param g : the color green component (0 - 255)
     * @param b : the color blue component (0 - 255)
     * @return the new geosphere
     */
    public static final JGL_3DMesh getGeosphere(float radius, int depth, int r, int g, int b) {

        int i;
        float dist;
        JGL_3DVector p;
        Vector points;

        radius = Math.abs(radius);
        if (depth < 0) {
            depth = 0;
        }

        float piOnFive = (float) Math.PI * 0.2f;
        float piOnTen = (float) Math.PI * 0.1f;

        float h = (float) Math.cos(piOnFive);
        float s = (float) Math.sin(piOnFive) * 2f;
        float cx = (float) Math.cos(piOnTen);
        float cz = (float) Math.sin(piOnTen);
        float h1 = (float) Math.sqrt((s * s) - 1f);
        float h2 = (float) Math.sqrt(((h + 1f) * (h + 1f)) - (h * h));
        float y2 = (h2 - h1) * 0.5f;
        float y1 = y2 + h1;

        //create the icosahedron
        JGL_3DMesh mesh = new JGL_3DMesh();
        Color color = new Color(r, g, b);

        JGL_3DVector p0 = new JGL_3DVector(0, y1, 0);
        JGL_3DVector p1 = new JGL_3DVector(0, y2, 1f);
        JGL_3DVector p2 = new JGL_3DVector(cx, y2, cz);
        JGL_3DVector p3 = new JGL_3DVector(0.5f * s, y2, -h);
        JGL_3DVector p4 = new JGL_3DVector(-0.5f * s, y2, -h);
        JGL_3DVector p5 = new JGL_3DVector(-cx, y2, cz);
        JGL_3DVector p6 = new JGL_3DVector(0, -y2, -1f);
        JGL_3DVector p7 = new JGL_3DVector(-cx, -y2, -cz);
        JGL_3DVector p8 = new JGL_3DVector(-0.5f * s, -y2, h);
        JGL_3DVector p9 = new JGL_3DVector(0.5f * s, -y2, h);
        JGL_3DVector p10 = new JGL_3DVector(cx, -y2, -cz);
        JGL_3DVector p11 = new JGL_3DVector(0, -y1, 0);

        mesh.addFace(new JGL_3DTriangle(p0, p1, p2, color));
        mesh.addFace(new JGL_3DTriangle(p0, p2, p3, color));
        mesh.addFace(new JGL_3DTriangle(p0, p3, p4, color));
        mesh.addFace(new JGL_3DTriangle(p0, p4, p5, color));
        mesh.addFace(new JGL_3DTriangle(p0, p5, p1, color));
        mesh.addFace(new JGL_3DTriangle(p1, p8, p9, color));
        mesh.addFace(new JGL_3DTriangle(p9, p2, p1, color));
        mesh.addFace(new JGL_3DTriangle(p2, p9, p10, color));
        mesh.addFace(new JGL_3DTriangle(p10, p3, p2, color));
        mesh.addFace(new JGL_3DTriangle(p3, p10, p6, color));
        mesh.addFace(new JGL_3DTriangle(p6, p4, p3, color));
        mesh.addFace(new JGL_3DTriangle(p4, p6, p7, color));
        mesh.addFace(new JGL_3DTriangle(p7, p5, p4, color));
        mesh.addFace(new JGL_3DTriangle(p5, p7, p8, color));
        mesh.addFace(new JGL_3DTriangle(p8, p1, p5, color));
        mesh.addFace(new JGL_3DTriangle(p11, p6, p10, color));
        mesh.addFace(new JGL_3DTriangle(p11, p10, p9, color));
        mesh.addFace(new JGL_3DTriangle(p11, p9, p8, color));
        mesh.addFace(new JGL_3DTriangle(p11, p8, p7, color));
        mesh.addFace(new JGL_3DTriangle(p11, p7, p6, color));

        tessellate(mesh, depth);

        points = mesh.getPoints();
        for (i = 0; i < points.size(); i++) {
            p = (JGL_3DVector) points.get(i);
            dist = radius / p.norm();
            JGL_Math.vector_multiply(p, dist, p);
        }

        return mesh;
    }

    /**
     * Returns a new vertical cone with the specified parameters and color.
     *
     * @param height : the cone height
     * @param radius : the base horizontal radius
     * @param slices : the number of vertical slices
     * @param r : the color red component (0 - 255)
     * @param g : the color green component (0 - 255)
     * @param b : the color blue component (0 - 255)
     * @return the new vertical cone
     */
    public static final JGL_3DMesh getCone(float height, float radius, int slices, int r, int g, int b) {

        int i;

        height = Math.abs(height);
        radius = Math.abs(radius);
        if (slices < 3) {
            slices = 3;
        }

        float wGap = 360f / (float) slices;

        JGL_3DMesh mesh = new JGL_3DMesh();

        Color color = new Color(r, g, b);

        JGL_3DVector up = new JGL_3DVector(0f, height, 0f);
        JGL_3DVector dn = new JGL_3DVector();
        JGL_3DVector ed = new JGL_3DVector(0f, 0f, radius);

        JGL_3DVector[] mid = new JGL_3DVector[slices];
        for (i = 0; i < slices; i++) {
            mid[i] = new JGL_3DVector();
            JGL_Math.vector_fastYXrotate(ed, 0f, -i * wGap, mid[i]);
        }

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(up, mid[i], mid[i + 1], color));
        }
        mesh.addFace(new JGL_3DTriangle(up, mid[slices - 1], mid[0], color));

        for (i = 0; i < slices - 1; i++) {
            mesh.addFace(new JGL_3DTriangle(mid[i], dn, mid[i + 1], color));
        }
        mesh.addFace(new JGL_3DTriangle(mid[slices - 1], dn, mid[0], color));

        return mesh;
    }

    /**
     * Tessellates the specified mesh : quadruplates the face count at each
     * depth iteration (the mesh is unchanged if depth<=0).
     *

     *
     * @param mesh : the mesh to tessellate
     * @param depth : the tessellate iterations number
     * @return : the tessellated mesh
     */
    public static final JGL_3DMesh tessellate(JGL_3DMesh mesh, int depth) {

        int i, j;
        Vector faces, newFaces;
        JGL_3DTriangle t;
        JGL_3DVector vn1, vn2, vn3;

        for (i = 0; i < depth; i++) {
            faces = mesh.getFaces();
            newFaces = new Vector();

            for (j = 0; j < faces.size(); j++) {
                t = (JGL_3DTriangle) faces.get(j);
                vn1 = new JGL_3DVector();
                vn2 = new JGL_3DVector();
                vn3 = new JGL_3DVector();
                JGL_Math.vector_interpolationLinear(t.point1, t.point2, 0.5f, vn1);
                JGL_Math.vector_interpolationLinear(t.point2, t.point3, 0.5f, vn2);
                JGL_Math.vector_interpolationLinear(t.point3, t.point1, 0.5f, vn3);

                newFaces.add(new JGL_3DTriangle(t.point1, vn1, vn3, t.color));
                newFaces.add(new JGL_3DTriangle(vn1, t.point2, vn2, t.color));
                newFaces.add(new JGL_3DTriangle(vn2, t.point3, vn3, t.color));
                newFaces.add(new JGL_3DTriangle(vn1, vn2, vn3, t.color));
            }
            mesh.clear();
            mesh.addFaces(newFaces);
        }

        return mesh;
    }

    /**
     * Returns a height map in a 2 dimensions array of floats.
     *
     * @param sizeX : the number of lines in the array
     * @param sizeZ : the number of columns in the array
     * @param maxHeight : the max height of the height map
     * @param hills : the number of hills
     * @param minRadius : the min radius of the hills
     * @param maxRadius : the max radius of the hills
     * @param flattening : the flattening iterations number
     * @param seed : parameter used for random generation
     * @return a 2 dimensions array of floats representing the height map
     */
    public static final float[][] getHeightMap(int sizeX, int sizeZ, float maxHeight,
            int hills, float minRadius, float maxRadius,
            int flattening, long seed) {
        int i, j, k;
        Random random;
        float radius;
        float x, z;
        float radius2;
        float dist2;
        float height;
        int xMin, xMax;
        int zMin, zMax;
        float nMin, nMax, nHeight;
        float scale;

        if (sizeX <= 1 || sizeZ <= 1) {
            return new float[0][0];
        }

        if (maxHeight < 0f) {
            maxHeight = 0f;
        }

        if (hills < 0) {
            hills = 0;
        }

        if (minRadius < JGL_Math.EPSILON) {
            minRadius = JGL_Math.EPSILON;
        }

        if (maxRadius < JGL_Math.EPSILON) {
            maxRadius = JGL_Math.EPSILON;
        }

        if (minRadius >= maxRadius) {
            maxRadius = minRadius + JGL_Math.EPSILON;
        }

        if (flattening < 0) {
            flattening = 0;
        }

        float[][] result = new float[sizeX][sizeZ];

        // Adds the hills
        random = new Random(seed);
        for (k = 0; k < hills; k++) {
            radius = (random.nextInt() * (maxRadius - minRadius) / Integer.MAX_VALUE) + minRadius;
            x = (random.nextInt() * ((sizeX + radius) - (-radius)) / Integer.MAX_VALUE) + (-radius);
            z = (random.nextInt() * ((sizeZ + radius) - (-radius)) / Integer.MAX_VALUE) + (-radius);

            radius2 = radius * radius;

            xMin = Math.round(x - radius - 1);
            xMax = Math.round(x + radius + 1);

            zMin = Math.round(z - radius - 1);
            zMax = Math.round(z + radius + 1);

            if (xMin < 0) {
                xMin = 0;
            }
            if (xMax > sizeX) {
                xMax = sizeX - 1;
            }

            if (zMin < 0) {
                zMin = 0;
            }
            if (zMax > sizeZ) {
                zMax = sizeZ - 1;
            }

            for (i = xMin; i <= xMax; i++) {
                for (j = zMin; j <= zMax; j++) {
                    dist2 = (x - i) * (x - i) + (z - j) * (z - j);
                    height = radius2 - dist2;
                    if (height > 0) {
                        result[i][j] += height;
                    }
                }
            }
        }

        //Normaizes the heightmap for a good flattening
        nMin = result[0][0];
        nMax = nMin;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeZ; j++) {
                nHeight = result[i][j];
                if (nHeight < nMin) {
                    nMin = nHeight;
                }
                if (nHeight > nMax) {
                    nMax = nHeight;
                }
            }
        }
        if (nMax != nMin) {
            for (i = 0; i < sizeX; i++) {
                for (j = 0; j < sizeZ; j++) {
                    result[i][j] = (result[i][j] - nMin) / (nMax - nMin);
                }
            }
        }

        // Flattens the heightmap
        if (flattening > 1) {
            for (i = 0; i < sizeX; i++) {
                for (j = 0; j < sizeZ; j++) {
                    float flat = 1.0f;
                    float original = result[i][j];

                    // Flatten as many times as desired;
                    for (k = 0; k < flattening; k++) {
                        flat *= original;
                    }
                    result[i][j] = flat;
                }
            }
        }

        // Scales the map according to the max height
        nMin = result[0][0];
        nMax = nMin;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeZ; j++) {
                nHeight = result[i][j];
                if (nHeight < nMin) {
                    nMin = nHeight;
                }
                if (nHeight > nMax) {
                    nMax = nHeight;
                }
            }
        }
        scale = nMax - nMin;
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeZ; j++) {
                result[i][j] = ((result[i][j] - nMin) / scale) * maxHeight;
            }
        }

        return result;
    }
}
