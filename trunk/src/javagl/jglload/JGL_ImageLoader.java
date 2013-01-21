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
import java.net.URL;

/**
 * Class useful to load an image object according to its URL.
 *
 * @author Nicolas Devere
 *
 */
public class JGL_ImageLoader implements ImageObserver, Runnable {

    private URL url;
    private Image image;
    private boolean finished;

    /**
     * Constructs an image loader and loads the image according to the specified
     * path.
     *
     * @param path : the path to the image to load
     */
    public JGL_ImageLoader(URL path) {

        url = path;
        finished = false;
        new Thread(this).start();
        while (!finished) {
        }
    }

    public void run() {

        finished = false;
        int flag = 0;

        image = Toolkit.getDefaultToolkit().getImage(url);

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
            image = Toolkit.getDefaultToolkit().createImage(new byte[0]);
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
     * Returns the loaded image, or an empty one if a problem occurred.
     *
     * @return the loaded image, or an empty one
     */
    public Image getImage() {
        return image;
    }
}
