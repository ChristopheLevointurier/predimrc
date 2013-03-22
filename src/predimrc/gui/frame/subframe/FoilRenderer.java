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

import java.awt.*;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
import predimrc.model.element.XfoilConfig;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilRenderer extends ExternalFrame {

    private ChartPanel chart;
    private String s0, s1, s2;
    public static final ArrayList<Color> listColor = new ArrayList<>();

    static {
        listColor.add(Color.red);
        listColor.add(Color.blue);
        listColor.add(Color.green.darker());
    }

    public FoilRenderer(AbstractButton _caller, XfoilConfig xfoilconfig) {
        super(_caller);
        title = "Reynolds Panels";
        setTitle(title);
        s0 = xfoilconfig.getFoilName(0);
        s1 = xfoilconfig.getFoilName(1);
        s2 = xfoilconfig.getFoilName(2);
        setSize(800, 200);
        updateChart();
    }

    public final void updateChart() {
        getContentPane().removeAll();
        chart = createChart(createDataset());
        chart.setPreferredSize(new Dimension(480, 110));
        chart.setMouseWheelEnabled(true);
        getContentPane().add(chart);
        chart.repaint();
    }

    private XYSeriesCollection createDataset() {
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        addFoil(xyseriescollection, 1, s0);
        addFoil(xyseriescollection, 2, s1);
        addFoil(xyseriescollection, 3, s2);
        return xyseriescollection;
    }

    private void addFoil(XYSeriesCollection xyseriescollection, int i, String s) {

        XYSeries series = new XYSeries(i, false, true);
        if (s.length() > 1) {
            ArrayList<DrawablePoint> l = Utils.loadDrawablePoints("AirFoils/" + s, Utils.VIEW_TYPE.GRAPH, false);
            for (DrawablePoint p : l) {
                series.add(p.getFloatX(), p.getFloatY());
            }
        }
        xyseriescollection.addSeries(series);
    }

    public ChartPanel createChart(XYDataset xydataset) {
        JFreeChart jfreechart = ChartFactory.createXYLineChart("", "", "", xydataset, PlotOrientation.VERTICAL, false, false, false);
        jfreechart.getXYPlot().getRangeAxis().setRange(-0.10, 0.10);
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) jfreechart.getXYPlot().getRenderer();

        for (int i = 0; i < 3; i++) {
            xylineandshaperenderer.setSeriesPaint(i, listColor.get(i));
        }
        ChartPanel chartPanel = new ChartPanel(jfreechart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    public void setS0(String s0) {
        this.s0 = s0;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    @Override
    public void save() {
        //nothing to do
    }
}
