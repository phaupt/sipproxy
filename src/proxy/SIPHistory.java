package proxy;

import config.Configuration;
import ui.history.nodes.HistoryClient2PbxMessageTreeNode;
import ui.history.nodes.HistoryCompositeTreeNode;
import ui.history.nodes.HistoryPbx2ClientMessageTreeNode;
import ui.history.nodes.IHistoryTreeNode;
import util.MiscUtil;

public class SIPHistory {

    public static final int PBX2CLIENT = 0;
    public static final int CLIENT2PBX = 1;

    private Configuration config;

    private HistoryCompositeTreeNode timeOrderedRoot; // Root-node for "Ordered by Time
                                                        // Modus"
    private HistoryCompositeTreeNode groupedByCSeqRoot; // Root-node for "Grouped by CSeq
                                                        // Modus"

    public SIPHistory(Configuration config) {
        this.config = config;

        // Create root nodes
        timeOrderedRoot = new HistoryCompositeTreeNode("Root- Ordered by time");
        groupedByCSeqRoot = new HistoryCompositeTreeNode("Root - Grouped by CSeq");
    }

    public void addSIPMessage( UDPDatagram sipMessage ) {
        // Add new SIP-Message to history

        String cSeq = MiscUtil.getCSeq(sipMessage);

        // Get Composite-Node for CSeq of Message (or null if node group-node with CSeq
        // doesn't exist)
        HistoryCompositeTreeNode cSeqNode = (HistoryCompositeTreeNode) groupedByCSeqRoot
                .getChild(cSeq);

        if (cSeqNode == null) {
            // Composite-Node doesn't exist --> Create and add new node to Root-Node
            cSeqNode = new HistoryCompositeTreeNode(cSeq);
            groupedByCSeqRoot.addChildNode(cSeqNode);

        }

        IHistoryTreeNode newNode;
        // Create Node for new Message for specific direction
        if (getDirection(sipMessage) == PBX2CLIENT) {
            newNode = new HistoryPbx2ClientMessageTreeNode(sipMessage);
        }
        else {
            newNode = new HistoryClient2PbxMessageTreeNode(sipMessage);
        }

        // Add node to root-Node
        timeOrderedRoot.addChildNode(newNode);

        // Add node to corresponding CSeq-Group-Node
        cSeqNode.addChildNode(newNode);
    }

    private int getDirection( UDPDatagram sipMessage ) {
        // Returns the direction of the Message
        if (sipMessage.getDstIP().equals(config.getClientIP())) {
            return PBX2CLIENT;
        }
        else if (sipMessage.getDstIP().equals(config.getPbxIP())) {
            return CLIENT2PBX;
        }
        else {
            return -1;
        }
    }

    public HistoryCompositeTreeNode getOrderedByTimeRoot() {
        return timeOrderedRoot;
    }

    public HistoryCompositeTreeNode getGroupedByCSeqRoot() {
        return groupedByCSeqRoot;
    }

    public void setConfig( Configuration config ) {
        this.config = config;
    }

    public void clearHistory() {
        // Removes all Child-Nodes from Root-Nodes

        while (timeOrderedRoot.getChildCount() > 0) {
            timeOrderedRoot.removeChild(0);
        }

        while (groupedByCSeqRoot.getChildCount() > 0) {
            groupedByCSeqRoot.removeChild(0);
        }
    }
}
