package testexec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class VariableReplacer implements IVariableReplacer {
    private IVariableHandler variableHander;
    
    public VariableReplacer(IVariableHandler variableHandler){
        this.variableHander = variableHandler;
    }
    
    public String replaceVariables( String content ) {
        String regex = "(/')(.*?)('/)"; // /'varName'/
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        
        StringBuffer sb = new StringBuffer();
        String varName;
        String varValue = null;
        
        //Search for variables in content
        while(matcher.find()){
            //Get value of variable
            varName = matcher.group(2);

           
            varValue = variableHander.getVar(varName); 
                
            //if variable is defined, replace it
            if(varValue != null){
                //Replace a single \ with \\ and $ with \$
                //Otherwise it will throw an Exception since \ and $
                //are escape characters !
                varValue = varValue.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
                try{
                    matcher.appendReplacement(sb, varValue);
                    
                }
                catch (OutOfMemoryError e){
                    throw new OutOfMemoryError("variable /'" + varName + "'/ could not be replaces. Out of memory!");
                }             

            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
