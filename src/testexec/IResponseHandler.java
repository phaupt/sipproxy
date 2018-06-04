package testexec;

import pd.IRequest;
import pd.IResponse;
import testexec.history.ITestCaseRunHandler;


public interface IResponseHandler {
    public IResponse handleResponses(IRequest request,  ITestCaseRunHandler testCaseRunHandler);
    public void abort();
}
