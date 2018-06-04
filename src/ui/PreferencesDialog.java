package ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import proxy.BadProxyException;
import proxy.Proxy;

import config.Configuration;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Insets;

public class PreferencesDialog extends JDialog {

    private JPanel jContentPane = null;
    private JLabel proxyModeAddressLabel = null;
    private JLabel proxyModePortLabel = null;
    private JLabel clientLabel = null;
    private JTextField clientIPTextField = null;
    private JLabel clientPortLabel = null;
    private JTextField clientPortTextField = null;
    private JPanel proxySettingsPanel = null;
    private JLabel proxyLabel = null;
    private JTextField proxyIPTextField = null;
    private JLabel proxyPortLabel = null;
    private JTextField proxyPortTextField = null;
    private JLabel pbxLabel = null;
    private JTextField pbxIPTextField = null;
    private JLabel pbxPortLabel = null;
    private JTextField pbxPortTextField = null;
    private Configuration config = null;
    private Proxy proxy = null;
    private JPanel buttonPanel = null;
    private JButton applyButton = null;
    private JButton cancelButton = null;
    private JButton okButton = null;
    private JPanel testCaseSettingsPanel = null;
    private JLabel testCaseSocketLabel = null;
    private JLabel targetLabel = null;
    private JLabel testCaseSocketPortLabel = null;
    private JLabel targetPortLabel = null;
    private JTextField testCaseSocketIPTextField = null;
    private JTextField testCaseSocketPortTextField = null;
    private JTextField targetIPTextField = null;
    private JTextField targetPortTextField = null;
    private SIPProxyWindow mainWindow = null;
    private JLabel testCaseModeAddressLabel = null;
    private JLabel testCaseModePortLabel = null;
    private StringBuffer sb;
    private JPanel proxyModeImagePanel = null;
    private JPanel testCaseModeImagePanel = null;
    private JLabel proxyModeImageLabel = null;
    private JLabel testCaseModeImageLabel = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel proxyFieldsPanel = null;
    private JPanel testCaseFieldsPanel = null;
    private JLabel testCaseDirLabel = null;
    private JTextField testCaseDirTextField = null;
    private JButton browseButton = null;
    private JPanel testCaseDirPanel = null;
    private JLabel proxySocketPortLabel = null;
    private JLabel cliendIPPortLabel = null;
    private JLabel pbxIPPortLabel = null;
    private JLabel socketIPPortLabel = null;
    private JLabel targetIPPortLabel = null;

    public PreferencesDialog(Proxy proxy, SIPProxyWindow mainWindow) {
        super();
        this.proxy = proxy;
        this.config = proxy.getConfig();
        this.mainWindow = mainWindow;
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setName("preferencesDialog");
        this.setTitle("Preferences");
        this.setLocation(new java.awt.Point(100, 100));
        this.setResizable(false);
        this.setModal(true);
        this.setContentPane(getJContentPane());

        setFields();

    }

