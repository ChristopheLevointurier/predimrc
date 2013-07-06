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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import predimrc.common.Utils;
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
    //private final static float[][] dashs = {{10.0f}, {5.0f, 10.0f}, {10.0f, 10.0f}, {10.0f, 20.0f}, {20.0f, 20.0f}, {50.0f, 20.0f}, {50.0f, 50.0f}};
    private ReentrantLock lock = new ReentrantLock();

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
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawWidth((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        chartPanel.setMaximumDrawHeight((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        xylineandshaperenderer = (XYLineAndShapeRenderer) chartPanel.getChart().getXYPlot().getRenderer();
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension((int) (Utils.DEFAULT_X_FRAME / 2), (int) (Utils.DEFAULT_Y_FRAME / 2)));
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel);
        chartPanel.repaint();
    }

    public void addSeries(Color col, ArrayList<DrawablePoint> l) {
        lock.lock();
        try {
            XYSeries series = new XYSeries(xyseriescollection.getSeriesCount(), false, true);
            for (DrawablePoint p : l) {
                series.add(p.getFloatX(), p.getFloatY());
            }
            xylineandshaperenderer.setSeriesPaint(xyseriescollection.getSeriesCount(), col);
            // xylineandshaperenderer.setSeriesStroke(xyseriescollection.getSeriesCount(), new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND, 10.0f, dashs[indexReynolds], 0.0f));
            xyseriescollection.addSeries(series);
        } finally {
            lock.unlock();
        }
    }

    public void clean() {
        xyseriescollection.removeAllSeries();
    }

    @Override
    public void repaint() {
        super.repaint();
        if (chartPanel != null) {
            chartPanel.repaint();
        }
    }

    public void addPoint(double x, double y, String str) {
        XYPointerAnnotation pointer = new XYPointerAnnotation(str, x, y, 35.0);
        ((XYPlot) chartPanel.getChart().getPlot()).addAnnotation(pointer);

    }
}
