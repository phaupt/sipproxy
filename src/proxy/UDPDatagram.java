package proxy;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPDatagram {

    private String timestamp;
    private InetAddress srcIP;
    private int srcPort;
    private InetAddress dstIP;
    private int dstPort;
    byte[] data;

    public UDPDatagram(InetAddress src, int srcPort, InetAddress dst, int dstPort,
            byte[] data) {
        this.srcIP = src;
        this.srcPort = srcPort;
        this.dstIP = dst;
        this.dstPort = dstPort;
        this.data = data;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
        this.timestamp = sdf.format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public InetAddress getDstIP() {
        return dstIP;
    }

    public int getDstPort() {
        return dstPort;
    }

    public byte[] getData() {
        return data;
    }

    public InetAddress getSrcIP() {
        return srcIP;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setDstIP( InetAddress dst ) {
        this.dstIP = dst;
    }

    public void setSrcIP( InetAddress src ) {
        this.srcIP = src;
    }

    public void setDstPort( int dstPort ) {
        this.dstPort = dstPort;
    }

    public void setSrcPort( int srcPort ) {
        this.srcPort = srcPort;
    }

    public void setData( byte[] data ) {
        this.data = data;
    }

    public DatagramPacket getDatagramPacket() {
        DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setData(data, 0, data.length);
        packet.setAddress(dstIP);
        packet.setPort(dstPort);
        return packet;
    }

}
