package ui.testexec;

import java.awt.GridBagLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import testexec.history.ITestCaseRun;
import testexec.history.ITestCaseRunObserver;
import testexec.history.TestCaseRunPdfReport;
import ui.DebugLogFrame;
import util.SimpleLogger;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.JFreeChart;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.awt.ComponentOrientation;

public class TestCaseInfoPanel extends JPanel implements ITestCaseRunObserver{
    
    //Class which appends the extension ".pdf" to saving files
    class PDFFileChooser extends JFileChooser{
        
        public PDFFileChooser(){
            super();
            this.addChoosableFileFilter(new FileFilter(){
                public boolean accept( File f ) {
                    return f.getName().toLowerCase().endsWith(".pdf") || f.isDirectory();
                }
                
                public String getDescription() {
                    return "PDF file";
                }
                
            });
        }
        //overwrite
        public File getSelectedFile() {
            
            //append extension ".pdf" to file
            File selectedFile = super.getSelectedFile();
            if(selectedFile != null && !selectedFile.getName().toLowerCase().endsWith(".pdf")){
                selectedFile = new File(selectedFile.getAbsolutePath() + ".pdf");
            }
            return selectedFile;
            
        }
    }
    private JPanel infoPanel = null;
    private JLabel targetAddressLabel = null;
    private JLabel TestCaseNameLabel = null;
    private JLabel startTimeLabel = null;
    private JLabel endTimeLabel = null;
    private JPanel statusPanel = null;
    private JLabel status = null;
    private JLabel cyclesLabel = null;
    private JLabel cyclesCount = null;
    
    private ITestCaseRun testCaseRun = null; 
    private StatisticsChartPanel statisticChartPanel = null;
    private JLabel testCaseName = null;
    private JLabel targetAddress = null;
    private JLabel startTime = null;
    private JLabel endTime = null;
    private JLabel cyclesNumberLabel = null;
    private JLabel statusLabel = null;
    private JButton viewLogButton = null;
    private JButton exportReportButton = null;
	private JToolBar buttonToolBar = null;
	private JPanel toolBarPanel = null;
    private JLabel targetUATypeLabel = null;
    private JLabel targetUA = null;

