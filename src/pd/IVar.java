package pd;

import pd.modules.IModule;
import testexec.IVariableReplacer;

public interface IVar {
    public abstract String getValue(IVariableReplacer variableReplacer);

    public abstract IModule getModule();

    public abstract String getName();

}