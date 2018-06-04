package testexec.history;

public interface IMessagesLog {

    public static final String CATEGORY_OKAY = "Okay";
    public static final String CATEGORY_WARNING = "Warning";
    public static final String CATEGORY_UNKNOWN = "Unknown";

    public abstract void logRequest( String to, String requestMsgID, String content );

    public abstract void logResponse( String from, String requestMsgID, String category,
            String content );

    public abstract void logTimeout( String requestMsgID, long milliseconds,
            String message );

    public abstract void logError( String message );
 
    public abstract void delete();

    public abstract String getContent();
}