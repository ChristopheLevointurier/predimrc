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
 * Class representing a sorted world of JGL_3DMovable objects. These ones are
 * sorted from near to far according to an eye position.
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_Sorter implements JGL_3DStruct {

    private Vector list;

    /**
     * Constructs an empty sorter.
     */
    public JGL_Sorter() {

        list = new Vector();
    }

    /**
     * Adds an object and sorts it according to the specified eye.
     *
     * @param movable : the object to sort
     * @param eye : the eye position
     */
    public void add(JGL_3DMovable movable, JGL_3DVector eye) {

        float d;
        int i = 0;
        movable.setEyeSquareDistance(eye);
        if (!list.isEmpty()) {
            d = movable.getEyeSquareDistance();
            while (i < list.size() && d < ((JGL_3DMovable) list.get(i)).getEyeSquareDistance()) {
                i++;
            }
        }
        list.add(i, movable);
    }

    /**
     * Sorts the objects world.
     *
     * @param eye : the eye position
     */
    public void sort(JGL_3DVector eye) {

        int i, j;
        int size = list.size();
        for (i = 0; i < size; i++) {
            ((JGL_3DMovable) list.get(i)).setEyeSquareDistance(eye);
        }
        for (i = 0; i < size; i++) {
            for (j = i; j > 0 && ((JGL_3DMovable) list.get(j - 1)).getEyeSquareDistance() < ((JGL_3DMovable) list.get(j)).getEyeSquareDistance(); j--) {
                list.set(j - 1, list.set(j, (JGL_3DMovable) list.get(j - 1)));
            }
        }
    }

    /**
     * Removes all objects.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Returns the objects list in the current state.
     *
     * @return the objects list
     */
    public Vector getList() {
        return list;
    }

    public void display(JGL_3DVector eye) {

        for (int i = 0; i < list.size(); i++) {
            ((JGL_3DMovable) list.get(i)).display(eye);
        }
    }

    public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
        for (int i = 0; i < list.size(); i++) {
            ((JGL_3DMovable) list.get(i)).display(eye, cone);
        }
    }

    public JGL_Sorter clone() {

        JGL_Sorter newSorter = new JGL_Sorter();
        for (int i = 0; i < list.size(); i++) {
            newSorter.list.add(((JGL_3DMovable) list.get(i)).clone());
        }
        return newSorter;
    }
}
