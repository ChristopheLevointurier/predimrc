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
package predimrc.gui.graphic.config;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * This class has Widgets for the 3D view configuration
 *
 * @author Christophe Levointurier, 19 déc. 2012
 * @version
 * @see
 * @since
 */
public final class Config3DView extends JPanel {

    private JPanel write_panel = new JPanel();
    private JPanel read_panel = new JPanel();
    private JCheckBox cull_check = new JCheckBox("CullFacing", false);
    private JCheckBox solid_check = new JCheckBox("Solid", false);
    private JCheckBox lighting_check = new JCheckBox("Lightnings", false);
    private JCheckBox rotX_check = new JCheckBox("RotationX", true);
    private JCheckBox rotY_check = new JCheckBox("RotationY", false);
    private JCheckBox rotZ_check = new JCheckBox("RotationZ", false);
    private MegaLabel zoom_label = new MegaLabel("Zoom factor:", "xx", false, 6);
    private MegaLabel degX_label = new MegaLabel("X angle:", "xx", false, 6);
    private MegaLabel degY_label = new MegaLabel("Y angle:", "xx", false, 6);
    private MegaLabel degZ_label = new MegaLabel("Z angle:", "xx", false, 6);

    public Config3DView() {
        super();


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        write_panel.setLayout(new BoxLayout(write_panel, BoxLayout.Y_AXIS));
        write_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Config"));
        write_panel.add(cull_check);
        write_panel.add(solid_check);
        write_panel.add(lighting_check);
        write_panel.add(rotX_check);
        write_panel.add(rotY_check);
        write_panel.add(rotZ_check);


        read_panel.setLayout(new BoxLayout(read_panel, BoxLayout.Y_AXIS));
        read_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Status"));
        read_panel.add(zoom_label);
        read_panel.add(degX_label);
        read_panel.add(degY_label);
        read_panel.add(degZ_label);

        add(write_panel);
        add(read_panel);
    }

    public JCheckBox getCull_check() {
        return cull_check;
    }

    public JCheckBox getSolid_check() {
        return solid_check;
    }

    public JCheckBox getLighting_check() {
        return lighting_check;
    }

    public JCheckBox getRotX_check() {
        return rotX_check;
    }

    public JCheckBox getRotY_check() {
        return rotY_check;
    }

    public JCheckBox getRotZ_check() {
        return rotZ_check;
    }

    public MegaLabel getZoom_label() {
        return zoom_label;
    }

    public MegaLabel getDegX_label() {
        return degX_label;
    }

    public MegaLabel getDegY_label() {
        return degY_label;
    }

    public MegaLabel getDegZ_label() {
        return degZ_label;
    }
}
