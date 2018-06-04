package testexec.history;

import java.io.File;


public interface IHistoryLogsFactory {
    IMessagesLog createMessagesLog(File messagesLog);
    IWarningLog createWarningLog(File warningLog);
    
}
