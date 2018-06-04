package junit.persistence;

import java.io.File;
import java.util.Vector;

import pd.ITestCaseRef;
import persistence.TestCaseRefParser;
import junit.framework.TestCase;


public class TestTestCaseRefParser extends TestCase {
    String testCaseDir = "src/junit/persistence/testCaseDir";
    File tc1;
    File tc2;
    File tc3;

    protected void setUp() throws Exception {
        tc1 = new File(testCaseDir + "/TestCase1.xml");
        tc2 = new File(testCaseDir + "/TestCase2.xml");
        tc3 = new File(testCaseDir + "/zblabla3.xml");
    }
        
    public void testDirectoryParsing(){
        TestCaseRefParser parser = new TestCaseRefParser();
        Vector<ITestCaseRef> testCaseRefs = parser.getTestCaseRefs(new File(testCaseDir));
        assertEquals(3, testCaseRefs.size());

        
        ITestCaseRef t1 = testCaseRefs.get(0);
        assertEquals("A TestCase Blabla3", t1.getName());
        assertEquals(tc3, t1.getTestCaseFile());

        ITestCaseRef t2 = testCaseRefs.get(1);
        assertEquals("TestCase 1", t2.getName());
        assertEquals(tc1, t2.getTestCaseFile());
        
        
        ITestCaseRef t3 = testCaseRefs.get(2);
        assertEquals("TestCase 2 XML", t3.getName());
        assertEquals(tc2, t3.getTestCaseFile());



    }
}
