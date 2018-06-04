package junit.testexec.history;

import junit.framework.TestCase;
import testexec.history.ITestCaseRun;
import testexec.history.MokTestCaseRun;
import testexec.history.TestCaseRunHandler;


public class TestTestCaseRunHandler extends TestCase {
    TestCaseRunHandler handler = null;
    ITestCaseRun mokTestCaseRun = null;

    protected void setUp() throws Exception {
        mokTestCaseRun = new MokTestCaseRun();
        handler = new TestCaseRunHandler(mokTestCaseRun);
    }
    
    public void testCategories(){
        assertEquals(0, mokTestCaseRun.getOkayCount());
        assertEquals(0, mokTestCaseRun.getWarningCount());
        assertEquals(0, mokTestCaseRun.getUnknownCount());
        
        handler.started();
        assertEquals(ITestCaseRun.STATUS_RUNNING, mokTestCaseRun.getStatus());
        
        handler.setActualCycle(1);
        handler.newRequest("to", 1, "request1");
        handler.newOkayResponse("from", "ok");
        assertEquals(1, mokTestCaseRun.getOkayCount());
        
        handler.newRequest("to",3, "request3");
        handler.newOkayResponse("from","ok");
        assertEquals(2, mokTestCaseRun.getOkayCount());
        
        handler.newRequest("to",4, "request4");
        handler.newUnknownResponse("from","unknown");
        assertEquals(1, mokTestCaseRun.getUnknownCount());

        handler.newRequest("to",5, "request5");
        handler.newTimeout(1000,"timeout");
        assertEquals(1, mokTestCaseRun.getWarningCount());
        handler.newRequest("to",8, "request8");
        handler.newWarningResponse("from","warning");
        assertEquals(2, mokTestCaseRun.getWarningCount());     
        
    }
    public void testCycles(){
        handler.setActualCycle(1);
        assertEquals(1, mokTestCaseRun.getCycles());
        handler.setActualCycle(2);
        assertEquals(2, mokTestCaseRun.getCycles());
        
        
    }
    public void testStatusFinished(){
        handler.started();
        assertEquals(ITestCaseRun.STATUS_RUNNING, mokTestCaseRun.getStatus());
        
        handler.finished();
        assertEquals(ITestCaseRun.STATUS_FINISHED, mokTestCaseRun.getStatus());        
    }
    
    public void testStatusAborted(){
        handler.started();
        assertEquals(ITestCaseRun.STATUS_RUNNING, mokTestCaseRun.getStatus());

        handler.aborted();
        assertEquals(ITestCaseRun.STATUS_CANCELED, mokTestCaseRun.getStatus());        
        
    }
    
    public void testMsgIDs(){
        assertEquals(0, mokTestCaseRun.getWarningRequestMsgIDs().size());
        for(int testCycle= 1; testCycle < 11; testCycle++){
            handler.setActualCycle(testCycle);
            handler.newRequest("to",1, "request 1"); //<testCycle>_1_1
            
            handler.newOkayResponse("from", "ok");
            handler.newRequest("to",2, "request 2");//<testCycle>_2_2
            
            //warning
            handler.newRequest("to",4, "request 4");//<testCycle>_4_3
            handler.newWarningResponse("from","bla warning");
            assertEquals(testCycle + "_4_3", mokTestCaseRun.getWarningRequestMsgIDs().get(testCycle-1));
        }
    }
}
