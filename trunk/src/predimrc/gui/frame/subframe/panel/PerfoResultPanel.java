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
package predimrc.gui.frame.subframe.panel;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 17 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class PerfoResultPanel extends JPanel {

    private MegaLabel load = new MegaLabel("Load");
    private MegaLabel cz = new MegaLabel("Cz");
    private MegaLabel glidr = new MegaLabel("GlideRatio");
    private MegaLabel chutte = new MegaLabel("Txchutte");
    private String title = "none";

    public PerfoResultPanel(double _load, double _cz, double _glidr, double _chutte, String _title, Color c) {
        super();
        title = _title;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        load.setValueBackground(c);
        add(load);
        cz.setValueBackground(c);
        add(cz);
        glidr.setValueBackground(c);
        add(glidr);
        chutte.setValueBackground(c);
        add(chutte);
        update(_load, _cz, _glidr, _chutte, title);
    }

    public PerfoResultPanel(Color c) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        load.setValueBackground(c);
        add(load);
        cz.setValueBackground(c);
        add(cz);
        glidr.setValueBackground(c);
        add(glidr);
        chutte.setValueBackground(c);
        add(chutte);
        clear();
    }

    public final void update(double _load, double _cz, double _glidr, double _chutte, String _title) {
        title = _title;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), title));
        load.setValue("" + _load);
        cz.setValue("" + _cz);
        glidr.setValue("" + _glidr);
        chutte.setValue("" + _chutte);
    }

    public void clear() {
        update(0, 0, 0, 0, "none");
    }
}
