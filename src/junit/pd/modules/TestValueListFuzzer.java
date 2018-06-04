package junit.pd.modules;

import java.util.Vector;

import pd.modules.ValueListFuzzer;

public class TestValueListFuzzer extends junit.framework.TestCase {
    
    private ValueListFuzzer valueListFuzzer;
    private Vector<String> valueList; 
    
    public TestValueListFuzzer(){
    	valueList = new Vector<String>();
        
        for (int i = 0; i < 5; i++){
            // fill in pseudo data
            valueList.add("Test Data " + (i+1));
        }
        
        assertEquals(5, valueList.size());
    }
    
    public void testValueListFuzzer1A(){
        try {
            valueListFuzzer = new ValueListFuzzer("src/junit/pd/modules/valueList.txt",false);
        }
        catch (IllegalArgumentException e){
            fail(e.getMessage());
        }
        
        assertNotNull(valueListFuzzer);
        assertEquals("127.0.0.1",valueListFuzzer.getValue(null));
        assertEquals("localhost",valueListFuzzer.getValue(null));
        assertEquals("localhost.localdomain",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
    }
    
    public void testValueListFuzzer1B(){
        try {
            valueListFuzzer = new ValueListFuzzer("src/junit/pd/modules/valueList.txt",true);
        }
        catch (IllegalArgumentException e){
            fail(e.getMessage());
        }
        
        assertNotNull(valueListFuzzer);
        //cycle 1
        assertEquals("127.0.0.1",valueListFuzzer.getValue(null));
        assertEquals("localhost",valueListFuzzer.getValue(null));
        assertEquals("localhost.localdomain",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        //cycle 2
        assertEquals("127.0.0.1",valueListFuzzer.getValue(null));
        assertEquals("localhost",valueListFuzzer.getValue(null));
        assertEquals("localhost.localdomain",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
        //cycle 3
        assertEquals("127.0.0.1",valueListFuzzer.getValue(null));
        assertEquals("localhost",valueListFuzzer.getValue(null));
        assertEquals("localhost.localdomain",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
    }
    
    public void testValueListFuzzer2A(){
        valueListFuzzer = new ValueListFuzzer(valueList, false);
        assertNotNull(valueListFuzzer); 
        assertEquals("Test Data 1",valueListFuzzer.getValue(null));
        assertEquals("Test Data 2",valueListFuzzer.getValue(null));
        assertEquals("Test Data 3",valueListFuzzer.getValue(null));
        assertEquals("Test Data 4",valueListFuzzer.getValue(null));
        assertEquals("Test Data 5",valueListFuzzer.getValue(null));
        assertEquals("",valueListFuzzer.getValue(null));
    }
    
    public void testValueListFuzzer2B(){
        valueListFuzzer = new ValueListFuzzer(valueList, true);
        assertNotNull(valueListFuzzer); 
        //cycle 1
        assertEquals("Test Data 1",valueListFuzzer.getValue(null));
        assertEquals("Test Data 2",valueListFuzzer.getValue(null));
        assertEquals("Test Data 3",valueListFuzzer.getValue(null));
        assertEquals("Test Data 4",valueListFuzzer.getValue(null));
        assertEquals("Test Data 5",valueListFuzzer.getValue(null));
        //cycle 2
        assertEquals("Test Data 1",valueListFuzzer.getValue(null));
        assertEquals("Test Data 2",valueListFuzzer.getValue(null));
        assertEquals("Test Data 3",valueListFuzzer.getValue(null));
        assertEquals("Test Data 4",valueListFuzzer.getValue(null));
        assertEquals("Test Data 5",valueListFuzzer.getValue(null));
        //cycle 3
        assertEquals("Test Data 1",valueListFuzzer.getValue(null));
        assertEquals("Test Data 2",valueListFuzzer.getValue(null));
        assertEquals("Test Data 3",valueListFuzzer.getValue(null));
        assertEquals("Test Data 4",valueListFuzzer.getValue(null));
        assertEquals("Test Data 5",valueListFuzzer.getValue(null));
    }
    
}
