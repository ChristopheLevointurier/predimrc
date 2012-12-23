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
package predimrc.model;

import java.io.Serializable;
import java.util.ArrayList;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.model.element.Fuselage;
import predimrc.model.element.Tail;
import predimrc.model.element.Wing;

/**
 * This class contains all model caracteristics for I/O
 *
 * @author Christophe Levointurier, 9 déc. 2012
 * @version
 * @see
 * @since
 */
public class Model implements Serializable {

    /**
     * *
     * Elements of the model
     *
     */
    private String name;
    private String note;
    private ArrayList<Wing> wings;
    private Tail tail;
    private Fuselage fuselage;

    public Model() {
        name = "";
        note = "";
        wings = new ArrayList<>();
        wings.add(new Wing(1, 15, 10, 40));
        wings.add(new Wing(1, 10, 8, 25));
        tail = new Tail();
        fuselage = new Fuselage();
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
        ModelController.applyChange();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        ModelController.applyChange();
    }

    public ArrayList<Wing> getWings() {
        return wings;
    }

    public Tail getTail() {
        return tail;
    }

    public void setTail(Tail tail) {
        this.tail = tail;
        ModelController.applyChange();
    }

    public Fuselage getFuselage() {
        return fuselage;
    }

    public void setFuselage(Fuselage fuselage) {
        this.fuselage = fuselage;
        ModelController.applyChange();
    }

    public void setWingSectionNumber(int _i) {
        ArrayList<Wing> wingsTemp = new ArrayList<>();
        for (int i = 0; i < _i; i++) {
            if (!wings.isEmpty()) {
                wingsTemp.add(wings.remove(0));
            } else {
                wingsTemp.add(new Wing(1, 10, 10, 25));
            }
        }
        wings = wingsTemp;
        ModelController.changeModel();
    }
}
