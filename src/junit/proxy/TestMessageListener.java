package junit.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import proxy.MessageListener;
import proxy.PacketQueue;
import proxy.UDPDatagram;
import junit.framework.TestCase;

public class TestMessageListener extends TestCase {

    final int LISTENER_PORT = 12345;
    final int SENDER_PORT = 12121;

    final String TEST_STRING = "TEST Message";
    MessageListener ml;
    DatagramSocket listenerSocket;
    DatagramSocket senderSocket;

    PacketQueue inputQueue;

    public void setUp() {
        try {
            listenerSocket = new DatagramSocket(LISTENER_PORT);
            senderSocket = new DatagramSocket(SENDER_PORT);
            inputQueue = new PacketQueue();
            ml = new MessageListener(listenerSocket, inputQueue);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void testListener() {
        try {
            assertTrue(inputQueue.size() == 0);

            new Thread(ml).start(); // Start Listener Thread

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            packet.setData(TEST_STRING.getBytes(), 0, TEST_STRING.length());

            packet.setAddress(InetAddress.getByName("localhost"));

            packet.setPort(LISTENER_PORT);

            senderSocket.send(packet);

            Thread.sleep(1000); // --> Time for MessageListener to add Message to
            // InputQueue

            assertEquals(1, inputQueue.size());
            UDPDatagram udpPacket = inputQueue.removePacket();

            assertEquals(udpPacket.getSrcPort(), SENDER_PORT);
            assertEquals(udpPacket.getDstPort(), LISTENER_PORT);
            assertEquals(udpPacket.getDstIP(), listenerSocket.getLocalAddress());

            assertEquals(LISTENER_PORT, udpPacket.getDatagramPacket().getPort());
            assertEquals(listenerSocket.getLocalAddress(), udpPacket.getDatagramPacket()
                    .getAddress());
            assertTrue(new String(udpPacket.getDatagramPacket().getData())
                    .startsWith(TEST_STRING));
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}