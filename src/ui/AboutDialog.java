package ui;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AboutDialog extends JDialog implements HyperlinkListener{

    private final String SOFTWARE_VERSION = "Release Version 2.1 beta";
    private final String TITLE = "SIP Proxy - VoIP Security Test Tool";
    private final String CONTENT = SOFTWARE_VERSION
            + "\nUniversity of Applied Sciences Rapperswil"
            + "\nIn cooperation with Compass Security AG"
            + "\n\nDeveloper: Philipp Haupt, Matthias Hürlimann";
    private final String URL = "http://sourceforge.net/projects/sipproxy";

    private JPanel jContentPane = null;
    private JPanel logoPanel = null;
    private JPanel infoPanel = null;
    private JLabel logoLabel = null;
    private JTextArea infoTextArea = null;
    private JTextArea titleTextArea = null;
    private JEditorPane jEditorPane = null;

    public AboutDialog() {
        super();
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("About SIP Proxy");
        this.setLocation(new java.awt.Point(100, 100));
        this.setResizable(false);
        this.setModal(true);
        this.setContentPane(getJContentPane());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getLogoPanel(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getInfoPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JPanel getLogoPanel() {
        if (logoPanel == null) {
            logoLabel = new JLabel();
            logoLabel.setIcon(new ImageIcon(getClass().getResource(
                    "/ui/img/aboutLogo.gif")));
            logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
            logoLabel.setHorizontalTextPosition(SwingConstants.LEFT);
            logoPanel = new JPanel();
            logoPanel.setName("logoPanel");
            logoPanel.setLayout(new BorderLayout());
            logoPanel.add(logoLabel, BorderLayout.NORTH);
        }
        return logoPanel;
    }

    private JPanel getInfoPanel() {
        if (infoPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.gridx = 0;
            infoPanel = new JPanel();
            infoPanel.setName("infoPanel");
            infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            infoPanel.setLayout(new GridBagLayout());
            infoPanel.setBackground(java.awt.SystemColor.control);
            infoPanel.add(getInfoTextArea(), gridBagConstraints);
            infoPanel.add(getTitleTextArea(), gridBagConstraints4);
            infoPanel.add(getJEditorPane(), gridBagConstraints5);
        }
        return infoPanel;
    }

    private JEditorPane getJEditorPane() {
        if (jEditorPane == null) {
            jEditorPane = new JEditorPane();
            jEditorPane.setContentType("text/html");
            jEditorPane.setEditable(false);
            jEditorPane.setBackground(null);
            jEditorPane.addHyperlinkListener(this);
            jEditorPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
            jEditorPane.setText("<a href=\"" + URL + "\" style=\"text-decoration: none; font-weight:700\"><font face=\"Verdana\" size=\"2\" color=\"#000080\">" + URL + "</a></font>");
        }
        return jEditorPane;
    }
    
    private JTextArea getInfoTextArea() {
        if (infoTextArea == null) {
            infoTextArea = new JTextArea();
            infoTextArea.setText(CONTENT);
            infoTextArea.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 10));
            infoTextArea.setBackground(null);
            infoTextArea.setEditable(false);
        }
        return infoTextArea;
    }

    private JTextArea getTitleTextArea() {
        if (titleTextArea == null) {
            titleTextArea = new JTextArea();
            titleTextArea.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 14));
            titleTextArea.setText(TITLE);
            titleTextArea.setBackground(null);
            titleTextArea.setEditable(false);
        }
        return titleTextArea;
    }

    public void hyperlinkUpdate( HyperlinkEvent e ) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                new ProcessBuilder(new String[] { "cmd", "/c", "start",
                        e.getURL().toString() }).start();
            }
            catch (IOException e1) {
            }
        }       
    }

}
