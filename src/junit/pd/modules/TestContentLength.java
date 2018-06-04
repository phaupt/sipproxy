package junit.pd.modules;

import pd.modules.ContentLength;;

public class TestContentLength extends junit.framework.TestCase {
    
    //Missing "\r\n"-Line at the end of the SIP message
    private String messageError1 = 
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
      + "Content-Length: ?\r\n";
    
    //Missing Content-Length Header
    private String messageError2 = 
        "REGISTER sip:192.168.0.18 SIP/2.0\r\n"
      + "Via: SIP/2.0/UDP 192.168.0.15:5060;rport;branch=z9hG4bK297DEC382B8D4F99ACE64DAF8A83F16F\r\n"
      + "From: testclient <sip:343434@192.168.0.18>;tag=3779692192\r\n"
      + "To: testclient <sip:343434@192.168.0.18>\r\n"
      + "Contact: \"testclient\" <sip:343434@192.168.0.15:5060>\r\n"
      + "Call-ID: 85E8F6630642436AA7FE49BEEAD3CE7F@192.168.0.18\r\n"
      + "CSeq: 27427 REGISTER\r\n"
      + "Expires: 1800\r\n"
      + "Max-Forwards: 70\r\n"
      + "User-Agent: X-Lite release 1105x\r\n";
    
    private String message0_input = 
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
        + "Content-Length: 999\r\n"
        + "\r\n";
    
    private String message0_output = 
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
      + "\r\n";

    private String message310_input = 
         "INVITE sip:*98@192.168.0.19 SIP/2.0\r\n"
       + "Via: SIP/2.0/UDP 192.168.0.19:5060;rport;branch=z9hG4bK53284\r\n"
       + "To: <sip:*98@192.168.0.18>\r\n"
       + "From: \"2222\" <sip:2222@192.168.0.18>;tag=2072\r\n"
       + "Call-ID: 1164613149-3284-PINFLA19@192.168.0.18\r\n"
       + "CSeq: 104 INVITE\r\n"
       + "Max-Forwards: 20\r\n"
       + "User-Agent: Express Talk 2.02\r\n"
       + "Contact: <sip:2222@192.168.0.19:5060>\r\n"
       + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, INFO, REFER, NOTIFY\r\n"
       + "Supported: replaces\r\n"
       + "Content-Type: application/sdp\r\n"
       + "Content-Length: 999\r\n"
       + "\r\n"
       + "v=0\r\n"
       + "o=- 825966733 825966735 IN IP4 192.168.0.19\r\n"
       + "s=Express Talk\r\n"
       + "c=IN IP4 192.168.0.19\r\n"
       + "t=0 0\r\n"
       + "m=audio 8000 RTP/AVP 0 8 96 3 13 101\r\n"
       + "a=rtpmap:0 PCMU/8000\r\n"
       + "a=rtpmap:8 PCMA/8000\r\n"
       + "a=rtpmap:96 G726-32/8000\r\n"
       + "a=rtpmap:3 GSM/8000\r\n"
       + "a=rtpmap:13 CN/8000\r\n"
       + "a=rtpmap:101 telephone-event/8000\r\n"
       + "a=fmtp:101 0-16\r\n"
       + "a=sendrecv\r\n";
    
    private String message310_output = 
        "INVITE sip:*98@192.168.0.19 SIP/2.0\r\n"
      + "Via: SIP/2.0/UDP 192.168.0.19:5060;rport;branch=z9hG4bK53284\r\n"
      + "To: <sip:*98@192.168.0.18>\r\n"
      + "From: \"2222\" <sip:2222@192.168.0.18>;tag=2072\r\n"
      + "Call-ID: 1164613149-3284-PINFLA19@192.168.0.18\r\n"
      + "CSeq: 104 INVITE\r\n"
      + "Max-Forwards: 20\r\n"
      + "User-Agent: Express Talk 2.02\r\n"
      + "Contact: <sip:2222@192.168.0.19:5060>\r\n"
      + "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, INFO, REFER, NOTIFY\r\n"
      + "Supported: replaces\r\n"
      + "Content-Type: application/sdp\r\n"
      + "Content-Length: 310\r\n"
      + "\r\n"
      + "v=0\r\n"
      + "o=- 825966733 825966735 IN IP4 192.168.0.19\r\n"
      + "s=Express Talk\r\n"
      + "c=IN IP4 192.168.0.19\r\n"
      + "t=0 0\r\n"
      + "m=audio 8000 RTP/AVP 0 8 96 3 13 101\r\n"
      + "a=rtpmap:0 PCMU/8000\r\n"
      + "a=rtpmap:8 PCMA/8000\r\n"
      + "a=rtpmap:96 G726-32/8000\r\n"
      + "a=rtpmap:3 GSM/8000\r\n"
      + "a=rtpmap:13 CN/8000\r\n"
      + "a=rtpmap:101 telephone-event/8000\r\n"
      + "a=fmtp:101 0-16\r\n"
      + "a=sendrecv\r\n";
    
    private  ContentLength clength;

    public TestContentLength(){
        clength = new ContentLength();
    }
    
    public void testValidLength(){
        assertEquals(message0_output,clength.getMessage(message0_input));
        assertEquals(message310_output,clength.getMessage(message310_input));
    }  
    
    public void testInvalidLength(){
        assertEquals(messageError1,clength.getMessage(messageError1));
        assertEquals(messageError2,clength.getMessage(messageError2));
    }
    

}
