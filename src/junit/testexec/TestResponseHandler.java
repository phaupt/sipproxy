package junit.testexec;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import junit.framework.TestCase;
import pd.IRequest;
import pd.IResponse;
import pd.IResponseVar;
import pd.Request;
import pd.Response;
import pd.ResponseVar;
import pd.Timeout;
import pd.modules.ResponseCatcher;
import testexec.IResponseHandler;
import testexec.IVariableHandler;
import testexec.IVariableReplacer;
import testexec.RequestHandler;
import testexec.ResponseHandler;
import testexec.VariableHandler;
import testexec.VariableReplacer;
import testexec.history.MokTestCaseRunHandler;
import config.Configuration;


public class TestResponseHandler extends TestCase {
    IResponseHandler responseHandler;
    IRequest request1;
    IRequest timeoutRequest;
    int timeoutInMilliseconds = 3000;
    
    private String responseVar1Name = "nonce";
    private String responseVar1Value = "123jkl";
    private IResponse matchingResponse;
    private IResponse r2;
    private Configuration config;
    int senderPort = 11111;
    int listeningPort = 11112;
    DatagramSocket senderSocket;
    DatagramSocket listenerSocket;
    IVariableHandler variableHandler;
    IVariableReplacer variableReplacer;
        

    protected void setUp() throws Exception {
        //Setup sockets:
        //senderSocket: SIPProxy -> waits for response messages
        //listenerSocket: Target -> which returns response message
        config = new Configuration();

        config.setTestCaseSocketIP(InetAddress.getByName("localhost"));
        config.setTestCaseSocketPort(senderPort);
        config.setTargetIP(InetAddress.getByName("localhost"));
        config.setTargetPort(listeningPort);
        senderSocket = new DatagramSocket(config.getTestCaseSocketPort(), config.getTestCaseSocketIP());
        senderSocket.setReuseAddress(true);
        listenerSocket = new DatagramSocket(config.getTargetPort(), config.getTargetIP());
        listenerSocket.setReuseAddress(true);
        
        variableHandler = new VariableHandler();
        variableReplacer = new VariableReplacer(variableHandler);        
        responseHandler = new ResponseHandler(senderSocket,variableHandler,variableReplacer);

        //Create response message with response variable (nonce)
        ResponseVar rVar = new ResponseVar(responseVar1Name,new ResponseCatcher("(nonce=\")(.*)(\")", 2));
        Vector<IResponseVar> responseVars = new Vector<IResponseVar>();
        responseVars.add(rVar);
        matchingResponse = new Response("(200 OK)","warning");    //Response message regex
        matchingResponse.setFollowingRequestID(2);
        matchingResponse.setVariables(responseVars);
        
        //Create second expected response message
        r2 = new Response("(407)", "okay");           
        
        //Add expected response messages to vector and create
        //Request message
        Vector<IResponse> req1responses = new Vector<IResponse>();
        req1responses.add(matchingResponse);
        req1responses.add(r2);
        request1 = new Request(1,null,req1responses);        
        
        timeoutRequest = new Request(2,"timeoutcontent", req1responses);
        timeoutRequest.setTimeout(new Timeout(timeoutInMilliseconds,"Timeout"));
    }
    @Override
    protected void tearDown() throws Exception {
        senderSocket.close();
        listenerSocket.close();
    }
    
    public void testResponseHandling(){
        //No variables should be set
        assertNull(variableHandler.getVar(responseVar1Name));
        
        //send not expected response
        new Thread(new ResponseSender(listenerSocket, config, "blabla nonce=\"value\" blablabla", 1000)).start();
        
        //Still noo variables should be set
        assertNull(variableHandler.getVar(responseVar1Name));
        
        //send matching response
        new Thread(new ResponseSender(listenerSocket, config, "200 OK , blabla nonce=\"" + responseVar1Value + "\", blablabla", 1500)).start();

        //wait for responses
        IResponse response = responseHandler.handleResponses(request1, new MokTestCaseRunHandler());
        
        assertEquals(matchingResponse, response);
        assertEquals(responseVar1Value, variableHandler.getVar(responseVar1Name));
  
        RequestHandler reqHandler = new RequestHandler(config.getTargetIP(), config.getTargetPort(),senderSocket,variableHandler,new VariableReplacer(variableHandler));
        
        //send request and start timer
        reqHandler.handleRequest(timeoutRequest, new MokTestCaseRunHandler());

        //send response which matches because message is sent before timout occurs
        new Thread(new ResponseSender(listenerSocket, config, "200 OK , blabla nonce=\"" + responseVar1Value + "\", blablabla", timeoutInMilliseconds - 1000)).start();
        //wait for responses, receive 
        IResponse receivedResponose1 = responseHandler.handleResponses(timeoutRequest, new MokTestCaseRunHandler());
        assertEquals(matchingResponse, receivedResponose1);        
        assertFalse(timeoutRequest.getTimeout().timeoutExpired());
        
        
        //send request and start timer
        reqHandler.handleRequest(timeoutRequest, new MokTestCaseRunHandler());

        //send response which doesn't match
        new Thread(new ResponseSender(listenerSocket, config, "unknown response", 1000)).start();

        //send response which doesn't match
        new Thread(new ResponseSender(listenerSocket, config, "unknown response2", 2000)).start();
        
        //send response which would match, but will be received after timeout occurred
        new Thread(new ResponseSender(listenerSocket, config, "200 OK , blabla nonce=\"" + responseVar1Value + "\", blablabla", timeoutInMilliseconds + 1000)).start();
        //wait for responses, receive 
        IResponse response2 = responseHandler.handleResponses(timeoutRequest, new MokTestCaseRunHandler());
        assertTrue(timeoutRequest.getTimeout().timeoutExpired());
        assertEquals(null, response2);        
        
        
    }
}
