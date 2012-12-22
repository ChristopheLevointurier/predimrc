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

import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier,  12 déc. 2012
 * @version
 * @see
 * @since 
 */
public class Wing  extends ModelElement{

    
    float diedre;
    //width_1 is nearest the fuselage
    float width_1, width_2,lenght;
    
    
    
    public Wing()
    {
        diedre=2f;
        width_1=13f;
        width_2=10f;
        lenght=30f;
    }

    public Wing(float _diedre, float _width_1, float _width_2, float _lenght) {
        diedre = _diedre;
        width_1 = _width_1;
        width_2 = _width_2;
        lenght = _lenght;
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
    }

    public void setWidth_1(float width_1) {
        this.width_1 = width_1;
    }

    public void setWidth_2(float width_2) {
        this.width_2 = width_2;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }
    
}
