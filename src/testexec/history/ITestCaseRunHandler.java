package testexec.history;

public interface ITestCaseRunHandler {
    public abstract void newRequest(String to, int requestID, String content);
	public abstract void newOkayResponse(String from, String content);
	public abstract void newWarningResponse(String from, String content);
	public abstract void newTimeout(long milliseconds, String message);
	public abstract void newUnknownResponse(String from, String content);
    public abstract void newErrorMessage(String message);
	public abstract void setActualCycle(int cycle);
    public abstract void started();
    public abstract void finished();
    public abstract void aborted();
}
 
