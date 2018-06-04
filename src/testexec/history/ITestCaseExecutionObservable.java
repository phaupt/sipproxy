package testexec.history;


public interface ITestCaseExecutionObservable {
    public void addTestCaseExecutionObserver(ITestCaseExecutionObserver observer);
    public void removeTestCaseExecutionObserver(ITestCaseExecutionObserver observer);
}
