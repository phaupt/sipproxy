package proxy;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import transforming.ITransformer;
import transforming.PacketRegexTransformer;
import util.SimpleLogger;
import config.Configuration;

public class Proxy {

    private PacketQueue inputMessageQueue = new PacketQueue();
    private PacketQueue outputMessageQueue = new PacketQueue();
    private SIPHistory sipHistory;
    private MessageListener listener;
    private MessageSender sender;
    private MessageHandler handler;
    private DatagramSocket socket;
    private Configuration config;
    private ITransformer transformer;
    private final int SOCKET_TIMEOUT = 500;
    private Thread listenerThread;
    private Thread senderThread;

    public Proxy(Configuration config) {
        this.config = config;
        initialize();

    }

    private void initialize() {
        transformer = new PacketRegexTransformer(config);
        sipHistory = new SIPHistory(config);

        handler = new MessageHandler(inputMessageQueue, outputMessageQueue, transformer,
                sipHistory);
        new Thread(handler).start();
    }

    public void startProxy() throws BadProxyException {
        boolean socketError = false;

        try {
            if (socket != null)
                socket.close(); // Unbind Port Resource !
            socket = new DatagramSocket(config.getProxySocketPort(), config.getProxySocketIP());
            socket.setSoTimeout(SOCKET_TIMEOUT);

            listener = new MessageListener(socket, inputMessageQueue);
            sender = new MessageSender(socket, outputMessageQueue);

            listenerThread = new Thread(listener);
            listenerThread.start();
            senderThread = new Thread(sender);
            senderThread.start();

        }
        catch (SocketException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            socketError = true;
        }

        if (socketError)
            throw new BadProxyException();
    }

    public void setConfigChanges( InetAddress clientIP, int clientPort,
            InetAddress proxyIP, int proxyPort, InetAddress pbxIP, int pbxPort )
            throws BadProxyException {

        config.setClientIP(clientIP);
        config.setClientPort(clientPort);
        config.setPbxIP(pbxIP);
        config.setPbxPort(pbxPort);

        if (socket == null || socket.getLocalAddress() == null
                || socket.getLocalPort() == -1
                || !socket.getLocalAddress().equals(proxyIP)
                || socket.getLocalPort() != proxyPort) {
            // Proxy Settings have been changed --> renew Socket

            if (listener != null) {
                listener.shutdown();
                listenerThread.interrupt();
            }

            if (sender != null) {
                sender.shutdown();
                senderThread.interrupt();
            }

            if (sender != null) {
                while (senderThread.isAlive()) {
                    try {
                        // wait until both Threads are destroyed
                        Thread.sleep(10);
                        Thread.yield();
                    }
                    catch (InterruptedException e) {
                        SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                    }
                }
            }
            if (listener != null) {
                while (listenerThread.isAlive()) {
                    try {
                        // wait until both Threads are destroyed
                        Thread.sleep(10);
                        Thread.yield();
                    }
                    catch (InterruptedException e) {
                        SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                    }
                }
            }
            config.setProxySocketIP(proxyIP);
            config.setProxySocketPort(proxyPort);

            startProxy(); // Restart Proxy
        }
    }

    public SIPHistory getSipHistory() {
        return sipHistory;
    }

    public void sendMessage( UDPDatagram packet ) {
        sipHistory.addSIPMessage(packet);
        outputMessageQueue.addPacket(packet);
    }

    public InetAddress getProxyIP() {
        return config.getProxySocketIP();
    }

    public int getProxyPort() {
        return config.getProxySocketPort();
    }

    public Configuration getConfig() {
        return config;
    }

}
