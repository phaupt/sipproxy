package pd.modules;

import java.util.Vector;

import testexec.IVariableReplacer;



public class MockValueListFuzzer implements IModule{
    
    public String filepath;
    public Vector<String> valueList;
    public boolean repeatList;
    
    public MockValueListFuzzer(String filepath, boolean repeatList) {
        this.filepath = filepath;
        this.repeatList = repeatList;
    }
    
    public MockValueListFuzzer(Vector<String> valueList, boolean repeatList) {
        this.valueList = valueList;
        this.repeatList = repeatList;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }

}
