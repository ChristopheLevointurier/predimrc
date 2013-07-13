/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.JTextArea;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.graphic.drawable.model.DrawableModel;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class Vlm_Frame extends ExternalFrame {

    public Vlm_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public Vlm_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "Vlm";
        setTitle(title);
        getContentPane().add(new JTextArea("Zone de " + title));


        /**
         *
         * String[] files; files = Directory.GetFileSystemEntries("./Map"); int
         * filecount = files.GetUpperBound(0) + 1; for (int i = 0; i <
         * filecount; i++) { combo.Items.Add(files[i]); } FileInfo fi = new
         * FileInfo(files[i]); combo.Items.Add(fi.Name);
         * Path.GetFileNameWithoutExtension.
         *
         *
         */
        
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
         * xylineandshaperenderer.setSeriesVisibleInLegend(i, false); }*
         */
            /**
     *
     * // private JComboBox ncrit1_combobox = new JComboBox(); // private
     * JComboBox ncrit2_combobox = new JComboBox(); // private JComboBox
     * ncrit3_combobox = new JComboBox(); public static XYDataset data1;
     *
     *
     *
     * public static XYDataset getCollectionSeries(String filePath) {
     *
     * XYSeries series1 = new XYSeries("Naca"); XYSeries series2 = new
     * XYSeries(" ");
     *
     * try { Scanner scanner = new Scanner(new File(filePath)); while
     * (scanner.hasNextLine()) { String line = scanner.nextLine();
     *
     * System.out.println("Ligne BRUTE :" + line);
     *
     * Pattern p = Pattern.compile("^\\s*(\\d+\\.\\d+)\\s+((-?)\\d*\\.\\d+)");
     * Matcher m = p.matcher(line); boolean findpattern = m.matches(); if
     * (findpattern) {
     *
     * if (m.group(3).equals("-")) { series2.add(Double.parseDouble(m.group(1)),
     * Double.parseDouble(m.group(2))); } else {
     * series1.add(Double.parseDouble(m.group(1)),
     * Double.parseDouble(m.group(2))); } System.out.println("X=" + m.group(1) +
     * "Y=" + m.group(2));
     *
     * for (int i = 0; i <= m.groupCount(); i++) { System.out.println("Groupe "
     * + i + " : " + m.group(i)); }
     *
     * }
     * }
     *
     * scanner.close();
     *
     *
     * } catch (IOException ioe) { // erreur de fermeture des flux
     * System.out.println("Erreur --" + ioe.toString()); }
     *
     * XYSeriesCollection result = new XYSeriesCollection(series1);
     * result.addSeries(series2); return result;
     *
     * }
     *
     * public static JPanel createDemoPanel() { // create plot NumberAxis xAxis
     * = new NumberAxis("X"); xAxis.setAutoRangeIncludesZero(false); NumberAxis
     * yAxis = new NumberAxis("Y"); yAxis.setAutoRangeIncludesZero(false);
     *
     * XYSplineRenderer renderer1 = new XYSplineRenderer();
     * renderer1.setSeriesPaint(0, Color.RED); renderer1.setSeriesPaint(1,
     * Color.RED); renderer1.setBaseToolTipGenerator(null);
     *
     *
     * renderer1.setSeriesShapesVisible(0, false);
     * renderer1.setSeriesShapesVisible(1, false);
     *
     *
     * // renderer1.setLegendItemLabelGenerator(null); //
     * renderer1.setSeriesStroke(1, null, false);
     *
     * BasicStroke newStroke = new BasicStroke((float) (4.0));
     * renderer1.setSeriesStroke(0, newStroke);
     * renderer1.setSeriesOutlineStroke(0, newStroke);
     * renderer1.setUseOutlinePaint(false);
     *
     *
     * // renderer1.setSeriesOutlineStroke(1, newStroke, false); //
     * XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
     *
     *
     * XYPlot plot = new XYPlot(data1, xAxis, yAxis, renderer1);
     *
     *
     * // plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4)); //
     * plot.setDomainPannable(true); // plot.setRangePannable(true); ///
     * plot.setBackgroundPaint(Color.lightGray); //
     * plot.setDomainGridlinePaint(Color.white); //
     * plot.setRangeGridlinePaint(Color.black);
     *
     *
     *
     * XYLineAndShapeRenderer renderer2 = new XYSplineRenderer();
     * renderer2.setSeriesShapesVisible(0, false); BasicStroke newStroke = new
     * BasicStroke((float) (4.0)); renderer2.setSeriesStroke(0, newStroke);
     * renderer2.setSeriesPaint(0, java.awt.Color.black);
     * renderer2.setSeriesVisibleInLegend(0,false); *
     *
     * // create and return the chart panel...
     *
     *
     *
     * JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
     * plot, true); ChartPanel chartPanel = new ChartPanel(chart);
     * chartPanel.setMouseWheelEnabled(true); return chartPanel; }*
     */
        
        
    }

    @Override
    public void save() {
        predimrc.PredimRC.logDebugln("Save from " + title);
    }

    @Override
    public void updateModel(DrawableModel m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
