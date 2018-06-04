package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import config.Configuration;

import pd.IRequest;
import pd.IResponse;
import pd.Request;
import pd.TestCase;
import proxy.Proxy;
import proxy.UDPDatagram;
import testexec.TestCaseExecutionHandler;
import util.SimpleLogger;

import java.awt.FlowLayout;

public class SendMessageDialog extends JDialog {

    private Proxy proxy;

    private JPanel jContentPane = null;
    private JLabel srcIPLabel = null;
    private JTextField srcIP = null;
    private JLabel srcPortLabel = null;
    private JTextField sourcePort = null;
    private JLabel dstIPLabel = null;
    private JTextField dstIP = null;
    private JLabel dstPortLabel = null;
    private JTextField dstPort = null;
    private JTextArea message = null;
    private JLabel messageLabel = null;
    private JPanel buttonPanel = null;
    private JButton sendButton = null;
    private JButton cancelButton = null;
    private JScrollPane messageScrollPane = null;
    private SIPProxyWindow sipProxyWindow = null;
    private Configuration config = null;

    public SendMessageDialog(Proxy proxy) {
        this(proxy, null);
    }

    public SendMessageDialog(Proxy proxy, UDPDatagram packet) {
        super();
        this.proxy = proxy;

        initialize();
        initializeFields(packet);
    }
    
    public SendMessageDialog(Configuration config, SIPProxyWindow sipProxyWindow, 
                             String requestMessage, String targetIP, int targetPort){
        this.config = config;
        this.sipProxyWindow = sipProxyWindow;
        
        initialize();
        initializeFields( requestMessage, targetIP, targetPort );
        
    }
    
    private void initialize() {
        this.setSize(650, 460);
        this.setModal(false);
        this.setName("sendMessageDialog");
        this.setResizable(false);
        this.setLocation(new java.awt.Point(100, 100));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Send Message");
        this.setContentPane(getJContentPane());
    }

    private void initializeFields( UDPDatagram packet ) {
        //Send Message in Proxy Mode
        
        srcIP.setText(proxy.getProxyIP().getHostAddress());
        sourcePort.setText("" + proxy.getProxyPort());
        
        if (packet != null) {
            dstIP.setText(packet.getDstIP().getHostAddress());           
            dstPort.setText("" + packet.getDstPort());            
            message.setText(new String(packet.getData()));
            message.setCaretPosition(0);
        }

    }
    
    private void initializeFields( String requestMessage, String targetIP, int targetPort ) {
        //Resend Request Message in Test Case Mode
        
        if (sipProxyWindow != null && config != null){
            srcIP.setText("" + config.getTestCaseSocketIP().getHostAddress());
            sourcePort.setText("" + config.getTestCaseSocketPort());

            dstIP.setText(targetIP);
            dstPort.setText(new Integer(targetPort).toString());
            message.setText(requestMessage);
            message.setCaretPosition(0);

        }
       
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints12.weighty = 1.0;
            gridBagConstraints12.weightx = 1.0;
            messageLabel = new JLabel();
            messageLabel.setText("Message-Content:");
            dstPortLabel = new JLabel();
            dstPortLabel.setText("Destination Port:");

            dstIPLabel = new JLabel();
            dstIPLabel.setText("Destination IP-Address:");

            srcPortLabel = new JLabel();
            srcPortLabel.setText("Source Port:");

            srcIPLabel = new JLabel();
            srcIPLabel.setText("Source IP-Address:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());

            jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints1.insets = new java.awt.Insets(3, 5, 2, 2);
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            jContentPane.add(srcIPLabel, gridBagConstraints1);

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.insets = new java.awt.Insets(3, 3, 2, 2);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 1.0;
            jContentPane.add(getSrcIP(), gridBagConstraints2);

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints3.insets = new java.awt.Insets(3, 3, 2, 2);
            gridBagConstraints3.fill = GridBagConstraints.NONE;
            jContentPane.add(srcPortLabel, gridBagConstraints3);

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.gridx = 3;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(3, 3, 2, 5);
            gridBagConstraints4.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            jContentPane.add(getSourcePort(), gridBagConstraints4);

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints5.insets = new java.awt.Insets(3, 5, 2, 2);
            gridBagConstraints5.fill = GridBagConstraints.NONE;
            jContentPane.add(dstIPLabel, gridBagConstraints5);

            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.insets = new java.awt.Insets(3, 3, 2, 2);
            gridBagConstraints6.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.weightx = 1.0;
            jContentPane.add(getDstIP(), gridBagConstraints6);

            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 2;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints7.insets = new java.awt.Insets(3, 3, 2, 2);
            gridBagConstraints7.fill = GridBagConstraints.NONE;
            jContentPane.add(dstPortLabel, gridBagConstraints7);

            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridy = 1;
            gridBagConstraints8.gridx = 3;
            gridBagConstraints8.insets = new java.awt.Insets(3, 3, 2, 5);
            gridBagConstraints8.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.weightx = 1.0;
            jContentPane.add(getDstPort(), gridBagConstraints8);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints.insets = new java.awt.Insets(15, 5, 2, 2);
            jContentPane.add(messageLabel, gridBagConstraints);

            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints9.gridy = 3;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.weighty = 1.0;
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridwidth = 4;
            gridBagConstraints9.insets = new java.awt.Insets(3, 5, 2, 5);

            jContentPane.add(getMessageScrollPane(), gridBagConstraints9);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 4;
            gridBagConstraints11.gridwidth = 4;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            jContentPane.add(getButtonPanel(), gridBagConstraints11);
        }
        return jContentPane;
    }

