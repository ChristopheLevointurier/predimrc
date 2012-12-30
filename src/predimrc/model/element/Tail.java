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

import java.util.ArrayList;
import predimrc.controller.ModelController;
import predimrc.model.ModelElement;
import predimrc.model.element.WingSection.USED_FOR;

/**
 *
 * @author Christophe Levointurier, 12 déc. 2012
 * @version
 * @see
 * @since
 */
public class Tail extends ModelElement {

    private ArrayList<WingSection> horizontal;
    //vertical_height_1 is nearest the cokpit
    private float vertical_height_1, vertical_height_2;
    //vertical_length_1 is nearest the fuselage
    private float vertical_length_1, vertical_length_2;
    private boolean exist;

    public Tail() {
        horizontal = new ArrayList<>();

        WingSection first = new WingSection(0, 0, 20, 20, 35, USED_FOR.HORIZONTAL_PLAN);
        first.setPosXY(405, 355);
        horizontal.add(first);
        vertical_height_1 = 2f;
        vertical_height_2 = 2f;
        vertical_length_1 = 2f;
        vertical_length_2 = 2f;
        exist = true;
    }

    public Tail makeEmptyTail() {
        Tail t = new Tail();
        t.setHorizontal(new ArrayList<WingSection>());
        t.setVertical_height_1(0f);
        t.setVertical_height_2(0f);
        t.setVertical_length_1(0f);
        t.setVertical_length_2(0f);
        exist = false;
        return t;
    }

    public Tail(ArrayList<WingSection> horizontal, float vertical_height_1, float vertical_height_2, float vertical_length_1, float vertical_length_2) {
        this.horizontal = horizontal;
        this.vertical_height_1 = vertical_height_1;
        this.vertical_height_2 = vertical_height_2;
        this.vertical_length_1 = vertical_length_1;
        this.vertical_length_2 = vertical_length_2;
    }

    public ArrayList<WingSection> getHorizontal() {
        return horizontal;
    }

    public float getVertical_height_1() {
        return vertical_height_1;
    }

    public void setVertical_height_1(float vertical_height_1) {
        this.vertical_height_1 = vertical_height_1;
        ModelController.applyChange();
    }

    public float getVertical_height_2() {
        return vertical_height_2;
    }

    public void setVertical_height_2(float vertical_height_2) {
        this.vertical_height_2 = vertical_height_2;
        ModelController.applyChange();
    }

    public float getVertical_length_1() {
        return vertical_length_1;
    }

    public void setVertical_length_1(float vertical_length_1) {
        this.vertical_length_1 = vertical_length_1;
        ModelController.applyChange();
    }

    public float getVertical_length_2() {
        return vertical_length_2;
    }

    public void setVertical_length_2(float vertical_length_2) {
        this.vertical_length_2 = vertical_length_2;
        ModelController.applyChange();
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
        ModelController.changeModel();
    }

    public void setTailWingNumber(int _i) {
        ArrayList<WingSection> wingsTemp = new ArrayList<>();
        for (int i = 0; i < _i; i++) {
            if (!horizontal.isEmpty()) {
                wingsTemp.add(horizontal.remove(0));
            } else {
                wingsTemp.add(new WingSection(1, 0, 10, 10, 20, USED_FOR.HORIZONTAL_PLAN));
            }
        }
        horizontal = wingsTemp;
        ModelController.changeModel();
    }

    private void setHorizontal(ArrayList<WingSection> arrayList) {
        horizontal = arrayList;
    }

    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
        for (WingSection w : (ArrayList<WingSection>) horizontal.clone()) {
            w.computePositions();
        }
    }

    public void setDiedre(float diedre) {
        for (WingSection w : (ArrayList<WingSection>) horizontal.clone()) {
            w.setDiedre(diedre);
        }
    }
}
