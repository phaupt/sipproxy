package pd.modules;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import testexec.IVariableReplacer;
import util.SimpleLogger;

public class StringMutationFuzzer implements IModule {
    private int length;
    private char[] characterSet = null;
    private Random random;
    
    public StringMutationFuzzer(int length) {
        this(length, DEFAULT_CHARACTER_SET);
    }
    
    public StringMutationFuzzer(int length, char[] charSet){
        this.length = length;
        if(length < 1){
            throw new IllegalArgumentException("Length must be a positive integer value !");
        }
        if(charSet != null){
            this.characterSet = charSet;
        } else {
            characterSet = DEFAULT_CHARACTER_SET;
        }
        initialize();
    }
    
    private void initialize(){
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e) {
            //if initialization of SecureRandom class failes, create Random instance
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
 
            random = new Random();
        }
    }
    
    public String getValue(IVariableReplacer variableReplacer) {
        //create random string value
        StringBuffer bf = new StringBuffer();
        
        for(int i = 0; i < length; i ++){
            //append random character from character array to string buffer
            //get random character with random index in array
            int randomIndex = random.nextInt(characterSet.length);
            bf.append(characterSet[randomIndex]);
        }
        return bf.toString();
    }

}
