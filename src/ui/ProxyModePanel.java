package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import config.transformation.TransformationConfig;



public class ProxyModePanel extends JPanel {

    private JSplitPane horizontalSplitPane = null;
    private JPanel topPanel = null;
    private JScrollPane sipMessageScrollPane = null;
    private JTextArea sipMessageTextArea = null;
    private JPanel bottomPanel = null;
    private JPanel listRegexRulesPanel = null;
    private JButton addRuleButton = null;
    private JScrollPane listRegexRulesScrollPane = null;
    private JTable listRegexRulesTable = null;
    private RegexRulesTableModel regexRulesTableModel = null;
    private TransformationConfig dynamicTransformationConfig;

    public ProxyModePanel(){
        super();
        initialize();
    }

    
    public ProxyModePanel(TransformationConfig dynamicTransformationConfig){
        super();
        this.dynamicTransformationConfig = dynamicTransformationConfig;
        initialize();
    }
    
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.BOTH;
        gridBagConstraints6.weighty = 1.0;
        gridBagConstraints6.weightx = 1.0;
        this.setLayout(new GridBagLayout());
        this.add(getHorizontalSplitPane(), gridBagConstraints6);
    }

    private JSplitPane getHorizontalSplitPane() {
        if (horizontalSplitPane == null) {
            horizontalSplitPane = new JSplitPane();
            horizontalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            horizontalSplitPane.setBottomComponent(getBottomPanel());
            horizontalSplitPane.setTopComponent(getTopPanel());
            horizontalSplitPane.setDividerLocation(400);
            
        }
        return horizontalSplitPane;
    }

    private JPanel getTopPanel() {
        if (topPanel == null) {
            topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout());
            topPanel.setPreferredSize(new Dimension(123, 400));
            topPanel.add(getSipMessageScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return topPanel;
    }

    private JScrollPane getSipMessageScrollPane() {
        if (sipMessageScrollPane == null) {
            sipMessageScrollPane = new JScrollPane();
            sipMessageScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            sipMessageScrollPane.setViewportView(getSipMessageTextArea());
        }
        return sipMessageScrollPane;
    }

    private JTextArea getSipMessageTextArea() {
        if (sipMessageTextArea == null) {
            sipMessageTextArea = new JTextArea();
            sipMessageTextArea.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            sipMessageTextArea.setEditable(false);
        }
        return sipMessageTextArea;
    }

    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.add(getListRegexRulesPanel(), BorderLayout.CENTER);            
        }
        return bottomPanel;
    }

    private JPanel getListRegexRulesPanel() {
        if (listRegexRulesPanel == null) {
            listRegexRulesPanel = new JPanel();
            listRegexRulesPanel.setLayout(new BorderLayout());
            listRegexRulesPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,
                    0, 0, 0));
            listRegexRulesPanel.add(getAddRuleButton(), BorderLayout.NORTH);
            listRegexRulesPanel.add(getListRegexRulesScrollPane(), BorderLayout.CENTER);
        }
        return listRegexRulesPanel;
    }

    private JButton getAddRuleButton() {
        if (addRuleButton == null) {
            addRuleButton = new JButton();
            addRuleButton.setText("Add Rule");
            addRuleButton.setName("addRuleButton");
            addRuleButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    showAddRuleDialog();
                }
            });
        }
        return addRuleButton;
    }
    private void showAddRuleDialog() {
        AddRuleDialog addRuleDialog = new AddRuleDialog(regexRulesTableModel);
        addRuleDialog.pack();
        addRuleDialog.setVisible(true);
    }

    private JScrollPane getListRegexRulesScrollPane() {
        if (listRegexRulesScrollPane == null) {
            listRegexRulesScrollPane = new JScrollPane();
            listRegexRulesScrollPane.setName("jScrollPane");
            listRegexRulesScrollPane
                    .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            listRegexRulesScrollPane
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            listRegexRulesScrollPane.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(0, 0, 0, 0));
            listRegexRulesScrollPane.setViewportView(getListRegexRulesTable());
        }
        return listRegexRulesScrollPane;
    }
    private JTable getListRegexRulesTable() {
        if (listRegexRulesTable == null) {
            if (regexRulesTableModel == null) {
                regexRulesTableModel = new RegexRulesTableModel(
                        dynamicTransformationConfig);
            }
            listRegexRulesTable = new JTable(regexRulesTableModel);

            listRegexRulesTable
                    .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            listRegexRulesTable.setRowHeight(25);
            listRegexRulesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            for (int i = 0; i < 5; i++) {
                TableColumn column = listRegexRulesTable.getColumnModel().getColumn(i);
                if (i == RegexRulesTableModel.ACTIVE_COLUMN) {
                    column.setPreferredWidth(60);
                }
                if (i == RegexRulesTableModel.DIRECTION_COLUMN) {
                    column.setPreferredWidth(100);
                }
                if (i == RegexRulesTableModel.REGEXP_COLUMN) {
                    column.setPreferredWidth(400);
                    column.setWidth(400);
                }
                if (i == RegexRulesTableModel.REPLACEMENT_COLUMN) {
                    column.setPreferredWidth(200);
                }
                if (i == RegexRulesTableModel.EDIT_COLUMN) {
                    column.setPreferredWidth(60);
                }
                if (i == RegexRulesTableModel.DELETE_COLUMN) {
                    column.setPreferredWidth(60);
                }
            }
            listRegexRulesTable.setRowSelectionAllowed(false);
            setUpDirectionColumn(listRegexRulesTable, listRegexRulesTable
                    .getColumnModel().getColumn(1));
            listRegexRulesTable.addMouseListener(new MouseAdapter() {

                public void mouseClicked( MouseEvent event ) {
                    int row = listRegexRulesTable.getSelectedRow();
                    if (listRegexRulesTable.getSelectedColumn() == RegexRulesTableModel.EDIT_COLUMN) {
                        AddRuleDialog addRuleDialog = new AddRuleDialog(
                                regexRulesTableModel);
                        addRuleDialog.setInputFields(row, (Boolean) listRegexRulesTable
                                .getValueAt(row, RegexRulesTableModel.ACTIVE_COLUMN),
                                (String) listRegexRulesTable.getValueAt(row,
                                        RegexRulesTableModel.DIRECTION_COLUMN),
                                (String) listRegexRulesTable.getValueAt(row,
                                        RegexRulesTableModel.REGEXP_COLUMN),
                                (String) listRegexRulesTable.getValueAt(row,
                                        RegexRulesTableModel.REPLACEMENT_COLUMN));
                        addRuleDialog.pack();
                        addRuleDialog.setVisible(true);
                    }
                    else if (listRegexRulesTable.getSelectedColumn() == RegexRulesTableModel.DELETE_COLUMN) {
                        int selectedOption = JOptionPane.showConfirmDialog(null,
                                "Delete this Rule ? ", "Delete Rule",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (selectedOption == JOptionPane.YES_OPTION) {
                            regexRulesTableModel.deleteRow(row);
                        }
                    }
                }

            });
        }
        return listRegexRulesTable;
    }
    public void setUpDirectionColumn( JTable table, TableColumn directionColumn ) {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem(RegexRulesTableModel.CLIENT2PBX_DIRECTION);
        comboBox.addItem(RegexRulesTableModel.PBX2CLIENT_DIRECTION);
        directionColumn.setCellEditor(new DefaultCellEditor(comboBox));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Choose Direction");
        directionColumn.setCellRenderer(renderer);
    }
    
    public void clearMsgTextArea(){
        sipMessageTextArea.setText("");
    }
    
    public void updateMsgTextArea(String msg){
        sipMessageTextArea.setText(msg);
        sipMessageTextArea.setCaretPosition(0);
    }

}
