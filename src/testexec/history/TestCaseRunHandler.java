package testexec.history;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.MiscUtil;

public class TestCaseRunHandler implements ITestCaseRunHandler {
 
	private String actualRequestMsgID = null;	 
	private String actualRequestMsgContent = null;
	private int actualCycle = 0;
	private int actualRequestMsgInCycle=0;
	private ITestCaseRun testCaseRun = null;
	 
    
    public TestCaseRunHandler(ITestCaseRun testCaseStatistic){
        this.testCaseRun = testCaseStatistic;
    }
    
	private String getNextRequestMsgID(int requestID) {
        //Build next ID of request Message
		return actualCycle + "_" + requestID + "_" + (++actualRequestMsgInCycle);
	}
	 
    private void setActualRequestMsg(String msgID, String content) {
        this.actualRequestMsgID = msgID;
        this.actualRequestMsgContent = content;
	}
	 
    public void setActualCycle(int cycle) {
        this.actualCycle = cycle;
        testCaseRun.setCycles(cycle);
        //reset count for request messages in cycle
        actualRequestMsgInCycle = 0; 
    }

    public void newRequest(String to, int requestID, String content) {
        //save information for use in further response messages        
        String msgID = getNextRequestMsgID(requestID);
        setActualRequestMsg(msgID, content);
        testCaseRun.getMessagesLog().logRequest(to, msgID, content);
	}
	 
	public void newOkayResponse(String from, String content) {
        testCaseRun.getMessagesLog().logResponse(from, actualRequestMsgID, IMessagesLog.CATEGORY_OKAY, content);
        testCaseRun.addOkay();
        setTargetUAType(content);
	}


    public void newWarningResponse(String from, String content) {
        testCaseRun.getMessagesLog().logResponse(from, actualRequestMsgID, IMessagesLog.CATEGORY_WARNING, content);
        testCaseRun.addWarning(actualRequestMsgID, actualRequestMsgContent, content);
        setTargetUAType(content);
	}
	 
	public void newTimeout(long milliseconds, String message) {
        testCaseRun.getMessagesLog().logTimeout(actualRequestMsgID, milliseconds, message);
        testCaseRun.addWarning(actualRequestMsgID, actualRequestMsgContent, "[TIMEOUT] " + message);
	}
	 
	public void newUnknownResponse(String from, String content) {
        testCaseRun.getMessagesLog().logResponse(from, actualRequestMsgID, IMessagesLog.CATEGORY_UNKNOWN, content);
        testCaseRun.addUnknown();
        setTargetUAType(content);
	}

    public void aborted() {
        String timestamp = MiscUtil.getTimestamp(ITestCaseRun.SAVE_TIMESTAMP_FORMAT);
        testCaseRun.setEndTimestamp(timestamp);
        testCaseRun.setStatus(ITestCaseRun.STATUS_CANCELED);
    }

    public void finished() {
        String timestamp = MiscUtil.getTimestamp(ITestCaseRun.SAVE_TIMESTAMP_FORMAT);
        testCaseRun.setEndTimestamp(timestamp);
        testCaseRun.setStatus(ITestCaseRun.STATUS_FINISHED);                
    }

    public void started() {
        testCaseRun.setStatus(ITestCaseRun.STATUS_RUNNING);
        testCaseRun.setTargetUA(ITestCaseRun.UATYPE_UNKNOWN);
    }

    public void newErrorMessage( String message ) {
        testCaseRun.getMessagesLog().logError(message);
        
    }
    
    private void setTargetUAType(String message) {
        String uaType = getUAType(message);
        if(uaType != null && !uaType.equals(testCaseRun.getTargetUA())){
            //set User-Agent to actual value
            testCaseRun.setTargetUA(uaType);
        }
    }
    
    private String getUAType(String message){
        //Extract user agent from message
        String uaType = null;
        
        String regex = "([Uu][Ss][Ee][Rr]-[Aa][Gg][Ee][Nn][Tt].*?:)(.*)";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(message);
        if(matcher.find()){
            //Second group (.*) contains name of user agent
            uaType = matcher.group(2).trim();
        }
        return uaType;
    }
	 
	 
}
 
