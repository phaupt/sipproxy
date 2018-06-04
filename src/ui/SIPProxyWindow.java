package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import pd.ITestCase;
import pd.ITestCaseRef;
import persistence.ElementFactory;
import persistence.ParserException;
import persistence.TestCaseParser;
import persistence.TestCaseRefParser;
import persistence.ValidatorException;
import proxy.BadProxyException;
import proxy.Proxy;
import proxy.UDPDatagram;
import testexec.TestCaseExecutionHandler;
import testexec.history.IMessagesLog;
import testexec.history.ITestCaseExecutionObserver;
import testexec.history.ITestCaseHistory;
import testexec.history.ITestCaseRun;
import testexec.history.TestCaseHistory;
import ui.history.HistoryTreeCellRenderer;
import ui.history.HistoryTreeModel;
import ui.history.nodes.HistoryCompositeTreeNode;
import ui.history.nodes.HistoryMessageTreeNode;
import ui.history.nodes.IHistoryTreeNode;
import ui.testexec.TestCaseHistoryTreeCellRenderer;
import ui.testexec.TestCaseHistoryTreeModel;
import util.MiscUtil;
import util.SimpleLogger;
import config.Configuration;
import config.XMLConfigFile;
import config.transformation.TransformationConfig;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class SIPProxyWindow extends JFrame implements ITestCaseExecutionObserver{
    
    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu viewMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenu helpMenu = null;
    private JMenuItem aboutMenuItem = null;
    private JMenu designMenu = null;
    private JRadioButtonMenuItem defaultRadioButtonMenuItem = null;
    private JRadioButtonMenuItem javaRadioButtonMenuItem = null;
    private JRadioButtonMenuItem windowsRadioButtonMenuItem = null;
    private JPanel middlePanel = null;
    private JSplitPane verticalSplitPane = null;
    private JPanel leftPanel = null;
    private JTree historyTree = null;
    private Proxy proxy = null;
    private TransformationConfig dynamicTransformationConfig = null;
    private Configuration config = null;
    private final String CONFIG_FILENAME = "config.xml";
    private HistoryTreeModel historyTreeModel;
    private JScrollPane historyScrollPane = null;
    private JMenu sipHistoryMenu = null;
    private JRadioButtonMenuItem timeorderedRadioButtonMenuItem = null;
    private JRadioButtonMenuItem groupedByCSeqRadioButtonMenuItem = null;
    private JMenuItem preferencesMenuItem = null;
    private JMenuItem proxyLogMenuItem = null;
    private JMenuItem proxyLogPopupMenuItem = null;
    private JPopupMenu proxyHistoryTreePopupMenu = null;
    private JPopupMenu testCaseHistoryTreePopupMenu = null;
    private JPopupMenu testCaseHistoryTreeWarningPopupMenu = null;
    private JMenuItem resendMenuItem = null;
    private JMenuItem saveAsMenuItem = null;
    private JMenuItem viewDebugLogMenuItem = null;
    private JMenuItem resendRequestMenuItem = null;
    private JMenuItem deleteSelectedMenuItem = null;
    private JMenu actionMenu = null;
    private JMenuItem sendMessageMenuItem = null;
    private JMenuItem copyToClipboardMenuItem = null;
    private JMenuItem clearMenuItem = null;
    private JMenuItem clearPopupMenuItem = null;
    private JTabbedPane leftTabbedPane = null;
    private JPanel leftProxyPanel = null;
    private JPanel leftTestCasePanel = null;
    private JPanel leftTopPanel = null;
    private JPanel leftBottomPanel = null;
    private JScrollPane testCaseScrollPane = null;
    private JLabel executeTestLabel = null;
    private JButton runTestCaseButton = null;
    private JComboBox testCaseComboBox = null;
    private TestCaseModePanel rightTestCasePanel = null;
    private ProxyModePanel proxyModePanel = null;
    private TestCaseRefComboBoxModel testCaseRefComboBoxModel = null;
    
    private ITestCaseHistory testCaseHistory = null;
    private TestCaseHistoryTreeModel testCaseHistoryTreeModel = null;  
    private TestCaseRefParser testCaseRefParser = null;
    private TestCaseParser testCaseParser = null;
    private TestCaseExecutionHandler testCaseExecutionHandler = null;  
    private JButton reloadTestCaseButton = null;
    
    private StringBuffer sb;
    private JTree testCaseHistoryTree = null;
    private JPanel rightPanel = null;
    
    public SIPProxyWindow() {
        super();

        MiscUtil.deleteDebugLog(SimpleLogger.DEBUG_LOG_PROXY);
        config = XMLConfigFile.load(CONFIG_FILENAME);

        //Initialize parsers
        testCaseRefParser = new TestCaseRefParser();
        testCaseParser = new TestCaseParser(new ElementFactory(),config);
        
        //Initialize testCaseHistory
        testCaseHistory = new TestCaseHistory(SimpleLogger.LOG_DIR);
        
        try {
            testCaseExecutionHandler = new TestCaseExecutionHandler(config);
        }
        catch(BadProxyException e){
            setTestCaseSocketLocalIP();    
        }
          
        startProxy();

        dynamicTransformationConfig = config.getDynamicTransformationConfig();

        initialize();

        setLookAndFeel('w');
        
    }
    
    public void checkErrorStatus(){
        if (sb != null) {
            showProxyError();
            showPreferencesWindow();
        }
        
        checkRefParserError();
    }
    
    private void setTestCaseSocketLocalIP(){
        try {
            config.setTestCaseSocketIP(java.net.InetAddress.getLocalHost());
            try {    
                renewSocket();
            }
            catch (BadProxyException e) {
                if(sb == null){
                    sb = new StringBuffer();
                }
                sb.append("\n- Test Case Socket IP & Port settings");
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
        }
        catch (UnknownHostException e) {
            if(sb == null){
                sb = new StringBuffer();
            }
            sb.append("\n- Test Case Socket IP & Port settings");
        }
    }
    
    private void setProxySocketLocalIP(){
        try {
            proxy.setConfigChanges(config.getClientIP(), config.getClientPort(), java.net.InetAddress.getLocalHost(), config.getProxySocketPort(), config.getPbxIP(),
                    config.getPbxPort());
        }
        catch (BadProxyException e) {
            if(sb == null){
                sb = new StringBuffer();
            }
            sb.append("\n- Proxy Socket IP & Port settings");
        }
        catch (UnknownHostException e) {
            if(sb == null){
                sb = new StringBuffer();
            }
            sb.append("\n- Proxy Socket IP & Port settings");
        }
    }
    
    private void startProxy(){
        try {
            proxy = new Proxy(config);
            proxy.startProxy();
        }
        catch (BadProxyException e) {
            setProxySocketLocalIP();
        }
    }
    
    private void showProxyError(){
        JOptionPane
        .showMessageDialog(
                this,
                "Cannot bind requested address.\n" +
                "Please check:" +
                sb.toString(),
                "Socket Error", JOptionPane.ERROR_MESSAGE);
        sb = null;   
    }
    
    public void renewSocket() throws SocketException{
        if (testCaseExecutionHandler == null){
            testCaseExecutionHandler = new TestCaseExecutionHandler(config);
        } else {
            testCaseExecutionHandler.renewSocket();
        }    
    }
    
    private void checkRefParserError(){
        if(testCaseRefParser.getErrorBuffer() != null){
            showParserException(testCaseRefParser.getErrorBuffer().toString());
            testCaseRefParser.resetErrorBuffer();
        }
    }

    private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocation(new java.awt.Point(50, 50));
        this.setJMenuBar(getJJMenuBar());
        this.setPreferredSize(new java.awt.Dimension(1280, 1024));
        this.setSize(1024, 768);
        this.setContentPane(getJContentPane());
        this.setTitle("SIP Proxy - VoIP Security Test Tool");
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing( java.awt.event.WindowEvent e ) {
                exit();
            }
        });
        initializeHistoryTree();
        getTimeorderedRadioButtonMenuItem().setSelected(true);
    }

    private void initializeHistoryTree() {
        historyTreeModel = new HistoryTreeModel(proxy.getSipHistory());
        historyTree.setModel(historyTreeModel);
        historyTree.setCellRenderer(new HistoryTreeCellRenderer());
    }

    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getActionMenu());
            jJMenuBar.add(getViewMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.setMnemonic('F');
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    private JMenu getViewMenu() {
        if (viewMenu == null) {
            viewMenu = new JMenu();
            viewMenu.setText("Window");
            viewMenu.setMnemonic('W');
            viewMenu.add(getDesignMenu());
            viewMenu.addSeparator();
            viewMenu.add(getPreferencesMenuItem());
        }
        return viewMenu;
    }

    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/exit.gif")));
            exitMenuItem.setMnemonic('x');
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    exit();
                }
            });
        }
        return exitMenuItem;
    }

    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("?");
            helpMenu.setMnemonic('?');
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            aboutMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/about.gif")));
            aboutMenuItem.setMnemonic('o');
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    showInfo();
                }
            });
        }
        return aboutMenuItem;
    }

    private void showInfo() {
        AboutDialog about = new AboutDialog();
        about.pack();
        about.setVisible(true);
    }

    private JMenu getDesignMenu() {
        if (designMenu == null) {
            designMenu = new JMenu();
            designMenu.setText("Application Design");
            designMenu.setIcon(new ImageIcon(getClass().getResource("/ui/img/design.gif")));
            designMenu.setMnemonic('D');
            JRadioButtonMenuItem metal = getJavaRadioButtonMenuItem();
            JRadioButtonMenuItem cdeMotif = getDefaultRadioButtonMenuItem();
            JRadioButtonMenuItem windows = getWindowsRadioButtonMenuItem();
            ButtonGroup group = new ButtonGroup();
            group.add(metal);
            group.add(cdeMotif);
            group.add(windows);
            designMenu.add(getDefaultRadioButtonMenuItem());
            designMenu.add(getJavaRadioButtonMenuItem());
            designMenu.add(getWindowsRadioButtonMenuItem());
        }
        return designMenu;
    }

    private JRadioButtonMenuItem getDefaultRadioButtonMenuItem() {
        if (defaultRadioButtonMenuItem == null) {
            defaultRadioButtonMenuItem = new JRadioButtonMenuItem();
            defaultRadioButtonMenuItem.setText("Current OS");
            defaultRadioButtonMenuItem.setSelected(true);
            defaultRadioButtonMenuItem.setMnemonic('C');
            defaultRadioButtonMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                            if (defaultRadioButtonMenuItem.isArmed()) {
                                setLookAndFeel('d');
                            }
                        }
                    });
        }
        return defaultRadioButtonMenuItem;
    }

    private JRadioButtonMenuItem getJavaRadioButtonMenuItem() {
        if (javaRadioButtonMenuItem == null) {
            javaRadioButtonMenuItem = new JRadioButtonMenuItem();
            javaRadioButtonMenuItem.setText("Java Style");
            javaRadioButtonMenuItem.setMnemonic('J');
            javaRadioButtonMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                            if (javaRadioButtonMenuItem.isArmed()) {
                                setLookAndFeel('j');
                            }
                        }
                    });
        }
        return javaRadioButtonMenuItem;
    }

    private JRadioButtonMenuItem getWindowsRadioButtonMenuItem() {
        if (windowsRadioButtonMenuItem == null) {
            windowsRadioButtonMenuItem = new JRadioButtonMenuItem();
            windowsRadioButtonMenuItem.setText("Windows Style");
            windowsRadioButtonMenuItem.setMnemonic('W');
            windowsRadioButtonMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                            if (windowsRadioButtonMenuItem.isArmed()) {
                                setLookAndFeel('w');
                            }
                        }
                    });
        }
        return windowsRadioButtonMenuItem;
    }

    private JPanel getMiddlePanel() {
        if (middlePanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.weightx = 1.0;
            middlePanel = new JPanel();
            middlePanel.setLayout(new GridBagLayout());
            middlePanel.add(getVerticalSplitPane(), gridBagConstraints);
        }
        return middlePanel;
    }

    private JSplitPane getVerticalSplitPane() {
        if (verticalSplitPane == null) {
            verticalSplitPane = new JSplitPane();
            verticalSplitPane.setDividerLocation(250);
            verticalSplitPane.setLeftComponent(getLeftPanel());
        }
        return verticalSplitPane;
    }

    private JPanel getLeftPanel() {
        if (leftPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.weighty = 1.0;
            gridBagConstraints1.weightx = 1.0;
            leftPanel = new JPanel();
            leftPanel.setLayout(new GridBagLayout());
            leftPanel.add(getLeftTabbedPane(), gridBagConstraints1);
        }
        return leftPanel;
    }

    private JPanel getRightPanel() {
        if (leftTabbedPane.getSelectedIndex() == 1){
            rightPanel = getRightTestCaseModePanel();
        } else {
            rightPanel = getRightProxyModePanel();
        }
        return rightPanel;
    }

    private JPanel getRightTestCaseModePanel() {
        if (rightTestCasePanel == null) {
            rightTestCasePanel = new TestCaseModePanel();
        }
        return rightTestCasePanel;
    }
    
    private ProxyModePanel getRightProxyModePanel(){
        if(proxyModePanel==null){
            proxyModePanel = new ProxyModePanel(dynamicTransformationConfig);
        }
        return proxyModePanel;
    }

    private JTree getHistoryTree() {
        if (historyTree == null) {
            historyTree = new JTree();
            historyTree.setRootVisible(false);
            historyTree
                    .setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
            historyTree.setShowsRootHandles(true);
            // historyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            historyTree.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseReleased( java.awt.event.MouseEvent e ) {
                    mayShowProxyHistoryTreePopupMenu(e);
                }

                public void mousePressed( java.awt.event.MouseEvent e ) {
                    mayShowProxyHistoryTreePopupMenu(e);
                }

                public void mouseClicked( java.awt.event.MouseEvent e ) {
                    // In Case Nothing has been selected
                    TreePath path = historyTree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) {
                        clearHistorySelection();
                    }
                }
            });
            historyTree
                    .addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

                        public void valueChanged( javax.swing.event.TreeSelectionEvent e ) {
                            Object selectedNode = null;
                            if (historyTree.getSelectionPaths() != null) {
                                selectedNode = historyTree.getSelectionPaths()[historyTree
                                        .getSelectionPaths().length - 1]
                                        .getLastPathComponent();
                            }
                            if (selectedNode != null) {
                                if (selectedNode instanceof HistoryMessageTreeNode) {
                                    // A SIP Message has been selected
                                    showSIPMessage(new String(
                                            ((HistoryMessageTreeNode) selectedNode)
                                                    .getUDPDatagram().getData()));
                                }
                                else if (selectedNode instanceof HistoryCompositeTreeNode) {
                                    // A SIP Message Group has been selected
                                    // clearHistory();
                                    historyTree.removeSelectionPath(historyTree
                                            .getLeadSelectionPath());
                                }

                            }
                        }
                    });
        }
        return historyTree;
    }

    private void mayShowProxyHistoryTreePopupMenu( MouseEvent e ) {
        if (e.isPopupTrigger()) {
            TreePath path = historyTree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                // Select Right-Clicked Node
                historyTree.addSelectionPath(path);
                getProxyHistoryTreePopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    private void mayShowTestCaseHistoryTreePopupMenu( MouseEvent e ) {
        if (e.isPopupTrigger()) {
            TreePath path = testCaseHistoryTree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                //Show PopUp
                
                Object obj = path.getLastPathComponent();
                if (obj instanceof ITestCaseRun){
                    //TestCase selected -> PopUp
                    testCaseHistoryTree.addSelectionPath(path);
                    getTestCaseHistoryTreePopupMenu().show(e.getComponent(), e.getX(), e.getY());
                    if (testCaseExecutionHandler.isTestExecutionRunning()){
                        deleteSelectedMenuItem.setEnabled(false);
                    }
                } 
                else if (obj instanceof String) {
                    //Warning selected -> PopUp
                    testCaseHistoryTree.addSelectionPath(path);
                    getTestCaseHistoryTreeWarningPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
    
    private void clearHistorySelection() {
        historyTree.clearSelection();
        proxyModePanel.clearMsgTextArea();
    }

    private void showSIPMessage( String sipMessage ) {
        proxyModePanel.updateMsgTextArea(sipMessage);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(0, 0, 0, 0));
            jContentPane.add(getMiddlePanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private void setLookAndFeel( char type ) {
        try {
            switch (type) {
                case 'w':
                    UIManager
                            .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case 'd':
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case 'j':
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
            }
        }
        catch (Exception e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void exit() {
        if(testCaseExecutionHandler != null && testCaseExecutionHandler.isTestExecutionRunning()){
            //abort running test case                        
            testCaseExecutionHandler.abortRunningTestCase();
            Thread.yield();
        }
        
        XMLConfigFile.save(config, CONFIG_FILENAME);
        System.exit(0);
    }

    private JScrollPane getHistoryScrollPane() {
        if (historyScrollPane == null) {
            historyScrollPane = new JScrollPane();
            historyScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0,
                    0, 0));
            historyScrollPane.setViewportView(getHistoryTree());
        }
        return historyScrollPane;
    }

    private JMenu getSipHistoryMenu() {
        if (sipHistoryMenu == null) {
            sipHistoryMenu = new JMenu();
            sipHistoryMenu.setText("Sort History");
            sipHistoryMenu.setIcon(new ImageIcon(getClass().getResource("/ui/img/order.gif")));
            sipHistoryMenu.setMnemonic('H');
            JRadioButtonMenuItem timeOrdered = getTimeorderedRadioButtonMenuItem();
            timeOrdered.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged( java.awt.event.ItemEvent e ) {
                    changeHistoryTreeModel();
                }
            });
            JRadioButtonMenuItem groupedByCSeq = getGroupedByCSeqRadioButtonMenuItem();
            groupedByCSeq.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged( java.awt.event.ItemEvent e ) {
                    changeHistoryTreeModel();
                }
            });
            ButtonGroup group = new ButtonGroup();
            group.add(timeOrdered);
            group.add(groupedByCSeq);

            sipHistoryMenu.add(getTimeorderedRadioButtonMenuItem());
            sipHistoryMenu.add(getGroupedByCSeqRadioButtonMenuItem());

        }
        return sipHistoryMenu;
    }

    private void changeHistoryTreeModel() {
        if (timeorderedRadioButtonMenuItem.isSelected()) {
            historyTreeModel.setMode(HistoryTreeModel.ORDER_BY_TIME_MODE);
        }
        else if (groupedByCSeqRadioButtonMenuItem.isSelected()) {
            historyTreeModel.setMode(HistoryTreeModel.GROUP_BY_CSEQ_MODE);
        }
    }

    private JRadioButtonMenuItem getTimeorderedRadioButtonMenuItem() {
        if (timeorderedRadioButtonMenuItem == null) {
            timeorderedRadioButtonMenuItem = new JRadioButtonMenuItem();
            timeorderedRadioButtonMenuItem.setText("Sort by Time");
            timeorderedRadioButtonMenuItem.setMnemonic('O');
        }
        return timeorderedRadioButtonMenuItem;
    }

    private JRadioButtonMenuItem getGroupedByCSeqRadioButtonMenuItem() {
        if (groupedByCSeqRadioButtonMenuItem == null) {
            groupedByCSeqRadioButtonMenuItem = new JRadioButtonMenuItem();
            groupedByCSeqRadioButtonMenuItem.setText("Sort by CSeq");
            groupedByCSeqRadioButtonMenuItem.setMnemonic('G');
        }
        return groupedByCSeqRadioButtonMenuItem;
    }

    private JMenuItem getPreferencesMenuItem() {
        if (preferencesMenuItem == null) {
            preferencesMenuItem = new JMenuItem();
            preferencesMenuItem.setText("Preferences...");
            preferencesMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/preferences.gif")));
            preferencesMenuItem.setMnemonic('P');
            preferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    showPreferencesWindow();
                }
            });
        }
        return preferencesMenuItem;
    }

    public void showPreferencesWindow() {
        PreferencesDialog preferences = new PreferencesDialog(proxy, this);
        preferences.pack();
        preferences.setVisible(true);
    }
    
    

    private JMenuItem getProxyLogMenuItem() {
        if (proxyLogMenuItem == null) {
            proxyLogMenuItem = new JMenuItem();
            proxyLogMenuItem.setText("View Log...");
            proxyLogMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/txtLogo.gif")));
            proxyLogMenuItem.setMnemonic('P');
            proxyLogMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    DebugLogFrame debugView;
                    try {
                        debugView = new DebugLogFrame(SimpleLogger.DEBUG_LOG_PROXY,"Proxy Mode Debug Log");
                        debugView.pack();
                        debugView.setVisible(true);
                    } catch(OutOfMemoryError e2) {
                        debugView = null;
                    }          
                }
            });
        }
        return proxyLogMenuItem;
    }
    
    private JMenuItem getProxyLogPopupMenuItem() {
        if (proxyLogPopupMenuItem == null) {
        	proxyLogPopupMenuItem = new JMenuItem();
        	proxyLogPopupMenuItem.setText("View Log...");
        	proxyLogPopupMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/txtLogo.gif")));
        	proxyLogPopupMenuItem.setMnemonic('P');
        	proxyLogPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    DebugLogFrame debugView;
                    try {
                        debugView = new DebugLogFrame(SimpleLogger.DEBUG_LOG_PROXY,"Proxy Mode Debug Log");
                        debugView.pack();
                        debugView.setVisible(true);
                    } catch(OutOfMemoryError e2) {
                        debugView = null;
                    }          
                }
            });
        }
        return proxyLogPopupMenuItem;
    }

    private JPopupMenu getProxyHistoryTreePopupMenu() {
        if (proxyHistoryTreePopupMenu == null) {
            proxyHistoryTreePopupMenu = new JPopupMenu();
            proxyHistoryTreePopupMenu.add(getCopyToClipboardMenuItem());
            proxyHistoryTreePopupMenu.addSeparator();
            proxyHistoryTreePopupMenu.add(getSaveAsMenuItem());
            proxyHistoryTreePopupMenu.add(getResendMenuItem());
            proxyHistoryTreePopupMenu.addSeparator();
            proxyHistoryTreePopupMenu.add(getClearPopupMenuItem());
            proxyHistoryTreePopupMenu.add(getProxyLogPopupMenuItem());
        }
        return proxyHistoryTreePopupMenu;
    }
    
    private JPopupMenu getTestCaseHistoryTreePopupMenu() {
        if (testCaseHistoryTreePopupMenu == null) {
            testCaseHistoryTreePopupMenu = new JPopupMenu();           
            testCaseHistoryTreePopupMenu.add(getDeleteSelectedMenuItem());
            testCaseHistoryTreePopupMenu.add(getViewDebugLogMenuItem());
        }
        return testCaseHistoryTreePopupMenu;
    }
    
    private JPopupMenu getTestCaseHistoryTreeWarningPopupMenu() {
        if (testCaseHistoryTreeWarningPopupMenu == null) {
            testCaseHistoryTreeWarningPopupMenu = new JPopupMenu();
            testCaseHistoryTreeWarningPopupMenu.add(getResendRequestMenuItem());
        }
        return testCaseHistoryTreeWarningPopupMenu;
    }
    
    private JMenuItem getResendRequestMenuItem() {
        if (resendRequestMenuItem == null) {
            resendRequestMenuItem = new JMenuItem();
            resendRequestMenuItem.setText("Resend...");
            resendRequestMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/send.gif")));
            resendRequestMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    resendRequestMessage();
                }
            });
        }
        return resendRequestMenuItem;
    }
    
    private JMenuItem getViewDebugLogMenuItem() {
        if (viewDebugLogMenuItem == null) {
            viewDebugLogMenuItem = new JMenuItem();
            viewDebugLogMenuItem.setText("View Log...");
            viewDebugLogMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/txtLogo.gif")));
            viewDebugLogMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    viewTestCaseDebug();
                }
            });
        }
        return viewDebugLogMenuItem;
    }
    
    private void resendRequestMessage(){
        TreePath[] selectedPahts = testCaseHistoryTree.getSelectionPaths();
        TreePath lastSelectedPath = selectedPahts[selectedPahts.length-1];
        
        Object selectedNode = lastSelectedPath.getLastPathComponent();
        
        if (selectedNode instanceof String){
            String msgID = (String)selectedNode;

            TreePath parentPath = lastSelectedPath.getParentPath();
            
            Object parentNode = parentPath.getLastPathComponent();
            if ( parentNode instanceof ITestCaseRun){ 
                String targetIP = ((ITestCaseRun) parentNode).getTargetIP();
                int targetPort = ((ITestCaseRun) parentNode).getTargetPort();
                String requestMessage = ((ITestCaseRun) parentNode).getWarningLog().getWarningRequest(msgID);

                new SendMessageDialog(config, this, requestMessage, targetIP, targetPort).setVisible(true);
            }
        } 
        
    }
    
    private void viewTestCaseDebug(){
        TreePath[] selectedPahts = testCaseHistoryTree.getSelectionPaths();
        TreePath lastSelectedPath = selectedPahts[selectedPahts.length-1];
        
        Object selectedNode = lastSelectedPath.getLastPathComponent();
        if(selectedNode instanceof ITestCaseRun){
            IMessagesLog log = ((ITestCaseRun) selectedNode).getMessagesLog();
            DebugLogFrame debugView;
            try {
                debugView = new DebugLogFrame(log,"Test Case Mode Debug Log");
                debugView.pack();
                debugView.setVisible(true);
            } catch(OutOfMemoryError e2) {
                debugView = null;
            }  
        }
    }
    
    private JMenuItem getDeleteSelectedMenuItem() {
        if (deleteSelectedMenuItem == null) {
            deleteSelectedMenuItem = new JMenuItem();
            deleteSelectedMenuItem.setText("Clear selected");
            deleteSelectedMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/trash.gif")));
            deleteSelectedMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                            deleteSelectedTestCases();
                        }
                    });
        }
        return deleteSelectedMenuItem;
    }
    private void deleteSelectedTestCases(){
        int answer = JOptionPane.showConfirmDialog(null, 
                "Delete the selected log files ?", "Delete Log Files", JOptionPane.YES_NO_OPTION);
        
        if(answer == 0){
        	//User has confirmed deletion
        	
            Vector<ITestCaseRun> testCaseRunsToDelete = new Vector<ITestCaseRun>();
            
            //Add selected test cases to vector
            for(TreePath selectionPath : testCaseHistoryTree.getSelectionPaths()){
                Object selectedNode = selectionPath.getLastPathComponent();
                if(selectedNode instanceof ITestCaseRun){
                    testCaseRunsToDelete.add((ITestCaseRun) selectedNode);
                }
            }
            
            //Delete testCases in Vector
            for(ITestCaseRun testCaseRun : testCaseRunsToDelete){
                testCaseHistory.deleteTestCaseRun(testCaseRun);
            }
            
            //Renew Test Case Report Panel
            int dividerLocation = verticalSplitPane.getDividerLocation();
            verticalSplitPane.remove(rightPanel);
            rightTestCasePanel = null;
            verticalSplitPane.add(getRightPanel());
            verticalSplitPane.setDividerLocation(dividerLocation);
            verticalSplitPane.revalidate();
        }   
    }

    private JMenuItem getResendMenuItem() {
        if (resendMenuItem == null) {
            resendMenuItem = new JMenuItem();
            resendMenuItem.setText("Resend...");
            resendMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/send.gif")));
            resendMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    IHistoryTreeNode treeNode = (IHistoryTreeNode) historyTree
                            .getSelectionPath().getLastPathComponent();
                    new SendMessageDialog(proxy, treeNode.getUDPDatagram())
                            .setVisible(true);
                }
            });
        }
        return resendMenuItem;
    }

    private JMenuItem getSaveAsMenuItem() {
        if (saveAsMenuItem == null) {
            saveAsMenuItem = new JMenuItem();
            saveAsMenuItem.setText("Save as...");
            saveAsMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/save.gif")));
            saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    saveMessageAs();
                }
            });
        }
        return saveAsMenuItem;
    }

    private void saveMessageAs() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            byte[] content = getSelectedContent();
            MiscUtil.saveContent(fc.getSelectedFile(), content);
        }
    }

    private byte[] getSelectedContent() {
        String beginOfMessage = "\r\n****** Begin of Message ******\r\n";
        String endOfMessage = "****** End of Message ******\r\n";

        StringBuffer selectedContent = new StringBuffer();
        for (TreePath selectedPath : historyTree.getSelectionPaths()) {
            IHistoryTreeNode selectedNode = (IHistoryTreeNode) selectedPath
                    .getLastPathComponent();
            UDPDatagram packet = selectedNode.getUDPDatagram();
            if (packet != null) {
                selectedContent.append(beginOfMessage);
                selectedContent.append(new String(packet.getDatagramPacket().getData()));
                selectedContent.append(endOfMessage);
            }

        }

        return selectedContent.toString().getBytes();
    }

    private JMenu getActionMenu() {
        if (actionMenu == null) {
            actionMenu = new JMenu();
            actionMenu.setText("Proxy Mode");
            actionMenu.setMnemonic('P');
            actionMenu.add(getSendMessageMenuItem());
            actionMenu.addSeparator();   
            actionMenu.add(getSipHistoryMenu());
            actionMenu.addSeparator();
            actionMenu.add(getClearMenuItem());
            actionMenu.add(getProxyLogMenuItem());
        }
        return actionMenu;
    }

    private JMenuItem getSendMessageMenuItem() {
        if (sendMessageMenuItem == null) {
            sendMessageMenuItem = new JMenuItem();
            sendMessageMenuItem.setText("Send Message...");
            sendMessageMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/send.gif")));
            sendMessageMenuItem.setMnemonic('S');
            sendMessageMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    new SendMessageDialog(proxy).setVisible(true);
                }
            });
        }
        return sendMessageMenuItem;
    }

    private JMenuItem getCopyToClipboardMenuItem() {
        if (copyToClipboardMenuItem == null) {
            copyToClipboardMenuItem = new JMenuItem();
            copyToClipboardMenuItem.setText("Copy to Clipboard");
            copyToClipboardMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/clipboard.gif")));
            copyToClipboardMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                            copyMessagesToClipboard();
                        }
                    });
        }
        return copyToClipboardMenuItem;
    }

    private void copyMessagesToClipboard() {
        ClipboardOwner o = new ClipboardOwner() {

            public void lostOwnership( Clipboard clipboard, Transferable contents ) {
                // do nothing
            }
        };
        getToolkit().getSystemClipboard().setContents(
                new StringSelection(new String(getSelectedContent())), o);
    }

    private JMenuItem getClearPopupMenuItem() {
        if (clearPopupMenuItem == null) {
        	clearPopupMenuItem = new JMenuItem();
        	clearPopupMenuItem.setText("Clear Messages");
        	clearPopupMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/trash.gif")));
        	clearPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    proxy.getSipHistory().clearHistory();
                    proxyModePanel.clearMsgTextArea();
                    MiscUtil.deleteDebugLog(SimpleLogger.DEBUG_LOG_PROXY);
                }
            });
        }
        return clearPopupMenuItem;
    }
    
    private JMenuItem getClearMenuItem() {
        if (clearMenuItem == null) {
            clearMenuItem = new JMenuItem();
            clearMenuItem.setText("Clear Messages");
            clearMenuItem.setIcon(new ImageIcon(getClass().getResource("/ui/img/trash.gif")));
            clearMenuItem.setMnemonic('C');
            clearMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    proxy.getSipHistory().clearHistory();
                    proxyModePanel.clearMsgTextArea();
                    MiscUtil.deleteDebugLog(SimpleLogger.DEBUG_LOG_PROXY);
                }
            });
        }
        return clearMenuItem;
    }

    private JTabbedPane getLeftTabbedPane() {
        if (leftTabbedPane == null) {
            leftTabbedPane = new JTabbedPane();
            leftTabbedPane.addTab("Proxy Mode", null, getLeftProxyPanel(), null);
            leftTabbedPane.addTab("Test Case Mode", null, getLeftTestCasePanel(), null);
        }
        return leftTabbedPane;
    }

    private JPanel getLeftProxyPanel() {
        if (leftProxyPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.gridy = -1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.gridx = -1;
            leftProxyPanel = new JPanel();
            leftProxyPanel.setLayout(new GridBagLayout());
            leftProxyPanel.add(getHistoryScrollPane(), gridBagConstraints3);
            leftProxyPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown( java.awt.event.ComponentEvent e ) {                  
                    clearMenuItem.setEnabled(true);
                    sendMessageMenuItem.setEnabled(true);
                    sipHistoryMenu.setEnabled(true);
                    proxyLogMenuItem.setEnabled(true);
                    verticalSplitPane.setRightComponent(getRightPanel());
                    verticalSplitPane.setDividerLocation(250);
                }
            });
        }
        return leftProxyPanel;
    }

    private JPanel getLeftTestCasePanel() {
        if (leftTestCasePanel == null) {
            leftTestCasePanel = new JPanel();
            leftTestCasePanel.setLayout(new BorderLayout());
            leftTestCasePanel.add(getLeftTopPanel(), BorderLayout.NORTH);
            leftTestCasePanel.add(getLeftBottomPanel(), BorderLayout.CENTER);
            leftTestCasePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown( java.awt.event.ComponentEvent e ) {
                    clearMenuItem.setEnabled(false);
                    sendMessageMenuItem.setEnabled(false);
                    sipHistoryMenu.setEnabled(false);
                    proxyLogMenuItem.setEnabled(false);                   
                    verticalSplitPane.setRightComponent(getRightPanel());
                    verticalSplitPane.setDividerLocation(250);
                }
            });
        }
        return leftTestCasePanel;
    }

    private JPanel getLeftTopPanel() {
        if (leftTopPanel == null) {

            executeTestLabel = new JLabel();
            executeTestLabel.setText("Choose a Test Case:");
            
            leftTopPanel = new JPanel();
            leftTopPanel.setLayout(new GridBagLayout());
     
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.weighty = 1.0;
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.weighty = 1.0;
            
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 2;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;

            
            leftTopPanel.add(executeTestLabel, gridBagConstraints1);
            leftTopPanel.add(getTestCaseComboBox(), gridBagConstraints2);
            leftTopPanel.add(getRunTestCaseButton(), gridBagConstraints3);
            leftTopPanel.add(getReloadTestCaseButton(), gridBagConstraints4);
        }
        return leftTopPanel;
    }

    private JPanel getLeftBottomPanel() {
        if (leftBottomPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.BOTH;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.weightx = 1.0;
            leftBottomPanel = new JPanel();
            leftBottomPanel.setLayout(new GridBagLayout());
            leftBottomPanel.add(getTestCaseScrollPane(), gridBagConstraints4);
        }
        return leftBottomPanel;
    }

    private JScrollPane getTestCaseScrollPane() {
        if (testCaseScrollPane == null) {
            testCaseScrollPane = new JScrollPane();
            testCaseScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0,
                    0, 0));
            testCaseScrollPane.setViewportView(getTestCaseHistoryTree());
        }
        return testCaseScrollPane;
    }

    private JButton getRunTestCaseButton() {
        if (runTestCaseButton == null) {
            runTestCaseButton = new JButton();
            runTestCaseButton.setText("Run");
            runTestCaseButton.setPreferredSize(new Dimension(80, 26));
            runTestCaseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    if(!testCaseExecutionHandler.isTestExecutionRunning()){
                        //start new testcase
                        runTestCaseRef((ITestCaseRef)testCaseRefComboBoxModel.getSelectedItem());
                        if(preferencesMenuItem != null){
                            preferencesMenuItem.setEnabled(false);
                        }
                    }
                    else{
                        //abort running test case                        
                        testCaseExecutionHandler.abortRunningTestCase();
                        if(preferencesMenuItem != null){
                            preferencesMenuItem.setEnabled(true);
                        }
                    }
                }
            });
        }
        return runTestCaseButton;
    }

    private void runTestCaseRef(ITestCaseRef testCaseRef){
        if(testCaseRef != null){
            ITestCase testCase;
            try {
                testCase = testCaseParser.getTestCase(testCaseRef.getTestCaseFile());
                runTestCase(testCase, config.getTargetIP(), config.getTargetPort());
            }
            catch (ParserException e) {
                showParserException("\"" + testCaseRef.getTestCaseFile().getName() + "\" " + e.getMessage());
            }
            catch (ValidatorException e) {
                showParserException("\"" + testCaseRef.getTestCaseFile().getName() + "\"" + " is not valid !");
            }
            catch ( IllegalArgumentException e){
                showIllegalArgumentException(e.getMessage());
            }
        }
    }

    void runTestCase(ITestCase testCase, InetAddress targetIP, int targetPort){
        if(testCase != null){
            ITestCaseRun testCaseRun = testCaseExecutionHandler.runTestCase(testCase, this,targetIP, targetPort);
    
            testCaseHistory.addTestCaseRun(testCaseRun);
            Thread.yield();
    
            //Select in tree
            TreePath selectionPath = new TreePath(new Object[]{testCaseHistory, testCaseRun});
            testCaseHistoryTree.setSelectionPath(selectionPath);
            testCaseHistoryTree.expandPath(selectionPath);
            runTestCaseButton.setText("Cancel");
        }
    }
    
    private void showIllegalArgumentException(String message){
        JOptionPane
        .showMessageDialog(
                this,
                "Test Case Specification fault:\n\n" +
                message +
                "\n\nPlease correct it and reload the Test Cases.",
                "XML Parser Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private JComboBox getTestCaseComboBox() {
        if (testCaseComboBox == null) {
            testCaseComboBox = new JComboBox();
            loadTestCaseComboBox();
        }
        return testCaseComboBox;
    }
    
    public void loadTestCaseComboBox(){     
        testCaseRefComboBoxModel = new TestCaseRefComboBoxModel(loadTestCaseRefs());
        testCaseComboBox.setModel(testCaseRefComboBoxModel);
    }

    private Vector<ITestCaseRef> loadTestCaseRefs() {
        Vector<ITestCaseRef> res;
        File testCaseDirectory = new File(config.getTestCaseDirPath());
        res = testCaseRefParser.getTestCaseRefs(testCaseDirectory);
        return res;
    }
    
    private void showParserException(String message){
        JOptionPane
        .showMessageDialog(
                this,
                "Test Case Specification Error:\n\n" +
                message +
                "\n\nPlease correct it and reload the Test Cases.",
                "XML Parser Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton getReloadTestCaseButton() {
        if (reloadTestCaseButton == null) {
            reloadTestCaseButton = new JButton();
            reloadTestCaseButton.setText("Reload");
            reloadTestCaseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    testCaseRefComboBoxModel = null;
                    loadTestCaseComboBox();                  
                    checkRefParserError();
                }
            });
        }
        return reloadTestCaseButton;
    }

    private JTree getTestCaseHistoryTree() {
        if (testCaseHistoryTree == null) {
            testCaseHistoryTree = new JTree();
            testCaseHistoryTree.setCellRenderer(new TestCaseHistoryTreeCellRenderer());
            testCaseHistoryTree.setShowsRootHandles(true);
            testCaseHistoryTree.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
            testCaseHistoryTree.setRootVisible(false);
            testCaseHistoryTreeModel = new TestCaseHistoryTreeModel(testCaseHistory);
            testCaseHistoryTree.setModel(testCaseHistoryTreeModel);
            testCaseHistoryTree.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseReleased( java.awt.event.MouseEvent e ) {
                    mayShowTestCaseHistoryTreePopupMenu(e);
                }

                public void mousePressed( java.awt.event.MouseEvent e ) {
                    mayShowTestCaseHistoryTreePopupMenu(e);
                }

            });
            testCaseHistoryTree
                    .addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                        public void valueChanged( javax.swing.event.TreeSelectionEvent e ) {
                            Object parentNode = null;
                            Object selectedNode = null;
                            if (testCaseHistoryTree.getSelectionPaths() != null) {
                                selectedNode = testCaseHistoryTree.getSelectionPath().getLastPathComponent();
                                int pathCount = testCaseHistoryTree.getSelectionPath().getPathCount();
                                parentNode = testCaseHistoryTree.getSelectionPath().getPathComponent(pathCount-2);
                            }
                            if (selectedNode != null) {
                                if (selectedNode instanceof ITestCaseRun) {
                                    testCaseRunSelected((ITestCaseRun) selectedNode);
                                }
                                if(selectedNode instanceof String){
                                    //Warning message is selected
                                    warningMessageSelected((ITestCaseRun) parentNode, (String) selectedNode);
                                    
                                }
                            }
                        }
                    });
        }
        return testCaseHistoryTree;
    }

    private void testCaseRunSelected( ITestCaseRun run ) {
        rightTestCasePanel.setTestCaseRun(run);
        verticalSplitPane.revalidate();
    }    
    private void warningMessageSelected(ITestCaseRun testCaseRun, String msgID){
        String request = testCaseRun.getWarningLog().getWarningRequest(msgID);
        String message = testCaseRun.getWarningLog().getWarningMessage(msgID);
        rightTestCasePanel.setMessagesContent(request, message);
        verticalSplitPane.revalidate();
    }

    public void testRunFinished() {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                runTestCaseButton.setText("Run");
                if (deleteSelectedMenuItem != null){
                    deleteSelectedMenuItem.setEnabled(true);
                }
                if(preferencesMenuItem != null){
                    preferencesMenuItem.setEnabled(true);
                }
            }
        });
        Thread.yield();
    }

}
