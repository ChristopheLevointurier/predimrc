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
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.data.xy.XYSeriesCollection;
import predimrc.PredimRC;
import predimrc.data.PolarData;
import predimrc.data.PolarDataBase;
import predimrc.data.PolarKey;
import predimrc.gui.MiniFrame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.frame.subframe.panel.FreeChartPanel;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 24 aout 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class InterpolViewer extends MiniFrame {

    private static FreeChartPanel chart = new FreeChartPanel("", "Cx", "Cz", new XYSeriesCollection());
    private static JSlider slider = new JSlider();
    private static PolarData data1;
    private static PolarData data2;
    private static ChangeListener slide = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            PredimRC.logln("slider value:" + slider.getValue());
        }
    };

    public static void initViewer() {
         XfoilConfig conf = PredimRC.getInstanceDrawableModel().getXfoilConfig();
        if (conf.getConfigs().size() < 2) {
            chart.getChart().setTitle("XfoilConfig has less than 2 data to interpolate");
            return;
        }
        data1 = PolarDataBase.getPolar(new PolarKey(conf.getConfigs().get(0)), true);
        data2 = PolarDataBase.getPolar(new PolarKey(conf.getConfigs().get(1)), true);
        chart.clean();
        chart.addSeries(XFoil_Frame.getAppropriateColor(data1.getColIndex(), ReynoldsConfig.getConfig().size() - 1), data1.getCzCxData());
        chart.addSeries(XFoil_Frame.getAppropriateColor(data2.getColIndex(), ReynoldsConfig.getConfig().size() - 1), data2.getCzCxData());
        //TODO computation of the interpolation
    }

    public InterpolViewer(AbstractButton _caller, JFrame _mother) {
        super(_caller, _mother);
        title = "Interpolation Viewer";
        setTitle(title);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Select Reynold"));
        // chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.addChangeListener(slide);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        initViewer();
        content.add(chart);
        content.add(slider);
        getContentPane().add(content);
        pack();
    }
}
