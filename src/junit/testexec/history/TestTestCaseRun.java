package junit.testexec.history;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;
import testexec.history.ITestCaseRun;
import testexec.history.MokHistoryLogFactory;
import testexec.history.TestCaseRun;


public class TestTestCaseRun extends TestCase{
    ITestCaseRun testCaseRun = null;
    @Override
    protected void setUp() throws Exception {
        testCaseRun = new TestCaseRun(new File("testStatistics.xml"),new MokHistoryLogFactory(), "timestamp", "testCaseName", "192.168.1.1", 5072);
    }
    @Override
    protected void tearDown() throws Exception {
        new File("testStatistics.xml").delete();
    }
    
    public void testUpdateCounters(){
        assertEquals(0, testCaseRun.getOkayCount());
        assertEquals(0, testCaseRun.getWarningCount());
        assertEquals(0, testCaseRun.getUnknownCount());
        
        testCaseRun.addUnknown();
        assertEquals(1, testCaseRun.getUnknownCount());

        testCaseRun.addWarning("1_1_1", " ", " ");
        assertEquals(1, testCaseRun.getWarningCount());
        testCaseRun.addWarning("1_1_2", " ", " ");
        assertEquals(2, testCaseRun.getWarningCount());
        
        testCaseRun.addOkay();
        assertEquals(1, testCaseRun.getOkayCount());        
    }
    
    public void testWarnings(){
        assertEquals(0, testCaseRun.getWarningRequestMsgIDs().size());

        for(int i=0; i < 10; i++){
            testCaseRun.addWarning(i + "_1", "request "+ i ,"message " + i);
        }
        
        assertEquals(10, testCaseRun.getWarningCount());
        assertEquals(10, testCaseRun.getWarningRequestMsgIDs().size());
        
        Vector<String> requestMessageIDs = testCaseRun.getWarningRequestMsgIDs();
        for(int j = 0; j < 10; j++){
            String requestMsgID = j + "_1";
            assertEquals(requestMsgID, requestMessageIDs.get(j));
            assertEquals("request " + j, testCaseRun.getWarningLog().getWarningRequest(requestMsgID));
            assertEquals("message " + j, testCaseRun.getWarningLog().getWarningMessage(requestMsgID));
        }
    }
    
    public void testSaveLoad(){
        File testFile = new File("testStatXML.xml");
        
        TestCaseRun statisticXML = new TestCaseRun(testFile, new MokHistoryLogFactory(),  "20061122112211123", "testCaseName", "192.168.1.1", 5072);
        assertEquals("22.11.2006 11:22:11:123", statisticXML.getStartTimestamp());
        // add 12 okay
        for(int i = 0; i < 12; i++){
            statisticXML.addOkay();
        }
        
        //add 5 unknown
        for (int j=0; j < 5; j++){
            statisticXML.addUnknown();
        }
        
        statisticXML.setStatus("running");
        //add 3 cycles, 3 warnings
        for(int k=0; k<3; k++){
            statisticXML.setCycles(k+1);
            statisticXML.addWarning("1_1_" + k, "request_" + k, "warning message");
        }
        
        assertEquals("running", statisticXML.getStatus());
        
        statisticXML.setEndTimestamp("20061212121221321");
        statisticXML.setStatus("finished");
        statisticXML = null;
        
        
        //create new StatisticsXML which loads crated file
        ITestCaseRun statisticXML2 = TestCaseRun.load(testFile, new MokHistoryLogFactory());
        
        assertEquals("22.11.2006 11:22:11:123", statisticXML2.getStartTimestamp());
        assertEquals("12.12.2006 12:12:21:321", statisticXML2.getEndTimestamp());
        

        assertEquals("finished", statisticXML2.getStatus());
        assertEquals("192.168.1.1", statisticXML2.getTargetIP());
        assertEquals(5072, statisticXML2.getTargetPort());
        assertEquals(3, statisticXML2.getCycles());
        assertEquals(12, statisticXML2.getOkayCount());
        assertEquals(5, statisticXML2.getUnknownCount());
        assertEquals(3, statisticXML2.getWarningCount());
        Vector<String> requestIDs = statisticXML2.getWarningRequestMsgIDs();
        for(int l = 0; l < 3; l++){
            assertEquals("1_1_" + l, requestIDs.get(l));
        }
        testFile.delete();
    }
}
