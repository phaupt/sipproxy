package pd.modules;

import testexec.IVariableReplacer;

public class ClearText implements IModule {
    
    private String value;
    
    public ClearText(String value) {
        this.value = value;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        String value = this.value;
        if (variableReplacer != null){
            value = variableReplacer.replaceVariables(value);
        }
        return value;
    }

}
