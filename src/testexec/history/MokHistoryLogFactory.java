package testexec.history;

import java.io.File;


public class MokHistoryLogFactory implements IHistoryLogsFactory {

    public IMessagesLog createMessagesLog( File messagesLog ) {
        return new MokMessagsLog();
    }

    public IWarningLog createWarningLog( File warningLog ) {
        return new MokWarningLog();
    }

}
