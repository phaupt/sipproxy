package junit;

import junit.config.TestConfiguration;
import junit.config.TestXMLConfigFile;
import junit.config.transformation.TestXMLTransformationConfigFile;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;
import junit.proxy.*;
import junit.transforming.*;
import junit.ui.history.nodes.TestHistoryCompositeTreeNode;
import junit.ui.history.nodes.TestMessageTreeNode;
import junit.util.TestMiscUtil;

public class RunProxyTests {

    public static Test suite() {

        //EXECUTE ALL JUNIT TESTS !

        TestSuite testSuite = new TestSuite();

        //junit.config
        testSuite.addTestSuite(TestConfiguration.class);
        testSuite.addTestSuite(TestXMLConfigFile.class);

        //junit.config.transformation
        testSuite.addTestSuite(TestXMLTransformationConfigFile.class);

        //junit.proxy
        testSuite.addTestSuite(TestMessageHandler.class);
        testSuite.addTestSuite(TestMessageListener.class);
        testSuite.addTestSuite(TestMessageSender.class);
        testSuite.addTestSuite(TestPacketQueue.class);
        testSuite.addTestSuite(TestSIPHistory.class);

        //junit.transforming
        testSuite.addTestSuite(TestPacketRegexTransformer.class);

        //junit.ui.history.nodes
        testSuite.addTestSuite(TestHistoryCompositeTreeNode.class);
        testSuite.addTestSuite(TestMessageTreeNode.class);

        //junit.util
        testSuite.addTestSuite(TestMiscUtil.class);

        return testSuite;
    }

    public static void main( String[] args ) {
        TestRunner.run(RunProxyTests.class);
    }

}
