package pd;

import java.util.Vector;

public interface ITestCase {
    public static final int INFINITE_CYCLES = -1;
    public void setVariables(Vector<IVar> variables);

    public abstract String getName();

    public abstract int getCycles();
    
    public abstract void setCycles(int cycles);

    public abstract int getInitialRequestMessageID();

    public abstract Vector<IVar> getVars();

    public abstract IVar getVar( String name );

    public abstract Vector<IRequest> getRequests();

    public abstract IRequest getRequest( int requestID );

}