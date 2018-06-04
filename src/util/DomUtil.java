package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DomUtil {
    public static char[] unsupportedChars = new char[]{0,1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
    public static final String escapeSequence = "###";
    
    public static String getAttributeValue( Node node, String attrName ) {
        Node attr = node.getAttributes().getNamedItem(attrName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return null;
    }

    public static String getCDATAValue( Node node ) {
        String cData = null;
        for (int j = 0; j < node.getChildNodes().getLength(); j++) {
            Node childNode = node.getChildNodes().item(j);
            if (childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
                cData = childNode.getNodeValue();
                // Since the control characters within a CDATA section is dependet
                // on the editor, we must override the line end characters!
                cData = cData.replace("\r\n", "\n");
                cData = cData.replace("\n", "\r\n");
                return cData;
            }
        }
        //No CDATA Section used
        String textContent = node.getTextContent();
        if (textContent != null){
            //Text Content has been found
            return textContent;
        } else {
            //Nothing at all
            return null;
        }
        
    }
    
    public static Document loadDOMDocument(File file){
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }        
        if (builder != null && file.exists()) {
            try {  
                // Parse the XML file and generate the DOM-tree
                doc = builder.parse(file);
            }
            catch (Exception e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            } 
        }
        return doc;
    }
    
    public static void saveDOMDocument(File file, Document doc){
        // Settings for the file output
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");


            DOMSource source = new DOMSource(doc);

            // Save DOM-Tree to file
            FileOutputStream os = new FileOutputStream(file);
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);          
            os.flush();
            os.close();
        }
        catch (TransformerConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (TransformerFactoryConfigurationError e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (FileNotFoundException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (TransformerException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
                
    }
    
    public static Document createDocument(){
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();                
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        return doc;
    }
    
    public static String encodeUnsuportedChars(String content){
        for(char c : unsupportedChars){
            int index = -1;
            while((index = content.indexOf(c)) > -1){
                content = content.substring(0, index) +
                          escapeSequence + ((int) c) + escapeSequence +
                          content.substring(index+1);
            }
        }
        return content;
    }
    public static String decodeUnsuportedChars(String encodedContent){
        //regex: one or two digits enclosed in a escape Sequence
        String regex = "(" + escapeSequence + ")" + "(\\d{1,2})" + "(" + escapeSequence + ")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(encodedContent);
        StringBuffer sb = new StringBuffer();
        
        while(matcher.find()){
            char character = (char) Integer.parseInt(matcher.group(2));
            matcher.appendReplacement(sb,"" + character );
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
}
