package ui.history;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ui.history.nodes.IHistoryTreeNode;

public class HistoryTreeCellRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);
        // Set Icon if defined
        Icon icon = ((IHistoryTreeNode) value).getIcon();
        if (icon != null) {
            setIcon(icon);
        }

        // Set closed Icon if defined
        Icon closedIcon = ((IHistoryTreeNode) value).getClosedIcon();
        if (closedIcon != null) {
            setClosedIcon(closedIcon);
        }

        // Set open Icon if defined
        Icon openIcon = ((IHistoryTreeNode) value).getOpenIcon();
        if (openIcon != null) {
            setOpenIcon(openIcon);
        }

        setForeground(((IHistoryTreeNode) value).getTextColor());
        setText(((IHistoryTreeNode) value).getTitle());

        return this;
    }
}
