package junit.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import config.Configuration;

import proxy.SIPHistory;
import proxy.UDPDatagram;
import ui.history.HistoryTreeChange;
import ui.history.nodes.HistoryClient2PbxMessageTreeNode;
import ui.history.nodes.HistoryCompositeTreeNode;
import ui.history.nodes.HistoryPbx2ClientMessageTreeNode;
import junit.framework.TestCase;

public class TestSIPHistory extends TestCase implements Observer {

    private SIPHistory sipHistory;
    private InetAddress pbxIP;
    private InetAddress clientIP;
    private InetAddress proxyIP;

    private Configuration config;

    private boolean testPbx2ClientMessage;
    private boolean testClient2PbxMessage;
    private boolean isNotifyOk;

    private byte[] messageData1;
    private byte[] messageData2;

    public void setUp() {
        try {
            pbxIP = InetAddress.getByName("192.168.0.10");
            clientIP = InetAddress.getByName("192.168.0.15");
            proxyIP = InetAddress.getByName("192.168.0.18");

            config = new Configuration();
            config.setClientIP(clientIP);
            config.setClientPort(0);
            config.setProxySocketIP(proxyIP);
            config.setProxySocketPort(0);

            sipHistory = new SIPHistory(config);

            isNotifyOk = false;
            testPbx2ClientMessage = false;
            testClient2PbxMessage = false;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        messageData1 = new String("REGISTER sip:" + proxyIP.getHostAddress()
                + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP " + clientIP.getHostAddress() + ":"
                + 5060 + ";rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
                + "From: testclient <sip:343434@" + proxyIP.getHostAddress()
                + ">;tag=3779692192\r\n" + "To: testclient <sip:343434@"
                + proxyIP.getHostAddress() + ">\r\n"
                + "Contact: \"testclient\" <sip:343434@" + clientIP.getHostAddress()
                + ":" + 5060 + ">\r\n" + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                + proxyIP.getHostAddress() + "\r\n" + "CSeq: 27427 REGISTER\r\n"
                + "Content-Length: 0\r\n").getBytes();

        // message with other CSeq-Nr
        messageData2 = new String("REGISTER sip:" + proxyIP.getHostAddress()
                + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP " + clientIP.getHostAddress() + ":"
                + 5060 + ";rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
                + "From: testclient <sip:343434@" + proxyIP.getHostAddress()
                + ">;tag=3779692192\r\n" + "To: testclient <sip:343434@"
                + proxyIP.getHostAddress() + ">\r\n"
                + "Contact: \"testclient\" <sip:343434@" + clientIP.getHostAddress()
                + ":" + 5060 + ">\r\n" + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                + proxyIP.getHostAddress() + "\r\n" + "CSeq: 27428 REGISTER\r\n"
                + "Content-Length: 0\r\n").getBytes();

    }

    public void testClient2PbxMessage() {
        sipHistory.getOrderedByTimeRoot().addObserver(this);
        testClient2PbxMessage = true;

        addClient2PbxMessage();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(isNotifyOk);
        assertEquals(1, sipHistory.getOrderedByTimeRoot().getChildCount());
        addClient2PbxMessage();
        assertEquals(2, sipHistory.getOrderedByTimeRoot().getChildCount());
    }

    private void addClient2PbxMessage() {
        // CSeq-Message 1
        sipHistory
                .addSIPMessage(new UDPDatagram(proxyIP, 5060, pbxIP, 5060, messageData1));
    }

    public void testPbx2ClientMessage() {
        sipHistory.getOrderedByTimeRoot().addObserver(this);
        testPbx2ClientMessage = true;

        addPbx2ClientMessage();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(isNotifyOk);
        assertEquals(1, sipHistory.getOrderedByTimeRoot().getChildCount());
        addPbx2ClientMessage();
        assertEquals(2, sipHistory.getOrderedByTimeRoot().getChildCount());

    }

    private void addPbx2ClientMessage() {
        // CSeq-Message 2
        sipHistory.addSIPMessage(new UDPDatagram(proxyIP, 5060, clientIP, 5060,
                messageData2));
    }

    public void testCSeqGroupMessage() {
        sipHistory.getGroupedByCSeqRoot().addObserver(this);

        // Add 2 Messages with same CSeq
        addClient2PbxMessage();
        addClient2PbxMessage();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(isNotifyOk);

        // Test if Node for CSeq has been created
        assertEquals(1, sipHistory.getGroupedByCSeqRoot().getChildCount());
        // Test if CSeq-Node has 2 Child-Nodes
        assertEquals(2, sipHistory.getGroupedByCSeqRoot().getChild(0).getChildCount());
        assertTrue(sipHistory.getGroupedByCSeqRoot().getChild(0) instanceof HistoryCompositeTreeNode);

        // Add Message with other CSeq
        addPbx2ClientMessage();
        // Test if new Node for CSeq has been created
        assertEquals(2, sipHistory.getGroupedByCSeqRoot().getChildCount());
        // Test if new CSeq-Node has one Child-Node
        assertEquals(1, sipHistory.getGroupedByCSeqRoot().getChild(1).getChildCount());
        assertTrue(sipHistory.getGroupedByCSeqRoot().getChild(1) instanceof HistoryCompositeTreeNode);

    }

    public void update( Observable o, Object arg ) {
        isNotifyOk = true;
        if (testPbx2ClientMessage) {
            HistoryTreeChange change = (HistoryTreeChange) arg;
            assertTrue(change.getNode() instanceof HistoryPbx2ClientMessageTreeNode);
            assertEquals(sipHistory.getOrderedByTimeRoot(), change.getParentNode());

        }
        else if (testClient2PbxMessage) {
            HistoryTreeChange change = (HistoryTreeChange) arg;
            assertTrue(change.getNode() instanceof HistoryClient2PbxMessageTreeNode);
            assertEquals(sipHistory.getOrderedByTimeRoot(), change.getParentNode());

        }

    }
}
