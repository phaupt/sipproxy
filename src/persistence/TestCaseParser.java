package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import util.CharSetParser;
import util.DomUtil;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.SimpleLogger;
import config.Configuration;

import pd.IRequest;
import pd.IResponse;
import pd.IResponseVar;
import pd.ITestCase;
import pd.ITimeout;
import pd.IVar;
import pd.modules.IMessageModifier;
import pd.modules.IModule;

public class TestCaseParser implements ITestCaseSpecification {

    private Configuration config;
    private IElementFactory elementFactory;
    
    public TestCaseParser(IElementFactory elementFactory, Configuration config) {
        this.config = config;
        this.elementFactory = elementFactory;
    }

    public ITestCase getTestCase(File testSpecFile) throws ParserException, ValidatorException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }

        // Document Object (DOM-Tree)
        Document doc = null;

        ErrorHandler parserErrorHandler = new ErrorHandler();
        ErrorHandler validatorErrorHandler = new ErrorHandler();
        
        // Test if XMLSchema exists
        File schemaFile = new File(SCHEMA_FILE);
        if (!schemaFile.exists()){
        	throw new IllegalArgumentException(SCHEMA_FILE + " not found !");
        }
        
        if (builder != null) {
            try {  
                builder.setErrorHandler(parserErrorHandler);
                
                // Parse the TestSpecification XML file and generate the DOM-tree
                doc = builder.parse(testSpecFile);
                // Load XML schema definition and validate the XML file
                Schema xsd = SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
                Validator validator = xsd.newValidator();
                validator.setErrorHandler(validatorErrorHandler);
                validator.validate(new DOMSource(doc)); 
            }
            catch (Exception e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            } 
            finally {
                if ( parserErrorHandler.hasException() ){
                    throw new ParserException("is not well-formed !");
                } else if (validatorErrorHandler.hasException()){
                    throw new ValidatorException(validatorErrorHandler.getMessage());
                }
            }
        }
        
        ITestCase testCase = null;
        
        if (doc != null) {
            // Get the root element
            Node rootNode = doc.getDocumentElement();
            // Load the TestCase
            testCase = loadTestCase(rootNode);
        }
        
        return testCase;
    }

    private ITestCase loadTestCase( Node rootNode ) throws ParserException {
        // TestCase Attributes
        String name = DomUtil.getAttributeValue(rootNode,ATT_NAME);
        int initialRequestMessageID = getInteger(DomUtil.getAttributeValue(rootNode,ATT_INITIALID),"initialRequestMessageID");
               
        Vector<IRequest> requests = new Vector<IRequest>();
        Vector<IVar> variables = new Vector<IVar>();
        
        for (int i = 0; i < rootNode.getChildNodes().getLength(); i++) {
            Node childNode = rootNode.getChildNodes().item(i);
            if (childNode.getNodeName().equals(NODE_REQ)) {
                // Subelement "Request"
                requests.add(getRequest(childNode));
            }
            else if (childNode.getNodeName().equals(NODE_VARIABLES)) {
                for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
                    Node var = childNode.getChildNodes().item(j);
                    if (var.getNodeName().equals(NODE_VAR)) {
                        // Subelement "Variables (Var)"
                        variables.add(getVar(var));
                    }
                }
            }
        }
        
        ITestCase testCase = elementFactory.createTestCase(name, initialRequestMessageID, requests);
        String sCycles = null;
        if ((sCycles =DomUtil.getAttributeValue(rootNode,ATT_CYCLES)) != null){
            testCase.setCycles(getInteger(sCycles,"cycles"));    
        }
        
        if (variables.size() > 0){
            testCase.setVariables(variables);
        }      
        return testCase;
    }

    private IRequest getRequest( Node requestNode ) throws ParserException {
        // Attribute
        int requestID = getInteger(DomUtil.getAttributeValue(requestNode,ATT_REQUESTID),"requestID");
        
        String content = "";
        Vector<IMessageModifier> messageModifiers = new Vector<IMessageModifier>();
        Vector<IVar> variables = new Vector<IVar>();
        Vector<IResponse> responses = new Vector<IResponse>();
        ITimeout timeout = null;
        
        for (int i = 0; i < requestNode.getChildNodes().getLength(); i++) {
            Node childNode = requestNode.getChildNodes().item(i);
            if (childNode.getNodeName().equals(NODE_MESSAGEMODIFIER)) {
                // Subelement "MessageModifiers"
                for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
                    Node var = childNode.getChildNodes().item(j);
                    if (var.getNodeName().equals(NODE_CONTENTLENGTH)) {
                        // Subelement "ContentLength"
                        messageModifiers.add(elementFactory.createContentLength());
                    }
                    else if (var.getNodeName().equals(NODE_LINEPERMUTATOR)) {
                        // Subelement "LinePermutator"
                        String startLine = DomUtil.getAttributeValue(var,ATT_STARTLINE);
                        String endLine = DomUtil.getAttributeValue(var,ATT_ENDLINE);
                        messageModifiers.add(elementFactory.createLinePermutator(
                                getInteger(startLine, ATT_STARTLINE),
                                getInteger(endLine, ATT_ENDLINE)));
                    }
                }
            }
            else if (childNode.getNodeName().equals(NODE_CONTENT)) {
                // Subelement "Content"
                content = DomUtil.getCDATAValue(childNode);
            }
            else if (childNode.getNodeName().equals(NODE_TIME)) {
                // Subelement "Timeout"
                timeout = getTimeout(childNode);
            }
            else if (childNode.getNodeName().equals(NODE_RESP)) {
                // Subelement "Response"
                responses.add(getResponse(childNode));
            }
            else if (childNode.getNodeName().equals(NODE_VARIABLES)) {
                for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
                    Node var = childNode.getChildNodes().item(j);
                    if (var.getNodeName().equals(NODE_VAR)) {
                        // Subelement "Variables (Var)"
                        variables.add(getVar(var));
                    }
                }
            }
        }
        
        IRequest request = elementFactory.createRequest(requestID, content, responses);
        if (messageModifiers.size() > 0){
            request.setModifiers(messageModifiers);
        }
        if (variables.size() > 0){
            request.setVariables(variables);
        }
        if (timeout != null){
            request.setTimeout(timeout);
        }
        
        return request; 
    }
    
    private ITimeout getTimeout( Node timeoutNode ) {
        // Attributes
        String message = DomUtil.getAttributeValue(timeoutNode,ATT_MESSAGE);
        int timeInMilliseconds = getInteger(DomUtil.getAttributeValue(timeoutNode,ATT_TIME),"timeInMilliseconds");
        
        return elementFactory.createTimeout(timeInMilliseconds, message);
    }
    
    private IResponse getResponse( Node responseNode ) throws ParserException {
        // Mandatory attribute
        String category = DomUtil.getAttributeValue(responseNode,ATT_CATEGORY);
        
        // Optional attributes
        Node optionalNodeAttribute = responseNode.getAttributes().getNamedItem(ATT_FOLLREQID);
        int followingRequestID = -1;
        if (optionalNodeAttribute != null) {
            followingRequestID = getInteger(optionalNodeAttribute.getNodeValue(),"followingRequestID");
        } 
        optionalNodeAttribute = responseNode.getAttributes().getNamedItem(ATT_WAITINGTIME);
        int waitingTimeInMilliseconds = 0;
        if (optionalNodeAttribute != null) {
            waitingTimeInMilliseconds = getInteger(optionalNodeAttribute.getNodeValue(),"waitingTimeInMilliseconds");
        }
        
        String regex = "";
        Vector<IResponseVar> variables = new Vector<IResponseVar>();
        
        for (int i = 0; i < responseNode.getChildNodes().getLength(); i++) {
            Node childNode = responseNode.getChildNodes().item(i);
            if (childNode.getNodeName().equals(NODE_REGEX)) {
                // Subelement "Regex"
                regex = DomUtil.getCDATAValue(childNode);
            }
            else if (childNode.getNodeName().equals(NODE_RESPVARIABLES)) {
                for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
                    Node respVar = childNode.getChildNodes().item(j);
                    if (respVar.getNodeName().equals(NODE_RESPVAR)) {
                        // Subelement "ResponseVariables (ResponseVar)"
                        variables.add(getResponseVar(respVar));
                    }
                }
            }
        }
        IResponse response = elementFactory.createResponse(regex, category);
        if ( followingRequestID != -1 ){
            response.setFollowingRequestID(followingRequestID);
        }
        if (waitingTimeInMilliseconds != 0){
            response.setWaitingTimeInMilliseconds(waitingTimeInMilliseconds);
        }
        if (variables.size() > 0){
            response.setVariables(variables);
        }
        
        return response;
    }
   
    private IVar getVar( Node varNode ) throws ParserException {
        String name = DomUtil.getAttributeValue(varNode, ATT_NAME);
        IModule module = getModule(varNode.getChildNodes());
        return elementFactory.createVar(name, module);
    }
    
    private IResponseVar getResponseVar( Node varNode ) throws ParserException {
        String name = DomUtil.getAttributeValue(varNode,ATT_NAME);
        IModule module = getCatcher(varNode.getChildNodes());
        return elementFactory.createResponseVar(name, module);
    }

    private IModule getCatcher( NodeList childNodes ) throws ParserException{ 
        IModule catcher = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node moduleNode = childNodes.item(i);
            if (moduleNode.getNodeName().equals(NODE_RESPCATCHER)) {
            	// Attribute
                int catchGroupIndex = getInteger(DomUtil.getAttributeValue(moduleNode,MODATT_CATCHGROUP),"catchGroupIndex");

            	String regex = "";
                for (int j = 0; j < moduleNode.getChildNodes().getLength(); j++) {
                    Node childNode = moduleNode.getChildNodes().item(j);
                    if (childNode.getNodeName().equals(NODE_REGEX)) {
                        // Get the regular expression
                        regex = DomUtil.getCDATAValue(childNode);
                    }
                }
                try{
                    catcher = elementFactory.createResponseCatcher(regex, catchGroupIndex);
                }
                catch(IllegalArgumentException iae){
                    throw new ParserException(iae.getMessage());
                }
            }
        }
        return catcher;
    }
    
    private IModule getModule( NodeList childNodes ) throws ParserException {
        for (int i = 0; i < childNodes.getLength(); i++) {
        	// Check all child nodes for matching modules
            Node moduleNode = childNodes.item(i);
            
            if (moduleNode.getNodeName().equals(MODNODE_CLEARTEXT)) {
                String value = DomUtil.getCDATAValue(moduleNode);
                return elementFactory.createClearText(value);
            }
            else if (moduleNode.getNodeName().equals(MODNODE_CONFIGVALUE)) {
                String paramName = DomUtil.getAttributeValue(moduleNode, MODATT_PARAMNAME);
                return elementFactory.createConfigValue(config, paramName);
            }
            else if (moduleNode.getNodeName().equals(MODNODE_NUMBERCOUNTER)) {
                long startValue = getLong(DomUtil.getAttributeValue(moduleNode, MODATT_STARTVALUE),MODATT_STARTVALUE);
                String incrementStr = DomUtil.getAttributeValue(moduleNode, MODATT_COUNTERINCREMENT);
                if (incrementStr != null){ 
                    return elementFactory.createNumberCounter(startValue, getLong(incrementStr,MODATT_COUNTERINCREMENT));
                } else {
                    return elementFactory.createNumberCounter(startValue);
                }   
            }
            else if (moduleNode.getNodeName().equals(MODNODE_DIGEST)) {
                String username = DomUtil.getAttributeValue(moduleNode, MODATT_USERNAME);
                String secret = DomUtil.getAttributeValue(moduleNode, MODATT_SECRET);
                String realm = DomUtil.getAttributeValue(moduleNode, MODATT_REALM);
                String nonce = DomUtil.getAttributeValue(moduleNode, MODATT_NONCE);
                String uri = DomUtil.getAttributeValue(moduleNode, MODATT_URI);
                String method = DomUtil.getAttributeValue(moduleNode, MODATT_METHOD);
                String qop = DomUtil.getAttributeValue(moduleNode, MODATT_QPOP);
                String noncecount = DomUtil.getAttributeValue(moduleNode, MODATT_NONCECOUNT);
                String cnonce = DomUtil.getAttributeValue(moduleNode, MODATT_CNONCE);
                String body = DomUtil.getAttributeValue(moduleNode, MODATT_BODY);
                if (qop == null) {
                	return elementFactory.createDigest(username, secret, realm, nonce, uri, method, "md5");
                }
                else {
                    if (qop.equals("AUTH")){
                        return elementFactory.createDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, "md5");
                    } else if (qop.equals("AUTH-INT")){
                        return elementFactory.createDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, body, "md5");
                    }
                	
                }
            }
            else if (moduleNode.getNodeName().equals(MODNODE_NUMBER_RANGE_FUZZER)) {
                long minimum = getLong(DomUtil.getAttributeValue(moduleNode, MODATT_MIN),MODATT_MIN);
                long maximum = getLong(DomUtil.getAttributeValue(moduleNode, MODATT_MAX),MODATT_MAX);
                return elementFactory.createNumberRangeFuzzer(minimum, maximum);
            }
            else if (moduleNode.getNodeName().equals(MODNODE_STRING_EXP_FUZZER)) {
                int increment = getInteger(DomUtil.getAttributeValue(moduleNode, MODATT_INCREMENT),MODATT_INCREMENT);

                String expansionString = null;
                String characterSet = null;
                for(int k = 0; k < moduleNode.getChildNodes().getLength(); k++){
                    Node childNode = moduleNode.getChildNodes().item(k);
                    if (childNode.getNodeName().equals(NODE_EXPSTRING)) {
                        // Subelement "ExpansionString"
                        expansionString = DomUtil.getCDATAValue(childNode);
                    }
                    else if(childNode.getNodeName().equals(NODE_CHARSET)){
                        //Subelement "CharacterSet"
                        characterSet = DomUtil.getCDATAValue(childNode);
                    }
                }
                try{
                    if (expansionString == null) {
                        if(characterSet != null){
                            //use specified character set for fuzzing
                            return elementFactory.createStringExpansionFuzzer(increment, new CharSetParser().getCharArray(characterSet));
                        }
                        else{
                            //use default character set for fuzzing
                            return elementFactory.createStringExpansionFuzzer(increment);
                        }
                    }
                    else {
                        //use expansion string for fuzzing
                    	return elementFactory.createStringExpansionFuzzer(expansionString, increment);
                    }
                }
                catch(IllegalArgumentException iae){
                    throw new ParserException(iae.getMessage());
                }                
            }
            else if (moduleNode.getNodeName().equals(MODNODE_STRING_MUT_FUZZER)) {
                int length = getInteger(DomUtil.getAttributeValue(moduleNode, MODATT_LENGTH),"length");
                String characterSet = null;
                for(int l = 0; l < moduleNode.getChildNodes().getLength(); l++){
                    Node childNode = moduleNode.getChildNodes().item(l);
                    if(childNode.getNodeName().equals(NODE_CHARSET)){
                        //Subelement "CharacterSet"
                        characterSet = DomUtil.getCDATAValue(childNode);
                    }
                }                
                try{
                    if (characterSet != null && characterSet.length() > 0){
                        return elementFactory.createStringMutationFuzzer(length, (new CharSetParser()).getCharArray(characterSet));
                    } else {
                        return elementFactory.createStringMutationFuzzer(length);
                    }
                }
                catch(IllegalArgumentException iae){
                    throw new ParserException(iae.getMessage());
                }
            }
            else if (moduleNode.getNodeName().equals(MODNODE_VALUE_LIST_FUZZER)
                    || moduleNode.getNodeName().equals(MODNODE_HEX_VALUE_LIST_FUZZER)) {
                
                boolean repeatList = Boolean.parseBoolean(DomUtil.getAttributeValue(moduleNode, MODATT_REPEATLIST));
                
                String values = DomUtil.getCDATAValue(moduleNode);
                if (values != null && values.length() > 0) {
                    //CDATA Section has been found (higher priority to a filepath)
                    Vector<String> valueList = new Vector<String>();
                    BufferedReader buffReader = new BufferedReader(new StringReader(values));
                    String value;
                    try {
                        while ((value = buffReader.readLine()) != null) {
                            valueList.add(value);
                        }
                    }
                    catch (IOException e) {
                        SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                    }

                    try{
                        if (moduleNode.getNodeName().equals(MODNODE_VALUE_LIST_FUZZER)) {
                            return elementFactory.createValueListFuzzer(valueList, repeatList);
                        }
                        else {
                            return elementFactory.createHexValueListFuzzer(valueList, repeatList);
                        }     
                    } catch (IllegalArgumentException iae){
                        throw new ParserException(iae.getMessage());
                    }
                    
                }
                else {
                    //No CDATA Section available, look for FilePath...
                    String filepath = DomUtil.getAttributeValue(moduleNode, MODATT_FILEPATH);
                    try {
                        if (moduleNode.getNodeName().equals(MODNODE_VALUE_LIST_FUZZER)) {
                            return elementFactory.createValueListFuzzer(filepath, repeatList);
                        }
                        else {
                            return elementFactory.createHexValueListFuzzer(filepath, repeatList);
                        }
                    }
                    catch (IllegalArgumentException iae){
                        throw new ParserException(iae.getMessage());
                    }
                    
                }
            }
        }
        return null;
    }

    private int getInteger(String value, String attributeName){
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("\"" + attributeName + "\" must be an integer value !");
        }
    }
    
    private long getLong(String value, String attributeName){
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("\"" + attributeName + "\" must be a long value !");
        }
    }
    
}
