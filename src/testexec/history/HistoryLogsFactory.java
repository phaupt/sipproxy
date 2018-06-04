package testexec.history;

import java.io.File;


public class HistoryLogsFactory implements IHistoryLogsFactory {

    public IMessagesLog createMessagesLog( File messagesLog ) {
        return new MessagesLog(messagesLog);
    }

    public IWarningLog createWarningLog( File warningLog ) {
        return new WarningXML(warningLog);
    }

}
