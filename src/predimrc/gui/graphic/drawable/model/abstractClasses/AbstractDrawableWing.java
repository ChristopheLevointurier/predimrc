/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic.drawable.model.abstractClasses;

import predimrc.gui.graphic.drawable.model.DrawablePoint;

/**
 *
 * @author Chris
 */
public interface AbstractDrawableWing {

    abstract public void setDiedre(float diedre);

    abstract public DrawablePoint getFrontPoint();

    abstract public DrawablePoint getBackPoint();

    abstract public DrawablePoint getDiedrePoint();
}
