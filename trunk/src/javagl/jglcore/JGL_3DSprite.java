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

import java.awt.Image;
import java.awt.Toolkit;
import javagl.jglload.JGL_ImageScaler;

/**
 * Class representing 3D sprites objects.
 *
 * @author Nicolas Devere
 *
 */
public class JGL_3DSprite implements JGL_3DStruct {

    private JGL_3DVector pos;
    private Image[] mipmaps;
    private float h;

    /**
     * COnstrucst a 3D sprite.
     *
     * @param position : the 3D position of the center of the sprite.
     * @param image : the biggest image of the sprite
     * @param nbMipmaps : number of reduced images until 1 pixel height image
     * @param height : the image height in the 3D world
     */
    public JGL_3DSprite(JGL_3DVector position, Image image, int nbMipmaps, float height) {

        setPosition(position);

        if (image == null) {
            image = Toolkit.getDefaultToolkit().createImage(new byte[0]);
        }

        if (nbMipmaps < 1) {
            nbMipmaps = 1;
        }

        mipmaps = new Image[nbMipmaps];

        int maxWidth = image.getWidth(null);
        int maxHeight = image.getHeight(null);

        float scaleImageWidth = maxWidth / (float) nbMipmaps;
        float scaleImageHeight = maxHeight / (float) nbMipmaps;

        mipmaps[0] = image;
        for (int i = 1; i < nbMipmaps; i++) {
            mipmaps[i] = new JGL_ImageScaler(image,
                    maxWidth - (int) (scaleImageWidth * i),
                    maxHeight - (int) (scaleImageHeight * i),
                    Image.SCALE_REPLICATE).getImage();
        }

        setHeight(height);
    }

    /**
     * Constructs a new sprite at the specified position but with the same
     * shared mipmap images than the specified sprite.
     *
     * @param position : the sprite position
     * @param sprite : the object which shares its images
     */
    public JGL_3DSprite(JGL_3DVector position, JGL_3DSprite sprite) {

        if (sprite == null) {
            sprite = new JGL_3DSprite(new JGL_3DVector(), Toolkit.getDefaultToolkit().createImage(new byte[0]), 1, 1f);
        }

        setPosition(position);

        mipmaps = sprite.mipmaps;
        setHeight(sprite.h);
    }

    /**
     * Assigns the specified position vector to this sprite.
     *
     * @param position : the new position vector
     */
    public void setPosition(JGL_3DVector position) {

        if (position == null) {
            position = new JGL_3DVector();
        }
        pos = position;
    }

    /**
     * Returns the sprite position (center of the billboard).
     *
     * @return the sprite position (center of the billboard)
     */
    public JGL_3DVector getPosition() {
        return pos;
    }

    /**
     * Assigns a new 3D height to the sprite.
     *
     * @param height : the new height in the 3D world
     */
    public void setHeight(float height) {

        height = Math.abs(height);
        h = height;
    }

    /**
     * Returns the sprite height in the 3D world.
     *
     * @return the sprite height in the 3D world
     */
    public float getHeight() {
        return h;
    }

    public void display(JGL_3DVector eye) {
        JGL.displaySprite(mipmaps, h, pos.x, pos.y, pos.z);
    }

    public void display(JGL_3DVector eye, JGL_3DVector[] cone) {
        display(eye);
    }

    public JGL_3DSprite clone() {
        return new JGL_3DSprite(pos.clone(), this);
    }
}
