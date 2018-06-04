package ui.testexec;

import java.awt.Font;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import java.awt.BorderLayout;

public class StatisticsChartPanel extends JPanel {

    private String sWarning = "WARNING";  
    private String sOkay = "OKAY";  
    private String sUnknown = "UNKNOWN";  
    
    private String category = "count"; 
    
    private DefaultCategoryDataset dataset = null;  
    private JFreeChart chart = null;  

    public StatisticsChartPanel() {
        super();
        createDataset();
        initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.add(getChartPanel(), BorderLayout.CENTER);
    }

    public void setWarningCount(long warningCount){
        dataset.setValue((double) warningCount, sWarning, category);
    }
    
    public void setUnkndownCount(long unknownCount){
        dataset.setValue((double) unknownCount, sUnknown, category);
    }
    
    public void setOkayCount(long okayCount){
        dataset.setValue((double) okayCount, sOkay, category);
    }
    
    private void createDataset(){
        dataset = new DefaultCategoryDataset();
        dataset.addValue((double) 0, sWarning, category);
        dataset.addValue((double) 0, sOkay, category);
        dataset.addValue((double) 0, sUnknown, category);
    }
    
    private ChartPanel getChartPanel() {

        return new ChartPanel(getChart());
        
    }   
    public JFreeChart getChart(){
        if(chart == null){
            // create the chart...
            chart = ChartFactory.createBarChart(
                null, //title of Chart
                null,             // domain axis label
                null,             // range axis label
                dataset,          // dataset
                PlotOrientation.VERTICAL,
                true,  // include legend
                true,  // show tooltips
                false  // show URLs
            );
            
            chart.getLegend().setBackgroundPaint(null);
            chart.getLegend().setBorder(0, 0, 0, 0);
            chart.getLegend().setPadding(new RectangleInsets(0,0,10,0)); //top,left,bottom,right
            chart.getLegend().setPosition(RectangleEdge.TOP);
    
            chart.setAntiAlias(true);
            chart.setBorderVisible(false);
            chart.setPadding(new RectangleInsets(30,0,30,0)); //top,left,bottom,right    
            
            // get a reference to the plot
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(null);
            plot.setOutlinePaint(null);
            
            // set the range axis to display integers only
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setVisible(false);
            
            CategoryAxis categoryAxis = plot.getDomainAxis();
            categoryAxis.setVisible(false);
            categoryAxis.setLowerMargin(0.0); //margin before first bar
            categoryAxis.setUpperMargin(0.0); //margin after last bar
            
            BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
            barRenderer.setItemMargin(0.0); //space between bars
    
            //Show values of categories (-> number of warning, okay, unknown)
            CategoryItemRenderer renderer = plot.getRenderer();
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setItemLabelsVisible(true);
            renderer.setItemLabelFont(new Font("Dialog", Font.PLAIN, 12));
            
            //Set Warning color
            GradientPaint gpWarning = new GradientPaint(
                0.0f, 0.0f, ChartColor.LIGHT_RED, 
                0.0f, 0.0f, ChartColor.LIGHT_RED
            );
            renderer.setSeriesPaint(0, gpWarning);
            
            //Set Okay color
            GradientPaint gpOkay = new GradientPaint(
                0.0f, 0.0f, ChartColor.LIGHT_GREEN, 
                0.0f, 0.0f, ChartColor.LIGHT_GREEN
            );
            renderer.setSeriesPaint(1, gpOkay);
            
            //Set Unknown color
            GradientPaint gpUnknown = new GradientPaint(
                    0.0f, 0.0f, ChartColor.GRAY, 
                    0.0f, 0.0f, ChartColor.GRAY
                );
            renderer.setSeriesPaint(2, gpUnknown);
        }
        return chart;
    }
    
}