    private JTextField getSrcIP() {
        if (srcIP == null) {
            srcIP = new JTextField();
            srcIP.setEditable(false);
            srcIP.setPreferredSize(new java.awt.Dimension(200, 20));
        }
        return srcIP;
    }

    private JTextField getSourcePort() {
        if (sourcePort == null) {
            sourcePort = new JTextField();
            sourcePort.setPreferredSize(new java.awt.Dimension(50, 20));
            sourcePort.setEditable(false);
        }
        return sourcePort;
    }

    private JTextField getDstIP() {
        if (dstIP == null) {
            dstIP = new JTextField();
            dstIP.setPreferredSize(new java.awt.Dimension(200, 20));
        }
        return dstIP;
    }

    private JTextField getDstPort() {
        if (dstPort == null) {
            dstPort = new JTextField();
            dstPort.setPreferredSize(new java.awt.Dimension(50, 20));
        }
        return dstPort;
    }

    private JTextArea getMessage() {
        if (message == null) {
            message = new JTextArea();
        }
        return message;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(flowLayout);
            buttonPanel.add(getSendButton(), null);
            buttonPanel.add(getCancelButton(), null);
        }
        return buttonPanel;
    }

    private JButton getSendButton() {
        if (sendButton == null) {
            sendButton = new JButton();
            sendButton.setText("Send Message");
            sendButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    if (sendMessage()) {
                        dispose();
                        clearFields();
                    }
                }
            });
        }
        return sendButton;
    }

    private boolean sendMessage() {
        InetAddress srcIPAddress = null;
        int iSrcPort = -1;
        InetAddress dstIPAddress = null;
        int iDstPort = -1;
        byte[] messageContent = message.getText().getBytes();

        boolean matches = true;
        StringBuffer faults = new StringBuffer();

        String ipRegex = "(\\d){1,3}\\x2E(\\d){1,3}\\x2E(\\d){1,3}\\x2E(\\d){1,3}";
        String portRegex = "(\\d)+";

        Pattern ipPattern = Pattern.compile(ipRegex);
        Pattern portPattern = Pattern.compile(portRegex);

        if (!ipPattern.matcher(srcIP.getText()).matches()) {
            matches = false;
            faults.append("Wrong source IP-Address format!\n");
        }
        else {
            try {
                srcIPAddress = InetAddress.getByName(srcIP.getText());
            }
            catch (UnknownHostException e) {
                matches = false;
                faults.append("Source IP-Address is invalid\n");
            }
        }

        if (!portPattern.matcher(sourcePort.getText()).matches()) {
            matches = false;
            faults.append("Wrong source port format!\n");
        }
        else {
            iSrcPort = Integer.parseInt(sourcePort.getText());
        }

        if (!ipPattern.matcher(dstIP.getText()).matches()) {
            matches = false;
            faults.append("Wrong destination IP-Address format!\n");
        }
        else {
            try {
                dstIPAddress = InetAddress.getByName(dstIP.getText());
            }
            catch (UnknownHostException e) {
                matches = false;
                faults.append("Destination IP-Address is invalid\n");
            }

        }

        if (!portPattern.matcher(dstPort.getText()).matches()) {
            matches = false;
            faults.append("Wrong destination port format!\n");
        }
        else {
            iDstPort = Integer.parseInt(dstPort.getText());
        }

        if (matches) {
            if (sipProxyWindow == null){
                UDPDatagram packet = new UDPDatagram(srcIPAddress, iSrcPort, dstIPAddress,
                        iDstPort, messageContent);
                proxy.sendMessage(packet);
            } else {
                
                //Create a test case with only one request message and no response messages
                //Therefore, all response messages will be categorized as "unknown"
                Vector<IRequest> requests = new Vector<IRequest>();
                requests.add(new Request(1, message.getText(),new Vector<IResponse>()));
                TestCase simpleTestCase = new TestCase("Resend", 1, requests);
                sipProxyWindow.runTestCase(simpleTestCase,dstIPAddress,iDstPort);
            }
                      
        }
        else {
            JOptionPane.showMessageDialog(this, faults.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return matches;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    dispose();
                    clearFields();
                }
            });
        }
        return cancelButton;
    }

    private void clearFields() {
        srcIP.setText("");
        sourcePort.setText("");
        dstIP.setText("");
        dstPort.setText("");
        message.setText("");
    }

    private JScrollPane getMessageScrollPane() {
        if (messageScrollPane == null) {
            messageScrollPane = new JScrollPane();
            messageScrollPane.setViewportView(getMessage());
        }
        return messageScrollPane;
    }

} // @jve:decl-index=0:visual-constraint="10,10"
