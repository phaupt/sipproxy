package junit.util;

import util.DomUtil;
import junit.framework.TestCase;


public class TestDOMUtil extends TestCase {
    public void testEncoding(){

        StringBuffer content = new StringBuffer();
        StringBuffer expectedEncoding = new StringBuffer();
        
        for(char c : DomUtil.unsupportedChars){
            content.append("char:" + c + c + " " + c);
            expectedEncoding.append("char:" + DomUtil.escapeSequence + ((int) c) + DomUtil.escapeSequence+ 
                                              DomUtil.escapeSequence + ((int) c) + DomUtil.escapeSequence  + 
                                              " " + DomUtil.escapeSequence + ((int) c) + DomUtil.escapeSequence );        
        }
        String encoding = DomUtil.encodeUnsuportedChars(content.toString());
        assertEquals(expectedEncoding.toString(), encoding);
        
        String decoding = DomUtil.decodeUnsuportedChars(encoding);
        assertEquals(content.toString(), decoding);
    }
}
