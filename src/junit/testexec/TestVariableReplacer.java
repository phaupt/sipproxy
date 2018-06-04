package junit.testexec;

import java.util.Vector;

import pd.Var;
import pd.modules.ClearText;
import testexec.IVariableHandler;
import testexec.IVariableReplacer;
import testexec.VariableHandler;
import testexec.VariableReplacer;
import junit.framework.TestCase;


public class TestVariableReplacer extends TestCase {
    
    IVariableReplacer variableReplacer;
    IVariableHandler variableHandler;
    
    String testMessage="bla/'1'/ /'2'/ /'4'/ /'9'/ /'fjsdlak'/asdf";
    String expectedReplacement = "blabla1 bla2 bla4 bla9 /'fjsdlak'/asdf";
    protected void setUp() throws Exception {
        variableHandler = new VariableHandler();
        variableReplacer = new VariableReplacer(variableHandler);
        Vector<Var> vars = new Vector<Var>();
        for(int i = 0; i < 10; i++){
            vars.add(new Var("" + i, new ClearText("bla" + i)));
        }
        variableHandler.addVariables(vars);
       
        
        
    }
    
    public void testReplacing(){
        String replacement = variableReplacer.replaceVariables(testMessage);
        assertEquals(expectedReplacement, replacement);
    }
}
;