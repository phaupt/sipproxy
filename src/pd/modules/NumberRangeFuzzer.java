package pd.modules;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import testexec.IVariableReplacer;
import util.SimpleLogger;



public class NumberRangeFuzzer implements IModule {
    private long min;
    private long max;
    private long modulo;
    
    private Random random;
    
    public NumberRangeFuzzer(long minimum, long maximum) {
        this.min = minimum;
        this.max = maximum;
        
        //check if max is less than min
        if(min > max){
            throw new IllegalArgumentException("Minimum value must be less than the maximum value !");
        }

        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e) {
            //if initialization of SecureRandom class failes, create Random instance
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
 
            random = new Random();
        }
        
        //calculate modulo
        modulo = max - min + 1;
        
    }
    
    long getRandomValueInRange(){
        //calculate random value in range, abs of random value is necessary because
        //also negative are returned:
        //min + (negative value) --> out of range
        
        return min + (Math.abs(random.nextLong()) % modulo);
    }
    
    public String getValue(IVariableReplacer variableReplacer) {
        return new Long(getRandomValueInRange()).toString();
    }

}
