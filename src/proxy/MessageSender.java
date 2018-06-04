package proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import util.SimpleLogger;

public class MessageSender implements Runnable {

    private DatagramSocket socket;
    private PacketQueue outputPacketQueue;
    private boolean running = false;

    public MessageSender(DatagramSocket socket, PacketQueue outputMessageQueue) {
        this.socket = socket;
        this.outputPacketQueue = outputMessageQueue;
    }

    public void setSocket( DatagramSocket socket ) {
        this.socket = socket;
    }

    public void shutdown() {
        running = false;
    }

    public void run() {
        running = true;

        while (running) {
            // Remove packet from output queue
            try {
                UDPDatagram sipPacket = outputPacketQueue.removePacket();
                DatagramPacket packet = sipPacket.getDatagramPacket();
                try {
                    // Send packet
                    socket.send(packet);
                }
                catch (IOException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                Thread.yield();
            }
            catch (InterruptedException e) {
                // blocking removePacket operation was interrupted
            }

        }

    }

}
