/*This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.controller;

import java.util.ArrayList;
import predimrc.gui.graphic.drawable.model.DrawableModel;

/**
 * Manage interactions between model and gui
 *
 * @author Christophe Levointurier, 23 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class ModelController {

    /**
     * Listeners of the model
     */
    private static ArrayList<IModelListener> listeners;

    static {
        listeners = new ArrayList<>();
    }

    public static void addModelListener(IModelListener l) {
        listeners.add(l);
    }

    public static void removeModelListener(IModelListener l) {
        listeners.remove(l);
    }

    public static void applyChange() {
        updateModel(predimrc.PredimRC.getInstanceDrawableModel());
    }

    public static void updateModel(DrawableModel m) {
        predimrc.PredimRC.logln("update of the model");
        m.computePositions();
        for (IModelListener l : listeners) {
            l.updateModel(m);
        }
    }
}
