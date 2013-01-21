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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

/**
 * Class useful to rescale safely an image object.
 *
 * @author Nicolas Devere
 *
 */
public class JGL_ImageScaler implements ImageObserver, Runnable {

    private Image o;
    private Image image;
    private int width;
    private int height;
    private int style;
    private boolean finished;

    /**
     * Constructs an image scaler and scales the original image according to the
     * specified sizes and scaling style.
     *
     * @param original : the original image to scale
     * @param newWidth : the scaled image width
     * @param newHeight : the scaled image height
     * @param scaleStyle : the scaling style (for ex.
     * : <code>Image.SCALE_REPLICATE</code>)
     */
    public JGL_ImageScaler(Image original, int newWidth, int newHeight, int scaleStyle) {

        if (original == null) {
            original = Toolkit.getDefaultToolkit().createImage(new byte[0]);
        }

        o = original;
        width = newWidth;
        height = newHeight;
        style = scaleStyle;
        finished = false;
        new Thread(this).start();
        while (!finished) {
        }
    }

    public void run() {

        finished = false;
        int flag = 0;

        image = o.getScaledInstance(width, height, style);

        if (image == null) {
            endLoading(true);
            return;
        }

        if (Toolkit.getDefaultToolkit().prepareImage(image, -1, -1, this)) {
            endLoading(false);
            return;
        }

        do {
            try {
                Thread.sleep(10);
                Thread.yield();
            } catch (InterruptedException ex) {
                endLoading(false);
            }
            flag = Toolkit.getDefaultToolkit().checkImage(image, -1, -1, this);
        } while ((flag & (ImageObserver.ALLBITS | ImageObserver.ABORT | ImageObserver.ERROR)) == 0);

        if ((flag & ImageObserver.ALLBITS) != 0) {
            endLoading(false);
        } else {
            endLoading(true);
        }
    }

    private void endLoading(boolean error) {
        if (error) {
            image = o;
        }
        finished = true;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {

        switch (infoflags) {
            case ImageObserver.WIDTH | ImageObserver.HEIGHT:
                return true;

            case ImageObserver.SOMEBITS:
                return true;

            case ImageObserver.PROPERTIES:
                return true;

            case ImageObserver.FRAMEBITS:
                return false;

            case ImageObserver.ALLBITS:
                return false;

            case ImageObserver.ERROR:
                return false;

            case ImageObserver.ABORT:
                return false;
        }
        return false;
    }

    /**
     * Returns the scaled image, or the original one if a problem occurred.
     *
     * @return the scaled image, or the original one
     */
    public Image getImage() {
        return image;
    }
}
