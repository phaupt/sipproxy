package proxy;

import java.util.concurrent.LinkedBlockingQueue;

public class PacketQueue {

    private LinkedBlockingQueue<UDPDatagram> queue = new LinkedBlockingQueue<UDPDatagram>();

    public void addPacket( UDPDatagram packet ) {
        // add Message to Queue
        queue.add(packet);
    }

    public UDPDatagram removePacket() throws InterruptedException {
        // Return and remove Head of Queue
        UDPDatagram packet = null;
        packet = queue.take();
        return packet;
    }

    public int size() {
        return queue.size();
    }
}
