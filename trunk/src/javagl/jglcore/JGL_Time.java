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
 * Real-time manager.
 *
 * @author Nicolas Devere
 *
 */
public final class JGL_Time {

    private static float RESOLUTION = (1f / 1000000000l) * 50f;
    private static long MIN_TIME = 10000000l;
    private static long msStart;
    private static float rtFactor;

    /**
     * Resets the real time system.
     */
    public static final void initTimer() {
        msStart = System.nanoTime();
        rtFactor = 1f;
    }

    /**
     * Computes the real time factor since the last call of this method. A 1
     * value means a 50 frames/second frame-rate.
     */
    public static final void setTimer() {
        long time = System.nanoTime();
        long diff = time - msStart;

        if (diff < MIN_TIME) {
            while (diff + (System.nanoTime() - time) < MIN_TIME) {
            }
        }

        rtFactor = diff * RESOLUTION;
        msStart = time;
    }

    /**
     * Returns the real-time factor. A 1 value means a 50 frames/second
     * frame-rate.
     *
     * @return the real-time factor
     */
    public static final float getTimer() {
        return rtFactor;
    }
}
