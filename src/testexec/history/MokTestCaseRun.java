package testexec.history;

import java.util.Vector;


public class MokTestCaseRun implements ITestCaseRun {
    private long okay = 0;
    private long warning = 0;
    private long unknown = 0;
    
    private long cycles =0;
    private String status = null;
    
    private Vector<String> warningRequestIDs = new Vector<String>();
    private IWarningLog warningLog = new MokWarningLog();
    private IMessagesLog messagesLog = new MokMessagsLog();

    public void addOkay() {
        okay++;
    }

    public void addUnknown() {
        unknown++;
    }

    public void addWarning( String requestMsgID, String request, String message ) {
        warning++;
        warningRequestIDs.add(requestMsgID);
    }

    public long getOkayCount() {
        return okay;
    }

    public long getUnknownCount() {
        return unknown;
    }

    public long getWarningCount() {
        return warning;
    }

    public Vector<String> getWarningRequestMsgIDs() {
        return warningRequestIDs;
    }

    public IMessagesLog getMessagesLog() {
        // TODO Auto-generated method stub
        return messagesLog;
    }

    public IWarningLog getWarningLog() {
        // TODO Auto-generated method stub
        return warningLog;
    }

    public String getTestCaseName() {
        // TODO Auto-generated method stub
        return "testcasename";
    }

    public String getTimestamp() {
        // TODO Auto-generated method stub
        return "timestamp";
    }

    public void addTestCaseRunObserver( ITestCaseRunObserver observer ) {
        // TODO Auto-generated method stub
        
    }

    public void removeTestCaseRunObserver( ITestCaseRunObserver observer ) {
        // TODO Auto-generated method stub
        
    }

    public long getCycles() {
        // TODO Auto-generated method stub
        return cycles;
    }

    public String getEndTimestamp() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getStartTimestamp() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getStatus() {
        // TODO Auto-generated method stub
        return status;
    }

    public String getTargetIP() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getTargetPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setCycles( long cycles ) {
        this.cycles = cycles;
        
    }

    public void setEndTimestamp( String timestamp ) {
        // TODO Auto-generated method stub
        
    }

    public void setStatus( String status ) {
        this.status = status;
        
    }

    public void addTestCaseExecutionObserver( ITestCaseExecutionObserver observer ) {
        // TODO Auto-generated method stub
        
    }

    public void removeTestCaseExecutionObserver( ITestCaseExecutionObserver observer ) {
        // TODO Auto-generated method stub
        
    }

    public void delete() {
        // TODO Auto-generated method stub
        
    }

    public String getTargetUA() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTargetUA(String targetUA) {
        // TODO Auto-generated method stub
        
    }

}
