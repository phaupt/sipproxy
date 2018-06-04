package pd.modules;

import testexec.IVariableReplacer;



public class MockResponseCatcher implements IModule {
    
    public String regex;
    public int catchGroupIndex;

    public MockResponseCatcher(String regex, int catchGroupIndex) {
        this.regex = regex;
        this.catchGroupIndex = catchGroupIndex;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        return null;
    }

}
