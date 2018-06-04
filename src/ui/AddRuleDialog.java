package ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import config.transformation.DynamicRegexRule;
import config.transformation.RegexRule;
import javax.swing.JCheckBox;

import util.MiscUtil;
import java.awt.FlowLayout;

public class AddRuleDialog extends JDialog {

    // @jve:decl-index=0:

    public static final String ADD_BUTTON_TEXT = "Add this Rule";
    public static final String MODIFY_BUTTON_TEXT = "Apply Changes";

    private JPanel jContentPane = null;
    private JPanel inputPanel = null;
    private JTextField regExpTextField = null;
    private JLabel regExpLabel = null;
    private JLabel replacementLabel = null;
    private JTextField replacementTextField = null;
    private JComboBox directionComboBox = null;
    private JLabel validationLabel = null;
    private JTextField validationTextField = null;
    private JButton validateButton = null;
    private JButton addButton = null;
    private JLabel directionLabel = null;
    private JTextField outputTextField = null;
    private JLabel outputLabel = null;
    private RegexRulesTableModel regexRulesTableModel = null;
    private JLabel isActiveLabel = null;
    private JCheckBox isActiveCheckBox = null;
    private MiscUtil util = null;
    private int row;
    private JPanel buttonPanel = null;
    private JButton cancelButton = null;
    private JPanel rulePanel = null;

