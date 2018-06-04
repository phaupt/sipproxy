package pd.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import testexec.IVariableReplacer;
import util.SimpleLogger;

public class ValueListFuzzer implements IModule {

    protected Vector<String> valueList;
    private boolean repeatList;
    private int index = 0;

    public ValueListFuzzer(Vector<String> valueList, boolean repeatList) {
        this.valueList = valueList;
        this.repeatList = repeatList;
    }

    public ValueListFuzzer(String filepath, boolean repeatList) { 
        this.valueList = getValuesFromFile(filepath);
        this.repeatList = repeatList;
    }
    private Vector<String> getValuesFromFile(String filepath){
        File file = new File(filepath);
        if (!file.exists()){
            //catch invalid filepath
            throw new IllegalArgumentException("Value list file not found !");
        }

        Vector<String> valueList = new Vector<String>();
        String value;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            while ((value = in.readLine()) != null) {
                valueList.add(value);
            }
            in.close();
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        return valueList;
    }
    public String getValue(IVariableReplacer variableReplacer) {
        if (repeatList){
            int tmp = index;
            //increment index and start over from the beginning if end-index has been reached
            index = ++index % valueList.size();
            return valueList.get(tmp);          
        } else {
            if (!valueList.isEmpty()) {
                //remove next value from the list
                return valueList.remove(0);
            }
            //value list empty
            return "";
        }       
    }

}
