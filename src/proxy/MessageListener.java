package proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import util.SimpleLogger;

public class MessageListener implements Runnable {

    private DatagramSocket socket;

    private boolean running = false;

    private final int bufferSize = 6144;

    private PacketQueue inputPacketQueue;

    public MessageListener(DatagramSocket socket, PacketQueue inputMessageQueue) {
        this.socket = socket;
        this.inputPacketQueue = inputMessageQueue;
    }

    public void setSocket( DatagramSocket socket ) {
        this.socket = socket;
    }

    public void shutdown() {
        running = false;
    }

    public void run() {

        running = true;
        byte[] buffer = new byte[bufferSize];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (running) {
            try {
                socket.receive(packet); // blocked until socket timeout or packet received

                // Set packet data
                packet.setData(new String(packet.getData()).substring(0,
                        packet.getLength()).getBytes());

                if (packet.getData().length > 2) {
                    // Add packet to input Queue
                    inputPacketQueue.addPacket(new UDPDatagram(packet.getAddress(),
                            packet.getPort(), socket.getLocalAddress(), socket
                                    .getLocalPort(), packet.getData()));
                }
                else {
                    // Empty UDP Packet ! (contains only 2 whitespaces) -> discard packet
                    SimpleLogger.log(">>>RECEIVED FROM: "
                            + packet.getAddress().toString().substring(1) + ":"
                            + packet.getPort() + "\n[Empty UDP Packet]" + "\n",
                            SimpleLogger.DEBUG_LOG_PROXY);
                }

                // initialize buffer
                buffer = new byte[bufferSize];
                packet = new DatagramPacket(buffer, buffer.length);

                Thread.yield();

            }
            catch (SocketException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
            catch (SocketTimeoutException e) {
                // Socket Receive Timeout !
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }

    }

}
