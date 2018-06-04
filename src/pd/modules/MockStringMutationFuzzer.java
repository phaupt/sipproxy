package pd.modules;

import testexec.IVariableReplacer;



public class MockStringMutationFuzzer implements IModule {
    
    public int length;
    public char[] charSet;

    public MockStringMutationFuzzer(int length) {
        this.length = length;
    }
    
    public MockStringMutationFuzzer(int length, char[] charSet) {
        this.length = length;
        this.charSet = charSet;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }

}
