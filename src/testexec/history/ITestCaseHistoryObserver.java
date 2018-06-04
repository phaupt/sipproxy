package testexec.history;


public interface ITestCaseHistoryObserver {
    public void testCaseRunAdded(ITestCaseRun testCaseRun);
    public void testCaseRunRemoved(ITestCaseRun testCaseRun, int index);
}
