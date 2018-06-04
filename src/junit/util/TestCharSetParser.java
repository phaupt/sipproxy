package junit.util;

import util.CharSetParser;
import junit.framework.TestCase;

public class TestCharSetParser extends TestCase {

    private String testData1A = "0x41-0x44,0x23,0x26,0x45-0x47";
    private char[] testData1B = {'A','B','C','D','#','&','E','F','G'}; 
    private String testData2A = "0x23,0x41-0x44,0x26,0x45-0x47";
    private char[] testData2B = {'#','A','B','C','D','&','E','F','G'};
    private String testData3A = "0x41-0x44,0x23,0x45-0x47,0x26";
    private char[] testData3B = {'A','B','C','D','#','E','F','G','&'};
    private String testData4A = "0x5C";
    private char[] testData4B = {'\\'};
    private String testData5A = "A-C,!,\\,,0-3,:-<,@,\\-,\\\\,\\,";
    private char[] testData5B = {'A','B','C','!',',','0','1','2','3',':',';','<','@','-','\\',','};
    private String testData6A = "A,!,\\,,0-3,:-<,@,\\-,\\\\";
    private char[] testData6B = {'A','!',',','0','1','2','3',':',';','<','@','-','\\'};
    private String testData7A = "!,\\,,0-3,:-<,@,\\-";
    private char[] testData7B = {'!',',','0','1','2','3',':',';','<','@','-'};
    private String testData8A = "!,\\,,0-3,:-<,@,\\-,X";
    private char[] testData8B = {'!',',','0','1','2','3',':',';','<','@','-','X'};
    private String testData9A = "!";
    private char[] testData9B = {'!'};
    private String testData10A = "\\\\";
    private char[] testData10B = {'\\'};
    
    private CharSetParser parser;
    
    public void testCharSet() {      
        parser = new CharSetParser();
        compareContent(testData1B,parser.getCharArray(testData1A));
        compareContent(testData2B,parser.getCharArray(testData2A));
        compareContent(testData3B,parser.getCharArray(testData3A));
        compareContent(testData4B,parser.getCharArray(testData4A));
        compareContent(testData5B,parser.getCharArray(testData5A));
        compareContent(testData6B,parser.getCharArray(testData6A));
        compareContent(testData7B,parser.getCharArray(testData7A));
        compareContent(testData8B,parser.getCharArray(testData8A));
        compareContent(testData9B,parser.getCharArray(testData9A));
        compareContent(testData10B,parser.getCharArray(testData10A));
    }
    
    private void compareContent(char[] expected, char[] returned){
        
        assertEquals (expected.length,returned.length);
        
        for (int i = 0; i < expected.length; i++){
            assertEquals(expected[i],returned[i]);
        }
        
    }
    
}
