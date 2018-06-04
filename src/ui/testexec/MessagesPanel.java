package ui.testexec;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

public class MessagesPanel extends JSplitPane {

    private JPanel requestPanel = null;
    private JPanel messagePanel = null;
    private JLabel requestLabel = null;
    private JTextArea requestTextArea = null;
    private JLabel messageLabel = null;
    private JTextArea messageTextArea = null;
    private JScrollPane requestScrollPane = null;
    private JScrollPane messageScrollPane = null;

    public MessagesPanel() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setBottomComponent(getMessagePanel());
        this.setTopComponent(getRequestPanel());
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.setDividerLocation(320);
    }

    private JPanel getRequestPanel() {
        if (requestPanel == null) {
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = GridBagConstraints.BOTH;
            gridBagConstraints21.gridy = 2;
            gridBagConstraints21.weightx = 1.0;
            gridBagConstraints21.weighty = 1.0;
            gridBagConstraints21.gridx = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.anchor = GridBagConstraints.WEST;
            requestLabel = new JLabel();
            requestLabel.setText("Request Message:");
            requestPanel = new JPanel();
            requestPanel.setLayout(new GridBagLayout());
            requestPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            requestPanel.add(requestLabel, gridBagConstraints11);
            requestPanel.add(getRequestScrollPane(), gridBagConstraints21);
        }
        return requestPanel;
    }

    private JPanel getMessagePanel() {
        if (messagePanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 0;
            messageLabel = new JLabel();
            messageLabel.setText("Response Message:");
            messagePanel = new JPanel();
            messagePanel.setLayout(new GridBagLayout());
            messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            messagePanel.add(messageLabel, gridBagConstraints1);
            messagePanel.add(getMessageScrollPane(), gridBagConstraints);
        }
        return messagePanel;
    }

    private JTextArea getRequestTextArea() {
        if (requestTextArea == null) {
            requestTextArea = new JTextArea();
            requestTextArea.setEditable(false);
        }
        return requestTextArea;
    }

    private JTextArea getMessageTextArea() {
        if (messageTextArea == null) {
            messageTextArea = new JTextArea();
            messageTextArea.setEditable(false);
        }
        return messageTextArea;
    }

    public void setContent(String request, String message){
        requestTextArea.setText(request);
        requestTextArea.setCaretPosition(0);
        messageTextArea.setText(message);
        messageTextArea.setCaretPosition(0);
    }

    private JScrollPane getRequestScrollPane() {
        if (requestScrollPane == null) {
            requestScrollPane = new JScrollPane();
            requestScrollPane.setViewportView(getRequestTextArea());
            requestPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return requestScrollPane;
    }

    private JScrollPane getMessageScrollPane() {
        if (messageScrollPane == null) {
            messageScrollPane = new JScrollPane();
            messageScrollPane.setViewportView(getMessageTextArea());
        }
        return messageScrollPane;
    }
}
