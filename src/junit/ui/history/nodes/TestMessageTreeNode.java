package junit.ui.history.nodes;

import java.awt.Color;

import proxy.UDPDatagram;
import ui.history.nodes.HistoryClient2PbxMessageTreeNode;
import ui.history.nodes.HistoryMessageTreeNode;
import ui.history.nodes.HistoryPbx2ClientMessageTreeNode;
import junit.framework.TestCase;

public class TestMessageTreeNode extends TestCase {

    private HistoryMessageTreeNode messageNodeClient2Pbx;
    private HistoryMessageTreeNode messageNodePbx2Client;
    private String commandZeile1 = "REGISTER sip:192.168.0.18 SIP/2.0";
    private String commandZeile2 = "SIP/2.0 100 Trying";
    private String sipHeader1 = commandZeile1
            + "\r\nVia: SIP/2.0/UDP 192.168.0.6:2051;branch=z9hG4bK-tkbbxz99x27h;rport\n\r";
    private String sipHeader2 = commandZeile2
            + "\r\nVia: SIP/2.0/UDP 192.168.0.18:5060;branch=z9hG4bK-tkbbxz99x27h;rport;received=192.168.0.18\n\r";
    private UDPDatagram packet1 = new UDPDatagram(null, -1, null, -1, sipHeader1
            .getBytes());
    private UDPDatagram packet2 = new UDPDatagram(null, -1, null, -1, sipHeader2
            .getBytes());

    public void setUp() {
        messageNodeClient2Pbx = new HistoryClient2PbxMessageTreeNode(packet1);
        messageNodePbx2Client = new HistoryPbx2ClientMessageTreeNode(packet2);
    }

    public void testClient2PbxMessage() {
        assertTrue(messageNodeClient2Pbx.isLeaf());
        assertNull(messageNodeClient2Pbx.getChild(1));
        assertEquals(messageNodeClient2Pbx.getTitle(), commandZeile1);

    }

    public void testPbx2ClientMessage() {
        assertTrue(messageNodePbx2Client.isLeaf());
        assertNull(messageNodePbx2Client.getChild(0));
        assertEquals(messageNodePbx2Client.getTitle(), commandZeile2);
    }

    public void testInterfaceMethods() {
        assertTrue(messageNodeClient2Pbx.getClosedIcon() == null);
        assertTrue(messageNodeClient2Pbx.getOpenIcon() == null);
        assertTrue(messageNodeClient2Pbx.getIcon() != null);
        assertTrue(new Color(0, 64, 0).equals(messageNodeClient2Pbx.getTextColor()));
        assertTrue(new Color(64, 0, 0).equals(messageNodePbx2Client.getTextColor()));
        assertTrue(messageNodeClient2Pbx.isLeaf());
    }

}
