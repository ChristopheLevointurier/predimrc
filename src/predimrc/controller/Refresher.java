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

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class Refresher {

    private static ArrayList<IBusy> b;

    static {
        b = new ArrayList<>();
    }

    public static void add(IBusy ibusy) {
        b.add(ibusy);
    }

    public static void refresh() {
        while (!b.isEmpty()) {
            while (b.get(0).isBusy()) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
                b.remove(0);
            }
        }
        ModelController.applyChange();
    }

    public static void waitFor(IBusy busyProcess) {
        System.out.println("start wait for:" + System.currentTimeMillis());
        while (busyProcess.isBusy()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("stop wiat for:" + System.currentTimeMillis());
    }
}
