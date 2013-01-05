/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.model.element;

import predimrc.controller.ModelController;
import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class WingSection extends ModelElement {

    //position x,y,z of the wing is the front first point.
    //diedre & fleche are in degree.
    protected float diedre, fleche;
    //width_1 is nearest the fuselage
    protected float width_1, width_2, lenght;

    public WingSection() {
        fleche = 0f;
        diedre = 2f;
        width_1 = 55f;
        width_2 = 65f;
        lenght = 60f;
    }

    public WingSection(float _diedre, float _fleche, float _width_1, float _width_2, float _lenght) {
        diedre = _diedre;
        fleche = _fleche;
        width_1 = _width_1;
        width_2 = _width_2;
        lenght = _lenght;
    }

    public WingSection(WingSection _in) {
        diedre = _in.getDiedre();
        fleche = _in.getFleche();
        width_1 = _in.getWidth_1();
        width_2 = _in.getWidth_2();
        lenght = _in.getLenght();
    }

    public float getDiedre() {
        return diedre;
    }

    public float getWidth_1() {
        return width_1;
    }

    public float getWidth_2() {
        return width_2;
    }

    public float getLenght() {
        return lenght;
    }

    public void setDiedre(float diedre) {
        this.diedre = diedre;
        //TODO calc and apply new coord for the rest of the wing
        ModelController.applyChange();
    }

    public void setWidth_1(float width_1) {
        this.width_1 = width_1;
        ModelController.applyChange();
    }

    public void setWidth_2(float width_2) {
        this.width_2 = width_2;
        ModelController.applyChange();
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
        ModelController.applyChange();
    }

    public float getFleche() {
        return fleche;
    }

    public void setFleche(float fleche) {
        this.fleche = fleche;
        ModelController.applyChange();
    }

    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
    }
}
