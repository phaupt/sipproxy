package testexec.history;

import java.io.File;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.DomUtil;

public class WarningXML implements IWarningLog {
 
	private File warningFile;
	
    private Document doc = null;
    
    //Attributes-Names
    private static final String ATT_ID = "id";

    //Node-Names
    private static final String NODE_ROOT = "WarningLog";
    private static final String NODE_REQUEST = "Request";
    private static final String NODE_REQUEST_CONTENT = "Content";    
    private static final String NODE_REQUEST_MESSAGE = "Message";
    
    //Nodes
    private Node rootNode; 
    
    public WarningXML(File warningFile){
        this.warningFile = warningFile;
        load();
        save();
    }

	public void addWarning(String requestMsgID, String request, String message) {       
        //create new node for request
        Node warningRequest = doc.createElement(NODE_REQUEST);
        
        //add ID attribute to request 
        Attr id = doc.createAttribute(ATT_ID);
        id.setTextContent(requestMsgID);
        warningRequest.getAttributes().setNamedItem(id);
        
        //add content of request message in CDATA section
        Node content = doc.createElement(NODE_REQUEST_CONTENT);
        //Escape unsuporrted characters
        content.appendChild(doc.createCDATASection(DomUtil.encodeUnsuportedChars(request)));
        warningRequest.appendChild(content);
        
        //add message (response / timeout) in CDATA section
        Node nMessage = doc.createElement(NODE_REQUEST_MESSAGE);
        //Escape unsuporrted characters        
        nMessage.appendChild(doc.createCDATASection(DomUtil.encodeUnsuportedChars(message)));
        warningRequest.appendChild(nMessage);
        
        //append new request-node to root-node
        rootNode.appendChild(warningRequest);
        
        save(); //save changes
	}


    public String getWarningRequest(String requestMsgID) {
        String request = null;

        //Get node of request
        Node requestNode = getRequestNode(requestMsgID);
        if(requestNode != null){
            //Get content of request
            request = getContentValue(requestNode);
        }
        return request;
	}
	 
	public String getWarningMessage(String requestMsgID) {
        String request = null;
        //Get node of request
        Node requestNode = getRequestNode(requestMsgID);
        
        if(requestNode != null){
            //Get content of request
            request = getMessageValue(requestNode);
        }
        return request;
    }
	 
    private Node getRequestNode(String requestMsgID){
        int i = 0;
        Node requestNode = null;
        
        Node child = null;
        while(requestNode == null && i < rootNode.getChildNodes().getLength()){
            child = rootNode.getChildNodes().item(i);
            if(child.getNodeName().equals(NODE_REQUEST)){
                if(requestMsgID.equals(DomUtil.getAttributeValue(child, ATT_ID))){
                    requestNode = child;
                }
            }
            i++;
        }
        return requestNode;
    }
    private String getContentValue( Node requestNode ) {
        return getElementCDATAValue(requestNode, NODE_REQUEST_CONTENT);
    }
     
    private String getMessageValue( Node requestNode ) {
        return getElementCDATAValue(requestNode, NODE_REQUEST_MESSAGE);
    }
    
    private String getElementCDATAValue(Node parentNode, String elementName){
        //Return value (CDATA) of sub-element
        String value = null;
        Node child = null;
        int i = 0;
        while(value == null && i < parentNode.getChildNodes().getLength()){
            child = parentNode.getChildNodes().item(i);
            if(child.getNodeName().equals(elementName)){
                value = DomUtil.getCDATAValue(child);
            }
            i++;
        }
        //Remove escape sequence from unsupported characters
        return DomUtil.decodeUnsuportedChars(value);        
    }
    
    private void load(){
        doc = DomUtil.loadDOMDocument(warningFile);
        
        if(doc != null){
            rootNode = doc.getDocumentElement();
        }
        else{
            //file doesn't exist --> create new DOM document and root node;
            doc = DomUtil.createDocument();
            rootNode = doc.createElement(NODE_ROOT);
            doc.appendChild(rootNode);
        }
    }

    private void save() {
        DomUtil.saveDOMDocument(warningFile, doc);
    }    
    
    
    public void delete(){
        warningFile.delete();
    }
}
 
