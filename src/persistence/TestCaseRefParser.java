package persistence;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import config.transformation.DynamicRegexRule;

import pd.ITestCaseRef;
import pd.TestCaseRef;
import util.SimpleLogger;

public class TestCaseRefParser extends DefaultHandler implements ITestCaseRefParser {
    
    private SAXParser saxParser = null;
    private String name = null;
    private StringBuffer errorBuffer = null;
    
    public TestCaseRefParser(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        try {
            saxParser = factory.newSAXParser();
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        catch (SAXException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        } 
    }
    
    public Vector<ITestCaseRef> getTestCaseRefs( File parentDirectory ){
        
        //Create file filter --> only accept xml files
        FilenameFilter filter = new FilenameFilter(){
            public boolean accept( File dir, String name ) {
                return name.endsWith(".xml");
            }
        };
        
        //Get all XML files in directory
        File[] xmlFiles = parentDirectory.listFiles(filter);
        
        Vector<ITestCaseRef> testCaseRefs = new Vector<ITestCaseRef>();
        
        ITestCaseRef testCaseRef = null;
        
        if(xmlFiles != null){
            //add all TestCaseRefs to vector
            for(File xmlFile: xmlFiles){
                testCaseRef = getTestCaseRef(xmlFile);
                
                //if xmlFile represents a test specification, add ref to vector
                if(testCaseRef != null){
                    testCaseRefs.add(testCaseRef);
                }
            }
        }
        
        Collections.sort(testCaseRefs, new Comparator() {
            public int compare( Object o1, Object o2 ) {
                String s1 = ((ITestCaseRef) o1).getName();
                String s2 = ((ITestCaseRef) o2).getName();
                return s1.compareTo(s2);
            }
        });
        
        return testCaseRefs;
    }

    private ITestCaseRef getTestCaseRef( File xmlFile ) {
        //Method parses xmlFile and creates TestCaseRef for valid files
        
        ITestCaseRef testCaseRef = null;

        try {
            saxParser.parse(xmlFile, this);
        }
        catch (SAXException e) {
            name = null;
            if (errorBuffer == null){
                errorBuffer = new StringBuffer();
            }
            errorBuffer.append("\"" + xmlFile.getName() + "\"" + " is not well formed !");
        }
        catch (IOException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }

        if(name != null){
            testCaseRef = new TestCaseRef(name, xmlFile);
            name = null;
        }
        
        return testCaseRef;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes atts ) {
        if ( qName.equals("TestCase") ) {
            name = atts.getValue("", "name"); 
        }
    }
    
    public StringBuffer getErrorBuffer(){
        return errorBuffer;
    }
    
    public void resetErrorBuffer(){
        this.errorBuffer = null;
    }

}
