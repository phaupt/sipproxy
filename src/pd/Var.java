package pd;

import pd.modules.IModule;
import testexec.IVariableReplacer;

public class Var implements IVar {
	private String name;
	protected IModule module;
    
    public Var(String name, IModule module){
        this.name = name;
        this.module = module;
    }

	public String getValue(IVariableReplacer variableReplacer) {
        String value = null;
        try{
            value = module.getValue(variableReplacer);
        }
        catch (OutOfMemoryError e){
            throw new OutOfMemoryError("Value of variable /'" + name + "'/ run out of memory\r\n");
        }
		return value;
	}
    
    public IModule getModule(){
        return module;
    }

	public String getName() {
		return name;
	}

}
