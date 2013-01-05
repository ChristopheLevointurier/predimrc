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
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.panel.TopPanel;
import predimrc.model.element.Fuselage;
import predimrc.model.element.Wing;
import predimrc.model.element.WingSection;

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
    private ArrayList<Wing> tail;
    private ArrayList<Wing> derive;
    private Fuselage fuselage;

    public Model() {
        name = "";
        note = "";
        wings = new ArrayList<>();
        Wing mainwing = new Wing(Wing.USED_FOR.MAIN_WING);
        wings.add(mainwing);
        mainwing.setPosXY(TopPanel.defaultWingConnection);
        mainwing.add(new WingSection(6, 8, 70, 60, 100));
        mainwing.add(new WingSection(3, -6, 60, 50, 140));
        mainwing.add(new WingSection(-5, -4, 60, 30, 80));

        tail = new ArrayList<>();
        Wing maintail = new Wing(Wing.USED_FOR.HORIZONTAL_PLAN);
        tail.add(maintail);
        maintail.setPosXY(TopPanel.defaultTailConnection);
        maintail.add(new WingSection(5, -20, 35, 20, 75));
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

    public ArrayList<Wing> getTail() {
        return tail;
    }

    public Fuselage getFuselage() {
        return fuselage;
    }

    public void setFuselage(Fuselage fuselage) {
        this.fuselage = fuselage;
        ModelController.applyChange();
    }

    public void computePositions() {
        for (Wing w : wings) {
            w.computePositions();
        }
    }
}
