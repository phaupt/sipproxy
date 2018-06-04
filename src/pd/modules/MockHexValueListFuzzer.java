package pd.modules;

import java.util.Vector;



public class MockHexValueListFuzzer extends MockValueListFuzzer implements IModule {

    public MockHexValueListFuzzer(String filepath, boolean repeatList) {
        super(filepath, repeatList);
    }
    
    public MockHexValueListFuzzer(Vector<String> valueList, boolean repeatList) {
        super(valueList, repeatList);
    }


}
