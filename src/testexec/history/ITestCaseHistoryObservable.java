package testexec.history;


public interface ITestCaseHistoryObservable {
    public void addTestHistoryObserver(ITestCaseHistoryObserver observer);
    public void removeTestHistoryRunObserver(ITestCaseHistoryObserver observer);

}
