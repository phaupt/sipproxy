package testexec.history;

import java.util.Vector;


public interface ITestCaseHistory extends ITestCaseHistoryObservable {
    public Vector<ITestCaseRun> getTestCaseRuns();
    public void addTestCaseRun(ITestCaseRun testCaseRun);
    public void deleteTestCaseRun(ITestCaseRun testCaseRun);
}
