package testexec;

import java.net.InetAddress;

import pd.ITestCase;
import testexec.history.ITestCaseExecutionObserver;
import testexec.history.ITestCaseRun;


public interface ITestCaseExecutionHandler {
    public ITestCaseRun runTestCase(ITestCase testCase, ITestCaseExecutionObserver observer, InetAddress targetIP, int targetPort);
    public void abortRunningTestCase();
    public IVariableHandler getVariableHandler();
    public IVariableReplacer getVariableReplacer();
}
