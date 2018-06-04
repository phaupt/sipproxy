package pd.modules;

import testexec.IVariableReplacer;
import config.Configuration;


public class MockConfigValue implements IModule {
    
    public Configuration config;
    public String paramName;

    public MockConfigValue(Configuration config, String paramName) {
        this.config = config;
        this.paramName = paramName;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }


}
