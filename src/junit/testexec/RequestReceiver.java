package junit.testexec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class RequestReceiver implements Runnable{
    private String receivedRequest;
    private int waitTimeInMilliseconds;
    private DatagramSocket socket;
    
    public RequestReceiver(DatagramSocket socket, int waitTimeInMilliseconds){
        this.waitTimeInMilliseconds = waitTimeInMilliseconds;
        this.socket = socket;
    }
    
    public void run() {
        try {
            Thread.sleep(waitTimeInMilliseconds);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        receivedRequest = receivePacket();
        
    }
    
    public String getReceivedRequest(){
        return receivedRequest;
    }
    
    
    private String receivePacket(){
        byte[] buffer = new byte[6000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            //receive packet (blocks a specific amount of time)
            socket.receive(packet);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new String(packet.getData()).substring(0,packet.getLength());
        
        
    }    
    
}