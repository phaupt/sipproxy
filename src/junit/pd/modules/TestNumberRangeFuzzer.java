package junit.pd.modules;

import pd.modules.NumberRangeFuzzer;
import junit.framework.TestCase;


public class TestNumberRangeFuzzer extends TestCase {
    NumberRangeFuzzer nrf;
    
    public void testArguments() throws Exception{
        long min = 10;
        long max = 10;
        try{
            new NumberRangeFuzzer(min, max);
            //ok
        }
        catch (IllegalArgumentException iae) {
            throw new Exception();
        }
        
        min = -1;
        max = -2;
        try{
            new NumberRangeFuzzer(min, max);
            throw new Exception();
        }
        catch (IllegalArgumentException iae) {
            //ok
        }
        
    }
    public void testSingleValue(){
        long min = 10;
        long max = 10;

        testValues(min, max, 5);
        
    }
    
    public void testNegativeRange(){
        long min = -10;
        long max = -3;

        testValues(min, max, 30);
        
    }
    
    public void testNegativeAndPositiveRange(){
        long min = -2;
        long max = 2;

        testValues(min, max, 15);
    }
    
    public void testNormalRange(){
        long min = 0;
        long max = 3;

        testValues(min, max, 15);
    }
    
    public void testIntegerBorder(){
        long min = (long) -Math.pow(2.0, 31.0) - 10;
        long max = (long) -Math.pow(2.0, 31.0) + 10;
        testValues(min, max, 30);
        
        min = (long) Math.pow(2.0, 31.0) - 10;
        max = (long) Math.pow(2.0, 31.0) + 10;
        testValues(min, max, 30);
        
    }
    
    private void testValues(long min, long max, int numberOfValues){
        nrf = new NumberRangeFuzzer(min, max);
        String[] sValues = new String[numberOfValues];
        
        for(int i = 0; i < numberOfValues; i++){
            sValues[i]=nrf.getValue(null);
        }
        
        testValues(sValues, min, max);
    }
    
    
    private void testValues(String[]sValues, long min, long max){
        for(String sValue : sValues){
            long value = Long.parseLong(sValue);
            assertTrue(min <= value && value <= max);
        }
    }
}
