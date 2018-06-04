package junit.testexec.history;

import java.io.File;

import testexec.history.IWarningLog;
import testexec.history.WarningXML;
import junit.framework.TestCase;


public class TestWarningLog extends TestCase {
    IWarningLog warningLog = null;
    
    @Override
    protected void setUp() throws Exception {
        warningLog = new WarningXML(new File("testWarning.xml"));
    }
    
    @Override
    protected void tearDown() throws Exception {
        new File("testWarning.xml").delete();
    }
    
    public void testWarningLog(){
        //add warning messages
        for(int i = 0; i < 5; i++){
            String requestID = i + "_1_1";
            String requestMsg = "msg request " + i;
            String message = "WARNING response " + i;
            warningLog.addWarning(requestID, requestMsg, message);
        }
        
        //check if warning log consists of previously added warning messages
        for(int j= 0; j < 5; j++){
            String requestID = j + "_1_1";
            String requestMsg = "msg request " + j;
            String message = "WARNING response " + j;
            assertEquals( requestMsg, warningLog.getWarningRequest(requestID));
            assertEquals( message, warningLog.getWarningMessage(requestID));
        }
    }
    
    public void testSaveAndLoad(){
        File testFile = new File("testWarningXML.xml");
        IWarningLog warningXML = new WarningXML(testFile);
        
        //writing log file
        for(int i = 0; i < 50; i++){
            String requestID = i + "_1_1";
            //requestMsg which contains s special character
            String requestMsg = "msg request " + i + ((char) i) + ((char) i+1);
            String message = "WARNING response " + i;
            warningXML.addWarning(requestID, requestMsg, message);
        }
        warningXML = null;
        
        //reading log file        
        IWarningLog warningXML2 = new WarningXML(testFile);
        for(int j= 0; j < 50; j++){
            String requestID = j + "_1_1";
            String requestMsg = "msg request " + j + ((char) j) + ((char) j+1);
            String message = "WARNING response " + j;
            printChars(requestMsg);
            printChars(warningXML2.getWarningRequest(requestID));
            testChars(requestMsg, warningXML2.getWarningRequest(requestID));
            assertEquals( requestMsg, warningXML2.getWarningRequest(requestID));
            assertEquals( message, warningXML2.getWarningMessage(requestID));
        }
//        String testRequest = "blabla\n sd d\r\n\r\n";
//        String testRespose = "fklsd fjkls \r\n \n";
//        String testID = "id1";
//        warningXML.addWarning(testID, testRequest, testRespose);
//        IWarningLog warningXML2 = new WarningXML(testFile);
//        
//        System.out.println("testRequest:");
//        printChars(testRequest );
//        System.out.println("\n request from warning:");
//        printChars(warningXML2.getWarningRequest(testID));
//        
//        testChars(testRequest, warningXML2.getWarningRequest(testID));        
//        assertEquals(testRequest,warningXML2.getWarningRequest(testID));
        //testFile.delete();
    }
    
    private void printChars( String message) {
        for(int i = 0; i < message.length();i++){
            System.out.print("'" + (int)message.charAt(i) + "' ");
        }
        System.out.println("");
        
    }

    private void testChars(String s1, String s2){
        
        for(int i = 0; i < s1.length(); i++){
            assertEquals((int)s1.charAt(i), (int)s2.charAt(i));
        }
    }
}
