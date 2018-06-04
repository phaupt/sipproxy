package pd;

import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Response implements IResponse {
	private String regex;
	private String category;
	private int followingRequestID = -1; // default -1
    private int waitingTimeInMilliseconds = 0; // default 0
    private Vector<IResponseVar> variables = new Vector<IResponseVar>();

    public Response(String regex, String category){
        this.regex = regex;
        try{
            Pattern.compile(regex);
        }
        catch(PatternSyntaxException pse){
            throw new IllegalArgumentException("Invalid regular expression for the Response !");
        }
        this.category = category;
    }
    
    public void setFollowingRequestID(int followingRequestID){
        if (followingRequestID < 0){
            throw new IllegalArgumentException("followingRequestID must be a positive integer value");
        }
        this.followingRequestID = followingRequestID;
    }
    
    public void setWaitingTimeInMilliseconds(int waitingTimeInMilliseconds){
        if (waitingTimeInMilliseconds < 0){
            throw new IllegalArgumentException("waitingTimeInMilliseconds must be a positive integer value");
        }
        this.waitingTimeInMilliseconds = waitingTimeInMilliseconds;
    }
    
    public void setVariables(Vector<IResponseVar> variables){
        this.variables = variables;
    }
    
    public void setRegex(String regex){
        this.regex = regex;
    }
    
    public Vector<IResponseVar> getResponseVars(){
        return variables;
    }

	public String getCategory() {
		return category;
	}

	public int getFollowingRequestID() {
		return followingRequestID;
	}

	public String getRegex() {
		return regex;
	}
    
    public int getWaitingTimeInMilliseconds() {
        return waitingTimeInMilliseconds;
    }
    
}
