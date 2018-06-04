package testexec.history;

public interface IWarningLog {
    public abstract void addWarning( String requestMsgID, String request, String message );
    public abstract String getWarningRequest( String requestMsgID );
    public abstract String getWarningMessage( String requestMsgID );

    public abstract void delete();
}