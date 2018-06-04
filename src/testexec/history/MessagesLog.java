package testexec.history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import util.SimpleLogger;

public class MessagesLog implements IMessagesLog {
    private File messagesLog;
	
    public MessagesLog(File messagesLog){
        this.messagesLog = messagesLog;
    }
	 
	public void logRequest(String to, String requestMsgID, String content) {
	    
        StringBuffer sb = new StringBuffer();
        
        sb.append("# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\r\n");
        sb.append("# REQUEST to: " + to + "\r\n");
        sb.append("# ID: " + requestMsgID + "\r\n");
        sb.append(">>" + content+ "<<\r\n");
        SimpleLogger.log(sb.toString(), messagesLog);
               
	}
	 
	public void logResponse(String from, String requestMsgID, String category, String content) {
        StringBuffer sb = new StringBuffer();

        sb.append("# <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\r\n");
        sb.append("# RESPONSE from: " + from + "\r\n");
        sb.append("# Category: " + category.toUpperCase() + "\r\n");
        sb.append("# ID: " + requestMsgID + "\r\n");
        sb.append(">>" + content + "<<\r\n");
        SimpleLogger.log(sb.toString(), messagesLog);
	}
	 
	public void logTimeout(String requestMsgID, long milliseconds, String message) {
        StringBuffer sb = new StringBuffer();
        sb.append("# TIMEOUT of " + milliseconds + "ms\r\n");
        sb.append("# ID: " + requestMsgID+ "\r\n");        
        sb.append("# Message: " + message + "\r\n");
        SimpleLogger.log(sb.toString(), messagesLog);        
	}

    public void logError( String message ) {
        StringBuffer sb = new StringBuffer();     
        sb.append("# ERROR: " + message + "\r\n");
        SimpleLogger.log(sb.toString(), messagesLog);     
    }
    
    public void delete(){
        messagesLog.delete();
    }

    public String getContent() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(messagesLog);
            int fileSize = fis.available();
            byte logByte[] = new byte[fileSize];
            fis.read(logByte);
            return new String(logByte);
        }
        catch (FileNotFoundException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }catch (OutOfMemoryError e){
            throw new IllegalArgumentException(
                    "The Debug Log file is too large to open !\n" +
                    "Please use an external editor.");
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }
        return null;
    }
	 
}
 
