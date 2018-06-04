package pd;

import java.util.Vector;

public interface IResponse {
    
    public void setFollowingRequestID(int followingRequestID);
    
    public void setWaitingTimeInMilliseconds(int waitingTimeInMilliseconds);
    
    public void setVariables(Vector<IResponseVar> variables);

    public abstract Vector<IResponseVar> getResponseVars();

    public abstract String getCategory();

    public abstract int getFollowingRequestID();

    public abstract String getRegex();

    public abstract int getWaitingTimeInMilliseconds();
    
    public abstract void setRegex(String regex);

}