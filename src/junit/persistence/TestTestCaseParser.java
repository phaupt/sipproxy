package junit.persistence;

import java.io.File;

import persistence.MockElementFactory;
import persistence.ParserException;
import persistence.TestCaseParser;
import persistence.ValidatorException;
import pd.ITestCase;
import pd.ResponseVar;
import config.Configuration;
import config.XMLConfigFile;
import pd.modules.*;

public class TestTestCaseParser extends junit.framework.TestCase {

    private Configuration config;
    private TestCaseParser parser;
    private ITestCase testCase = null;
    
    public TestTestCaseParser(){
    	config = XMLConfigFile.load("src/junit/persistence/testConfig.xml");
        parser = new TestCaseParser(new MockElementFactory(),config);
        try {
            //Load Example Test Case
            testCase = parser.getTestCase(new File("src/junit/persistence/SampleTestCaseSpecification.xml"));
        }
        catch (ParserException e) {
            fail(e.getMessage());          
        }
        catch (ValidatorException e) {
            fail(e.getMessage());  
        }  
    }
    
    public void testParsing(){      
        assertNotNull(testCase);
    }

    public void testTestCase() {      
        //Check name of test case
        assertEquals("SampleTestCaseSpecification",testCase.getName());
        assertEquals(10,testCase.getCycles());
        assertEquals(1,testCase.getInitialRequestMessageID());
    }
    
    public void testAttributesAndVariables(){
        
        assertEquals(9,testCase.getVars().size());
        
        assertEquals(MockConfigValue.class,testCase.getVar("AsteriskIPAddress").getModule().getClass());
        assertEquals("PbxIP",((MockConfigValue)testCase.getVar("AsteriskIPAddress").getModule()).paramName);
        
        assertEquals(ClearText.class,testCase.getVar("TargetNr").getModule().getClass());
        assertEquals("121212",((ClearText)testCase.getVar("TargetNr").getModule()).getValue(null));
        
        assertEquals(MockNumberRangeFuzzer.class,testCase.getVar("numberRangeFuzzer").getModule().getClass());
        assertEquals(0,((MockNumberRangeFuzzer)testCase.getVar("numberRangeFuzzer").getModule()).minimum);
        assertEquals(1000,((MockNumberRangeFuzzer)testCase.getVar("numberRangeFuzzer").getModule()).maximum);
        
        assertEquals(MockStringMutationFuzzer.class,testCase.getVar("stringMutationFuzzer").getModule().getClass());
        assertEquals(10,((MockStringMutationFuzzer)testCase.getVar("stringMutationFuzzer").getModule()).length);
        
        char[] inputArray = {'a','b','c','!','#','-'};
        
        //Check character set of string mutation fuzzer module
        char[] outputArray1 = ((MockStringMutationFuzzer)testCase.getVar("stringMutationFuzzer").getModule()).charSet;
        assertEquals(inputArray.length, outputArray1.length);       
        for (int i = 0; i < outputArray1.length; i++){
            assertEquals(inputArray[i],outputArray1[i]);
        }
        
        assertEquals(MockStringExpansionFuzzer.class,testCase.getVar("stringExpansionFuzzer1").getModule().getClass());
        assertEquals(5,((MockStringExpansionFuzzer)testCase.getVar("stringExpansionFuzzer1").getModule()).increment);
        assertEquals("abcd",((MockStringExpansionFuzzer)testCase.getVar("stringExpansionFuzzer1").getModule()).expansionString);

        //Check character set of string mutation expansion module
        assertEquals(MockStringExpansionFuzzer.class,testCase.getVar("stringExpansionFuzzer2").getModule().getClass());
        assertEquals(6,((MockStringExpansionFuzzer)testCase.getVar("stringExpansionFuzzer2").getModule()).increment);
        char[] outputArray2 = ((MockStringExpansionFuzzer)testCase.getVar("stringExpansionFuzzer2").getModule()).charSet;
        assertEquals(inputArray.length, outputArray2.length);       
        for (int i = 0; i < outputArray2.length; i++){
            assertEquals(inputArray[i],outputArray2[i]);
        }

        
        assertEquals(MockValueListFuzzer.class,testCase.getVar("valueListFuzzer").getModule().getClass());
        assertEquals("valueList.txt",((MockValueListFuzzer)testCase.getVar("valueListFuzzer").getModule()).filepath);
        assertEquals(false,((MockValueListFuzzer)testCase.getVar("valueListFuzzer").getModule()).repeatList);

        assertEquals(MockHexValueListFuzzer.class,testCase.getVar("hexValueListFuzzer").getModule().getClass());
        assertEquals("hexValueList.txt",((MockHexValueListFuzzer)testCase.getVar("hexValueListFuzzer").getModule()).filepath);
        assertEquals(false,((MockHexValueListFuzzer)testCase.getVar("hexValueListFuzzer").getModule()).repeatList);

        assertEquals(MockDigest.class,testCase.getVar("digest").getModule().getClass());
        assertEquals("alice",((MockDigest)testCase.getVar("digest").getModule()).username);
        assertEquals("123456",((MockDigest)testCase.getVar("digest").getModule()).secret);
        assertEquals("asterisk",((MockDigest)testCase.getVar("digest").getModule()).realm);
        assertEquals("ABABAB",((MockDigest)testCase.getVar("digest").getModule()).nonce);
        assertEquals("biloxi.com",((MockDigest)testCase.getVar("digest").getModule()).uri);
        assertEquals("REGISTER",((MockDigest)testCase.getVar("digest").getModule()).method);
        assertEquals("AUTH-INT",((MockDigest)testCase.getVar("digest").getModule()).qop);
        assertEquals("CDCDCD",((MockDigest)testCase.getVar("digest").getModule()).noncecount);
        assertEquals("DEDEDE",((MockDigest)testCase.getVar("digest").getModule()).cnonce);
        assertEquals("EFEFEF",((MockDigest)testCase.getVar("digest").getModule()).body);       
    }
    
