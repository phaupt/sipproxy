package junit.config;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import config.Configuration;
import config.transformation.TransformationConfig;
import junit.config.transformation.TestXMLTransformationConfigFile;
import junit.framework.TestCase;

public class TestConfiguration extends TestCase {

    //ProxyMode
    InetAddress proxySocketIP;
    int proxySocketPort = 5060;
    InetAddress pbxIP;;
    int pbxPort = 5061;
    InetAddress clientIP;
    int clientPort = 5062;
    String fileRef = "testTConfig.xml";
    String dynFileRef = "testDynamicTransformationConfig.xml";
    
    //TestCaseMode
    InetAddress testCaseSocketIP;
    int testCaseSocketPort = 5062;
    InetAddress targetIP;
    int targetPort = 5062;
    String testCaseDir = "testCases";
    
    
    public void setUp() {
        try {
            proxySocketIP = InetAddress.getByName("192.168.0.18");
            pbxIP = InetAddress.getByName("192.168.0.10");
            clientIP = InetAddress.getByName("192.168.0.15");
            
            TestXMLTransformationConfigFile.writeToFile(
                    TestXMLTransformationConfigFile.testData, fileRef);
            
            
            testCaseSocketIP = InetAddress.getByName("192.168.0.18");
            targetIP = InetAddress.getByName("192.168.0.111");

        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void tearDown() {
        new File(fileRef).delete();
    }

    public void testSettersAndGetters() {
        Configuration config = new Configuration();
        //Proxy Mode
        config.setProxySocketIP(proxySocketIP);
        config.setProxySocketPort(proxySocketPort);
        config.setPbxIP(pbxIP);
        config.setPbxPort(pbxPort);
        config.setClientIP(clientIP);
        config.setClientPort(clientPort);
        config.setTransormationConfigFileRef(fileRef);
        
        assertEquals(proxySocketIP, config.getProxySocketIP());
        assertEquals(proxySocketPort, config.getProxySocketPort());
        assertEquals(pbxIP, config.getPbxIP());
        assertEquals(pbxPort, config.getPbxPort());
        assertEquals(fileRef, config.getTransformationConfigFileRef());
      
        //TestCaseMode
        config.setTestCaseSocketIP(testCaseSocketIP);
        config.setTestCaseSocketPort(testCaseSocketPort);
        config.setTargetIP(targetIP);
        config.setTargetPort(targetPort);
        config.setTestCaseDirPath(testCaseDir);
       
        assertEquals(testCaseSocketIP, config.getTestCaseSocketIP());
        assertEquals(testCaseSocketPort, config.getTestCaseSocketPort());
        assertEquals(targetIP, config.getTargetIP());
        assertEquals(targetPort, config.getTargetPort());
        assertEquals(testCaseDir, config.getTestCaseDirPath());
    }

    public void testTransformationConfig() {
        Configuration config = new Configuration(proxySocketIP, proxySocketPort, pbxIP, pbxPort,
                clientIP, clientPort, fileRef, dynFileRef,
                testCaseSocketIP, testCaseSocketPort, targetIP, targetPort, testCaseDir);
        TransformationConfig tConfig = config.getTransformationConfig();
        assertNotNull(tConfig);
        assertTrue(tConfig.getDefinitionsSize() >= 6); // 6 aufgrund 6 Definitions aus
                                                        // ConfigFile

    }
}
