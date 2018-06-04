package testexec;

import java.util.Vector;

import pd.IVar;


public interface IVariableHandler {
    public void clear();
    public String getVar(String name);
    public void addVariables(Vector<? extends IVar> variables);
    public void addVariables(Vector<? extends IVar> variables, IVariableReplacer variableReplacer);
}
