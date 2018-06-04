package testexec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import pd.IRequest;
import pd.IResponse;
import pd.IResponseVar;
import testexec.history.IMessagesLog;
import testexec.history.ITestCaseRunHandler;
import util.SimpleLogger;


public class ResponseHandler implements IResponseHandler {
    private IVariableHandler variableHandler;
    private DatagramSocket socket;
    private int DEFAULT_SOCKET_TIMEOUT = 500;
    private boolean aborted = false;
    private IVariableReplacer variableReplacer;

    public ResponseHandler( DatagramSocket socket,
                            IVariableHandler variableHandler,
                            IVariableReplacer variableReplacer){
        this.socket = socket;
        this.variableHandler = variableHandler;
        this.variableReplacer = variableReplacer;
    }
    
    public void abort(){
        aborted = true;
    }
    
    public void renewSocket(DatagramSocket socket){
        this.socket = socket;
    }
    
    public IResponse handleResponses( IRequest request, ITestCaseRunHandler testCaseRunHandler ) {
        aborted = false;
        DatagramPacket responsePacket = null;
        String responseContent = null;
        IResponse response = null;
        
        try {
            //Set default Timeout to make responseHandler interruptable
            socket.setSoTimeout(DEFAULT_SOCKET_TIMEOUT);
        }
        catch (SocketException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        
        //Receive messages until an expected response message was received
        while(!aborted && (response == null && (request.getTimeout() == null || !request.getTimeout().timeoutExpired()))){
            try{
                //Receive Response packet
                if(request.getTimeout() != null){

                    //set timout to have the opportunity to check if the timout of the
                    //previously sent request message occured
                    int remainingTimeoutTime = request.getTimeout().getRemainingTimeInMilliseconds();
                    if(remainingTimeoutTime < DEFAULT_SOCKET_TIMEOUT){
                        try {
                            socket.setSoTimeout(remainingTimeoutTime);
                        }
                        catch (SocketException e) {
                            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                        }
                    }

                }
                responsePacket = receivePacket();
                responseContent = new String(responsePacket.getData()).substring(0,responsePacket.getLength());
                
                //check if received response is expected of the previously sent request
                response = request.getMatchingResponse(responseContent,variableReplacer);
    
                if(response != null){
                    //expected response arrived  -> Update History
                    if (response.getCategory().toLowerCase().equals(IMessagesLog.CATEGORY_WARNING.toLowerCase())){
                        //Warning
                        testCaseRunHandler.newWarningResponse(
                                responsePacket.getAddress().getHostAddress()+ ":" + responsePacket.getPort(), 
                                responseContent);
                    }
                    else{
                        //Okay
                        testCaseRunHandler.newOkayResponse(
                                responsePacket.getAddress().getHostAddress()+ ":" + responsePacket.getPort(), 
                                responseContent);
                    }
                    
                    //add response variables to variable handler
                    Vector<IResponseVar> vars = response.getResponseVars();
                    for(IResponseVar var: vars){
                        //set actual response message in response variable
                        var.setResponseMessage(responseContent);
                    }
                    variableHandler.addVariables(vars);                
                }
                else{
                    testCaseRunHandler.newUnknownResponse( 
                            responsePacket.getAddress().getHostAddress()+ ":" + responsePacket.getPort(),
                            responseContent);
                }
            }
            catch(SocketTimeoutException ste){
                //do nothing, --> check timeout of request after loop 
            }
        }
        
        if(!aborted && ( response == null && request.getTimeout() != null)){
            //Update History
            testCaseRunHandler.newTimeout(request.getTimeout().getTimeInMilliseconds(), 
                                       request.getTimeout().getMessage());
        }
        return response;
    }

    private DatagramPacket receivePacket() throws SocketTimeoutException{
        //Initialize datagram packet to receive response
        byte[] buffer = new byte[6000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            //receive packet (blocks a specific amount of time)
            socket.receive(packet);
        }
        catch (SocketTimeoutException ste){
            throw ste;
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        return packet;
    }
}
