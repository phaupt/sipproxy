package junit.transforming;

import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.TestCase;
import proxy.UDPDatagram;
import transforming.PacketRegexTransformer;
import config.Configuration;
import config.XMLConfigFile;

public class TestPacketRegexTransformer extends TestCase {

    PacketRegexTransformer transformer;
    Configuration config;

    String filename = "src/junit/config/transformation/testConfig.xml";

    public void setUp() {
        config = XMLConfigFile.load(filename);

        transformer = new PacketRegexTransformer(config);

    }

    public void testClientToPbxTransforming() {

        UDPDatagram datagram = new UDPDatagram(config.getClientIP(), config
                .getClientPort(), config.getProxySocketIP(), config.getProxySocketPort(),
                getClient2PbxData());
        transformer.transformMessage(datagram);

        assertEquals(new String(getReplacedClient2PbxData()), new String(datagram
                .getData()));

        // Change Config-Parameters
        try {
            config.setClientIP(InetAddress.getByName("192.168.0.22"));
            config.setClientPort(5555);
            config.setPbxIP(InetAddress.getByName("192.168.0.21"));
            config.setPbxPort(1111);
            config.setProxySocketIP(InetAddress.getByName("192.168.0.11"));
            config.setProxySocketPort(222);
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Change config-parameter
        UDPDatagram datagram2 = new UDPDatagram(config.getClientIP(), config
                .getClientPort(), config.getProxySocketIP(), config.getProxySocketPort(),
                getClient2PbxData());
        transformer.transformMessage(datagram2);
        assertEquals(new String(getReplacedClient2PbxData()), new String(datagram2
                .getData()));

    }

    private byte[] getClient2PbxData() {
        return new String("REGISTER sip:" + config.getProxySocketIP().getHostAddress()
                + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP "
                + config.getClientIP().getHostAddress() + ":" + config.getClientPort()
                + ";rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
                + "From: testclient <sip:343434@" + config.getProxySocketIP().getHostAddress()
                + ">;tag=3779692192\r\n" + "To: testclient <sip:343434@"
                + config.getProxySocketIP().getHostAddress() + ">\r\n"
                + "Contact: \"testclient\" <sip:343434@"
                + config.getClientIP().getHostAddress() + ":" + config.getClientPort()
                + ">\r\n" + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                + config.getProxySocketIP().getHostAddress() + "\r\n"
                + "CSeq: 27427 REGISTER\r\n" + "Expires: 1800\r\n"
                + "Max-Forwards: 70\r\n" + "User-Agent: X-Lite release 1105x\r\n"
                + "Content-Length: 0\r\n").getBytes();
    }

    private byte[] getReplacedClient2PbxData() {
        return new String("REGISTER sip:" + config.getPbxIP().getHostAddress()
                + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP "
                + config.getProxySocketIP().getHostAddress() + ":" + config.getProxySocketPort()
                + ";rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
                + "From: testclient <sip:343434@" + config.getPbxIP().getHostAddress()
                + ">;tag=3779692192\r\n" + "To: testclient <sip:343434@"
                + config.getPbxIP().getHostAddress() + ">\r\n"
                + "Contact: \"testclient\" <sip:343434@"
                + config.getProxySocketIP().getHostAddress() + ":" + config.getProxySocketPort()
                + ">\r\n" + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                + config.getPbxIP().getHostAddress() + "\r\n"
                + "CSeq: 27427 REGISTER\r\n" + "Expires: 1800\r\n"
                + "Max-Forwards: 71\r\n" + "User-Agent: X-Lite release 1105x\r\n"
                + "Content-Length: 0\r\n").getBytes();
    }

    public void testPbxToClientTransforming_Trying() {

        UDPDatagram datagram = new UDPDatagram(config.getPbxIP(), config.getPbxPort(),
                config.getProxySocketIP(), config.getProxySocketPort(), getPbx2ClientData());
        transformer.transformMessage(datagram);

        assertEquals(new String(getReplacedPbx2ClientData()), new String(datagram
                .getData()));

        // Change Config-Parameters
        try {
            config.setClientIP(InetAddress.getByName("192.168.0.2"));
            config.setClientPort(133);
            config.setPbxIP(InetAddress.getByName("192.168.0.1"));
            config.setPbxPort(332);
            config.setProxySocketIP(InetAddress.getByName("192.168.0.121"));
            config.setProxySocketPort(2322);
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UDPDatagram datagram2 = new UDPDatagram(config.getPbxIP(), config.getPbxPort(),
                config.getProxySocketIP(), config.getProxySocketPort(), getPbx2ClientData());
        transformer.transformMessage(datagram2);

        assertEquals(new String(getReplacedPbx2ClientData()), new String(datagram2
                .getData()));

    }

    private byte[] getPbx2ClientData() {
        return new String(
                "SIP/2.0 100 Trying\r\n"
                        + "Via: SIP/2.0/UDP "
                        + config.getProxySocketIP().getHostAddress()
                        + ":"
                        + config.getProxySocketPort()
                        + ";rport;branch=z9hG4bK45D9E580C2FF47CB923237A850A46B9C;received="
                        + config.getProxySocketIP().getHostAddress()
                        + "\r\n"
                        + "From: testclient <sip:343434@"
                        + config.getPbxIP().getHostAddress()
                        + ">;tag=3779692192\r\n"
                        + "To: testclient <sip:343434@"
                        + config.getPbxIP().getHostAddress()
                        + ">\r\n"
                        + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                        + config.getPbxIP().getHostAddress()
                        + "\r\n"
                        + "CSeq: 27428 REGISTER\r\n"
                        + "User-Agent: Asterisk PBX\r\n"
                        + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY\r\n"
                        + "Contact: <sip:343434@" + config.getPbxIP().getHostAddress()
                        + ">\r\n" + "Content-Length: 0\r\n").getBytes();
    }

    private byte[] getReplacedPbx2ClientData() {
        return new String(
                "SIP/2.0 100 Trying\r\n"
                        + "Via: SIP/2.0/UDP "
                        + config.getClientIP().getHostAddress()
                        + ":"
                        + config.getClientPort()
                        + ";rport;branch=z9hG4bK45D9E580C2FF47CB923237A850A46B9C;received="
                        + config.getClientIP().getHostAddress()
                        + "\r\n"
                        + "From: testclient <sip:343434@"
                        + config.getProxySocketIP().getHostAddress()
                        + ">;tag=3779692192\r\n"
                        + "To: testclient <sip:343434@"
                        + config.getProxySocketIP().getHostAddress()
                        + ">\r\n"
                        + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@"
                        + config.getProxySocketIP().getHostAddress()
                        + "\r\n"
                        + "CSeq: 27428 REGISTER\r\n"
                        + "User-Agent: SIPProxy\r\n"
                        + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY\r\n"
                        + "Contact: <sip:343434@" + config.getProxySocketIP().getHostAddress()
                        + ">\r\n" + "Content-Length: 0\r\n").getBytes();
    }

    public void testPbxToClientTransforming_Notify() {

        UDPDatagram datagram = new UDPDatagram(config.getPbxIP(), config.getPbxPort(),
                config.getProxySocketIP(), config.getProxySocketPort(), getPbxToClientNotifyData());
        transformer.transformMessage(datagram);

        assertEquals(new String(getReplacedPbxToCientNotifyData()), new String(datagram
                .getData()));

        // Change Config-Parameters
        try {
            config.setClientIP(InetAddress.getByName("192.168.112.2"));
            config.setClientPort(1233);
            config.setPbxIP(InetAddress.getByName("192.168.232.1"));
            config.setPbxPort(2332);
            config.setProxySocketIP(InetAddress.getByName("192.168.121.121"));
            config.setProxySocketPort(22322);
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UDPDatagram datagram2 = new UDPDatagram(config.getPbxIP(), config.getPbxPort(),
                config.getProxySocketIP(), config.getProxySocketPort(), getPbxToClientNotifyData());
        transformer.transformMessage(datagram2);

        assertEquals(new String(getReplacedPbxToCientNotifyData()), new String(datagram2
                .getData()));

    }

    private byte[] getPbxToClientNotifyData() {
        return new String("NOTIFY sip:121212@" + config.getProxySocketIP().getHostAddress()
                + ":" + config.getProxySocketPort() + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP "
                + config.getPbxIP().getHostAddress() + ":" + config.getPbxPort()
                + ";branch=z9hG4bK7a8ff7c1\r\n" + "From: \"Unknown\" <sip:Unknown@"
                + config.getPbxIP().getHostAddress() + ">;tag=as64d19911\r\n"
                + "To: <sip:121212@" + config.getProxySocketIP().getHostAddress() + ":"
                + config.getProxySocketPort() + ">\r\n" + "Contact: <sip:Unknown@"
                + config.getPbxIP().getHostAddress() + ">\r\n"
                + "Call-ID: 1f918e7111fa72b64cba98a54bc579c3@"
                + config.getPbxIP().getHostAddress() + "\r\n" + "CSeq: 102 NOTIFY\r\n"
                + "User-Agent: Asterisk PBX\r\n" + "Max-Forwards: 70\r\n"
                + "Event: message-summary\r\n"
                + "Content-Type: application/simple-message-summary\r\n"
                + "Content-Length: 93\r\n" + "\r\n" + "Messages-Waiting: yes\r\n"
                + "Message-Account: sip:asterisk@" + config.getPbxIP().getHostAddress()
                + "\r\n" + "Voice-Message: 2/0 (0/0)\r\n").getBytes();
    }

    private byte[] getReplacedPbxToCientNotifyData() {
        return new String("NOTIFY sip:121212@" + config.getClientIP().getHostAddress()
                + ":" + config.getClientPort() + " SIP/2.0\r\n" + "Via: SIP/2.0/UDP "
                + config.getProxySocketIP().getHostAddress() + ":" + config.getProxySocketPort()
                + ";branch=z9hG4bK7a8ff7c1\r\n" + "From: \"Unknown\" <sip:Unknown@"
                + config.getProxySocketIP().getHostAddress() + ">;tag=as64d19911\r\n"
                + "To: <sip:121212@" + config.getClientIP().getHostAddress() + ":"
                + config.getClientPort() + ">\r\n" + "Contact: <sip:Unknown@"
                + config.getProxySocketIP().getHostAddress() + ">\r\n"
                + "Call-ID: 1f918e7111fa72b64cba98a54bc579c3@"
                + config.getProxySocketIP().getHostAddress() + "\r\n" + "CSeq: 102 NOTIFY\r\n"
                + "User-Agent: SIPProxy\r\n" + "Max-Forwards: 70\r\n"
                + "Event: message-summary\r\n"
                + "Content-Type: application/simple-message-summary\r\n"
                + "Content-Length: 93\r\n" + "\r\n" + "Messages-Waiting: yes\r\n"
                + "Message-Account: sip:asterisk@" + config.getProxySocketIP().getHostAddress()
                + "\r\n" + "Voice-Message: 2/0 (0/0)\r\n").getBytes();
    }
}
