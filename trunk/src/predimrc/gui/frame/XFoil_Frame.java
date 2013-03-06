/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import predimrc.gui.ExternalFrame;


import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import predimrc.common.Utils;




/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {
    
    private JPanel  panel           = new JPanel();
    private JPanel  panel_choice    = new JPanel();
    private JPanel  panel_choice1   = new JPanel();
    private JPanel  panel_choice2   = new JPanel();
    private JPanel  panel_choice3   = new JPanel();
    private JPanel  panel_airfoils  = new JPanel();
    private JButton button_airfoil1 = new JButton("Airfoil 1");
    private JButton button_airfoil2 = new JButton("Airfoil 2");
    private JButton button_airfoil3 = new JButton("Airfoil 3");
    private JLabel  label_airfoil1  = new JLabel();
    private JLabel  label_airfoil2  = new JLabel();    
    private JLabel  label_airfoil3  = new JLabel();
    
    public static XYDataset data1;
    
    public XFoil_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        
        label_airfoil1.setText("Airfoil 1");
        label_airfoil2.setText("Airfoil 2");
        label_airfoil3.setText("Airfoil 3");
   
        label_airfoil1.setForeground(Color.red);
        label_airfoil2.setForeground(Color.green);
        label_airfoil3.setForeground(Color.blue);
        
        panel_choice1.setLayout(new BoxLayout(panel_choice1,BoxLayout.LINE_AXIS));
        panel_choice2.setLayout(new BoxLayout(panel_choice2,BoxLayout.LINE_AXIS));
        panel_choice3.setLayout(new BoxLayout(panel_choice3,BoxLayout.LINE_AXIS));
        panel_choice.setLayout(new BoxLayout(panel_choice,BoxLayout.PAGE_AXIS));
        panel.setLayout(new BorderLayout());
        
        panel_choice1.add(button_airfoil1);
        panel_choice1.add(label_airfoil1);
        
        panel_choice2.add(button_airfoil2);
        panel_choice2.add(label_airfoil2);
        
        panel_choice3.add(button_airfoil3);
        panel_choice3.add(label_airfoil3);
        
        panel_choice.add(panel_choice1);
        panel_choice.add(panel_choice2);
        panel_choice.add(panel_choice3);
        
        panel.add(panel_choice,BorderLayout.NORTH);
        
        getContentPane().add(panel);
        
        button_airfoil1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathAirfoil = getAirFoil();
                label_airfoil1.setText(pathAirfoil);
                data1 = getCollectionSeries(pathAirfoil);
                panel_airfoils = createDemoPanel();
                panel.add(panel_airfoils,BorderLayout.SOUTH);
                getContentPane().add(panel);
                
            }
        });
        
        button_airfoil2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathAirfoil = getAirFoil();
                label_airfoil2.setText(pathAirfoil);
                data1 = getCollectionSeries(pathAirfoil);
                panel_airfoils = createDemoPanel();
                panel.add(panel_airfoils,BorderLayout.SOUTH);
                getContentPane().add(panel);
                
                
                
            }
        });
        
        button_airfoil3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathAirfoil = getAirFoil();
                label_airfoil3.setText(pathAirfoil);
                data1 = getCollectionSeries(pathAirfoil);
                panel_airfoils = createDemoPanel();
                
               // panel_airfoils.updateUI();
                
                panel.add(panel_airfoils,BorderLayout.SOUTH);
                getContentPane().add(panel);
            }
        });
      
       
              
        //data1 = createSampleData();
        
        //JPanel content = createDemoPanel();
       /////////////////////////////////////////// getContentPane().add(content);
        
        
        
    }
    
    @Override
    public void save() {
         predimrc.PredimRC.logln("Save from " + title);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    public static String getAirFoil() {
        String fileName = "0";
        JFileChooser chooser = new JFileChooser(new File(".\\src\\resource\\AirFoils"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select a airfoil file.");

        final int returnVal = chooser.showOpenDialog(null);
        // to do : filter and check data 
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           fileName = chooser.getSelectedFile().getAbsolutePath();
        }
        
        return fileName;
    }
    
    public static XYDataset getCollectionSeries(String filePath) {
        
        
        XYSeries series1 = new XYSeries("1");
        XYSeries series2 = new XYSeries("2");
        
        
        
        try{   
            Scanner scanner=new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                System.out.println("Ligne BRUTE :"+line);

                    Pattern p = Pattern.compile("^\\s*(\\d+\\.\\d+)\\s+((-?)\\d*\\.\\d+)");
                    Matcher m = p.matcher(line);
                    boolean findpattern = m.matches();
                    if(findpattern) {
                        
                        if (m.group(3).equals("-") ) {
                            series2.add(Double.parseDouble(m.group(1)),Double.parseDouble(m.group(2)));
                        }
                        else {
                            series1.add(Double.parseDouble(m.group(1)),Double.parseDouble(m.group(2)));
                        }
                        System.out.println("X="+m.group(1)+"Y="+m.group(2));
                        
                        for(int i=0; i<=m.groupCount(); i++) {
                        System.out.println("Groupe " + i + " : " + m.group(i)); }

                    }
           }
 
     scanner.close();

     
     } catch (IOException ioe) {
// erreur de fermeture des flux
System.out.println("Erreur --" + ioe.toString());
}
     
     XYSeriesCollection result = new XYSeriesCollection(series1);   
     result.addSeries(series2);
     return result;

    }
        
    public static JPanel createDemoPanel() {
        
        
     // create plot
        NumberAxis xAxis = new NumberAxis("X");
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis("Y");
        yAxis.setAutoRangeIncludesZero(false);

        XYSplineRenderer renderer1 = new XYSplineRenderer();
        renderer1.setSeriesPaint(0, Color.RED);
        renderer1.setSeriesPaint(1, Color.RED);
    
        
        renderer1.setSeriesOutlinePaint(0,null);
        renderer1.setSeriesShape(0,null);
        
        
      // renderer1.setSeriesStroke(1, null, false);
       /*
        BasicStroke newStroke = new BasicStroke((float) (4.0)); 
        renderer1.setSeriesStroke(0, newStroke); 
        renderer1.setSeriesOutlineStroke(0, newStroke);
        renderer1.setUseOutlinePaint(false);
*/
        
       // renderer1.setSeriesOutlineStroke(1, newStroke, false);
//        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        

        XYPlot plot = new XYPlot(data1, xAxis, yAxis, renderer1);
     
        
       
     //   plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
      //  plot.setDomainPannable(true);
    //    plot.setRangePannable(true);
    ///    plot.setBackgroundPaint(Color.lightGray);
     //   plot.setDomainGridlinePaint(Color.white);
     //   plot.setRangeGridlinePaint(Color.black);
        
        /*
        
        XYLineAndShapeRenderer renderer2 = new XYSplineRenderer(); 
renderer2.setSeriesShapesVisible(0, false); 
BasicStroke newStroke = new BasicStroke((float) (4.0)); 
renderer2.setSeriesStroke(0, newStroke); 
renderer2.setSeriesPaint(0, java.awt.Color.black); 
renderer2.setSeriesVisibleInLegend(0,false); 
        */
        
        // create and return the chart panel...

        
        
        JFreeChart chart = new JFreeChart(
                "",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    
}
