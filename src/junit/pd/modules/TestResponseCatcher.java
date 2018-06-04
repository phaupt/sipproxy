package junit.pd.modules;

import pd.modules.ResponseCatcher;

public class TestResponseCatcher extends junit.framework.TestCase {
    
    private ResponseCatcher catcher;
    
    private String message = "SIP/2.0 401 Unauthorized\r\n"
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
        + "\r\n";
    
    private String regex = "(nonce=\")(.*)(\")";

    public TestResponseCatcher(){
    	catcher = new ResponseCatcher(regex,2);
        assertNotNull(catcher);
        catcher.setMessage(message);
    }
    
    public void testValue(){      
        assertEquals("30419c6c",catcher.getValue(null));
    }
}
