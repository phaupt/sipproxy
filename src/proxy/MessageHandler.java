package proxy;

import transforming.ITransformer;
import util.SimpleLogger;

public class MessageHandler implements Runnable {

    private SIPHistory sipHistory;
    private PacketQueue inputPacketQueue;
    private PacketQueue outputPacketQueue;
    private ITransformer transformer;
    private boolean running = false;

    public MessageHandler(PacketQueue inputMessageQueue, PacketQueue outputMessageQueue,
            ITransformer transformer, SIPHistory sipHistory) {
        this.inputPacketQueue = inputMessageQueue;
        this.outputPacketQueue = outputMessageQueue;
        this.transformer = transformer;
        this.sipHistory = sipHistory;
    }

    public void shutdown() {
        running = false;
    }

    public void run() {
        running = true;
        while (running) {
            // Remove Message from Queue
            UDPDatagram sipPacket;
            try {
                sipPacket = inputPacketQueue.removePacket();

                String contentRCV = new String(sipPacket.getData());
                
                SimpleLogger.log("# RECEIVED FROM: "
                        + sipPacket.getSrcIP().getHostAddress().toString() + ":"
                        + sipPacket.getDstPort() + "\r\n"
                        + ">>" + contentRCV + "<<\r\n", SimpleLogger.DEBUG_LOG_PROXY);

                // Transform Message
                transformer.transformMessage(sipPacket);

                // Add Message to output queue --> Message is ready to be sent
                outputPacketQueue.addPacket(sipPacket);

                // Add Message to SIP-History
                sipHistory.addSIPMessage(sipPacket);

                String contentTRF = new String(sipPacket.getData());
                
                SimpleLogger.log("# FORWARDED TO: "
                        + sipPacket.getDstIP().getHostAddress().toString() + ":"
                        + sipPacket.getDstPort() + "\r\n"
                        + ">>" + contentTRF + "<<\r\n", SimpleLogger.DEBUG_LOG_PROXY);

                Thread.yield();
            }
            catch (InterruptedException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }

    }

    public void setSipHistory( SIPHistory sipHistory ) {
        this.sipHistory = sipHistory;
    }

}
