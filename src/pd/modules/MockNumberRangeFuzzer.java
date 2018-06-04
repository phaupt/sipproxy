package pd.modules;

import testexec.IVariableReplacer;



public class MockNumberRangeFuzzer implements IModule {
    
    public long minimum;
    public long maximum;

    public MockNumberRangeFuzzer(long minimum, long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }

}
