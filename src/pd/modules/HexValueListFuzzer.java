package pd.modules;

import java.util.Vector;

public class HexValueListFuzzer extends ValueListFuzzer {

    public HexValueListFuzzer(String filepath, boolean repeatList) {
        super(filepath, repeatList);
        super.valueList = hexToStringVector(super.valueList);
    }
    
    public HexValueListFuzzer(Vector<String> valueList, boolean repeatList) {
        super(valueList, repeatList);
        super.valueList = hexToStringVector(valueList);
    }

    private Vector<String> hexToStringVector(Vector<String> valueList){
        Vector<String> newValueList = new Vector<String>(valueList.size());       
        for (String hexValue : valueList){
            //Convert all hex values of each list item to a ASCII character string
            newValueList.add( hexToString(hexValue) );
        }       
        return newValueList;       
    }
    
    private String hexToString(String value){
        //Convert all hex values of each list item to a ASCII character string
        if(value.length() == 0)
            return "";
        else{
            if (value.length() >= 4){
                //Example: "0x410x42" = 0x41 ('A') + 0x42 ('B') = "AB"
                String subStr = value.substring(0, 4);
                //Remove leading '0x' to convert the hex number to char: 0x41 -> 41 => 'A'
                try {
                    char c = (char)Integer.parseInt(subStr.substring(2), 16);
                    //Get rest of values (recursive) to build complete ASCII string
                    return c + hexToString(value.substring(4));
                } catch (Exception e){
                    throw new IllegalArgumentException("Hex value list contains wrong values !");   
                }
            } else {
                throw new IllegalArgumentException("Hex value list contains wrong values !");
            }            
        }
    }
}
