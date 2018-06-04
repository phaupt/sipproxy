package pd.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import testexec.IVariableReplacer;

public class ResponseCatcher implements pd.modules.IModule {

	private String message;
	private String regex;
	private int catchGroupIndex;
	
	public ResponseCatcher(String regex, int catchGroupIndex){
        if (catchGroupIndex < 0){
            throw new IllegalArgumentException("catchGroupIndex must be a positive integer value");
        }
		this.regex = regex;
		this.catchGroupIndex = catchGroupIndex;
        
        try{
            Matcher m =Pattern.compile(regex).matcher("");
            int groupCount = m.groupCount();
            if(catchGroupIndex < 1 || catchGroupIndex > groupCount){
                throw new IllegalArgumentException("Invalid catchGroupIndex number !");
            }
        }
        catch(PatternSyntaxException pse){
            throw new IllegalArgumentException("Invalid regular expression for the ResponseCatcher !");
        }
	}
    
	public void setMessage(String message){
	    this.message = message;
    }
    
	public String getValue(IVariableReplacer variableReplacer) {
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(catchGroupIndex);
        }
        //Nothing found...
        return "";
	}

}
