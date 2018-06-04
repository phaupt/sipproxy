package testexec;

import pd.IRequest;
import testexec.history.ITestCaseRunHandler;


public interface IRequestHandler {
    public void handleRequest(IRequest request, ITestCaseRunHandler testCaseRunHandler);
}
