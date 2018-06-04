package pd;

import java.io.File;


public class TestCaseRef implements ITestCaseRef {
    private String name;
    private File testCaseFile;
    
    public TestCaseRef(String name, File testCaseFile){
        this.name = name;
        this.testCaseFile = testCaseFile;
    }
    public String getName() {
        return name;
    }

    public File getTestCaseFile() {
        return testCaseFile;
    }
    
    public String toString(){
        if (name.length() > 35){
            name = name.substring(0,35) + "...";
        }
        return name;
    }

}
