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
import javax.swing.text.DefaultStyledDocument;
import predimrc.model.element.Fuselage;
import predimrc.model.element.Wing;

/**
 * This class contains all model caracteristics for I/O
 *
 * @author Christophe Levointurier, 9 déc. 2012
 * @version
 * @see
 * @since
 */
public class Model extends ModelElement implements Serializable {

    /**
     * *
     * Elements of the model
     *
     */
    private String name;
    private DefaultStyledDocument note;
    private ArrayList<Wing> wings;
    private ArrayList<Wing> tail;
    private ArrayList<Wing> derive;
    private Fuselage fuselage;
    private float staticMargin;
    private float czAdjustment;

    public Model(String _filename, String _name, DefaultStyledDocument _note, ArrayList<Wing> _wings, ArrayList<Wing> _tail, ArrayList<Wing> _derive, Fuselage _fuselage, float _margeStatiqueDeCentrage, float _czCalage) {
        name = _name;
        note = _note;
        wings = _wings;
        tail = _tail;
        derive = _derive;
        fuselage = _fuselage;
        filename = _filename;
        staticMargin = _margeStatiqueDeCentrage;
        czAdjustment = _czCalage;
    }

    /**
     * Getters
     */
    public String getName() {
        return name;
    }

    public DefaultStyledDocument getNote() {
        return note;
    }

    public ArrayList<Wing> getWings() {
        return wings;
    }

    public ArrayList<Wing> getTail() {
        return tail;
    }

    public ArrayList<Wing> getDerive() {
        return derive;
    }

    public Fuselage getFuselage() {
        return fuselage;
    }

    public float getStaticMargin() {
        return staticMargin;
    }

    public void setStaticMargin(float staticMargin) {
        this.staticMargin = staticMargin;
    }

    public float getCzAdjustment() {
        return czAdjustment;
    }

    public void setCzAdjustment(float czAdjustment) {
        this.czAdjustment = czAdjustment;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("Model:");
        ret.append(name);
        ret.append(", -->Fuselage:").append(fuselage);
        ret.append(", -->Wings:").append(wings.size());
        ret.append(", -->Tails:").append(tail.size());
        ret.append(",Derive:").append(derive.size());
        ret.append(",statiqueMargin:").append(staticMargin);
        ret.append(",CzAdjutment:").append(czAdjustment);
        return ret.toString();
    }

    @Override
    public String toStringAll() {
        StringBuilder ret = new StringBuilder("Model:");
        ret.append(name);
        ret.append("\n -->Fuselage:").append(fuselage);
        ret.append("\n -->Wings:");
        for (Wing w : wings) {
            ret.append(w.toStringAll());
        }
        ret.append("\n -->Tails:");
        for (Wing w : tail) {
            ret.append(w.toStringAll());
        }
        ret.append("\nDerive:");
        for (Wing w : derive) {
            ret.append(w.toStringAll());
        }
        ret.append(",statiqueMargin:").append(staticMargin);
        ret.append(",CzAdjutment:").append(czAdjustment);
        ret.append("\n*****NOTE****\n").append(note);
        ret.append("\n*************\n\n");
        return ret.toString();
    }
}
