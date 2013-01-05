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
import java.util.Iterator;
import predimrc.controller.ModelController;
import predimrc.model.ModelElement;

/**
 *
 * @author Christophe Levointurier, 29 déc. 2012
 * @version
 * @see
 * @since
 */
public class Wing extends ModelElement implements Iterable<WingSection> {

    //position x,y,z of the wing is the front first point.
    private ArrayList<WingSection> wingsSection;
    protected USED_FOR used_for;

    public Wing() {
        this(USED_FOR.MAIN_WING);
    }

    public Wing(USED_FOR _used_for, int nbrSection) {
        used_for = _used_for;
        wingsSection = new ArrayList<>();
        for (int i = 0; i < nbrSection; i++) {
            wingsSection.add(new WingSection());
        }
    }

    public Wing(Wing in) {
        used_for = in.getUsed_for();
        wingsSection = new ArrayList<>();
        for (WingSection ws : in) {
            wingsSection.add(new WingSection(ws));
        }
    }

    public Wing(USED_FOR _used_for) {
        used_for = _used_for;
        wingsSection = new ArrayList<>();

    }

    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
    }

    public USED_FOR getUsed_for() {
        return used_for;
    }

    public void setUsed_for(USED_FOR used_for) {
        this.used_for = used_for;
    }

    public void add(WingSection newSection) {
        wingsSection.add(newSection);
    }

    public void setWingSectionNumber(int _i) {
        predimrc.PredimRC.logDebugln("setWingSectionNumber:" + _i);
        ArrayList<WingSection> wingsTemp = new ArrayList<>();
        for (int i = 0; i < _i; i++) {
            if (!wingsSection.isEmpty()) {
                wingsTemp.add(wingsSection.remove(0));
            } else {
                if (wingsTemp.isEmpty()) {
                    wingsTemp.add(new WingSection(0, 0, 10, 10, 10));
                } else {
                    wingsTemp.add(new WingSection(wingsTemp.get(0)));
                }
            }
        }
        wingsSection = wingsTemp;
        ModelController.changeModel();
    }

    public int getSize() {
        return wingsSection.size();
    }

    @Override
    public Iterator iterator() {
        return wingsSection.iterator();
    }

    public WingSection get(int index) {
        return wingsSection.get(index);
    }

    public static enum USED_FOR {

        MAIN_WING, VERTICAL_PLAN, HORIZONTAL_PLAN;
    }

    public void setDiedre(float diedre) {
        for (WingSection w : (ArrayList<WingSection>) wingsSection.clone()) {
            w.setDiedre(diedre);
        }
    }
}
