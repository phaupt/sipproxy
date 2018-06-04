package ui.history;

import ui.history.nodes.IHistoryTreeNode;

public class HistoryTreeChange {

    public static final int ADDED = 0;
    public static final int REMOVED = 1;

    int type;
    IHistoryTreeNode node;
    int index;
    IHistoryTreeNode parentNode;

    // Class which is used to inform Observers a change in the SIP-History
    public HistoryTreeChange(int type, IHistoryTreeNode node, int index,
            IHistoryTreeNode parentNode) {
        this.type = type;
        this.node = node;
        this.index = index;
        this.parentNode = parentNode;
    }

    public int getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public IHistoryTreeNode getNode() {
        return node;
    }

    public IHistoryTreeNode getParentNode() {
        return parentNode;
    }

}
