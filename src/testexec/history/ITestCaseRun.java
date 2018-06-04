package testexec.history;

import java.util.Vector;

public interface ITestCaseRun extends ITestCaseRunObservable, ITestCaseExecutionObservable {
    public static final String STATUS_RUNNING = "RUNNING...";
    public static final String STATUS_CANCELED = "CANCELED";
    public static final String STATUS_FINISHED = "COMPLETED";

    public static final String UATYPE_UNKNOWN = "< unknown >";
    
    public static final String SAVE_TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String DISPLAY_TIMESTAMP_FORMAT = "dd.MM.yyyy HH:mm:ss:SSS";
    
    public static final String DEFAULT_MESSAGES_LOG_SUFFIX = "_Messages.log";
    public static final String DEFAULT_WARNINGS_LOG_SUFFIX = "_Warnings.xml";    
    public static final String DEFAULT_STATISTICS_SUFFIX = "_Statisics.xml";
    
    public abstract void addWarning( String requestMsgID, String request, String message );
    public abstract void addOkay();
    public abstract void addUnknown();
    public abstract void setCycles(long cycles);
    public abstract void setEndTimestamp(String timestamp);
    public abstract void setStatus(String status);
    public abstract void setTargetUA(String targetUA);
    
    public abstract String getTestCaseName();
    public abstract String getStartTimestamp();
    public abstract String getEndTimestamp();
    public abstract long getOkayCount();
    public abstract long getUnknownCount();
    public abstract long getWarningCount();
    public abstract long getCycles();
    public abstract String getTargetIP();
    public abstract int getTargetPort();
    public abstract String getStatus();
    public abstract String getTargetUA();
    
    public abstract IWarningLog getWarningLog();
    public abstract IMessagesLog getMessagesLog();
    public abstract Vector<String> getWarningRequestMsgIDs();
    
    public abstract void delete();
    
}

