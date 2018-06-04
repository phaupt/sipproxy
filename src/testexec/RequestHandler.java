package testexec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

import pd.IRequest;
import pd.IVar;
import pd.modules.IMessageModifier;
import testexec.history.ITestCaseRunHandler;
import config.Configuration;


public class RequestHandler implements IRequestHandler {
    private IVariableHandler variableHandler;
    private IVariableReplacer variableReplacer;
    private DatagramSocket socket;
    private InetAddress targetIP;
    private int targetPort;
    private StringBuffer errorBuffer;

    public RequestHandler(InetAddress targetIP,
                          int targetPort, 
                          DatagramSocket socket,
                          IVariableHandler variableHandler,
                          IVariableReplacer variableReplacer){
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.socket = socket;
        this.variableHandler = variableHandler;
        this.variableReplacer = variableReplacer;
    }

    public void handleRequest( IRequest request, ITestCaseRunHandler testCaseRunHandler ) {
        errorBuffer = new StringBuffer();

        //add variable to variable handler
        Vector<IVar> vars = request.getVars();
        variableHandler.addVariables(vars, variableReplacer);
             
        //Send packet with replaced content
        String content = request.getContent();
        String replacedContent = variableReplacer.replaceVariables(content);
        
        //Execute MessageModifiers
        Vector<IMessageModifier> messageModifiers = request.getModifiers();
        if(messageModifiers.size() > 0){
            for (IMessageModifier modifier : messageModifiers){
                //Replace the message with the modified message
                replacedContent = modifier.getMessage(replacedContent);
            }
        }
        //Send the UDP message
        if(sendPacket(replacedContent)){
            //Update History
            testCaseRunHandler.newRequest( targetIP.getHostAddress() + ":" + targetPort,
                                        request.getRequestId(),
                                        replacedContent);
        }
        
        if(errorBuffer.length() > 0){
            testCaseRunHandler.newRequest( targetIP.getHostAddress() + ":" + targetPort,
                    request.getRequestId(),
                    errorBuffer.toString());
        }

        //Start timer, within specified amount of time,
        //an expected response message has to arrive
        if(request.getTimeout() != null){
            request.getTimeout().startTimeout();
        }        
    }

    private boolean sendPacket(String content){
        boolean packetSent = false;
        byte[] data = content.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setAddress(targetIP);
        packet.setPort(targetPort);
        try {
            socket.send(packet);
            packetSent = true;
        }
        catch (SocketException e){
            //UDP Packet size >64KB
            errorBuffer.append("Could not send Datagram. Packet Length >64KB ! ");
            return false;
        }
        catch (IOException e) {
            errorBuffer.append("Datagram not send (socket error) ! ");
        }
        return packetSent;
    }
}
