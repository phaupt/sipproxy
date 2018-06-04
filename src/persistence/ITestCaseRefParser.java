package persistence;

import java.io.File;
import java.util.Vector;

import pd.ITestCaseRef;


public interface ITestCaseRefParser {
    Vector<ITestCaseRef> getTestCaseRefs(File parentDirectory);
}
