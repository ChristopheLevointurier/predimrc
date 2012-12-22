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
package predimrc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

    public MegaCombo(String _name, boolean _editable, String... val) {
        name = new JLabel(_name + " : ");
        editable = _editable;
        value = new JComboBox(val);
        value.setBackground(new Color(195, 220, 235));
        value.setEnabled(editable);
        this.setLayout(new BorderLayout());
        add(name, BorderLayout.WEST);
        add(value, BorderLayout.CENTER);
        setVisible(true);
        setSize(getPreferredSize());
    }

    public void setValue(String _value) {
        value.setSelectedItem(_value);
    }

    public String getValue() {
        return (String) value.getSelectedItem();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        editable = editable;
        value.setEditable(editable);
    }
}
