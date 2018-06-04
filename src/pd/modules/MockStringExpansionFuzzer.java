package pd.modules;

import testexec.IVariableReplacer;



public class MockStringExpansionFuzzer implements IModule {
    
    public String expansionString;
    public int increment;
    public char[] charSet;

    public MockStringExpansionFuzzer(String expansionString, int increment) {
        this.expansionString = expansionString;
        this.increment = increment;
    }
    
    public MockStringExpansionFuzzer(int increment) {
        this.increment = increment;
    }
    
    public MockStringExpansionFuzzer(int increment, char[] charSet) {
        this.increment = increment;
        this.charSet = charSet;
    }


    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }

}
