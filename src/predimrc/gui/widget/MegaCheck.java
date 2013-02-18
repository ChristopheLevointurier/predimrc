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
package predimrc.gui.widget;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Christophe Levointurier, 18 fev. 2012
 * @version
 * @see
 * @since
 */
public class MegaCheck extends JPanel {

    private final JLabel name;
    private JCheckBox value;
    private boolean editable = false;
    public final Color backColor = new Color(175, 220, 235);

    public MegaCheck(String _name, boolean _editable, boolean check) {
        name = new JLabel(_name + " : ");
        editable = _editable;
        value = new JCheckBox("", check);
        value.setBackground(backColor);
        value.setEnabled(editable);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(name);
        add(value);
        setVisible(true);
        setSize(getPreferredSize());
    }

    public void setValue(boolean check, boolean withAction) {
        ActionListener[] al = value.getActionListeners();
        if (!withAction) {
            for (ActionListener a : al) {
                value.removeActionListener(a);
            }
        }
        value.setSelected(check);
        if (!withAction) {
            for (ActionListener a : al) {
                value.addActionListener(a);
            }
        }
    }

    public boolean getValue() {
        return value.isSelected();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean _editable) {
        editable = _editable;
        value.setEnabled(editable);
    }

    public void addActionListener(ActionListener a) {
        value.addActionListener(a);
    }
}
