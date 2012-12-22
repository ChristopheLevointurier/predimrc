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
public class MegaLabel extends JPanel {

    private final JLabel name;
    private JTextField value;
    private boolean editable = false;

    public MegaLabel(String _name, boolean _editable) {
        name = new JLabel(_name+" : ");
        value = new JTextField("***");
        value.setBackground(new Color(195, 220, 235));
        editable = _editable;
        value.setEditable(editable);
        this.setLayout(new BorderLayout());
        add(name, BorderLayout.WEST);
        add(value, BorderLayout.CENTER);
        setVisible(true);
        setSize(getPreferredSize());
    }

    public MegaLabel(String _name, String _value) {
        this(_name, false);
        setValue(_value);
    }

    public MegaLabel(String _name) {
        this(_name, false);
    }

    public MegaLabel(String _name, String _value, boolean _editable) {
        this(_name, _editable);
        setValue(_value);
    }

    public void setValue(String _value) {
        value.setText(_value);
    }

    public String getValue() {
        return value.getText();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        editable = editable;
        value.setEditable(editable);
    }
}
