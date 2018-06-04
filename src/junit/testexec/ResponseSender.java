package junit.testexec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import config.Configuration;


//Class which sends a response message 
class ResponseSender implements Runnable{
    private String content;
    private int waitTimeInMilliseconds;
    private DatagramSocket socket;
    private Configuration config;
    
    public ResponseSender(DatagramSocket socket, Configuration config, String content, int waitTimeInMilliseconds){
        this.socket = socket;
        this.config = config;
        this.content = content;
        this.waitTimeInMilliseconds = waitTimeInMilliseconds;
    }
    public void run() {
       sendResponse(content);
        
    }
    private void sendResponse(String content){
        byte[] data = content.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setAddress(config.getTestCaseSocketIP());
        packet.setPort(config.getTestCaseSocketPort());
        try {
            try {
                Thread.sleep(waitTimeInMilliseconds);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            socket.send(packet);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }     
}