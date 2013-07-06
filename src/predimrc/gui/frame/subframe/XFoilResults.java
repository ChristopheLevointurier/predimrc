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
package predimrc.gui.frame.subframe;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 7 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilResults extends JPanel {

    private MegaLabel cm01 = new MegaLabel("Cm0");
    private MegaLabel alpha01 = new MegaLabel("Alpha0");
    private MegaLabel cm02 = new MegaLabel("Cm0");
    private MegaLabel alpha02 = new MegaLabel("Alpha0");
    private MegaLabel cm03 = new MegaLabel("Cm0");
    private MegaLabel alpha03 = new MegaLabel("Alpha0");

    public XFoilResults() {
        super();
        cm01.setValueBackground(XFoil_Frame.getAppropriateColor(0, 6));
        alpha01.setValueBackground(XFoil_Frame.getAppropriateColor(0, 6));
        cm02.setValueBackground(XFoil_Frame.getAppropriateColor(1, 6));
        alpha02.setValueBackground(XFoil_Frame.getAppropriateColor(1, 6));
        cm03.setValueBackground(XFoil_Frame.getAppropriateColor(2, 6));
        alpha03.setValueBackground(XFoil_Frame.getAppropriateColor(2, 6));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Results:"));
        add(cm01);
        add(alpha01);
        add(cm02);
        add(alpha02);
        add(cm03);
        add(alpha03);
    }

    public MegaLabel getCm01() {
        return cm01;
    }

    public MegaLabel getAlpha01() {
        return alpha01;
    }

    public MegaLabel getCm02() {
        return cm02;
    }

    public MegaLabel getAlpha02() {
        return alpha02;
    }

    public MegaLabel getCm03() {
        return cm03;
    }

    public MegaLabel getAlpha03() {
        return alpha03;
    }
}
