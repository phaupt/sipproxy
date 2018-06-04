package pd;

import pd.modules.IModule;
import pd.modules.ResponseCatcher;

public class ResponseVar extends Var implements IResponseVar {
    
    public ResponseVar(String name, IModule module){
        super(name,module);
    } 

    public void setResponseMessage(String message){
        ((ResponseCatcher)module).setMessage(message);
    }
    
}
