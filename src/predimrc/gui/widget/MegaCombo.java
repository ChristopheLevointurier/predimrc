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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class MegaCombo extends JPanel {

    private final JLabel name;
    private JComboBox value;
    private boolean editable = false;
    public final Color backColor = new Color(175, 220, 235);
    private DefaultComboBoxModel model;

    public MegaCombo(String _name, boolean _editable, String... val) {
        name = new JLabel(_name + " : ");
        editable = _editable;
        model = new DefaultComboBoxModel(val);
        value = new JComboBox(model);
        value.setBackground(backColor);
        value.setEnabled(editable);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(name);
        add(value);
        setVisible(true);
        setSize(getPreferredSize());
    }

    public void setValue(String _value, boolean withAction) {
        ActionListener[] al = value.getActionListeners();
        if (!withAction) {
            for (ActionListener a : al) {
                value.removeActionListener(a);
            }
        }
        value.setSelectedItem(_value);
        if (!withAction) {
            for (ActionListener a : al) {
                value.addActionListener(a);
            }
        }
    }

    public void setSelectedValue(String item, boolean withAction) {
        ActionListener[] al = value.getActionListeners();
        if (!withAction) {
            for (ActionListener a : al) {
                value.removeActionListener(a);
            }
        }
        value.setSelectedItem(item);
        if (!withAction) {
            for (ActionListener a : al) {
                value.addActionListener(a);
            }
        }
    }

    public String getValue() {
        return (String) value.getSelectedItem();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean _editable) {
        editable = _editable;
        value.setEditable(editable);
    }

    public void addActionListener(ActionListener a) {
        value.addActionListener(a);
    }
}
