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

import java.awt.Color;
import java.util.ArrayList;
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
import predimrc.common.Utils;
import predimrc.data.PolarData;
import predimrc.data.PolarDataBase;
import predimrc.data.PolarKey;
import predimrc.gui.MiniFrame;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.frame.subframe.panel.FreeChartPanel;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
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
    private static int re1 = 0;
    private static int re2 = 0;
    private static int reynoldVoulu = 0;
    private static boolean computed = false;
    private static ChangeListener slide = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            PredimRC.logln("slider value:" + slider.getValue());
            re1 = ReynoldsConfig.reyIntValue[data1.getReynoldsIndex()];
            re2 = ReynoldsConfig.reyIntValue[data2.getReynoldsIndex()];
            if (re1 > re2) {
                int temp = re1;
                re1 = re2;
                re2 = temp;
            }
            reynoldVoulu = re1 + ((slider.getValue() * (re2 - re1)) / 100);
            //  System.out.println("revoulu=" + reynoldVoulu);
            compute();
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

    private static void compute() {
        if (computed) {
            chart.removeLastSerie();
        }

        ArrayList<DrawablePoint> res = new ArrayList<>();
        for (double czVoulu = -0.4; czVoulu < 0.6; czVoulu += 0.1) {
            //pts sur courbe 1
            DrawablePoint p1 = Utils.getnearestPoint(data1.getCzCxData(), czVoulu, false, false);
            DrawablePoint p2 = Utils.getnearestPoint(data1.getCzCxData(), czVoulu, false, true);

            //pts sur courbe 2
            DrawablePoint p3 = Utils.getnearestPoint(data2.getCzCxData(), czVoulu, false, false);
            DrawablePoint p4 = Utils.getnearestPoint(data2.getCzCxData(), czVoulu, false, true);
           // System.out.println("re1=" + re1 + " re2=" + re2 + " Points selectionnes p1=" + p1 + " p2=" + p2 + " p3=" + p3 + " p4=" + p4);


            //     cxmoy1 = cx1 +       (czVoulu -   cz1)     * (cx2       - cx1) /       (cz2       - cz1);
            double cxmoy1 = p1.getX() + (czVoulu - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
            //     cxmoy2 = cx3 +       (czVoulu - cz3)       * (cx4       - cx3)       / (cz4       - cz3);
            double cxmoy2 = p3.getX() + (czVoulu - p3.getY()) * (p4.getX() - p3.getX()) / (p4.getY() - p3.getY());

            double b = (cxmoy2 - cxmoy1) / (((double) 1 / (double) re2) - ((double) 1 / (double) re1));
            double a = cxmoy2 - (b / re1);

            res.add(new DrawablePoint(a + b / reynoldVoulu, czVoulu));
         //   System.out.println("reynoldVoulu " + reynoldVoulu + " cxmoy1 " + cxmoy1 + " cxmoy2 " + cxmoy2 + " a= " + a + " b= " + b + " pt=" + res.get(0));
        }
        chart.addSeries(Color.BLACK, res);
        computed = true;
    }
}
