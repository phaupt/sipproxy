package junit.pd.modules;

import pd.modules.LinePermutator;

public class TestLinePermutator extends junit.framework.TestCase {
    
    private String message =
        "1REGISTER sip:192.168.0.18 SIP/2.0\r\n"
        + "2Via: SIP/2.0/UDP 192.168.0.15:5060;rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
        + "3From: testclient <sip:343434@192.168.0.18>;tag=3779692192\r\n"
        + "4To: testclient <sip:343434@192.168.0.18>\r\n"
        + "5Contact: \"testclient\" <sip:343434@192.168.0.15:5060>\r\n"
        + "6Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@192.168.0.18\r\n"
        + "7CSeq: 27427 REGISTER\r\n"
        + "8Expires: 1800\r\n"
        + "9Max-Forwards: 70\r\n"
        + "10User-Agent: X-Lite release 1105x\r\n"
        + "11Content-Length: 999\r\n";
    
    private LinePermutator perm2_5;
    
    public void testValidLength(){
        perm2_5 = new LinePermutator(2,5);
        String shuffledMessage = perm2_5.getMessage(message);
        assertEquals(458,shuffledMessage.length());
        assertNotSame(message,shuffledMessage);
    }  

}
