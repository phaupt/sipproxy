package junit.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import junit.framework.TestCase;
import proxy.MessageSender;
import proxy.PacketQueue;
import proxy.UDPDatagram;

public class TestMessageSender extends TestCase {

    final int LISTENER_PORT = 22345;
    final int SENDER_PORT = 22121;

    final String TEST_STRING = "TEST Message";
    MessageSender ms;
    DatagramSocket listenerSocket;
    DatagramSocket senderSocket;

    PacketQueue outputQueue;

    public void setUp() {
        try {
            listenerSocket = new DatagramSocket(LISTENER_PORT);
            senderSocket = new DatagramSocket(SENDER_PORT);
            outputQueue = new PacketQueue();
            ms = new MessageSender(senderSocket, outputQueue);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void testMessageSender() {
        try {
            UDPDatagram packet = new UDPDatagram(InetAddress.getByName("localhost"),
                    SENDER_PORT, InetAddress.getByName("localhost"), LISTENER_PORT,
                    TEST_STRING.getBytes());
            outputQueue.addPacket(packet);

            assertTrue(outputQueue.size() == 1);

            new Thread(ms).start();

            Thread.sleep(1000); // Time for Message Sender to remove Message form Queue
                                // and send it

            assertTrue(outputQueue.size() == 0);

            byte[] buffer = new byte[1024];
            DatagramPacket recPacket = new DatagramPacket(buffer, 0, buffer.length);
            listenerSocket.receive(recPacket);

            assertEquals(senderSocket.getLocalPort(), recPacket.getPort());

        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(ms).start();
    }

}
