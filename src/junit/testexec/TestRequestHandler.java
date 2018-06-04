package junit.testexec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import junit.framework.TestCase;
import pd.IRequest;
import pd.IResponse;
import pd.IVar;
import pd.Request;
import pd.Response;
import pd.ResponseVar;
import pd.Var;
import pd.modules.ClearText;
import pd.modules.Digest;
import pd.modules.ResponseCatcher;
import testexec.IRequestHandler;
import testexec.IVariableHandler;
import testexec.IVariableReplacer;
import testexec.RequestHandler;
import testexec.VariableHandler;
import testexec.VariableReplacer;
import testexec.history.MokTestCaseRunHandler;
import config.Configuration;


public class TestRequestHandler extends TestCase {
    IRequestHandler requestHandler;
    IVariableHandler variableHandler;
    IVariableReplacer variableReplacer;
    
    private String reqVar1Name = "testVar1";
    private String reqVar1Value = "fjdlksjfklsdjf";
    private String req1Content = "content content/'testVar1'/content content";
    private String req1ReplacedContent = "content contentfjdlksjfklsdjfcontent content";
    
    private String responseVar1Name = "nonce";
    private String responseVar1Value = "123jkl";
    
    private String reqVar2Name = "digest";
    
    IRequest request1;
    IRequest request2;
    int senderPort = 11111;
    int listeningPort = 11112;
    DatagramSocket senderSocket;
    DatagramSocket listenerSocket;
    
    protected void setUp() throws Exception {
        Configuration config = new Configuration();
        config.setTestCaseSocketIP(InetAddress.getByName("localhost"));
        config.setTestCaseSocketPort(senderPort);
        config.setTargetIP(InetAddress.getByName("localhost"));
        config.setTargetPort(listeningPort);

        variableHandler = new VariableHandler();
        variableReplacer = new VariableReplacer(variableHandler);
        senderSocket = new DatagramSocket(config.getTestCaseSocketPort(), config.getTestCaseSocketIP());
        listenerSocket = new DatagramSocket(config.getTargetPort(), config.getTargetIP());
        
        requestHandler = new RequestHandler(config.getTargetIP(), config.getTargetPort(), senderSocket, variableHandler, variableReplacer );
        
        Vector<IVar> req1vars = new Vector<IVar>();
        req1vars.add(new Var(reqVar1Name, new ClearText(reqVar1Value)));
        
        ResponseVar rVar = new ResponseVar(responseVar1Name,new ResponseCatcher("(nonce=\")(.*)(\")", 2));
        rVar.setResponseMessage("blabla\r\nblabla nonce=\"" + responseVar1Value + "\" blabla");
        Response r1 = new Response("200","warning");
        r1.setFollowingRequestID(2);

        Vector<IResponse> req1responses = new Vector<IResponse>();
        req1responses.add(r1);
        request1 = new Request(1,req1Content,req1responses);
        request1.setVariables(req1vars);

        
        Vector<IVar> req2vars = new Vector<IVar>();
        req2vars.add(new Var(reqVar2Name, new Digest("username","secret", "realm", "/'nonce'/", "uri", "REGISTER", "md5")));
        request2 = new Request(2, "blabla diget:/'digest'/ blabla",null);
        request2.setVariables(req2vars);
    }
    
    protected void tearDown() throws Exception {
        senderSocket.close();
        listenerSocket.close();
    }
    
    public void testRequestHanding(){
        //new Thread(new Receiver(this)).start();

        requestHandler.handleRequest(request1, new MokTestCaseRunHandler());
        assertEquals(reqVar1Value, variableHandler.getVar(reqVar1Name));
        assertNull(variableHandler.getVar(reqVar2Name));
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        assertEquals(req1ReplacedContent, receivePacket());

        //requestHandler.handleRequest(request2);
        //assertEquals(reqVar2Value, variableHandler.getVar(reqVar2Name));
    }
    
    private String receivePacket(){
        byte[] buffer = new byte[6000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            //receive packet (blocks a specific amount of time)
            listenerSocket.receive(packet);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new String(packet.getData()).substring(0,packet.getLength());
        
        
    }
}
