package ui;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import pd.ITestCaseRef;

public class TestCaseRefComboBoxModel extends DefaultComboBoxModel {
    
    public TestCaseRefComboBoxModel(Vector<ITestCaseRef> testCaseRefs){
        super(testCaseRefs);
    }   

}
