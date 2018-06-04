package junit.pd.modules;

import pd.modules.HexValueListFuzzer;

public class TestHexValueListFuzzer extends junit.framework.TestCase {
    
    private HexValueListFuzzer valueListFuzzer;
    
    public void testValueListFuzzer1A(){
        try {
            valueListFuzzer = new HexValueListFuzzer("src/junit/pd/modules/hexValueList.txt",false);
        }
        catch (IllegalArgumentException e){
            fail(e.getMessage());
        }
        
        assertNotNull(valueListFuzzer);
        assertEquals("ABCDE",valueListFuzzer.getValue(null));
        assertEquals("+",valueListFuzzer.getValue(null));
        assertEquals("@@@",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
    }
    
    public void testValueListFuzzer1B(){
        try {
            valueListFuzzer = new HexValueListFuzzer("src/junit/pd/modules/hexValueList.txt",true);
        }
        catch (IllegalArgumentException e){
            fail(e.getMessage());
        }
        
        assertNotNull(valueListFuzzer);
        //cycle 1
        assertNotNull(valueListFuzzer);
        assertEquals("ABCDE",valueListFuzzer.getValue(null));
        assertEquals("+",valueListFuzzer.getValue(null));
        assertEquals("@@@",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        //cycle 2
        assertNotNull(valueListFuzzer);
        assertEquals("ABCDE",valueListFuzzer.getValue(null));
        assertEquals("+",valueListFuzzer.getValue(null));
        assertEquals("@@@",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        //cycle 3
        assertNotNull(valueListFuzzer);
        assertEquals("ABCDE",valueListFuzzer.getValue(null));
        assertEquals("+",valueListFuzzer.getValue(null));
        assertEquals("@@@",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
    }
    
}