    public AddRuleDialog(RegexRulesTableModel tmodel) {
        this.regexRulesTableModel = tmodel;
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setName("addRuleDialog");
        this.setTitle("Add a New Rule...");
        this.setLocation(new java.awt.Point(100, 100));
        this.setResizable(false);
        this.setModal(false);
        this.setContentPane(getJContentPane());
        util = new MiscUtil();
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
            jContentPane.add(getRulePanel(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getInputPanel(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    private JPanel getInputPanel() {
        if (inputPanel == null) {

            outputLabel = new JLabel();
            outputLabel.setText("Test Output String:");
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints41.weightx = 1.0;
            validationLabel = new JLabel();
            validationLabel.setText("Test Input String:");
            inputPanel = new JPanel();

            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 4;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.weighty = 1.0;

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 4;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.weighty = 1.0;

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 5;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.weighty = 1.0;

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.gridy = 5;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.weighty = 1.0;

            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            // gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints13.gridx = 1;
            gridBagConstraints13.gridy = 6;
            gridBagConstraints13.anchor = GridBagConstraints.LINE_START;
            // gridBagConstraints13.weightx = 1.0;
            // gridBagConstraints13.weighty = 1.0;

            inputPanel.setLayout(new GridBagLayout());
            inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    "Validate Rule Settings",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

            inputPanel.add(validationLabel, gridBagConstraints9);
            inputPanel.add(getValidationTextField(), gridBagConstraints10);

            inputPanel.add(outputLabel, gridBagConstraints11);
            inputPanel.add(getOutputTextField(), gridBagConstraints12);

            inputPanel.add(getValidateButton(), gridBagConstraints13);
        }
        return inputPanel;
    }

    private JTextField getRegExpTextField() {
        if (regExpTextField == null) {
            regExpTextField = new JTextField();
            regExpTextField.setPreferredSize(new Dimension(500, 20));
        }
        return regExpTextField;
    }

    private JTextField getReplacementTextField() {
        if (replacementTextField == null) {
            replacementTextField = new JTextField();
            replacementTextField.setPreferredSize(new Dimension(500, 20));
        }
        return replacementTextField;
    }

    private JComboBox getDirectionComboBox() {
        if (directionComboBox == null) {
            String[] directions = { RegexRulesTableModel.PBX2CLIENT_DIRECTION,
                    RegexRulesTableModel.CLIENT2PBX_DIRECTION };
            directionComboBox = new JComboBox(directions);
        }
        return directionComboBox;
    }

    private JTextField getValidationTextField() {
        if (validationTextField == null) {
            validationTextField = new JTextField();
            validationTextField.setPreferredSize(new Dimension(500, 20));
        }
        return validationTextField;
    }

    private JButton getValidateButton() {
        if (validateButton == null) {
            validateButton = new JButton();
            validateButton.setText("Validate");
            validateButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    if (validationTextField.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null,
                                "You have to define a Test Input String !",
                                "Validation Error", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        outputTextField.setText("");
                        outputTextField.setText(util.validateRegeExp(new RegexRule(
                                regExpTextField.getText(),
                                replacementTextField.getText(), isActiveCheckBox
                                        .isSelected()), validationTextField.getText()));
                    }
                }
            });
        }
        return validateButton;
    }

    private JButton getAddButton() {
        if (addButton == null) {
            addButton = new JButton();
            addButton.setText(ADD_BUTTON_TEXT);
            addButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    addButtonPressed();
                    close();
                }
            });
        }
        return addButton;
    }

    public void addButtonPressed() {
        if (addButton.getText().equals(MODIFY_BUTTON_TEXT)) {
            regexRulesTableModel.deleteRow(row);
        }
        regexRulesTableModel.addRow(new DynamicRegexRule(
                new RegexRule(regExpTextField.getText(), replacementTextField.getText(),
                        isActiveCheckBox.isSelected()), (String) directionComboBox
                        .getSelectedItem()));
    }

    private JTextField getOutputTextField() {
        if (outputTextField == null) {
            outputTextField = new JTextField();
            outputTextField.setPreferredSize(new Dimension(500, 20));
            outputTextField.setEditable(false);
        }
        return outputTextField;
    }

    private void close() {
        this.dispose();
    }

    private JCheckBox getIsActiveCheckBox() {
        if (isActiveCheckBox == null) {
            isActiveCheckBox = new JCheckBox();
            isActiveCheckBox.setSelected(true);
        }
        return isActiveCheckBox;
    }

    public void setInputFields( int row, boolean isActiveCheckBox,
            String directionComboBox, String regExpTextField, String replacementTextField ) {
        this.row = row;
        this.setName("modifyRuleDialog");
        this.setTitle("Edit Rule...");
        this.isActiveCheckBox.setSelected(isActiveCheckBox);
        if (directionComboBox.equals(RegexRulesTableModel.PBX2CLIENT_DIRECTION)) {
            this.directionComboBox.setSelectedIndex(0);
        }
        else if (directionComboBox.equals(RegexRulesTableModel.CLIENT2PBX_DIRECTION)) {
            this.directionComboBox.setSelectedIndex(1);
        }
        this.regExpTextField.setText(regExpTextField);
        this.replacementTextField.setText(replacementTextField);
        this.addButton.setText(MODIFY_BUTTON_TEXT);
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(flowLayout);
            buttonPanel.add(getAddButton(), null);
            buttonPanel.add(getCancelButton(), null);
        }
        return buttonPanel;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed( java.awt.event.ActionEvent e ) {
                    close();
                }
            });
        }
        return cancelButton;
    }

    private JPanel getRulePanel() {
        if (rulePanel == null) {
            rulePanel = new JPanel();
            rulePanel.setLayout(new GridBagLayout());
            rulePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    "Rule Settings",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            isActiveLabel = new JLabel();
            isActiveLabel.setText("Activate Rule");

            directionLabel = new JLabel();
            directionLabel.setText("Choose Direction:");

            replacementLabel = new JLabel();
            replacementLabel.setText("Replacement:");
            regExpLabel = new JLabel();
            regExpLabel.setText("Regular Expression:");

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.weighty = 1.0;

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;

            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.weighty = 1.0;

            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridy = 3;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.weighty = 1.0;

            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 3;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.weighty = 1.0;

            rulePanel.add(isActiveLabel, gridBagConstraints1);
            rulePanel.add(getIsActiveCheckBox(), gridBagConstraints2);

            rulePanel.add(directionLabel, gridBagConstraints3);
            rulePanel.add(getDirectionComboBox(), gridBagConstraints4);

            rulePanel.add(regExpLabel, gridBagConstraints5);
            rulePanel.add(getRegExpTextField(), gridBagConstraints6);

            rulePanel.add(replacementLabel, gridBagConstraints7);
            rulePanel.add(getReplacementTextField(), gridBagConstraints8);

        }
        return rulePanel;
    }

}
