/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic.drawable.model;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Chris
 */
public interface IDrawableObject {

    public void drawTop(Graphics2D g);

    public void drawLeft(Graphics2D g);

    public void drawFront(Graphics2D g);
}
