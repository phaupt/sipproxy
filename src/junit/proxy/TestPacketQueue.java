package junit.proxy;

import proxy.PacketQueue;
import proxy.UDPDatagram;
import junit.framework.TestCase;

public class TestPacketQueue extends TestCase {

    PacketQueue queue;

    public void setUp() {
        queue = new PacketQueue();
    }

    public void testQueue() {
        UDPDatagram packet1 = new UDPDatagram(null, 1, null, 2, null);
        UDPDatagram packet2 = new UDPDatagram(null, 3, null, 4, null);

        assertEquals(queue.size(), 0);
        queue.addPacket(packet1);
        assertEquals(queue.size(), 1);
        queue.addPacket(packet2);
        assertEquals(queue.size(), 2);

        try {
            assertEquals(queue.removePacket(), packet1);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(queue.size(), 1);
        try {
            assertEquals(queue.removePacket(), packet2);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(queue.size(), 0);

    }
}
