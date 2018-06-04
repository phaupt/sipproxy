package pd;

import java.util.Vector;

import pd.modules.IMessageModifier;
import testexec.IVariableReplacer;

public interface IRequest {
    
    public void setModifiers(Vector<IMessageModifier> messageModifiers);
    
    public void setVariables(Vector<IVar> variables);
    
    public void setTimeout(ITimeout timeout);

    public abstract String getContent();

    public abstract int getRequestId();

    public abstract Vector<IResponse> getResponses();
    
    public abstract Vector<IMessageModifier> getModifiers();

    public abstract ITimeout getTimeout();
    
    public abstract Vector<IVar> getVars();

    public abstract IResponse getMatchingResponse( String response, IVariableReplacer variableReplacer );

}