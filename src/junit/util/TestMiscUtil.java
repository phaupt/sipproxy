package junit.util;

import proxy.UDPDatagram;
import util.MiscUtil;
import junit.framework.TestCase;

public class TestMiscUtil extends TestCase {

    public void testCSeq() {
        String testCSeq = "13 REGISTER";
        String data = "blabla\r\n" + "CSeq: " + testCSeq + "\r\n" + "blabla";
        UDPDatagram packet = new UDPDatagram(null, 1, null, -1, data.getBytes());
        String cSeq = MiscUtil.getCSeq(packet);
        assertEquals(testCSeq, cSeq);
    }
}
