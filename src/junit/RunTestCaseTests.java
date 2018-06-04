package junit;

import junit.config.TestConfiguration;
import junit.config.TestXMLConfigFile;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;
import junit.persistence.*;
import junit.pd.modules.*;
import junit.testexec.*;
import junit.testexec.history.TestTestCaseRun;
import junit.testexec.history.TestTestCaseRunHandler;
import junit.testexec.history.TestWarningLog;
import junit.util.*;

public class RunTestCaseTests {

    public static Test suite() {

        //EXECUTE ALL JUNIT TESTS !

        TestSuite testSuite = new TestSuite();
        
        //junit.util
        testSuite.addTestSuite(TestCharSetParser.class);
        testSuite.addTestSuite(TestDOMUtil.class);        
        
        //junit.config
        testSuite.addTestSuite(TestXMLConfigFile.class);
        testSuite.addTestSuite(TestConfiguration.class);

        //junit.pd.modules
        testSuite.addTestSuite(TestDigest.class);
        testSuite.addTestSuite(TestHexValueListFuzzer.class);
        testSuite.addTestSuite(TestNumberRangeFuzzer.class);
        testSuite.addTestSuite(TestResponseCatcher.class);
        testSuite.addTestSuite(TestStringExpansionFuzzer.class);
        testSuite.addTestSuite(TestStringMutationFuzzer.class);
        testSuite.addTestSuite(TestValueListFuzzer.class);
        testSuite.addTestSuite(TestConfigValue.class);
        testSuite.addTestSuite(TestContentLength.class);
        testSuite.addTestSuite(TestLinePermutator.class);
        
        //junit.persistence
        testSuite.addTestSuite(TestTestCaseParser.class);
        testSuite.addTestSuite(TestTestCaseRefParser.class);        
        
        //junit.testexec
        testSuite.addTestSuite(TestRequestHandler.class);
        testSuite.addTestSuite(TestResponseHandler.class);
        testSuite.addTestSuite(TestTestExecutionHandler.class);
        testSuite.addTestSuite(TestVariableHandler.class);
        testSuite.addTestSuite(TestVariableReplacer.class);
        
        //junit.testexec.history
        testSuite.addTestSuite(TestTestCaseRun.class);
        testSuite.addTestSuite(TestTestCaseRunHandler.class);
        testSuite.addTestSuite(TestWarningLog.class);
        
        return testSuite;
    }

    public static void main( String[] args ) {
        TestRunner.run(RunTestCaseTests.class);
    }

}