    public void testRequestMessages() {
        assertEquals(2,testCase.getRequests().size());
        
        assertEquals(
                "REGISTER sip:192.168.0.18 SIP/2.0\r\n"
                + "Via: SIP/2.0/UDP 192.168.0.15:5060;rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
                + "From: testclient <sip:343434@192.168.0.18>;tag=3779692192\r\n"
                + "To: testclient <sip:343434@192.168.0.18>\r\n"
                + "Contact: \"testclient\" <sip:343434@192.168.0.15:5060>\r\n"
                + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@192.168.0.18\r\n"
                + "CSeq: 27427 REGISTER\r\n"
                + "Expires: 1800\r\n"
                + "Max-Forwards: 70\r\n"
                + "User-Agent: X-Lite release 1105x\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n",
                testCase.getRequest(1).getContent()
                );
        
        assertEquals(
                "SIP/2.0 401 Unauthorized\r\n"
                + "Via: SIP/2.0/UDP 192.168.0.18:5060;rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F;received=192.168.0.18\r\n"
                + "From: testclient <sip:343434@192.168.0.10>;tag=3779692192\r\n"
                + "To: testclient <sip:343434@192.168.0.10>;tag=as5eaff1ef\r\n"
                + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@192.168.0.10\r\n"
                + "CSeq: 27427 REGISTER\r\n"
                + "User-Agent: Asterisk PBX\r\n"
                + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY\r\n"
                + "Contact: <sip:343434@192.168.0.10>\r\n"
                + "WWW-Authenticate: Digest realm=\"asterisk\", nonce=\"30419c6c\"\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n",
                testCase.getRequest(2).getContent()
                );
        
        assertNull(testCase.getRequest(1).getTimeout());
        
        assertEquals(1000,testCase.getRequest(2).getTimeout().getTimeInMilliseconds());
        assertEquals("uups",testCase.getRequest(2).getTimeout().getMessage());
    }
    
    public void testResponse(){
        assertEquals(2,testCase.getRequest(1).getResponses().size(),2);
        assertEquals(1,testCase.getRequest(2).getResponses().size(),1);
        
        assertEquals("okay",testCase.getRequest(1).getResponses().get(0).getCategory());
        assertEquals(2,testCase.getRequest(1).getResponses().get(0).getFollowingRequestID());
        assertEquals(10000,testCase.getRequest(1).getResponses().get(0).getWaitingTimeInMilliseconds());
        assertEquals("SIP/2.0 (401|407)(.)*",testCase.getRequest(1).getResponses().get(0).getRegex());
        assertEquals(1,testCase.getRequest(1).getResponses().get(0).getResponseVars().size());
        
        assertEquals(ResponseVar.class,testCase.getRequest(1).getResponses().get(0).getResponseVars().get(0).getClass());
        assertEquals("nonce",testCase.getRequest(1).getResponses().get(0).getResponseVars().get(0).getName());
        assertEquals(2,((MockResponseCatcher)testCase.getRequest(1).getResponses().get(0).getResponseVars().get(0).getModule()).catchGroupIndex);
        assertEquals("(nonce=\")(.*)(\")",((MockResponseCatcher)testCase.getRequest(1).getResponses().get(0).getResponseVars().get(0).getModule()).regex);
        
        assertEquals("warning",testCase.getRequest(1).getResponses().get(1).getCategory());
        assertEquals("SIP/2.0 200 Ok",testCase.getRequest(1).getResponses().get(1).getRegex());
        
        assertEquals("warning",testCase.getRequest(2).getResponses().get(0).getCategory());
        assertEquals("SIP/2.0 401",testCase.getRequest(2).getResponses().get(0).getRegex());       
    }

}
