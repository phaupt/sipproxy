package pd;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pd.modules.IMessageModifier;
import testexec.IVariableReplacer;

public class Request implements IRequest {
	private int requestId;
	private String content;
    private Vector<IMessageModifier> messageModifiers = new Vector<IMessageModifier>();
    private Vector<IVar> variables = new Vector<IVar>();
	private Vector<IResponse> responses = new Vector<IResponse>();
	private ITimeout timeout = null;
    
    public Request(int requestID, String content, Vector<IResponse> responses){
        if (requestID < 0){
            throw new IllegalArgumentException("requestID must be a positive integer value");
        }
        this.requestId = requestID;
        this.content = content;  
        this.responses = responses;
    }
    
    public void setModifiers(Vector<IMessageModifier> messageModifiers){
        this.messageModifiers = messageModifiers;
    }
    
    public void setVariables(Vector<IVar> variables){
        this.variables = variables;
    }
    
    public void setTimeout(ITimeout timeout){
        this.timeout = timeout;
    }

	public String getContent() {
		return content;
	}

	public int getRequestId() {
		return requestId;
	}
    
    public Vector<IMessageModifier> getModifiers() {
        return messageModifiers;
    }

	public Vector<IResponse> getResponses() {
		return responses;
	}

	public ITimeout getTimeout() {
		return timeout;
	}
    
    public Vector<IVar> getVars(){
        return variables;
    }
	
	public IResponse getMatchingResponse(String response, IVariableReplacer variableReplacer){
		IResponse res = null;
		int i = 0;
		while((res==null) && i < responses.size()){
			IResponse r = responses.get(i);
			Pattern p = Pattern.compile(variableReplacer.replaceVariables(r.getRegex()), Pattern.MULTILINE);
            Matcher m = p.matcher(response);
			if(m.find()){
				res = r;
			}
			i++;
		}
		return res;
	}

}
