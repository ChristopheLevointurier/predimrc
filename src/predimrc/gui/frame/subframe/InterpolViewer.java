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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import predimrc.PredimRC;
import predimrc.gui.MiniFrame;
import predimrc.gui.frame.subframe.panel.FreeChartPanel;

/**
 *
 * @author Christophe Levointurier, 24 aout 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class InterpolViewer extends MiniFrame {
    
    private static FreeChartPanel chart;
    private static JSlider slider = new JSlider();
    private static boolean init = false;
    
    
    private static ChangeListener slide = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            PredimRC.logln("slider value:" + slider.getValue());
        }
    };
    
    public static void initViewer() {
        if (init) {
            return;
        }
        //computation of the interpolation
        //chart init
        init = true;
    }
    
    public InterpolViewer(AbstractButton _caller, JFrame _mother) {
        super(_caller, _mother);
        title = "Interpolation Viewer";
        setTitle(title);
        JPanel content = new JPanel();
        //  content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        content.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Select Reynold"));
        // chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));
        
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.addChangeListener(slide);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
              
        content.add(slider);
     //   content.add(chart);
        getContentPane().add(content);
        pack();     
    }
}
