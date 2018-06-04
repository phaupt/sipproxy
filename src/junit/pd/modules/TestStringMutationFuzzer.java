package junit.pd.modules;

import pd.modules.IModule;
import pd.modules.StringMutationFuzzer;
import junit.framework.TestCase;


public class TestStringMutationFuzzer extends TestCase {
    StringMutationFuzzer smf;
    
    public void testInvalidLength() throws Exception{
        try{
            smf = new StringMutationFuzzer(0);
            throw new Exception();
        }
        catch(IllegalArgumentException iae){
            //ok, do nothing
        }
        try{
            smf = new StringMutationFuzzer(-1);
            throw new Exception();
        }
        catch(IllegalArgumentException iae){
            //ok, do nothing
        }        
    }
    public void testSmallString(){
        int length = 3;
        int numberOfValues = 20;
        
        testValues(length, numberOfValues);
    }
    
    public void testNormalString(){
        int length = 10;
        int numberOfValues = 20;
        
        testValues(length, numberOfValues);
    }

    public void testBigString(){
        int length = 50;
        int numberOfValues = 20;
        
        testValues(length, numberOfValues);
    }

    public void testHugeString(){
        int length = 500;
        int numberOfValues = 20;
        
        testValues(length, numberOfValues);
    }
    
    private void testValues(int length, int numberOfValues){
        smf = new StringMutationFuzzer(length);
        
        String[] sValues = new String[numberOfValues];
        for(int i = 0; i < numberOfValues; i++){
            sValues[i] = smf.getValue(null);
            assertEquals(length, sValues[i].length());
        }
        
        testStringValues(sValues, IModule.DEFAULT_CHARACTER_SET);
    }

    private void testStringValues( String[] values, char[] charArray ) {
        //Check if values consists of correct characters
        
        for(String value : values){
            for(int i = 0; i < value.length(); i++){
                assertTrue(doesContain(charArray, value.charAt(i)));
            }
        }
    }
    
    public static boolean doesContain(char[] array, char c){
        //Methods checks if array contains character c
        boolean contains = false;
        int i = 0;
        
        while(!contains && i < array.length){
            contains = c == array[i];
            i++;
        }
        
        return contains;
    }
}

