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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 19 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FreeChartPanel extends JPanel {

    private XYSeriesCollection xyseriescollection = new XYSeriesCollection();
    private XYLineAndShapeRenderer xylineandshaperenderer;
    private String x, y, title;
    private ChartPanel chartPanel;
    private final static float[][] dashs = {{10.0f}, {5.0f, 10.0f}, {10.0f, 10.0f}, {10.0f, 20.0f}, {20.0f, 20.0f}, {50.0f, 20.0f}};
  
    public FreeChartPanel(String _title, String _x, String _y) {
        super();
        title = _title;
        x = _x;
        y = _y;
        updateChart();
    }

    private void updateChart() {
        removeAll();
        chartPanel = new ChartPanel(ChartFactory.createXYLineChart(title, x, y, xyseriescollection, PlotOrientation.VERTICAL, false, true, false));
        xylineandshaperenderer = (XYLineAndShapeRenderer) chartPanel.getChart().getXYPlot().getRenderer();
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(480, 330));
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel);
        chartPanel.repaint();
    }

    public void addSeries(Color col, int indexReynolds, String key, ArrayList<DrawablePoint> l) {
        XYSeries series = new XYSeries(xyseriescollection.getSeriesCount());
        for (DrawablePoint p : l) {
            series.add(p.getFloatX(), p.getFloatY());
        }
        xylineandshaperenderer.setSeriesPaint(xyseriescollection.getSeriesCount(), col);
        xylineandshaperenderer.setSeriesStroke(xyseriescollection.getSeriesCount(), new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND, 10.0f, dashs[indexReynolds], 0.0f));
        xyseriescollection.addSeries(series);
        chartPanel.repaint();
    }
}
