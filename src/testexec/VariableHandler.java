package testexec;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import pd.IVar;

public class VariableHandler implements IVariableHandler {
    private Map<String, String> variableMap;
    public VariableHandler(){
        variableMap = new Hashtable<String, String>();
    }
    public void addVariables(Vector<? extends IVar> variables){
        addVariables(variables, null);
    }
    public void addVariables( Vector<? extends IVar> variables, IVariableReplacer variableReplacer) {    
        //Add each Variable in Vector to Map.
        //Already defines Variables with same name will be replaced
        for(IVar var: variables){
            String name = var.getName();
            String value = var.getValue(variableReplacer);  
            variableMap.put(name, value); 
        }
    }

    public void clear() {
        variableMap.clear();

    }

    public String getVar( String name ) {
        return variableMap.get(name);
    }
}
