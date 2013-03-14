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
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import predimrc.common.Utils;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 14 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class FoilRenderer extends JPanel {

    private ChartPanel chart;
    private String s1 = "FAD05.dat";
    private String s2 = "FAD05.dat";
    private String s3 = "FAD05.dat";

    public FoilRenderer(String _s1, String _s2, String _s3) {
        s1 = _s1;
        s2 = _s2;
        s3 = _s3;
        updateChart();
    }

    public final void updateChart() {
        removeAll();
        chart = createChart(createDataset());
        chart.setPreferredSize(new Dimension(500, 200));
        chart.setMouseWheelEnabled(true);
        add(chart);
        repaint();
    }

    private XYSeriesCollection createDataset() {
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        addFoil(xyseriescollection, 1, s1);
        addFoil(xyseriescollection, 2, s2);
        addFoil(xyseriescollection, 3, s3);
        return xyseriescollection;
    }

    private void addFoil(XYSeriesCollection xyseriescollection, int i, String s) {
        XYSeries series = new XYSeries(i);
        XYSeries seriesBis = new XYSeries(i + "bis");
        ArrayList<DrawablePoint> l = Utils.loadDrawablePoints("AirFoils/" + s, Utils.VIEW_TYPE.GRAPH, false);

        float xbase = l.get(0).getFloatX();
        for (DrawablePoint p : l) {
            if (p.getFloatX() > xbase) {
                series.add(p.getFloatX(), p.getFloatY());
            }
            if (p.getFloatX() < xbase) {
                seriesBis.add(p.getFloatX(), p.getFloatY());
            }
            if (p.getFloatX() == 0) {
                series.add(p.getFloatX(), p.getFloatY());
                seriesBis.add(p.getFloatX(), p.getFloatY());
            }
            if (p.getFloatX() == 1) {
                series.add(p.getFloatX(), p.getFloatY());
                seriesBis.add(p.getFloatX(), p.getFloatY());
            }
            xbase = p.getFloatX();
        }
        xyseriescollection.addSeries(series);
        xyseriescollection.addSeries(seriesBis);
    }

    public ChartPanel createChart(XYDataset xydataset) {
        JFreeChart jfreechart = ChartFactory.createXYLineChart("Selected foil", "X", "Z", xydataset, PlotOrientation.VERTICAL, false, false, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        //  xyplot.setDomainZeroBaselineVisible(true);
        //   xyplot.setDomainPannable(true);
        //    xyplot.setRangePannable(true);
        //   ValueAxis valueaxis = xyplot.getRangeAxis();
        //  valueaxis.setAutoRange(true);
        // valueaxis.setLowerMargin(0.0D);
        // valueaxis.setUpperMargin(0.0D);
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        //  XYItemRenderer r=  xyplot.getRenderer();
        //   r.setSeriesPaint(0, new Paint());
        // xylineandshaperenderer.setDrawSeriesLineAsPath(false);
        /**
         * xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(1.5F));
         * xylineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 1,
         * 1.0F, new float[]{6F, 4F}, 0.0F));
         * xylineandshaperenderer.setSeriesStroke(2, new BasicStroke(2.0F, 1, 1,
         * 1.0F, new float[]{6F, 4F, 3F, 3F}, 0.0F));
         * xylineandshaperenderer.setSeriesStroke(3, new BasicStroke(2.0F, 1, 1,
         * 1.0F, new float[]{4F, 4F}, 0.0F));
         */
        /**
         * for (int i = 0; i < 6; i++) {
         * xylineandshaperenderer.setSeriesItemLabelsVisible(i, false);
         * xylineandshaperenderer.setSeriesVisibleInLegend(i, false); }*
         */
        xylineandshaperenderer.setSeriesPaint(0, Color.RED);
        xylineandshaperenderer.setSeriesPaint(1, Color.RED);
        xylineandshaperenderer.setSeriesPaint(2, Color.BLUE);
        xylineandshaperenderer.setSeriesPaint(3, Color.BLUE);
        xylineandshaperenderer.setSeriesPaint(4, Color.GREEN);
        xylineandshaperenderer.setSeriesPaint(5, Color.GREEN);
        ChartPanel chartPanel = new ChartPanel(jfreechart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }
}
