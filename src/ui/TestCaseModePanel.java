package ui;

import java.awt.Component;
import javax.swing.JPanel;

import testexec.history.ITestCaseRun;
import ui.testexec.MessagesPanel;
import ui.testexec.TestCaseInfoPanel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;

public class TestCaseModePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Component content = null;
    private MessagesPanel messagesPanel = new MessagesPanel();
    private TestCaseInfoPanel infoPanel = new TestCaseInfoPanel();

    public TestCaseModePanel() {
        super();
        initialize();
    }

    private void initialize() {
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 1, 1));
        this.setLayout(new BorderLayout());
    }
    
  
    public void setMessagesContent(String request, String message){     
        messagesPanel.setContent(request, message);
        //if messagspanel is not displayed
        if(content != messagesPanel){
            if(content != null){
                this.remove(content);
            }
            this.add(messagesPanel, BorderLayout.CENTER);
            content = messagesPanel;
        }
        this.repaint();
    }
    
    public void setTestCaseRun(ITestCaseRun testCaseRun){
        infoPanel.setTestCaseRun(testCaseRun);
        //if infoPanel is not displayed
        if(content != infoPanel){
            if(content != null){
                this.remove(content);
            }
            this.add(infoPanel, BorderLayout.CENTER);
            content = infoPanel;
        }
        this.repaint();
    }

}
