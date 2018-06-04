package persistence;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ErrorHandler implements org.xml.sax.ErrorHandler  {
    
    private StringBuffer sb = new StringBuffer();
    
    public void error( SAXParseException exception ) throws SAXException {
        sb.append("SAXParseError: " + exception.getMessage() + "\n\n");
    }

    public void fatalError( SAXParseException exception ) throws SAXException {
        sb.append("SAXParseFatalError: " + exception.getMessage() + "\n\n");
    }

    public void warning( SAXParseException exception ) throws SAXException {
        sb.append("SAXParseWarning: " + exception.getMessage() + "\n\n");
    }

    public String getMessage(){
        return sb.toString();
    }
    
    public boolean hasException(){
        return sb.length() > 0;
    }
    
}