    public TestCaseInfoPanel() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(300, 300);
        this.setLayout(new BorderLayout());
        this.add(getStatusPanel(), java.awt.BorderLayout.NORTH);
        this.add(getStatisticChartPanel(), java.awt.BorderLayout.CENTER);
        this.add(getInfoPanel(), BorderLayout.SOUTH);
    }

    public void cyclesChanged() {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateCycles();
            }
        });
        Thread.yield();
        
    }

    public void endTimestampChanged() {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateEndTime();
            }
        });
        Thread.yield();
    }

    public void okayAdded() {

        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateOkay();
            }
        });
        Thread.yield();
    }

    public void statusChanged() {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateStatus();
            }
        });
        Thread.yield();
        
    }

    public void unknownAdded() {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateUnknown();
            }
        });
        Thread.yield();
        
    }

    public void warningAdded( ITestCaseRun testCaseRun, String requestMsgID ) {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateWarning();
            }
        });        
    }

    public void targetUAChanged() {
        //Only EventDispatchingThread should update gui
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                updateTargetUAType();
            }
        });        
    }    

    private JPanel getInfoPanel() {
        if (infoPanel == null) {

            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints22.gridx = 1;
            gridBagConstraints22.gridy = 4;
            targetUA = new JLabel();
            targetUA.setText("");
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints14.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.gridy = 4;
            
            targetUATypeLabel = new JLabel();
            targetUATypeLabel.setText("Target UA:");
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.NONE;
            gridBagConstraints12.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints12.gridy = 7;
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.gridwidth = 2;
            gridBagConstraints12.insets = new Insets(20,0,0,0);
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints2.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.weightx = 1.0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints11.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints11.gridy = 2;
            gridBagConstraints11.gridx = 1;
            statusLabel = new JLabel();
            statusLabel.setText("Status:");
            cyclesNumberLabel = new JLabel();
            cyclesNumberLabel.setText("Executed Cycles:");
            GridBagConstraints gridBagConstraints66 = new GridBagConstraints();
            gridBagConstraints66.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints66.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints66.gridy = 0;
            gridBagConstraints66.gridx = 0;
            GridBagConstraints gridBagConstraints68 = new GridBagConstraints();
            gridBagConstraints68.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints68.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints68.gridy = 2;
            gridBagConstraints68.gridx = 0;
            GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
            gridBagConstraints61.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints61.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints61.gridy = 6;
            gridBagConstraints61.gridx = 1;
            GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
            gridBagConstraints51.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints51.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints51.gridy = 5;
            gridBagConstraints51.gridx = 1;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints21.gridy = 3;
            gridBagConstraints21.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints13.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints13.gridy = 1;
            gridBagConstraints13.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 6;
            endTimeLabel = new JLabel();
            endTimeLabel.setText("End Time:");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 5;
            startTimeLabel = new JLabel();
            startTimeLabel.setText("Start Time:");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints1.gridy = 3;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor= GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints.ipadx = 20;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            
            
            TestCaseNameLabel = new JLabel();
            TestCaseNameLabel.setText("Test Case Name:");
            targetAddressLabel = new JLabel();
            targetAddressLabel.setText("Target Address:");
            infoPanel = new JPanel();
            infoPanel.setLayout(new GridBagLayout());
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 157, 50, 0));
            infoPanel.add(statusLabel, gridBagConstraints66);
            infoPanel.add(cyclesNumberLabel, gridBagConstraints68);
            infoPanel.add(targetAddressLabel, gridBagConstraints1);
            infoPanel.add(TestCaseNameLabel, gridBagConstraints);
            infoPanel.add(startTimeLabel, gridBagConstraints3);
            infoPanel.add(endTimeLabel, gridBagConstraints4);
            infoPanel.add(status, gridBagConstraints2);
            infoPanel.add(getCyclesCount(), gridBagConstraints11);
            infoPanel.add(getTestCaseName(), gridBagConstraints13);
            infoPanel.add(getTargetAddress(), gridBagConstraints21);
            infoPanel.add(getStartTime(), gridBagConstraints51);
            infoPanel.add(getEndTime(), gridBagConstraints61);
            infoPanel.add(getToolBarPanel(), gridBagConstraints12);
            infoPanel.add(targetUATypeLabel, gridBagConstraints14);
            infoPanel.add(targetUA, gridBagConstraints22);
        }
        return infoPanel;
    }

    private JPanel getStatusPanel() {
        if (statusPanel == null) {
            
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;

            cyclesLabel = new JLabel();
            cyclesLabel.setText("Test Case Report");
            cyclesLabel.setFont(new Font("Dialog", Font.BOLD, 24));
            status = new JLabel();
            status.setText("[status]");
            status.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
            statusPanel = new JPanel();
            statusPanel.setLayout(new GridBagLayout());
            statusPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
            statusPanel.add(cyclesLabel, gridBagConstraints5);
        }
        return statusPanel;
    }

    private JLabel getCyclesCount() {
        if (cyclesCount == null) {
            cyclesCount = new JLabel();
            cyclesCount.setMinimumSize(new Dimension(120, 20));
        }
        return cyclesCount;
    }

    public void setTestCaseRun(ITestCaseRun testCaseRun){
        if (this.testCaseRun != null){
            //Remove itself from observer list of "old" TestCaseRun
            this.testCaseRun.removeTestCaseRunObserver(this);
        }
        this.testCaseRun = testCaseRun;
        
        //Add itself to observer list of "new" TestCaseRun        
        this.testCaseRun.addTestCaseRunObserver(this);
        updateValues();
    }
    
    private void updateValues(){
        updateCycles();
        updateEndTime();
        updateOkay();
        updateStartTime();
        updateStatus();
        updateTargetAddress();
        updateTestCaseName();
        updateUnknown();
        updateWarning();
        updateTargetUAType();
        
    }

    private void updateTargetUAType(){
        targetUA.setText(testCaseRun.getTargetUA());
    }

    private void updateStatus(){
        status.setText(testCaseRun.getStatus());
    }
    private void updateCycles(){
        cyclesCount.setText("" + testCaseRun.getCycles());
    }
    private void updateWarning(){
        statisticChartPanel.setWarningCount(testCaseRun.getWarningCount());
    }
    private void updateOkay(){
        statisticChartPanel.setOkayCount(testCaseRun.getOkayCount());
    }
    private void updateUnknown(){
        statisticChartPanel.setUnkndownCount(testCaseRun.getUnknownCount());
    }
    private void updateTestCaseName(){
        testCaseName.setText("\""+testCaseRun.getTestCaseName()+"\"");
    }
    private void updateTargetAddress(){
        targetAddress.setText(testCaseRun.getTargetIP() + ":" + testCaseRun.getTargetPort());
    }
    private void updateStartTime(){
        startTime.setText(testCaseRun.getStartTimestamp());
    }
    private void updateEndTime(){
        endTime.setText(testCaseRun.getEndTimestamp());
    }

    private JPanel getStatisticChartPanel() {
        if (statisticChartPanel == null) {
            statisticChartPanel = new StatisticsChartPanel();
            statisticChartPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 150));
        }
        return statisticChartPanel;
    }

    private JLabel getTestCaseName() {
        if (testCaseName == null) {
            testCaseName = new JLabel();
            testCaseName.setMinimumSize(new Dimension(120, 20));
        }
        return testCaseName;
    }

    private JLabel getTargetAddress() {
        if (targetAddress == null) {
            targetAddress = new JLabel();
            targetAddress.setMinimumSize(new Dimension(120, 20));
        }
        return targetAddress;
    }

    private JLabel getStartTime() {
        if (startTime == null) {
            startTime = new JLabel();
            startTime.setMinimumSize(new Dimension(120, 20));
        }
        return startTime;
    }

    private JLabel getEndTime() {
        if (endTime == null) {
            endTime = new JLabel();
            endTime.setMinimumSize(new Dimension(120, 20));
        }
        return endTime;
    }

    private JButton getViewLogButton() {
        if (viewLogButton == null) {
            viewLogButton = new JButton();
            viewLogButton.setText(" View Log ");
            viewLogButton.setToolTipText("View Test Case Log");
            viewLogButton.setIcon(new ImageIcon(getClass().getResource("/ui/img/txtLogo.gif")));
            viewLogButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    DebugLogFrame debugView;
                    try {
                        debugView = new DebugLogFrame(testCaseRun.getMessagesLog(),"TestCase Mode Debug Log");
                        debugView.pack();
                        debugView.setVisible(true);
                    } catch(OutOfMemoryError e2) {
                        debugView = null;
                    }  
                }
            });
        }
        return viewLogButton;
    }

    private JButton getExportReportButton() {
        if (exportReportButton == null) {
            exportReportButton = new JButton();
            exportReportButton.setText(" Export to PDF ");
            exportReportButton.setToolTipText("Export Report to PDF");
            exportReportButton.setIcon(new ImageIcon(getClass().getResource("/ui/img/pdfLogo.gif")));
            exportReportButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    exportReport();
                }
            });
        }
        return exportReportButton;
    }

    private void exportReport() {
        JFileChooser fileChooser = new PDFFileChooser();
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                
                //If file doesn't exist or existing file should be replaced
                if(!fileChooser.getSelectedFile().exists() ||
                        (fileChooser.getSelectedFile().exists() && 
                                JOptionPane.showConfirmDialog(this, "Selected file already exists.\nDo you want to replace it?","Save as",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                                == JOptionPane.YES_OPTION)){
                    new TestCaseRunPdfReport().createReport(testCaseRun, fileChooser.getSelectedFile(),(JFreeChart) statisticChartPanel.getChart().clone());
                }
            }
            catch(IllegalArgumentException iae){
                JOptionPane.showMessageDialog(this,iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (CloneNotSupportedException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }
        
    }

	private JToolBar getButtonToolBar() {
		if (buttonToolBar == null) {
			buttonToolBar = new JToolBar();
			buttonToolBar.setBorderPainted(false);
			buttonToolBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			buttonToolBar.setFloatable(false);
			buttonToolBar.add(getExportReportButton());
			buttonToolBar.addSeparator();
			buttonToolBar.add(getViewLogButton());			
		}
		return buttonToolBar;
	}

	private JPanel getToolBarPanel() {
		if (toolBarPanel == null) {
			toolBarPanel = new JPanel();
			toolBarPanel.setLayout(new BorderLayout());
			toolBarPanel.add(getButtonToolBar(), BorderLayout.CENTER);
		}
		return toolBarPanel;
	}
}
