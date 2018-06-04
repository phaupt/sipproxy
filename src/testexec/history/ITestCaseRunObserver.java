package testexec.history;


public interface ITestCaseRunObserver {
    public void warningAdded(ITestCaseRun testCaseRun, String requestMsgID);
    public abstract void okayAdded();
    public abstract void unknownAdded();
    public abstract void endTimestampChanged();
    public abstract void statusChanged();
    public abstract void cyclesChanged();  
    public abstract void targetUAChanged();
}
