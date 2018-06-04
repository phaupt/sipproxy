package testexec;

import java.util.Vector;

import javax.swing.JOptionPane;

import pd.IRequest;
import pd.IResponse;
import pd.ITestCase;
import pd.IVar;
import testexec.history.ITestCaseRunHandler;
import util.SimpleLogger;


public class TestCaseRunnable implements Runnable {
    private ITestCase testCase;
    private IVariableReplacer variableReplacer;
    private IVariableHandler variableHandler;
    private IRequestHandler requestHandler;
    private IResponseHandler responseHandler;
    private ITestCaseRunHandler testCaseRunHandler = null;
    private boolean aborted = false;

    public TestCaseRunnable(ITestCase testCase,
                            IVariableReplacer variableReplacer, 
                            IVariableHandler variableHandler, 
                            IRequestHandler requestHandler, 
                            IResponseHandler responseHandler, 
                            ITestCaseRunHandler testCaseRunHandler) {
        this.testCase = testCase;
        this.variableReplacer = variableReplacer;
        this.variableHandler = variableHandler;
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
        this.testCaseRunHandler = testCaseRunHandler;
    }


    public void run() {

        //Run test case for several times
        //for(int i = 0; i < testCase.getCycles(); i++){
        int i = -1;
        int numberOfCycles = testCase.getCycles();
        
        testCaseRunHandler.started();   //Set status
        while(!aborted && (numberOfCycles == ITestCase.INFINITE_CYCLES | (++i) < numberOfCycles)){
            try{
                //set actual test Cycle
                testCaseRunHandler.setActualCycle(i+1);
                
                //remove Variables of previous test cycles
                variableHandler.clear();
                
                //add Variables of test case to variable Handler
                //this has to be done each cycle because the list will be cleared
                //at the end of each test cycle
                Vector<IVar> vars = testCase.getVars();
                variableHandler.addVariables(vars, variableReplacer);
    
                //Get inital request message of test case
                IRequest request = testCase.getRequest(testCase.getInitialRequestMessageID());
                int waitingTimeInMilliseconds = 0;
                
                while(!aborted && request != null){
                    requestHandler.handleRequest(request, testCaseRunHandler);
                    IResponse response = responseHandler.handleResponses(request,testCaseRunHandler);
                    if(response != null){   
                        //get following request message, may be null if no following request
                        //message is defined
                        request = testCase.getRequest(response.getFollowingRequestID());
                        
                        //Wait a specific amount of time before sending next request
                        waitingTimeInMilliseconds = response.getWaitingTimeInMilliseconds();
                        if(waitingTimeInMilliseconds > 0){
                            try {
                                Thread.sleep(waitingTimeInMilliseconds);
                            }
                            catch (InterruptedException e) {
                                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                            }
                        }
                    }
                    else{
                        //Timeout expired
                        request = null;
                    }
                }
            }
            catch(OutOfMemoryError oome){
                stopExecution();
                JOptionPane.showMessageDialog(null, "Test execution has been aborted:\r\n" + oome.getMessage(),"Out of memory error", JOptionPane.ERROR_MESSAGE);
            }                
        }
        if(!aborted){
            testCaseRunHandler.finished(); 
        }
    }
    
    
    public void stopExecution(){
        aborted= true;
        testCaseRunHandler.aborted();
        responseHandler.abort();
    }
}
