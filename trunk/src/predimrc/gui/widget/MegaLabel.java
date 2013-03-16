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
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentListener;
import predimrc.PredimRC;

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
    public final Color backColor = new Color(175, 220, 235);
    private int LENGTH_MAX = 12;

    public MegaLabel(String _name, boolean _editable) {
        name = new JLabel(_name + " : ");
        value = new JTextField("***");
        value.setBackground(backColor);
        editable = _editable;
        value.setEditable(editable);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(name);
        add(value);
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

    public MegaLabel(String _name, String _value, boolean _editable, int nbrCarac) {
        this(_name, _editable);
        setValue(_value);
        LENGTH_MAX = nbrCarac;
    }

    public final void setValue(final String _value) {
        if (_value.length() > LENGTH_MAX) {
            value.setText(_value.substring(0, LENGTH_MAX));

        } else {
            value.setText(_value);
        }
    }

    public String getValue() {
        return value.getText();
    }

    public boolean isEditable() {
        return editable;
    }

    public float getFloatValue() {
        float ret;
        try {
            ret = Float.parseFloat(getValue());
        } catch (java.lang.NumberFormatException | NullPointerException exxx) {
            PredimRC.logln("Invalid value typed");
            ret = Float.MIN_VALUE;
        }
        return ret;
    }

    public void setEditable(boolean _editable) {
        editable = _editable;
        value.setEditable(editable);
    }

    public void addKeyListener(String key, Action l) {
        InputMap inputMap = value.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(key), "SAVE");
        value.getActionMap().put("SAVE", l);
    }

    public void addDocumentListener(DocumentListener d) {
        value.getDocument().addDocumentListener(d);
    }

    public void setDefaultColor() {
        value.setBackground(backColor);
    }

    public void setValueBackground(Color color) {
        value.setBackground(color);
    }
}
