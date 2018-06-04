package pd.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Vector;

import util.SimpleLogger;

public class LinePermutator implements IMessageModifier {

    private int startLine;
    private int endLine;
    
    private Vector<String> messageLines = null;
    
    public LinePermutator(int startLine, int endLine) {
        if (startLine < endLine){
            this.startLine = startLine-1; // convert to vector index range (-1)
            this.endLine = endLine-1; // convert to vector index range (-1)
        } else {
            throw new IllegalArgumentException("startLine must be less than endLine");
        }       
    }

    public String getMessage( String message ) {    
             
        loadMessageLinesIntoVector(message);   
        
        if (messageLines.size()-1 < endLine){
            //Index out of Range, reset to the maximum line number
            endLine = messageLines.size()-1;
        }
        
        shuffleMessageLines();
        return getVectorAsString();
    }
    
    private void loadMessageLinesIntoVector(String message){
        messageLines = new Vector<String>();       
        BufferedReader buffReader = new BufferedReader(new StringReader(message));
        String line;
        try {
            while ((line = buffReader.readLine()) != null) {
                messageLines.add(line);
            }
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
    }
    
    private void shuffleMessageLines(){
        Vector<String> shuffleVec = new Vector<String>();
        
        for (int i = startLine; i <= endLine; i++){
            //Extract the Message Lines 
            shuffleVec.add(messageLines.remove(startLine));
        }   
        
        //Shuffle the selected Message Lines
        Collections.shuffle(shuffleVec);
        
        for (String line : shuffleVec){
            //Insert the shuffled Message Lines
            messageLines.add(startLine, line);
        }
    }
    
    private String getVectorAsString(){
        String newMessage = "";
        
        for (String line : messageLines){
            newMessage += line + "\r\n";
        }
        
        return newMessage;
    }

}
