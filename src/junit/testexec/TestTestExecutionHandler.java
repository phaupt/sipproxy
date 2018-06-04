package junit.testexec;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import junit.framework.TestCase;
import pd.IRequest;
import pd.IResponse;
import pd.IResponseVar;
import pd.IVar;
import pd.Request;
import pd.Response;
import pd.ResponseVar;
import pd.Var;
import pd.modules.ClearText;
import pd.modules.ResponseCatcher;
import testexec.IRequestHandler;
import testexec.IResponseHandler;
import testexec.ITestCaseExecutionHandler;
import testexec.IVariableHandler;
import testexec.TestCaseExecutionHandler;
import testexec.history.ITestCaseExecutionObserver;
import config.Configuration;


public class TestTestExecutionHandler extends TestCase  {
    ITestCaseExecutionHandler testExecutionHandler;
    IResponseHandler responseHandler;
    IRequestHandler requestHandler;

    //TestCase Variables
    private String v1Name = "TargetNr";
    private String v1Value = "111122";
    
    //Request 1 Variables
    private String request1Content = "blabla kldsfjdklsfjlkds blabla content vjklsa\r\n jflksd";
    private String responseVar1Name = "nonce";
    private String responseVar1Value = "123jkl";
    
    //Request 2 Variables
    private String request2Content = "content /'" +responseVar1Name + "'/ content";
    private String replacedRequest2Content = "content " + responseVar1Value + " content";
    private String responseVar2Name = "realm";
    private String responseVar2Value = "responseVar2Value";
    private String req2VarName = "balbla";
    private String req2VarValue = "text /'" + responseVar1Name + "'/ text";
    
    private int testCycles = 3;
    
    private String response1Regex="(200) OK";
    private String matchingResponse1 = "200 OK, blabla " + responseVar1Name+ "=\"" + responseVar1Value + "\" blabla";
    private String response2Regex="(333)";
    private String matchingResponse2 = "333 OK, blabla " + responseVar2Name + "=\"" + responseVar2Value + "\" blablaljklsdf";
    
    private Configuration config;
    int senderPort = 11111;
    int listeningPort = 11112;

    DatagramSocket listenerSocket;
    private pd.TestCase testCase;
        
    public void setUp() throws UnknownHostException, SocketException{
        config = new Configuration();
        config.setTestCaseSocketIP(InetAddress.getByName("localhost"));
        config.setTestCaseSocketPort(senderPort);
        config.setTargetIP(InetAddress.getByName("localhost"));
        config.setTargetPort(listeningPort);

        listenerSocket = new DatagramSocket(config.getTargetPort(), config.getTargetIP());

     //Request 1, with response messags
        //Create response message with response variable (nonce)
        ResponseVar rVar = new ResponseVar(responseVar1Name,new ResponseCatcher("(" + responseVar1Name + "=\")(.*)(\")", 2));
        Vector<IResponseVar> responseVars = new Vector<IResponseVar>();
        responseVars.add(rVar);
        IResponse r1 = new Response(response1Regex,"okay");    //Response message regex
        r1.setFollowingRequestID(2);
        r1.setVariables(responseVars);
        
        //Create second expected response message
        IResponse r2 = new Response("(407)", "warning");           
        
        //Add expected response messages to vector and create
        //Request message
        Vector<IResponse> req1responses = new Vector<IResponse>();
        req1responses.add(r1);
        req1responses.add(r2);
        IRequest request1 = new Request(1,request1Content,req1responses);
        
     //Request 2
        //Create response message with response variable
        Vector<IResponse> req2responses = new Vector<IResponse>();
        Vector<IResponseVar> responseVars2 = new Vector<IResponseVar>();
        ResponseVar rVar2 = new ResponseVar(responseVar2Name,new ResponseCatcher("(" + responseVar2Name + "=\")(.*)(\")", 2));
        responseVars2.add(rVar2);        
        Response resp2 = new Response(response2Regex, "okay");
        resp2.setVariables(responseVars2);
        req2responses.add(resp2);
        
        //Create request message with variable
        IRequest request2 = new Request(2,request2Content, req2responses);
        Vector<IVar> req2vars = new Vector<IVar>();
        req2vars.add(new Var(req2VarName, new ClearText(req2VarValue)));
        request2.setVariables(req2vars);
        
     //Create Test Case with previously created requests
        Vector<IRequest> requests = new Vector<IRequest>();
        requests.add(request1);
        requests.add(request2);
        testCase = new pd.TestCase("test testcase", 1,requests);
        testCase.setCycles(testCycles);
        
        //Set Variables of testcase
        Vector<IVar> tcVariables = new Vector<IVar>();
        tcVariables.add(new Var(v1Name,new ClearText(v1Value)));
        testCase.setVariables(tcVariables);
        
        
        testExecutionHandler = new TestCaseExecutionHandler(config);
    }
    
