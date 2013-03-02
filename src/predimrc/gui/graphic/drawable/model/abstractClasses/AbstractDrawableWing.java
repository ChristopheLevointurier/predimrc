/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic.drawable.model.abstractClasses;

/**
 *
 * @author Chris
 */
public interface AbstractDrawableWing {

    abstract public void setDihedral(float diedre);

    public void setAngle(float currentAngle);

    public float getAngle();

    public float getSweep();
}
