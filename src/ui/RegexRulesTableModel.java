package ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import config.transformation.DynamicRegexRule;
import config.transformation.RegexRule;
import config.transformation.TransformationConfig;

public class RegexRulesTableModel extends AbstractTableModel {

    public static final String PBX2CLIENT_DIRECTION = "PBX to CLIENT";
    public static final String CLIENT2PBX_DIRECTION = "CLIENT to PBX";

    private final String[] COLUMN_NAMES = { "Active", "Direction", "Regular Expression",
            "Replacement", "Edit", "Delete" };
    public static final int ACTIVE_COLUMN = 0;
    public static final int DIRECTION_COLUMN = 1;
    public static final int REGEXP_COLUMN = 2;
    public static final int REPLACEMENT_COLUMN = 3;
    public static final int EDIT_COLUMN = 4;
    public static final int DELETE_COLUMN = 5;

    private TransformationConfig dynamicTransformationConfig = null;
    private Vector<DynamicRegexRule> rules = new Vector<DynamicRegexRule>();

    public RegexRulesTableModel(TransformationConfig dynamicTransformationConfig) {
        this.dynamicTransformationConfig = dynamicTransformationConfig;
        updateTable();
    }

    private void updateTable() {
        rules.clear();

        for (RegexRule rule : dynamicTransformationConfig.getClient2PbxRules()) {
            rules.add(new DynamicRegexRule(rule, CLIENT2PBX_DIRECTION));
        }

        for (RegexRule rule : dynamicTransformationConfig.getPbx2ClientRules()) {
            rules.add(new DynamicRegexRule(rule, PBX2CLIENT_DIRECTION));
        }

        Collections.sort(rules, new Comparator() {

            public int compare( Object o1, Object o2 ) {
                String s1 = ((DynamicRegexRule) o1).getDirection();
                String s2 = ((DynamicRegexRule) o2).getDirection();
                int res = s1.compareTo(s2);
                if (res == 0) {
                    s1 = ((DynamicRegexRule) o1).getRule().getRegex();
                    s2 = ((DynamicRegexRule) o2).getRule().getRegex();
                    res = s1.compareTo(s2);
                    if (res == 0) {
                        s1 = ((DynamicRegexRule) o1).getRule().getReplacement();
                        s2 = ((DynamicRegexRule) o2).getRule().getReplacement();
                        return s1.compareTo(s2);
                    }
                }
                return res;
            }
        });

        fireTableDataChanged();
    }

    public void deleteRow( int row ) {
        RegexRule rule = rules.get(row).getRule();
        if (rules.get(row).getDirection().equals(CLIENT2PBX_DIRECTION)) {
            int pos = dynamicTransformationConfig.getClient2PbxRules().indexOf(rule);
            dynamicTransformationConfig.getClient2PbxRules().remove(pos);
        }
        else if (rules.get(row).getDirection().equals(PBX2CLIENT_DIRECTION)) {
            int pos = dynamicTransformationConfig.getPbx2ClientRules().indexOf(rule);
            dynamicTransformationConfig.getPbx2ClientRules().remove(pos);
        }
        updateTable();
    }

    public void addRow( DynamicRegexRule dynRule ) {
        if (dynRule.getDirection().equals(CLIENT2PBX_DIRECTION)) {
            dynamicTransformationConfig.getClient2PbxRules().add(dynRule.getRule());
        }
        else if (dynRule.getDirection().equals(PBX2CLIENT_DIRECTION)) {
            dynamicTransformationConfig.getPbx2ClientRules().add(dynRule.getRule());
        }
        updateTable();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getRowCount() {
        if (dynamicTransformationConfig != null) {
            return rules.size();
        }
        else {
            return 0;
        }
    }

    public String getColumnName( int col ) {
        return COLUMN_NAMES[col];
    }

    public Object getValueAt( int row, int col ) {
        if (col == ACTIVE_COLUMN) {
            // SET ACTIVE
            return ((DynamicRegexRule) rules.get(row)).getRule().isActive();
        }
        else if (col == DIRECTION_COLUMN) {
            // DIRECTION
            return ((DynamicRegexRule) rules.get(row)).getDirection();
        }
        else if (col == REGEXP_COLUMN) {
            // REGEXP
            return ((DynamicRegexRule) rules.get(row)).getRule().getRegex();
        }
        else if (col == REPLACEMENT_COLUMN) {
            // REPLACEMENT
            return ((DynamicRegexRule) rules.get(row)).getRule().getReplacement();
        }
        else if (col == EDIT_COLUMN) {
            // Edit
            return new ImageIcon(this.getClass().getResource("img/edit.gif"));
        }
        else if (col == DELETE_COLUMN) {
            // Delete
            return new ImageIcon(this.getClass().getResource("img/delete.gif"));
        }
        else {
            return null;
        }
    }

    public Class getColumnClass( int c ) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable( int row, int col ) {
        if (col == ACTIVE_COLUMN || col == DIRECTION_COLUMN) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setValueAt( Object value, int row, int col ) {
        RegexRule rule = rules.get(row).getRule();
        if (rules.get(row).getDirection().equals(CLIENT2PBX_DIRECTION)) {
            int index = dynamicTransformationConfig.getClient2PbxRules().indexOf(rule);
            if (col == ACTIVE_COLUMN) {
                // CHANGED SET ACTIVE
                dynamicTransformationConfig.getClient2PbxRules().get(index).setActive(
                        (Boolean) value);
            }
            else if (col == DIRECTION_COLUMN) {
                // CHANGED DIRECTION
                if (((String) value).equals(PBX2CLIENT_DIRECTION)) {
                    dynamicTransformationConfig.getPbx2ClientRules().add(
                            dynamicTransformationConfig.getClient2PbxRules()
                                    .remove(index));
                }
            }
            else if (col == REGEXP_COLUMN) {
                // CHANGED REGEX
                dynamicTransformationConfig.getClient2PbxRules().get(index).setRegex(
                        (String) value);
            }
            else if (col == REPLACEMENT_COLUMN) {
                // CHANGED REPLACEMENT
                dynamicTransformationConfig.getClient2PbxRules().get(index)
                        .setReplacement((String) value);
            }
        }
        else if (rules.get(row).getDirection().equals(PBX2CLIENT_DIRECTION)) {
            int index = dynamicTransformationConfig.getPbx2ClientRules().indexOf(rule);
            if (col == ACTIVE_COLUMN) {
                // CHANGED SET ACTIVE
                dynamicTransformationConfig.getPbx2ClientRules().get(index).setActive(
                        (Boolean) value);
            }
            else if (col == DIRECTION_COLUMN) {
                // CHANGED DIRECTION
                if (((String) value).equals(CLIENT2PBX_DIRECTION)) {
                    dynamicTransformationConfig.getClient2PbxRules().add(
                            dynamicTransformationConfig.getPbx2ClientRules()
                                    .remove(index));
                }
            }
            else if (col == REGEXP_COLUMN) {
                // CHANGED REGEX
                dynamicTransformationConfig.getPbx2ClientRules().get(index).setRegex(
                        (String) value);
            }
            else if (col == REPLACEMENT_COLUMN) {
                // CHANGED REPLACEMENT
                dynamicTransformationConfig.getPbx2ClientRules().get(index)
                        .setReplacement((String) value);
            }
        }
        updateTable();
    }

}
