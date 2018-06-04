package pd.modules;

import pd.modules.StringMutationFuzzer;
import testexec.IVariableReplacer;

public class StringExpansionFuzzer implements IModule {
    
    private String fuzzedString = "";
    private String expansionString;
    private int increment;
    private char[] characterSet;

    public StringExpansionFuzzer(String expansionString, int increment) {
        this.increment = increment;
        if(increment < 1){
            throw new IllegalArgumentException("Increment must be a positive integer value !");
        }
        this.expansionString = expansionString;
    }
    
    public StringExpansionFuzzer(int increment) {
        this(increment, DEFAULT_CHARACTER_SET);
    }

    public StringExpansionFuzzer(int increment, char[] characterSet) {
        this.increment = increment;
        if(increment < 1){
            throw new IllegalArgumentException("Increment must be a positive integer value !");
        }
        this.characterSet = characterSet;
    }
    
    public String getValue(IVariableReplacer variableReplacer) {
        if (expansionString != null){
            //increment with a specific expansion string
            String tmpString = "";
            for(int i = 0; i < increment; i++){
                //Build expansion string
                tmpString += expansionString;
            }
            //Return new expanded string value
            return fuzzedString += tmpString;            
        } else {
            //increment with a random expansion string
            StringMutationFuzzer stringFuzzer = new StringMutationFuzzer(increment, characterSet);
            return fuzzedString += stringFuzzer.getValue(variableReplacer);
        }
    }

}
