package testexec.history;

import java.util.Hashtable;
import java.util.Map;

public class MokWarningLog implements IWarningLog {

    Map<String, String> messagesMap = new Hashtable<String, String>();
    Map<String, String> requestMap = new Hashtable<String, String>();
    public void addWarning( String requestMsgID, String request, String message ) {
        requestMap.put(requestMsgID, request);
        messagesMap.put(requestMsgID, message);
    }

    public String getWarningMessage( String requestMsgID ) {
        return messagesMap.get(requestMsgID);
    }

    public String getWarningRequest( String requestMsgID ) {
        return requestMap.get(requestMsgID);
    }

    public void delete() {
        // TODO Auto-generated method stub
        
    }

}
