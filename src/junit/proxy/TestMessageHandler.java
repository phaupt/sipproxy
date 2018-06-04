package junit.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.TestCase;
import proxy.MessageHandler;
import proxy.PacketQueue;
import proxy.SIPHistory;
import proxy.UDPDatagram;
import transforming.ITransformer;
import transforming.PacketRegexTransformer;
import config.Configuration;

public class TestMessageHandler extends TestCase {

    SIPHistory sipHistory;
    MessageHandler msgHandler;
    PacketQueue inputQueue;
    PacketQueue outputQueue;
    InetAddress proxyAddr;
    InetAddress pbxAddr;
    InetAddress clientAddr;
    Configuration config;

    String message;

    public void setUp() {
        try {
            proxyAddr = InetAddress.getByName("192.168.0.18");
            int proxyPort = 5060;
            pbxAddr = InetAddress.getByName("192.168.0.10");
            int pbxPort = 5060;
            clientAddr = InetAddress.getByName("192.168.0.15");
            int clientPort = 5060;
            
            config = new Configuration();
            config.setPbxIP(pbxAddr);
            config.setPbxPort(pbxPort);
            config.setProxySocketIP(proxyAddr);
            config.setProxySocketPort(proxyPort);
            config.setClientIP(clientAddr);
            config.setClientPort(clientPort);
            
            sipHistory = new SIPHistory(config);
            inputQueue = new PacketQueue();
            outputQueue = new PacketQueue();

            ITransformer transformer = new PacketRegexTransformer(config);

            msgHandler = new MessageHandler(inputQueue, outputQueue, transformer,
                    sipHistory);

            message = "SIP/2.0 100 Trying\n\r"
                    + "Via: SIP/2.0/UDP 192.168.0.15:5060;rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F;received=192.168.0.15\n\r"
                    + "From: testclient <sip:343434@192.168.0.18>;tag=3779692192\n\r"
                    + "To: testclient <sip:343434@192.168.0.18>\n\r"
                    + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@192.168.0.18\n\r"
                    + "CSeq: 27427 REGISTER\n\r"
                    + "User-Agent: Asterisk PBX\n\r"
                    + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY\n\r"
                    + "Contact: <sip:343434@192.168.0.18>\n\r" + "Content-Length: 0\n\r";
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void testMessageHandler() {
        try {

            // Packet from Client -> Proxy which should be forwarded ot the PBX
            UDPDatagram inPacket = new UDPDatagram(InetAddress.getByName("192.168.0.15"),
                    5060, proxyAddr, 5060, message.getBytes());
            inputQueue.addPacket(inPacket);

            assertTrue(inputQueue.size() == 1);
            assertTrue(outputQueue.size() == 0);

            new Thread(msgHandler).start();

            Thread.sleep(1000); // Time for Message Handler to remove Message from
            // Input-Queue and add it to Output-Queue

            assertTrue(inputQueue.size() == 0);
            assertTrue(outputQueue.size() == 1);

            UDPDatagram outPacket = outputQueue.removePacket();

            assertEquals(proxyAddr, outPacket.getSrcIP());
            assertEquals(pbxAddr, outPacket.getDstIP());
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
