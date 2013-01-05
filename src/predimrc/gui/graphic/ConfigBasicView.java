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
package predimrc.gui.graphic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import predimrc.PredimRC;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.gui.widget.MegaCombo;
import predimrc.gui.widget.MegaLabel;
import predimrc.gui.frame.Compare_Frame;
import predimrc.gui.frame.Engine_Frame;
import predimrc.gui.frame.Optim_Frame;
import predimrc.gui.frame.VlmStab_Frame;
import predimrc.gui.frame.Vlm_Frame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.model.Model;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 2 déc. 2012
 * @version
 * @see
 * @since
 */
public final class ConfigBasicView extends JPanel implements IModelListener {

    /**
     * widgets for structure config
     */
    private MegaLabel modelTitle;
    private JButton reset = new JButton("Reset model");
    private MegaCombo wingCombo = new MegaCombo("Number of wing :", true, "1", "2", "3", "4");
    private MegaCombo tailCombo = new MegaCombo("Number of tail :", true, "0", "1", "2", "3", "4");
    private MegaCombo deriveCombo = new MegaCombo("Number of derive :", true, "0", "1", "2", "3", "4");

    public ConfigBasicView() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        modelTitle = new MegaLabel("Model name (Enter to validate):", "undefined", true);
        modelTitle.addKeyListener("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceModel().setName(modelTitle.getValue());
                modelTitle.setDefaultColor();
            }
        });

        modelTitle.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modelTitle.setValueBackground(new Color(200, 150, 150));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modelTitle.setValueBackground(new Color(200, 150, 150));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });


        add(modelTitle);
        add(wingCombo);
        add(tailCombo);
        add(deriveCombo);
        add(reset);


        /**
         * *******---structure widgets----****
         */
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.resetModel();
            }
        });

        wingCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceModel().setWingNumber(Integer.parseInt(wingCombo.getValue()));
            }
        });


        tailCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceModel().setTailNumber(Integer.parseInt(tailCombo.getValue()));
            }
        });

        deriveCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceModel().setDeriveNumber(Integer.parseInt(deriveCombo.getValue()));
            }
        });
    }

    @Override
    public void changeModel(Model m) {
        wingCombo.setValue("" + m.getWings().size());
        tailCombo.setValue("" + m.getTail().size());
        deriveCombo.setValue("" + m.getDerive().size());
        modelTitle.setValue(m.getName());
        modelTitle.setDefaultColor();
    }

    @Override
    public void updateModel() {
        changeModel(PredimRC.getInstanceModel());
    }
}
