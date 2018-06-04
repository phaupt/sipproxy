package ui.history.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import proxy.UDPDatagram;
import util.SimpleLogger;

public abstract class HistoryMessageTreeNode implements IHistoryTreeNode {

    UDPDatagram packet;

    public HistoryMessageTreeNode(UDPDatagram packet) {
        this.packet = packet;
    }

    public String getTitle() {
        String commandHeader = null;

        if (packet != null) {
            String sipMessage = new String(packet.getData());
            BufferedReader reader = new BufferedReader(new StringReader(sipMessage));
            try {
                commandHeader = reader.readLine();
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                commandHeader = null;
            }
        }

        return commandHeader;
    }

    public boolean isLeaf() {
        return true;
    }

    public int getChildCount() {
        return 0;
    }

    public int getIndexOfChild( IHistoryTreeNode node ) {
        return -1;
    }

    public IHistoryTreeNode getChild( int index ) {
        return null;
    }

    public UDPDatagram getUDPDatagram() {
        return packet;
    }

}
