package testexec.history;


public interface ITestCaseRunObservable {
    public void addTestCaseRunObserver(ITestCaseRunObserver observer);
    public void removeTestCaseRunObserver(ITestCaseRunObserver observer);
}
