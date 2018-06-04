package ui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import testexec.history.IMessagesLog;
import util.MiscUtil;
import util.SimpleLogger;
import javax.swing.JMenuItem;

public class DebugLogFrame extends JFrame {

    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JScrollPane logScrollPane = null;
    private JTextArea logTextArea = null;
    private JMenuItem updateMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JMenuItem clearMenuItem = null;
    private JMenu windowMenu = null;
    private JMenuItem exitMenuItem = null;
    private String windowTitle = "Debug Log";
    private String content = null;  //  @jve:decl-index=0:
    private File proxyLogFile = null;
    private IMessagesLog log = null;

    public DebugLogFrame(File file, String windowTitle) {
        // Proxy Debug Log
        super();
        this.proxyLogFile = file;
        this.windowTitle = windowTitle;
        try {
            content = readContent(file);
        } catch (IllegalArgumentException e){
            JOptionPane
            .showMessageDialog(
                    this,
                    e.getMessage(),
                    "Cannot open file", JOptionPane.ERROR_MESSAGE);
        }       
        initialize();
        updateProxyTextArea();
    }
    
    public DebugLogFrame(IMessagesLog log, String windowTitle) {
        // Test Case Debug Log
        super();
        this.log = log;
        this.windowTitle = windowTitle;
        initialize();
        updateTestCaseTextArea();
    }

    private void initialize() {
        this.setSize(800, 600);
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setLocation(new java.awt.Point(100, 100));
        this.setJMenuBar(getJJMenuBar());
        this.setName("debugLogFrame");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
        this.setTitle(windowTitle);
    }
    
    private String readContent(File file){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int fileSize = fis.available();
            byte logByte[] = new byte[fileSize];
            fis.read(logByte);
            return new String(logByte);
        }
        catch (FileNotFoundException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }catch (OutOfMemoryError e){
            throw new IllegalArgumentException(
                    "The Debug Log file is too large to open !\n" +
                    "Please use an external editor.");
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }
        return null;
    }

    private void updateProxyTextArea() {
        
        content = readContent(proxyLogFile);
        
        if (content != null){
            logTextArea.setText(content);
            logTextArea.setCaretPosition(0);
        } else {
            logTextArea.setText("");
        }
  
    }
    
    private void updateTestCaseTextArea() {
        if (log != null){
            String content = log.getContent();
            if (content != null)
                logTextArea.setText(content);
            else 
                logTextArea.setText("");
            logTextArea.setCaretPosition(0);
        } else {
            logTextArea.setText("");
        }
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(0, 0, 0, 0));
            jContentPane.add(getLogScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getWindowMenu());
        }
        return jJMenuBar;
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getSaveMenuItem());
            fileMenu.setMnemonic('F');
            fileMenu.addSeparator();
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    private JScrollPane getLogScrollPane() {
        if (logScrollPane == null) {
            logScrollPane = new JScrollPane();
            logScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0,
                    0));
            logScrollPane.setViewportView(getLogTextArea());
        }
        return logScrollPane;
    }

    private JTextArea getLogTextArea() {
        if (logTextArea == null) {
            logTextArea = new JTextArea();
            logTextArea
                    .setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 2));
            logTextArea.setEditable(false);
        }
        return logTextArea;
    }

    private JMenuItem getUpdateMenuItem() {
        if (updateMenuItem == null) {
            updateMenuItem = new JMenuItem();
            updateMenuItem.setText("Update");
            updateMenuItem.setMnemonic('U');
            updateMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    if (proxyLogFile != null){
                        updateProxyTextArea();
                    } else {
                        updateTestCaseTextArea();
                    }
                    
                }
            });
        }
        return updateMenuItem;
    }

    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText("Save as...");
            saveMenuItem.setMnemonic('S');
            saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    saveLogfile();
                }
            });
        }
        return saveMenuItem;
    }

    private void saveLogfile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (log != null){
                MiscUtil.saveTestCaseContent(fc.getSelectedFile(), log.getContent());
            } else {
                MiscUtil.saveFile(fc.getSelectedFile(), SimpleLogger.DEBUG_LOG_PROXY);
            }
        }
    }

    private JMenuItem getClearMenuItem() {
        if (clearMenuItem == null) {
            clearMenuItem = new JMenuItem();
            clearMenuItem.setText("Clear Log");
            clearMenuItem.setMnemonic('C');
            clearMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    if (MiscUtil.deleteDebugLog(proxyLogFile)){
                        updateProxyTextArea();
                    }
                }
            });
        }
        return clearMenuItem;
    }

    private JMenu getWindowMenu() {
        if (windowMenu == null) {
            windowMenu = new JMenu();
            windowMenu.setText("Window");
            windowMenu.setMnemonic('W');
            windowMenu.add(getUpdateMenuItem());
            if (proxyLogFile != null){
                windowMenu.add(getClearMenuItem());
            }
        }
        return windowMenu;
    }

    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.setMnemonic('x');
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    dispose();
                }
            });
        }
        return exitMenuItem;
    }

}
