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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import predimrc.PredimRC;
import predimrc.common.Utils.USED_FOR;
import predimrc.controller.IModelListener;
import predimrc.gui.graphic.drawable.DrawablePanel;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.widget.MegaCheck;
import predimrc.gui.widget.MegaCombo;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2012
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
    private JButton resetView = new JButton("Reset view");
    private JButton compute = new JButton("reCompute");
    private MegaCheck fuseCheck = new MegaCheck("Fuselage", true, true);
    private MegaCheck stabCheck = new MegaCheck("Stab", true, true);
    private MegaCombo wingCombo = new MegaCombo("Number of wing", true, "1", "2", "3", "4");
    private MegaCombo tailCombo = new MegaCombo("Number of stab", true, "0", "1", "2", "3", "4");
    private MegaCombo deriveCombo = new MegaCombo("Number of fin", true, "0", "1", "2");

    public ConfigBasicView() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        modelTitle = new MegaLabel("Model name (Enter to validate)", "undefined", true, 100);
        modelTitle.addKeyListener("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().setName(modelTitle.getValue());
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
        //add(wingCombo);  Not yet
        //add(tailCombo);  Not yet

        add(deriveCombo);
        add(fuseCheck);
        add(stabCheck);
        add(reset);
        add(resetView);
        //     add(compute);

        /**
         * *******---structure widgets----****
         */
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.resetModel();
            }
        });

        resetView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawablePanel.panX = 0;
                DrawablePanel.panY = 0;
                DrawablePanel.panZ = 0;
                DrawablePanel.oldPanX = 0;
                DrawablePanel.oldPanY = 0;
                DrawablePanel.oldPanZ = 0;
                DrawablePanel.zoom = 1;
                PredimRC.repaintDrawPanels();
            }
        });

        compute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().computePositions();
            }
        });

        stabCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stabCheck.getValue()) {
                    PredimRC.getInstanceDrawableModel().setWingAmount(1, USED_FOR.HORIZONTAL_PLAN);
                } else {
                    PredimRC.getInstanceDrawableModel().setWingAmount(0, USED_FOR.HORIZONTAL_PLAN);
                }
            }
        });


        fuseCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().setFuseOnOff(fuseCheck.getValue());
            }
        });

        wingCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().setWingAmount(Integer.parseInt(wingCombo.getValue()), USED_FOR.MAIN_WING);
            }
        });


        tailCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().setWingAmount(Integer.parseInt(tailCombo.getValue()), USED_FOR.HORIZONTAL_PLAN);
            }
        });

        deriveCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PredimRC.getInstanceDrawableModel().setWingAmount(Integer.parseInt(deriveCombo.getValue()), USED_FOR.VERTICAL_PLAN);
            }
        });
    }

    @Override
    public void updateModel(DrawableModel m) {
        //  System.out.println("updateModel:" + m.getWings().size() + "," + m.getTail().size() + "," + m.getDerive().size());
        wingCombo.setSelectedValue(m.getWings().size(), false);
        tailCombo.setSelectedValue(m.getTail().size(), false);
        deriveCombo.setSelectedValue(m.getDerive().size(), false);
        stabCheck.setValue(m.hasStab(), false);
        fuseCheck.setValue(m.hasFuse(), false);
        modelTitle.setValue(m.getName());
        modelTitle.setDefaultColor();
    }
}
