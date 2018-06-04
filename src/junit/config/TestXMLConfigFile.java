package junit.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import config.Configuration;
import config.XMLConfigFile;

import junit.config.transformation.TestXMLTransformationConfigFile;
import junit.framework.TestCase;

public class TestXMLConfigFile extends TestCase {
    
    //Proxy Mode
    String proxySocketIP = "192.168.0.18";
    int proxySocketPort = 5060;
    String pbxIP = "192.168.0.10";
    int pbxPort = 5061;
    String clientIP = "192.168.0.15";
    int clientPort = 5062;

    String testFilename = "testconfiguration.xml";
    String testSavedFile = "testSavedFile.xml";
    String fileRef = "testTconfiguration.xml";
    String dynamicTransFileref = "testDynamicTransformationConfig.xml";

    //TestCase Mode
    String testCaseSocketIP = "192.168.0.20";
    int testCaseSocketPort = 5062;
    String targetIP = "192.168.0.21";
    int targetPort = 5060;
    String testCaseDirPath = "testCasesDir/tests";
    
    String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"+
                      "<SIPProxyConfig>\r\n" +
                          "<ProxyMode>\r\n"+
                              "<ProxySocket ip=\"" + proxySocketIP + "\" port=\"" + proxySocketPort + "\"/>\r\n" +
                              "<PBX ip=\"" + pbxIP + "\" port=\"" + pbxPort + "\"/>\r\n" + 
                              "<Client ip=\"" + clientIP + "\" port=\"" + clientPort + "\"/>\r\n" + 
                              "<Transformation fileRef=\"" + fileRef + "\"/>\r\n" + 
                              "<DynamicTransformation fileRef=\"" + dynamicTransFileref + "\"/>\r\n" +
                          "</ProxyMode>\r\n" +
                          "<TestCaseMode>" +
                              "<TestCaseSocket ip=\"" + testCaseSocketIP + "\" port=\"" + testCaseSocketPort + "\"/>\r\n" +
                              "<Target ip=\"" + targetIP + "\" port=\"" + targetPort + "\"/>\r\n" +
                              "<TestCaseDir path=\"" + testCaseDirPath + "\"/>\r\n" +
                          "</TestCaseMode>" +
                      "</SIPProxyConfig>";

    public void setUp() {
        writeToFile(TestXMLTransformationConfigFile.testData, fileRef);
        writeToFile(testData, testFilename);
    }

    public void tearDown() {
        new File(testFilename).delete();
        new File(testSavedFile).delete();
        new File(fileRef).delete();
    }

    public void testLoad() {
        Configuration config = XMLConfigFile.load(testFilename);
        try {
            //Proxy Mode
            InetAddress proxySocketIP = InetAddress.getByName(this.proxySocketIP);
            assertEquals(proxySocketIP, config.getProxySocketIP());
            assertEquals(proxySocketPort, config.getProxySocketPort());
            
            InetAddress pbxIP = InetAddress.getByName(this.pbxIP);
            assertEquals(pbxIP, config.getPbxIP());
            assertEquals(pbxPort, config.getPbxPort());

            assertEquals(fileRef, config.getTransformationConfigFileRef());
            assertNotNull(config.getTransformationConfig());
            assertEquals(dynamicTransFileref, config
                    .getDynamicTransformationConfigFileRef());
            assertNotNull(config.getDynamicTransformationConfig());

            //TestCase Mode
            InetAddress testCaseSocketIP = InetAddress.getByName(this.testCaseSocketIP);
            assertEquals(testCaseSocketIP, config.getTestCaseSocketIP());
            assertEquals(testCaseSocketPort, config.getTestCaseSocketPort());
            
            InetAddress targetIP = InetAddress.getByName(this.targetIP);
            assertEquals(targetIP, config.getTargetIP());
            assertEquals(targetPort, config.getTargetPort());
            
            assertEquals(testCaseDirPath, config.getTestCaseDirPath());
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testSave() {
        try {
            Configuration config = new Configuration(InetAddress.getByName(proxySocketIP),
                    proxySocketPort, InetAddress.getByName(pbxIP), pbxPort, InetAddress
                            .getByName(clientIP), clientPort, fileRef,dynamicTransFileref,
                            InetAddress.getByName(testCaseSocketIP), testCaseSocketPort,
                            InetAddress.getByName(targetIP), targetPort, testCaseDirPath);
            XMLConfigFile.save(config, testSavedFile);
            writeToFile(testData, testFilename);

            compareContent(testFilename, testSavedFile);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile( String data, String filename ) {
        BufferedWriter resWriter;
        try {
            resWriter = new BufferedWriter(new FileWriter(new File(filename), false));
            resWriter.write(data);
            resWriter.flush();
            resWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void compareContent( String file1, String file2 ) {
        File f1 = new File(file1);
        File f2 = new File(file2);
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            try {
                assertTrue(br1.read() == br2.read());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