    public void setFields() {
        clientIPTextField.setText(config.getClientIP().getHostAddress());
        clientPortTextField.setText(new Integer(config.getClientPort()).toString());

        proxyIPTextField.setText(config.getProxySocketIP().getHostAddress());
        proxyPortTextField.setText(new Integer(config.getProxySocketPort()).toString());

        pbxIPTextField.setText(config.getPbxIP().getHostAddress());
        pbxPortTextField.setText(new Integer(config.getPbxPort()).toString());
        
        testCaseSocketIPTextField.setText(config.getTestCaseSocketIP().getHostAddress());
        testCaseSocketPortTextField.setText(new Integer(config.getTestCaseSocketPort()).toString());
        
        targetIPTextField.setText(config.getTargetIP().getHostAddress());
        targetPortTextField.setText(new Integer(config.getTargetPort()).toString());
        
        testCaseDirTextField.setText(config.getTestCaseDirPath());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10,
                    10, 10));
            jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
            jContentPane.add(getJTabbedPane(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    private JTextField getClientIPTextField() {
        if (clientIPTextField == null) {
            clientIPTextField = new JTextField();
            clientIPTextField.setPreferredSize(new java.awt.Dimension(200, 20));
            clientIPTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return clientIPTextField;
    }

    private JTextField getClientPortTextField() {
        if (clientPortTextField == null) {
            clientPortTextField = new JTextField();
            clientPortTextField.setPreferredSize(new java.awt.Dimension(50, 20));
            clientPortTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return clientPortTextField;
    }

    private JPanel getProxySettingsPanel() {
        if (proxySettingsPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
            
            proxySettingsPanel = new JPanel();
            proxySettingsPanel.setLayout(new GridBagLayout());
            proxySettingsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
            proxySettingsPanel.add(getProxyModeImagePanel(), gridBagConstraints);
            proxySettingsPanel.add(getProxyFieldsPanel(), gridBagConstraints1);
        }
        return proxySettingsPanel;
    }

    private JTextField getProxyIPTextField() {
        if (proxyIPTextField == null) {
            proxyIPTextField = new JTextField();
            proxyIPTextField.setPreferredSize(new java.awt.Dimension(200, 20));
            proxyIPTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return proxyIPTextField;
    }

    private JTextField getProxyPortTextField() {
        if (proxyPortTextField == null) {
            proxyPortTextField = new JTextField();
            proxyPortTextField.setPreferredSize(new java.awt.Dimension(50, 20));
            proxyPortTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return proxyPortTextField;
    }

    private JTextField getPbxIPTextField() {
        if (pbxIPTextField == null) {
            pbxIPTextField = new JTextField();
            pbxIPTextField.setPreferredSize(new java.awt.Dimension(200, 20));
            pbxIPTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return pbxIPTextField;
    }

    private JTextField getPbxPortTextField() {
        if (pbxPortTextField == null) {
            pbxPortTextField = new JTextField();
            pbxPortTextField.setPreferredSize(new java.awt.Dimension(50, 20));
            pbxPortTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return pbxPortTextField;
    }

    private void applyProxyModeChanges() {

        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        applyButton.requestFocus(true);
        
        try {
            //Proxy Mode Settings
            InetAddress clientIP = InetAddress.getByName(clientIPTextField.getText());
            Integer clientPort = new Integer(clientPortTextField.getText());
            InetAddress proxyIP = InetAddress.getByName(proxyIPTextField.getText());
            Integer proxyPort = new Integer(proxyPortTextField.getText());
            InetAddress pbxIP = InetAddress.getByName(pbxIPTextField.getText());
            Integer pbxPort = new Integer(pbxPortTextField.getText());
            
            try {
                proxy.setConfigChanges(clientIP, clientPort, proxyIP, proxyPort, pbxIP,
                        pbxPort);
            }
            catch (BadProxyException e) {
                if(sb == null){
                    sb = new StringBuffer();
                }
                sb.append("\n- Proxy Socket IP & Port settings");
            }
        }
        catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Wrong IP Format", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong Port Format", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.pack();
    }
    
    private void applyTestCaseModeChanges() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        applyButton.requestFocus(true);
        
        try {    
            //TestCase Mode Settings
            InetAddress testCaseSocketIP = InetAddress.getByName(testCaseSocketIPTextField.getText());
            Integer testCaseSocketPort = new Integer(testCaseSocketPortTextField.getText());
            InetAddress targetIP = InetAddress.getByName(targetIPTextField.getText());
            Integer targetPort = new Integer(targetPortTextField.getText());
            config.setTestCaseSocketIP(testCaseSocketIP);
            config.setTestCaseSocketPort(testCaseSocketPort);
            config.setTargetIP(targetIP);
            config.setTargetPort(targetPort);
            config.setTestCaseDirPath(testCaseDirTextField.getText());
            mainWindow.loadTestCaseComboBox();
            mainWindow.renewSocket();
        }
        catch (BadProxyException e) {
            if(sb == null){
                sb = new StringBuffer();
            }
            sb.append("\n- Test Case Socket IP & Port settings");
        }
        catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Wrong IP Format", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong Port Format", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (SocketException e) {
            if(sb == null){
                sb = new StringBuffer();
            }
            sb.append("\n- Test Case Socket IP & Port settings");
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.pack();
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(flowLayout);
            buttonPanel.add(getOkButton(), null);
            buttonPanel.add(getCancelButton(), null);
            buttonPanel.add(getApplyButton(), null);
        }
        return buttonPanel;
    }

    private JButton getApplyButton() {
        if (applyButton == null) {
            applyButton = new JButton();
            applyButton.setText("Apply");
            applyButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    } 
                }
            });
        }
        return applyButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    dispose();
                }
            });
        }
        return cancelButton;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyProxyModeChanges();
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    } else {
                        dispose();
                    }
                }
            });
        }
        return okButton;
    }

    private void showProxyError(){
        JOptionPane
        .showMessageDialog(
                this,
                "Cannot bind requested address.\n" +
                "Please check:" +
                sb.toString(),
                "Socket Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private JPanel getTestCaseSettingsPanel() {
        if (testCaseSettingsPanel == null) {
            
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0;
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.weighty = 1.0;
            
            testCaseSettingsPanel = new JPanel();
            testCaseSettingsPanel.setLayout(new GridBagLayout());
            testCaseSettingsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
            testCaseSettingsPanel.add(getTestCaseModeImagePanel(), gridBagConstraints);
            testCaseSettingsPanel.add(getTestCaseFieldsPanel(), gridBagConstraints1);
            testCaseSettingsPanel.add(getTestCaseDirPanel(), gridBagConstraints2);
            
        }
        return testCaseSettingsPanel;
    }

    private JTextField getTestCaseSocketIPTextField() {
        if (testCaseSocketIPTextField == null) {
            testCaseSocketIPTextField = new JTextField();
            testCaseSocketIPTextField.setPreferredSize(new java.awt.Dimension(200, 20));
            testCaseSocketIPTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return testCaseSocketIPTextField;
    }

    private JTextField getTestCaseSocketPortTextField() {
        if (testCaseSocketPortTextField == null) {
            testCaseSocketPortTextField = new JTextField();
            testCaseSocketPortTextField.setPreferredSize(new java.awt.Dimension(50, 20));
            testCaseSocketPortTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return testCaseSocketPortTextField;
    }

    private JTextField getTargetIPTextField() {
        if (targetIPTextField == null) {
            targetIPTextField = new JTextField();
            targetIPTextField.setPreferredSize(new java.awt.Dimension(200, 20));
            targetIPTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return targetIPTextField;
    }

    private JTextField getTargetPortTextField() {
        if (targetPortTextField == null) {
            targetPortTextField = new JTextField();
            targetPortTextField.setPreferredSize(new java.awt.Dimension(50, 20));
            targetPortTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    applyTestCaseModeChanges();
                    if (sb != null){
                        showProxyError();
                        sb = null;   
                    }
                }
            });
        }
        return targetPortTextField;
    }

    private JPanel getProxyModeImagePanel() {
        if (proxyModeImagePanel == null) {
            proxyModeImagePanel = new JPanel();
            
            proxyModeImageLabel = new JLabel(); 
            proxyModeImageLabel.setIcon(new ImageIcon(getClass().getResource(
                    "/ui/img/ProxyModePreferencesSettings.gif")));
            proxyModeImageLabel.setHorizontalAlignment(SwingConstants.LEFT);
            proxyModeImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
            
            proxyModeImagePanel.setName("proxyModeImagePanel");
            proxyModeImagePanel.setLayout(new BorderLayout());
            proxyModeImagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
            proxyModeImagePanel.add(proxyModeImageLabel, BorderLayout.NORTH);
        }
        return proxyModeImagePanel;
    }

    private JPanel getTestCaseModeImagePanel() {
        if (testCaseModeImagePanel == null) {  
            testCaseModeImagePanel = new JPanel();
            testCaseModeImagePanel.setName("testCaseImagePanel");
            testCaseModeImagePanel.setLayout(new BorderLayout());
            testCaseModeImagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
            
            testCaseModeImageLabel = new JLabel();
            testCaseModeImageLabel.setIcon(new ImageIcon(getClass().getResource(
                    "/ui/img/TestCaseModePreferencesSettings.gif")));
            testCaseModeImageLabel.setHorizontalAlignment(SwingConstants.LEFT);
            testCaseModeImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
            testCaseModeImagePanel.add(testCaseModeImageLabel, BorderLayout.NORTH);
        }
        return testCaseModeImagePanel;
    }

    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jTabbedPane.addTab("Proxy Mode", null, getProxySettingsPanel(), null);
            jTabbedPane.addTab("Test Case Mode", null, getTestCaseSettingsPanel(), null);
        }
        return jTabbedPane;
    }

    private JPanel getProxyFieldsPanel() {
        if (proxyFieldsPanel == null) {

            pbxIPPortLabel = new JLabel();
            pbxIPPortLabel.setText("[ PbxIP ] : [ PbxPort ]");
            cliendIPPortLabel = new JLabel();
            cliendIPPortLabel.setText("[ ClientIP ] : [ ClientPort ]");         
            proxySocketPortLabel = new JLabel();
            proxySocketPortLabel.setText("[ ProxySocketIP ] : [ ProxySocketPort ]");
            proxyFieldsPanel = new JPanel();
            proxyFieldsPanel.setLayout(new GridBagLayout());
            proxyFieldsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "IP & Port Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            pbxPortLabel = new JLabel();
            pbxPortLabel.setText(":");
            pbxLabel = new JLabel();
            pbxLabel.setText("PBX");
            proxyPortLabel = new JLabel();
            proxyPortLabel.setText(":");
            proxyLabel = new JLabel();
            proxyLabel.setText("SOCKET");
            clientPortLabel = new JLabel();
            clientPortLabel.setText(":");
            clientLabel = new JLabel();
            clientLabel.setText("CLIENT");
            proxyModePortLabel = new JLabel();
            proxyModePortLabel.setText("[ Port ]");
            proxyModeAddressLabel = new JLabel();
            proxyModeAddressLabel.setText("[ IP Address ]");
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.NONE;
            gridBagConstraints2.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 0; 

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.NONE;
            gridBagConstraints3.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints3.gridx = 3;
            gridBagConstraints3.gridy = 0;

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.NONE;
            gridBagConstraints4.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 1;

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.NONE;
            gridBagConstraints5.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridy = 1;

            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.NONE;
            gridBagConstraints6.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints6.insets = new java.awt.Insets(5, 0, 5, 0);
            gridBagConstraints6.gridx = 2;
            gridBagConstraints6.gridy = 1;

            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = GridBagConstraints.NONE;
            gridBagConstraints7.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints7.gridx = 3;
            gridBagConstraints7.gridy = 1;

            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = GridBagConstraints.NONE;
            gridBagConstraints8.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridy = 2;

            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.NONE;
            gridBagConstraints9.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints9.gridx = 1;
            gridBagConstraints9.gridy = 2;

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = GridBagConstraints.NONE;
            gridBagConstraints10.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints10.insets = new java.awt.Insets(5, 0, 5, 0);
            gridBagConstraints10.gridx = 2;
            gridBagConstraints10.gridy = 2;

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.NONE;
            gridBagConstraints11.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints11.gridx = 3;
            gridBagConstraints11.gridy = 2;

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.NONE;
            gridBagConstraints12.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.gridy = 3;

            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = GridBagConstraints.NONE;
            gridBagConstraints13.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints13.gridx = 1;
            gridBagConstraints13.gridy = 3;

            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = GridBagConstraints.NONE;
            gridBagConstraints14.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints14.insets = new java.awt.Insets(5, 0, 5, 0);
            gridBagConstraints14.gridx = 2;
            gridBagConstraints14.gridy = 3;

            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = GridBagConstraints.NONE;
            gridBagConstraints15.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints15.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints15.gridx = 3;
            gridBagConstraints15.gridy = 3;
            
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 4;
            gridBagConstraints19.gridy = 1;
            gridBagConstraints19.weightx = 1.0;            
            gridBagConstraints19.anchor = GridBagConstraints.LINE_START;

            
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 4;
            gridBagConstraints20.gridy = 2;
            gridBagConstraints20.weightx = 1.0;
            gridBagConstraints20.anchor = GridBagConstraints.LINE_START;
            
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 4;
            gridBagConstraints21.gridy = 3;   
            gridBagConstraints21.weightx = 1.0;
            gridBagConstraints21.anchor = GridBagConstraints.LINE_START;
            

            proxyFieldsPanel.add(proxyModeAddressLabel, gridBagConstraints2);
            proxyFieldsPanel.add(proxyModePortLabel, gridBagConstraints3);
            
            proxyFieldsPanel.add(clientLabel, gridBagConstraints8);
            proxyFieldsPanel.add(getClientIPTextField(), gridBagConstraints9);
            proxyFieldsPanel.add(clientPortLabel, gridBagConstraints10);
            proxyFieldsPanel.add(getClientPortTextField(), gridBagConstraints11);
            
            proxyFieldsPanel.add(proxyLabel, gridBagConstraints4);
            proxyFieldsPanel.add(getProxyIPTextField(), gridBagConstraints5);
            proxyFieldsPanel.add(proxyPortLabel, gridBagConstraints6);
            proxyFieldsPanel.add(getProxyPortTextField(), gridBagConstraints7);
            
            proxyFieldsPanel.add(pbxLabel, gridBagConstraints12);
            proxyFieldsPanel.add(getPbxIPTextField(), gridBagConstraints13);
            proxyFieldsPanel.add(pbxPortLabel, gridBagConstraints14);
            proxyFieldsPanel.add(getPbxPortTextField(), gridBagConstraints15);
            proxyFieldsPanel.add(proxySocketPortLabel, gridBagConstraints19);
            proxyFieldsPanel.add(cliendIPPortLabel, gridBagConstraints20);
            proxyFieldsPanel.add(pbxIPPortLabel, gridBagConstraints21);
        }
        return proxyFieldsPanel;
    }

    private JPanel getTestCaseFieldsPanel() {
        if (testCaseFieldsPanel == null) {
            targetIPPortLabel = new JLabel();
            targetIPPortLabel.setText("[ TargetIP ] : [ TargetPort ]");
            socketIPPortLabel = new JLabel();
            socketIPPortLabel.setText("[ TestCaseSocketIP ] : [ TestCaseSocketPort ]");
            testCaseDirLabel = new JLabel();
            testCaseDirLabel.setText("Location");
            testCaseModePortLabel = new JLabel();
            testCaseModePortLabel.setText("[ Port ]");
            testCaseModeAddressLabel = new JLabel();
            testCaseModeAddressLabel.setText("[ IP Address ]");
            
            targetPortLabel = new JLabel();
            targetPortLabel.setText(":");
            testCaseSocketPortLabel = new JLabel();
            testCaseSocketPortLabel.setText(":");
            targetLabel = new JLabel();
            targetLabel.setText("TARGET");
            targetLabel.setVerticalAlignment(SwingConstants.TOP);
            testCaseSocketLabel = new JLabel();
            testCaseSocketLabel.setText("SOCKET");
            
            testCaseFieldsPanel = new JPanel();
            testCaseFieldsPanel.setLayout(new GridBagLayout());
            testCaseFieldsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "IP & Port Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
                           
            GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
            gridBagConstraints03.fill = GridBagConstraints.NONE;
            gridBagConstraints03.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints03.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints03.gridx = 1;
            gridBagConstraints03.gridy = 0;           
            testCaseFieldsPanel.add(testCaseModeAddressLabel, gridBagConstraints03);
            
            GridBagConstraints gridBagConstraints04 = new GridBagConstraints();
            gridBagConstraints04.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints04.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints04.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints04.gridx = 3;
            gridBagConstraints04.gridy = 0;
            gridBagConstraints04.weightx = 1.0;
            testCaseFieldsPanel.add(testCaseModePortLabel, gridBagConstraints04);

            GridBagConstraints gridBagConstraints05 = new GridBagConstraints();
            gridBagConstraints05.fill = GridBagConstraints.NONE;
            gridBagConstraints05.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints05.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints05.gridx = 0;
            gridBagConstraints05.gridy = 1;
            testCaseFieldsPanel.add(testCaseSocketLabel, gridBagConstraints05);
            
            GridBagConstraints gridBagConstraints06 = new GridBagConstraints();
            gridBagConstraints06.fill = GridBagConstraints.NONE;
            gridBagConstraints06.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints06.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints06.gridx = 1;
            gridBagConstraints06.gridy = 1;
            testCaseFieldsPanel.add(getTestCaseSocketIPTextField(), gridBagConstraints06);
            
            GridBagConstraints gridBagConstraints07 = new GridBagConstraints();
            gridBagConstraints07.fill = GridBagConstraints.NONE;
            gridBagConstraints07.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints07.insets = new java.awt.Insets(5, 0, 5, 0);
            gridBagConstraints07.gridx = 2;
            gridBagConstraints07.gridy = 1;
            testCaseFieldsPanel.add(testCaseSocketPortLabel, gridBagConstraints07);
            
            GridBagConstraints gridBagConstraints08 = new GridBagConstraints();
            gridBagConstraints08.fill = GridBagConstraints.NONE;
            gridBagConstraints08.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints08.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints08.gridx = 3;
            gridBagConstraints08.gridy = 1;
            testCaseFieldsPanel.add(getTestCaseSocketPortTextField(), gridBagConstraints08);
            
            GridBagConstraints gridBagConstraints09 = new GridBagConstraints();
            gridBagConstraints09.fill = GridBagConstraints.NONE;
            gridBagConstraints09.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints09.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints09.gridx = 0;
            gridBagConstraints09.gridy = 2;
            testCaseFieldsPanel.add(targetLabel, gridBagConstraints09);
            
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = GridBagConstraints.NONE;
            gridBagConstraints10.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 2;
            testCaseFieldsPanel.add(getTargetIPTextField(), gridBagConstraints10);
            
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.NONE;
            gridBagConstraints11.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints11.insets = new java.awt.Insets(5, 0, 5, 0);
            gridBagConstraints11.gridx = 2;
            gridBagConstraints11.gridy = 2;
            testCaseFieldsPanel.add(targetPortLabel, gridBagConstraints11);
            
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.NONE;
            gridBagConstraints12.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.gridx = 3;
            gridBagConstraints12.gridy = 2;   
            
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.gridx = 4;
            gridBagConstraints22.gridy = 1;
            gridBagConstraints22.weightx = 1.0;
            gridBagConstraints22.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints22.insets = new java.awt.Insets(0, 0, 0, 5);

            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.gridx = 4;
            gridBagConstraints23.gridy = 2;
            gridBagConstraints23.weightx = 1.0;
            gridBagConstraints23.anchor = GridBagConstraints.LINE_START;
            

            
            testCaseFieldsPanel.add(getTargetPortTextField(), gridBagConstraints12);

            testCaseFieldsPanel.add(socketIPPortLabel, gridBagConstraints22);
            testCaseFieldsPanel.add(targetIPPortLabel, gridBagConstraints23);
        }
        return testCaseFieldsPanel;
    }

    private JTextField getTestCaseDirTextField() {
        if (testCaseDirTextField == null) {
            testCaseDirTextField = new JTextField();
            testCaseDirTextField.setPreferredSize(new java.awt.Dimension(200, 20));
        }
        return testCaseDirTextField;
    }

    private JButton getBrowseButton() {
        if (browseButton == null) {
            browseButton = new JButton();
            browseButton.setText("Browse");
            browseButton.setPreferredSize(new Dimension(90, 20));
            browseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    showFileChooser();
                }
            });
        }
        return browseButton;
    }
    
    private void showFileChooser(){
        
        JFileChooser fc = new JFileChooser(config.getTestCaseDirPath());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showDialog(this, "Set Location") == JFileChooser.APPROVE_OPTION){
            String testCasePath = "";
            testCasePath = fc.getSelectedFile().getAbsolutePath();

            if (fc.getSelectedFile().exists()){
                testCaseDirTextField.setText(testCasePath);
            }
        }
     
    }

    private JPanel getTestCaseDirPanel() {
        if (testCaseDirPanel == null) {
            testCaseDirPanel = new JPanel();
            testCaseDirPanel.setLayout(new GridBagLayout());
            testCaseDirPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Test Cases", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
 
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints16.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.fill = GridBagConstraints.NONE;
            testCaseDirPanel.add(testCaseDirLabel, gridBagConstraints16);     
            
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints17.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.fill = GridBagConstraints.NONE;
            testCaseDirPanel.add(getTestCaseDirTextField(), gridBagConstraints17);
            
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.anchor = GridBagConstraints.FIRST_LINE_START;
            gridBagConstraints18.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints18.gridx = 2;
            gridBagConstraints18.gridy = 0;
            gridBagConstraints18.weightx = 1.0;
            gridBagConstraints18.fill = GridBagConstraints.NONE;
            testCaseDirPanel.add(getBrowseButton(), gridBagConstraints18);                 
                       
        }
        return testCaseDirPanel;
    }

}
