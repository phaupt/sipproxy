package pd.modules;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentLength implements IMessageModifier {
    
    private Vector<String> newMessage = null;
    private final String REGEX = "(Content-Length)(.*?)(:)(.*?)(\r\n)(\r\n)";
    
    private void getValidContentLength(String message){

        Pattern pattern = Pattern.compile(REGEX, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(message);
        
        
        newMessage = new Vector<String>();
        
        if (matcher.find()){   
            // Found matching regex (Content-Length Header)
            
            String[] splitSIP = message.split(REGEX);
            int lengthCount = 0;
            
            if (splitSIP.length > 1) {
                
                // Append Part before Regex
                newMessage.add(splitSIP[0]);

                for (int i = splitSIP.length-1; i >= 1; i--){
                    // For each Content-Length Header field...
                    
                    // SDP content must end with "....\r\n" + "\r\n" (empty line)
                    int length = splitSIP[i].length() + lengthCount;                           
                    
                    String replaceString = "Content-Length: " + length + "\r\n\r\n";
                    
                    lengthCount = length + replaceString.length(); // Summarize Content-Length
                    
                    newMessage.add(1,replaceString + splitSIP[i]);   
                }     
                
            } else {
                // Content is empty (splitSIP.length = 1)
                String replaceString = "Content-Length: 0\r\n\r\n";
                lengthCount += replaceString.length(); // Summarize Content-Length
                newMessage.add(splitSIP[0] + replaceString);
            }           
        } else {
            // No Match !
            newMessage.add(message);
        }              
    }
    
    public String getMessage( String message ) { 
        getValidContentLength(message);
        
        String messageString = "";
        
        for(String messageLine : newMessage){
            messageString += messageLine;
        }
        
        return messageString;       
    }

}
