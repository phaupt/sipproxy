package pd;

import java.util.Vector;

public class TestCase implements ITestCase {

    private String name;
    private int cycles = INFINITE_CYCLES;
    private int initialRequestMessageID;
    private Vector<IVar> variables = new Vector<IVar>();
    private Vector<IRequest> requests = new Vector<IRequest>();

    public TestCase(String name, int initialRequestMessageID, Vector<IRequest> requests ){
        if (initialRequestMessageID < 0){
            throw new IllegalArgumentException("initialRequestMessageID must be a positive integer value");
        }
        this.name = name;
        this.initialRequestMessageID = initialRequestMessageID;
        this.requests = requests;
    }
    
    public void setVariables(Vector<IVar> variables){
        this.variables = variables;
    }
    
    public String getName() {
        return name;
    }

    public int getCycles() {
        return cycles;
    }
    
    public void setCycles(int cycles){
        if (cycles < 0){
            throw new IllegalArgumentException("cycles must be a positive integer value");
        }
        this.cycles = cycles;
    }
    
    public int getInitialRequestMessageID() {
        return initialRequestMessageID;
    }

    public Vector<IVar> getVars() {
        return variables;
    }

    public IVar getVar( String name ) {
        IVar res = null;
        for (IVar v : variables) {
            if (v.getName().equals(name)) {
                res = v;
                break;
            }
        }
        return res;
    }

    public Vector<IRequest> getRequests() {
        return requests;
    }

    public IRequest getRequest( int requestID ) {
        IRequest res = null;
        for (IRequest r : requests) {
            if (r.getRequestId() == requestID) {
                res = r;
                break;
            }
        }
        return res;
    }

}
