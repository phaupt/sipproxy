package junit.testexec;

import java.util.Vector;

import pd.Var;
import pd.modules.ClearText;

import testexec.IVariableHandler;
import testexec.VariableHandler;
import junit.framework.TestCase;


public class TestVariableHandler extends TestCase {
    IVariableHandler variableHandler;
    
    protected void setUp() throws Exception {
        variableHandler = new VariableHandler();
    }
    
    public void testAddVariables(){
        Vector<Var> vars = new Vector<Var>();
        for(int i = 0; i < 10; i++){
            vars.add( new Var("" + i, new ClearText("bla" + i)));
            
        }
        variableHandler.addVariables(vars);
        for(int j = 0; j < 10; j++){
            assertEquals("bla" + j, variableHandler.getVar("" + j));
        }

    }
    
    public void testClear(){
        Vector<Var> vars = new Vector<Var>();
        for(int i = 0; i < 10; i++){
            vars.add( new Var("" + i, new ClearText("bla" + i)));
            
        }
        variableHandler.addVariables(vars);
        
        variableHandler.clear();
        
        for(int j = 0; j < 10; j++){
            assertNull(variableHandler.getVar("" + j));
        }
        
    }
}
