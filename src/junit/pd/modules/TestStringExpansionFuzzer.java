package junit.pd.modules;

import pd.modules.StringExpansionFuzzer;

public class TestStringExpansionFuzzer extends junit.framework.TestCase {
    
    private StringExpansionFuzzer stringExpFuzzer;
    
    public void testABC(){      
        stringExpFuzzer = new StringExpansionFuzzer("ABC",3);
        
        assertEquals("ABCABCABC",stringExpFuzzer.getValue(null));
        assertEquals("ABCABCABCABCABCABC",stringExpFuzzer.getValue(null));
        assertEquals("ABCABCABCABCABCABCABCABCABC",stringExpFuzzer.getValue(null));
        assertEquals("ABCABCABCABCABCABCABCABCABCABCABCABC",stringExpFuzzer.getValue(null));
        
    }
    
    public void testCharacterSet(){
        int increment = 4;
        char[] characterSet = new char[]{'a','b',',','K'};
        stringExpFuzzer = new StringExpansionFuzzer(increment, characterSet);
        
        String v1 = stringExpFuzzer.getValue(null);
        String v2 = stringExpFuzzer.getValue(null);
        String v3 = stringExpFuzzer.getValue(null);
        String[] values = new String[]{v1,v2,v3};
        for(int i = 0; i < 3; i++){
            assertEquals((i+1) * increment, values[i].length());
            String value = values[i];
            //Check if value consists only of allowed characters
            for(int j = 0; j < value.length(); j++){
                assertTrue(TestStringMutationFuzzer.doesContain(characterSet, value.charAt(j)));
            }

        }
    }
    


    public void testIncrement(){
        stringExpFuzzer = new StringExpansionFuzzer(3);
        
        assertEquals(3,stringExpFuzzer.getValue(null).length());
        assertEquals(6,stringExpFuzzer.getValue(null).length());
        assertEquals(9,stringExpFuzzer.getValue(null).length());
        assertEquals(12,stringExpFuzzer.getValue(null).length());
    }  
}
