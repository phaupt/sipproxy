package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger{

    public final static boolean ERRORLOGGING = false;

    public static final File ERROR_LOG = new File("SIPProxy_Error.log");
    public static final File DEBUG_LOG_PROXY = new File("SIPProxy_ProxyModeDebug.log");
    
    public static final File LOG_DIR = new File("Log");
    
    public static void log( String message, File filename ) {
        
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date now = new Date();

        if (filename.equals(ERROR_LOG) && ERRORLOGGING) {
            logMessage(sdf.format(now) + "\t" + message + "\r\n",ERROR_LOG);
        }
        else if (filename.equals(ERROR_LOG) && !ERRORLOGGING){
        	//do not log the error !
        }
        else if (filename.equals(DEBUG_LOG_PROXY)) {
            logMessage("# " + sdf.format(now) + "\r\n" + message + "\r\n", DEBUG_LOG_PROXY);
        }
        else {
        	//Test Case Log files
            logMessage("# " + sdf.format(now) + "\r\n" + message + "\r\n", filename);
        }

    }
    private static void logMessage(String message, File logFile){
        try {
            BufferedWriter resWriter = new BufferedWriter(new FileWriter(logFile, true));
            resWriter.write(message);
            resWriter.flush();
            resWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
