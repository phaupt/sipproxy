package testexec;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import pd.ITestCase;
import proxy.BadProxyException;
import testexec.history.HistoryLogsFactory;
import testexec.history.ITestCaseExecutionObserver;
import testexec.history.ITestCaseRun;
import testexec.history.ITestCaseRunHandler;
import testexec.history.TestCaseRun;
import testexec.history.TestCaseRunHandler;
import util.MiscUtil;
import util.SimpleLogger;
import config.Configuration;


public class TestCaseExecutionHandler implements ITestCaseExecutionHandler{
    private IVariableReplacer variableReplacer;
    private IVariableHandler variableHandler;
    private TestCaseRunnable testCaseRunnable = null;
    private Thread runningTestCaseThread = null;
    private DatagramSocket socket = null;
    private Configuration config;
    
    public TestCaseExecutionHandler(Configuration config) {   
        this.config = config;
        
        try {
            socket = new DatagramSocket(config.getTestCaseSocketPort(), this.config.getTestCaseSocketIP());
        }
        catch (SocketException e) {
            throw new BadProxyException();
        }
        
        variableHandler = new VariableHandler();
        variableReplacer = new VariableReplacer(variableHandler);
        
    }
    
    public void renewSocket() throws SocketException{
        if (socket != null){     
            socket.close();
            socket = new DatagramSocket(config.getTestCaseSocketPort(), config.getTestCaseSocketIP());
        }
    }
    
    public IVariableHandler getVariableHandler() {
        return variableHandler;
    }

    public IVariableReplacer getVariableReplacer() {
        return variableReplacer;
    }

    public ITestCaseRun runTestCase( ITestCase testCase, ITestCaseExecutionObserver observer, InetAddress targetIP, int targetPort) {
        clearSocketBuffer();

        //Filename for saving log file
        String timestamp = MiscUtil.getTimestamp(ITestCaseRun.SAVE_TIMESTAMP_FORMAT);
        File statisticLogfile = new File(SimpleLogger.LOG_DIR,timestamp + ITestCaseRun.DEFAULT_STATISTICS_SUFFIX);
        //Create new TestCaseRun
        ITestCaseRun testCaseRun = new TestCaseRun(statisticLogfile, new HistoryLogsFactory(), timestamp,testCase.getName(), targetIP.getHostAddress(), targetPort);        

        //Add observer to list of observers
        testCaseRun.addTestCaseExecutionObserver(observer);
        
        //Start execution of test case in separate thread if
        //no test case is executed
        if(runningTestCaseThread == null || !runningTestCaseThread.isAlive()){
            ITestCaseRunHandler testCaseRunHandler = new TestCaseRunHandler(testCaseRun);
            IRequestHandler requestHandler = new RequestHandler(targetIP, targetPort,socket,variableHandler, variableReplacer);
            IResponseHandler responseHandler = new ResponseHandler(socket, variableHandler, variableReplacer);

            testCaseRunnable = new TestCaseRunnable(testCase,variableReplacer, variableHandler, requestHandler, responseHandler, testCaseRunHandler);
            
            runningTestCaseThread = new Thread(testCaseRunnable);
            runningTestCaseThread.start();   
        }
        return testCaseRun;
    }
    

    
    private void clearSocketBuffer(){    
        //This method discards buffered response messages between two test case runs
        
        try {
            socket.setSoTimeout(10); //set socket timeout
            
            
            byte[] buffer = new byte[0];
            boolean isEmpty = false;
            while(!isEmpty){
                try{
                    //receive buffered  message
                    socket.receive(new DatagramPacket(buffer, buffer.length));                    
                }
                catch(SocketTimeoutException ste){
                    //socket timeout occurred, therfore no message are in buffer
                    isEmpty=true;
                }
                catch (IOException e) {
                    isEmpty=true;;
                }
            }            
        }
        catch (SocketException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }

    }
    public boolean isTestExecutionRunning(){
        return runningTestCaseThread != null && runningTestCaseThread.isAlive();
    }
    public void abortRunningTestCase() {
        //Abort execution of running test case
        if(isTestExecutionRunning()){
            testCaseRunnable.stopExecution();
            runningTestCaseThread.interrupt();
        }
    }
}
