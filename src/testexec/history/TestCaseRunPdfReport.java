package testexec.history;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.jfree.chart.JFreeChart;
import org.jfree.ui.RectangleInsets;

import util.SimpleLogger;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

class NoBorderCell extends Cell{
    public NoBorderCell(){
        super();
        this.setBorder(0);
    }
    public NoBorderCell(String content){
        super(content);
        this.setBorder(0);
    }
}
public class TestCaseRunPdfReport implements ITestCaseRunReport {

    public void createReport( ITestCaseRun testCaseRun, File reportFile, JFreeChart chart ) {
        Document document = new Document(PageSize.A4,50,50,50,50);
        
        try {
            
            PdfWriter.getInstance(document, new FileOutputStream(reportFile));
            
            //Start document
            document.open();
            
            //Add SIP Proxy logo
            try {
                Image img = Image.getInstance(
                        new ImageIcon(this.getClass().getResource("img/reportLogo.gif")).getImage(), null);
                img.setAlignment(Image.ALIGN_CENTER);
                img.scalePercent(80);
                document.add(img);
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
            
            //Title
            Paragraph title = new Paragraph("Test Case Report",  new Font(Font.HELVETICA, 24, Font.BOLD));
            title.setSpacingBefore(30);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);
            
            //Add chart
            int width = 460;
            int height = 361;
            chart.setPadding(new RectangleInsets(0,0,0,0)); //top,left,bottom,right
            chart.setBackgroundPaint(new Color(255,255,255));   //white
            BufferedImage bImg = chart.createBufferedImage(width, height);
            
            try {
                Image img = Image.getInstance(bImg, null);
                img.setAlignment(Image.ALIGN_CENTER);
                document.add(img);
                
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }

            //table for displaying test case run information
            Table table = new Table(2);
            table.setWidths(new float[]{0.3f, 0.7f});
            table.setBorder(0);
            
            
            //Status row
            table.addCell(new NoBorderCell("Status:"));
            table.addCell(new NoBorderCell(testCaseRun.getStatus()));

            //Name row
            table.addCell(new NoBorderCell("Test Case Name:"));
            table.addCell(new NoBorderCell("\"" + testCaseRun.getTestCaseName() + "\""));
            
            //Cycles row
            table.addCell(new NoBorderCell("Executed Cycles:"));
            table.addCell(new NoBorderCell("" + testCaseRun.getCycles()));
            
            //Target Address row
            table.addCell(new NoBorderCell("Target Address:"));
            table.addCell(new NoBorderCell(testCaseRun.getTargetIP() + ":" + testCaseRun.getTargetPort()));
            
            //Target UA row
            table.addCell(new NoBorderCell("Target UA:"));
            table.addCell(new NoBorderCell(testCaseRun.getTargetUA()));

            //Start & end time row
            table.addCell(new NoBorderCell("Start Time:"));
            table.addCell(new NoBorderCell(testCaseRun.getStartTimestamp()));
            table.addCell(new NoBorderCell("End Time:"));
            table.addCell(new NoBorderCell(testCaseRun.getEndTimestamp()));
            
            document.add(table);
            document.newPage();
            
            //Add warning request and responses
            for(String msgID: testCaseRun.getWarningRequestMsgIDs()){
                String request = testCaseRun.getWarningLog().getWarningRequest(msgID);
                String message = testCaseRun.getWarningLog().getWarningMessage(msgID);
                
                //Display Warnig title with ID
                Paragraph warning = new Paragraph("Warning " + msgID, new Font(Font.HELVETICA, Font.DEFAULTSIZE+2, Font.BOLD));
                document.add(warning);
                
                //Display request
                Paragraph requestTitle = new Paragraph("Request message:",new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD));
                requestTitle.setSpacingBefore(10);
                document.add(requestTitle);
                document.add(new Paragraph(request, new Font(Font.COURIER, 10, Font.NORMAL)));
                
                //Display response
                Paragraph responseTitle = new Paragraph("Response message:",new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD));
                responseTitle.setSpacingBefore(10);
                document.add(responseTitle);
                document.add(new Paragraph(message, new Font(Font.COURIER, 10, Font.NORMAL)));
                
                document.newPage();
            }
            document.close();
        }
        catch (DocumentException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        
    }

}