    public void testExecution(){
        RequestReceiver reqReceiver1 = null;
        RequestReceiver reqReceiver2 = null;
        RequestReceiver reqReceiver3 = null;
        RequestReceiver reqReceiver4 = null;
        RequestReceiver reqReceiver5 = null;
        RequestReceiver reqReceiver6 = null;
        
//1. Testcycle        
            //Send first response message which matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse1, 1000)).start();
        
        //send second response message wich matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse2, 2000)).start();


        
        //Start Request Receiver which will receive sent request messages
        reqReceiver1 = new RequestReceiver(listenerSocket, 500);
        new Thread(reqReceiver1).start();
        reqReceiver2 = new RequestReceiver(listenerSocket, 1500);
        new Thread(reqReceiver2).start();
        
//2. Testcycle
        
        //Send first response message which matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse1, 3000)).start();
        
        //send second response message wich matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse2, 4000)).start();

        
        //Start Request Receiver which will receive sent request messages
        reqReceiver3 = new RequestReceiver(listenerSocket, 2500);
        new Thread(reqReceiver3).start();
        reqReceiver4 = new RequestReceiver(listenerSocket, 3500);
        new Thread(reqReceiver4).start();

//      3. Testcycle        
        //Send first response message which matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse1, 5000)).start();
        
        //send second response message wich matches on a response
        new Thread(new ResponseSender(listenerSocket, config, matchingResponse2, 6000)).start();
        
        //Start Request Receiver which will receive sent request messages
        reqReceiver5 = new RequestReceiver(listenerSocket, 4500);
        new Thread(reqReceiver5).start();
        reqReceiver6 = new RequestReceiver(listenerSocket, 5500);
        new Thread(reqReceiver6).start();
        
        
        ITestCaseExecutionObserver observer = new ITestCaseExecutionObserver(){

            public void testRunFinished() {
                // TODO Auto-generated method stub
                
            }
        };
        
        //start testcase
        testExecutionHandler.runTestCase(testCase, observer, config.getTargetIP(), config.getTargetPort());
        try {
            Thread.sleep(6500);
        }
        catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        IVariableHandler vHandler = testExecutionHandler.getVariableHandler();
        assertEquals(v1Value, vHandler.getVar(v1Name));
        assertEquals(responseVar1Value, vHandler.getVar(responseVar1Name));
        assertEquals(responseVar2Value, vHandler.getVar(responseVar2Name));
        
        
        //Wait 2000 milliseconds so that second request receiver receives request
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(request1Content, reqReceiver1.getReceivedRequest());
        assertEquals(replacedRequest2Content, reqReceiver2.getReceivedRequest());
        assertEquals(request1Content, reqReceiver3.getReceivedRequest());
        assertEquals(replacedRequest2Content, reqReceiver4.getReceivedRequest());
        assertEquals(request1Content, reqReceiver5.getReceivedRequest());
        assertEquals(replacedRequest2Content, reqReceiver6.getReceivedRequest());

        
        //uncomment as soon as getValue() of modules is implemented --> variables will be replaced
//        assertEquals(replacedReq2VarValue,vHandler.getVar(req2VarName));

    }    
}
