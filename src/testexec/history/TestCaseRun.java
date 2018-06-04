package testexec.history;

import java.io.File;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.DomUtil;
import util.MiscUtil;

public class TestCaseRun implements ITestCaseRun {
    private File statisticsXMLFile;
    private IHistoryLogsFactory logFilesFactory;
    
    private IWarningLog warningLog;
    private IMessagesLog messagesLog = null;

    private Vector<ITestCaseRunObserver> testCaseRunObservers = new Vector<ITestCaseRunObserver>();
    private Vector<ITestCaseExecutionObserver> executionObservers = new Vector<ITestCaseExecutionObserver>();

    
    //Attributes-Names
    private static final String ATT_NAME = "name";
    private static final String ATT_STARTTIME = "startTimestamp";
    private static final String ATT_ENDTIME = "endTimestamp";
    private static final String ATT_CYCLES = "cycles";
    private static final String ATT_TARGETIP = "targetIP";
    private static final String ATT_TARGETPORT = "targetPort";
    private static final String ATT_STATUS = "status";
    private static final String ATT_COUNT = "count";
    private static final String ATT_ID = "id";
    private static final String ATT_WARNINGLOG = "warningLogFileRef";
    private static final String ATT_MSGGLOG = "messagesLogFileRef";
    private static final String ATT_TARGETUA = "targetUA";

    //Node-Names
    private static final String NODE_ROOT = "TestCaseExecution";
    private static final String NODE_WARNING = "Warning";
    private static final String NODE_WARNING_REQUEST = "Request";    
    private static final String NODE_OKAY = "Okay";
    private static final String NODE_UNKNOWN = "Unknown";  

    private Document doc = null;
    
    //Nodes
    private Node rootNode;
    private Node warningNode;
    private Node okayNode;
    private Node unknownNode;
    
    //Attributes
    private Attr testCaseName;  
    private Attr startTimestamp;
    private Attr endTimestamp;
    private Attr cycles;
    private Attr targetIP;
    private Attr targetPort;
    private Attr status;
    private Attr okayCount;
    private Attr unknownCount;
    private Attr warningLogFileRef;
    private Attr messagesLogFileRef;
    private Attr targetUA;
 
    private TestCaseRun(File statisticsXMLFile, IHistoryLogsFactory logFilesFactory){
        //Loads an 
        this.statisticsXMLFile = statisticsXMLFile;
        this.logFilesFactory = logFilesFactory;
        load();
    }
    
    public TestCaseRun(File statisticsXMLFile, IHistoryLogsFactory logFilesFactory, String starTimestamp, String testCaseName, String targetIP, int targetPort){
        this(statisticsXMLFile, logFilesFactory);
        this.startTimestamp.setTextContent(starTimestamp);
        this.testCaseName.setTextContent(testCaseName);
        this.targetIP.setTextContent(targetIP);
        this.targetPort.setTextContent("" + targetPort);
        
        
        File logDir = statisticsXMLFile.getParentFile();
        //Create Warning log
        String warningLogFileName = starTimestamp + DEFAULT_WARNINGS_LOG_SUFFIX;
        warningLogFileRef.setTextContent(warningLogFileName);
        warningLog = logFilesFactory.createWarningLog(new File(logDir, warningLogFileName));
        
        //Create Messages Log
        String messagesLogFileName = starTimestamp + DEFAULT_MESSAGES_LOG_SUFFIX;
        messagesLogFileRef.setTextContent(messagesLogFileName);
        messagesLog = logFilesFactory.createMessagesLog(new File(logDir, messagesLogFileName));
        
        save();
    }
    
	public void addWarning(String requestMsgID, String request, String message){
        //Only one warning per request can be defined
        if(!getWarningRequestMsgIDs().contains(requestMsgID)){
            warningLog.addWarning(requestMsgID, request, message);
    
            //Create new child node for warning request
            Node warningRequest = doc.createElement(NODE_WARNING_REQUEST);
            //Append attribute with id of request
            Attr id = doc.createAttribute(ATT_ID);
            id.setTextContent(requestMsgID);
            warningRequest.getAttributes().setNamedItem(id);
            
            //Append new node to warning node
            warningNode.appendChild(warningRequest);        
            save(); //Save change
            
            notifyObservers_WarningAdded(requestMsgID);
        }
	}

    public long getWarningCount() {
        return getWarningRequestMsgIDs().size();
    }
    public Vector<String> getWarningRequestMsgIDs(){
        Vector<String> requestMsgIDs = new Vector<String>();

        //Add all warning request msg ids to vector
        for(int i = 0; i < warningNode.getChildNodes().getLength(); i++){
            Node child = warningNode.getChildNodes().item(i);
            if(child.getNodeName().equals(NODE_WARNING_REQUEST)){              
                //Add ID of request to vector
                String requestID = DomUtil.getAttributeValue(child, ATT_ID);
                if (requestID != null){
                    requestMsgIDs.add(requestID);
                }
            }
        }   
        return requestMsgIDs;
    }
    
	public void addOkay() {
        incrementOkayCount();
        save(); //Save change        
        notifyObservers_OkayAdded();
	}
    private void incrementOkayCount(){
        long count = Long.parseLong(okayCount.getTextContent());
        count++;
        okayCount.setTextContent("" + count);
    }
    public long getOkayCount() {
        long res = 0;
        String count = okayCount.getTextContent();
        if(!count.equals("")){
            res = Long.parseLong(count);
        }
        return res;
    }
    
	public void addUnknown() {
        incrementUnknownCount();
        save(); //Save change      
        notifyObservers_UnknownAdded();
	}
    private void incrementUnknownCount(){
        long count = Long.parseLong(unknownCount.getTextContent());
        count++;
        unknownCount.setTextContent("" + count);
    }
    public long getUnknownCount() {
        long res = 0;
        String count = unknownCount.getTextContent();
        if(!count.equals("")){
            res = Long.parseLong(count);
        }
        return res;    
    }
    
    private void load(){
       doc = DomUtil.loadDOMDocument(statisticsXMLFile);
       
       //File exists --> load data from document
        if (doc != null) {
            // Get the root element
            rootNode = doc.getDocumentElement();
            testCaseName = (Attr) rootNode.getAttributes().getNamedItem(ATT_NAME);
            startTimestamp = (Attr) rootNode.getAttributes().getNamedItem(ATT_STARTTIME);
            endTimestamp =(Attr) rootNode.getAttributes().getNamedItem(ATT_ENDTIME);
            cycles =(Attr) rootNode.getAttributes().getNamedItem(ATT_CYCLES);
            targetIP =(Attr) rootNode.getAttributes().getNamedItem(ATT_TARGETIP);
            targetPort =(Attr) rootNode.getAttributes().getNamedItem(ATT_TARGETPORT);
            targetUA = (Attr) rootNode.getAttributes().getNamedItem(ATT_TARGETUA);
            status =(Attr) rootNode.getAttributes().getNamedItem(ATT_STATUS);

            //Log Dir
            File logDir = statisticsXMLFile.getParentFile();
            //Create Warning Log
            warningLogFileRef = (Attr) rootNode.getAttributes().getNamedItem(ATT_WARNINGLOG);
            if(warningLogFileRef != null){
                warningLog = logFilesFactory.createWarningLog(new File(logDir, warningLogFileRef.getTextContent()));
            }
            
            //Create Messages Log
            messagesLogFileRef = (Attr) rootNode.getAttributes().getNamedItem(ATT_MSGGLOG);
            if(messagesLogFileRef != null){
                messagesLog = logFilesFactory.createMessagesLog(new File(logDir, messagesLogFileRef.getTextContent()));
            }
            
            for(int i = 0; i < rootNode.getChildNodes().getLength(); i++){
                Node child = rootNode.getChildNodes().item(i);
                if(child.getNodeName().equals(NODE_WARNING)){
                    warningNode = child;
                }
                else if (child.getNodeName().equals(NODE_OKAY)){
                    okayNode = child;
                    okayCount = (Attr) child.getAttributes().getNamedItem(ATT_COUNT);
                }
                else if(child.getNodeName().equals(NODE_UNKNOWN)){
                    unknownNode = child;
                    unknownCount = (Attr) child.getAttributes().getNamedItem(ATT_COUNT);
                }
            }
        }
        else{
            //File does not exists
            buildEmptyDOMTree();
        }
    }

    private void buildEmptyDOMTree() {
        //creat new dom tree            
        doc = DomUtil.createDocument();
        
        //Build root node (TestCaseExecution)
        rootNode = doc.createElement(NODE_ROOT);
        
        //Create attributes
        testCaseName = doc.createAttribute(ATT_NAME);
        rootNode.getAttributes().setNamedItem(testCaseName);
        
        startTimestamp = doc.createAttribute(ATT_STARTTIME);
        rootNode.getAttributes().setNamedItem(startTimestamp);
        
        endTimestamp = doc.createAttribute(ATT_ENDTIME);
        rootNode.getAttributes().setNamedItem(endTimestamp);
        
        cycles = doc.createAttribute(ATT_CYCLES);
        rootNode.getAttributes().setNamedItem(cycles);

        targetIP = doc.createAttribute(ATT_TARGETIP);
        rootNode.getAttributes().setNamedItem(targetIP);

        targetPort = doc.createAttribute(ATT_TARGETPORT);
        rootNode.getAttributes().setNamedItem(targetPort);

        targetUA = doc.createAttribute(ATT_TARGETUA);
        rootNode.getAttributes().setNamedItem(targetUA);
        
        status = doc.createAttribute(ATT_STATUS);
        rootNode.getAttributes().setNamedItem(status);

        
        warningLogFileRef = doc.createAttribute(ATT_WARNINGLOG);
        rootNode.getAttributes().setNamedItem(warningLogFileRef);
        
        messagesLogFileRef = doc.createAttribute(ATT_MSGGLOG);
        rootNode.getAttributes().setNamedItem(messagesLogFileRef);
        
        
        //Create Warning node
        warningNode = doc.createElement(NODE_WARNING);          
        
        //Create Okay node and attributes
        okayNode = doc.createElement(NODE_OKAY);
        okayCount = doc.createAttribute(ATT_COUNT);
        okayCount.setTextContent("0");  //initialize with 0
        okayNode.getAttributes().setNamedItem(okayCount);
        
        //Create Unknonwn node and attributes
        unknownNode = doc.createElement(NODE_UNKNOWN);
        unknownCount = doc.createAttribute(ATT_COUNT);
        unknownCount.setTextContent("0"); //initialize with 0
        unknownNode.getAttributes().setNamedItem(unknownCount);
        
        //Append elements to document and root-node
        doc.appendChild(rootNode);
        rootNode.appendChild(warningNode);
        rootNode.appendChild(okayNode);
        rootNode.appendChild(unknownNode);
    }

    private void save(){
        DomUtil.saveDOMDocument(statisticsXMLFile, doc);
    }
    
    public static ITestCaseRun load(File statisticsXMLFile, IHistoryLogsFactory logFilesFactory ){
        return new TestCaseRun(statisticsXMLFile, logFilesFactory);
    }

    public IMessagesLog getMessagesLog(){
        return messagesLog;
    }
    public IWarningLog getWarningLog(){
        return warningLog;
    }
    
    public String getTestCaseName() {
        return testCaseName.getTextContent();
    }
    
    
    public long getCycles() {
        long res = 0;
        String count = cycles.getTextContent();
        if(!count.equals("")){
            res = Long.parseLong(count);
        }
        return res;
    }

    public String getEndTimestamp() {
        String endTime = endTimestamp.getTextContent();
        if(!endTime.equals("")){
            endTime =MiscUtil.getTimestamp(SAVE_TIMESTAMP_FORMAT, endTime, DISPLAY_TIMESTAMP_FORMAT);
        }
        return endTime;
    }

    public String getStartTimestamp() {
        String startTime = startTimestamp.getTextContent();
        if(!startTime.equals("")){
            startTime =MiscUtil.getTimestamp(SAVE_TIMESTAMP_FORMAT, startTimestamp.getTextContent(), DISPLAY_TIMESTAMP_FORMAT);
        }
        return startTime;

    }

    public String getStatus() {
        return status.getTextContent();
    }

    public String getTargetIP() {
        return targetIP.getTextContent();
    }

    public int getTargetPort() {
        int res = 0;
        String count = cycles.getTextContent();
        if(!count.equals("")){
            res = Integer.parseInt(targetPort.getTextContent());
        }
        return res;        
    }

    public String getTargetUA() {
        return targetUA.getTextContent();
    }

    public void setTargetUA(String targetUA) {
        this.targetUA.setTextContent(targetUA);
        save();
        notifyObservers_TargetUAChanged();
        
    }    

    public void setCycles( long cycles ) {
        this.cycles.setTextContent("" + cycles);
        save();
        notifyObservers_CyclesChanged();
    }

    public void setEndTimestamp( String timestamp ) {
        endTimestamp.setTextContent(timestamp);
        save();
        notifyObservers_EndTimestampChanged();        
    }

    public void setStatus( String status ) {
        this.status.setTextContent(status);
        save();
        notifyObservers_StatusChanged();
        
        if(!status.equals(STATUS_RUNNING)){
            notifyObservers_Finished();            
        }
    }
    
    public String toString(){
        return startTimestamp.getTextContent() + " " + testCaseName.getTextContent();
    }

    public void addTestCaseRunObserver( ITestCaseRunObserver observer ) {
        synchronized (testCaseRunObservers) {
            if(!testCaseRunObservers.contains(observer)){            
                testCaseRunObservers.add(observer);
            }
        }
    }

    public void removeTestCaseRunObserver( ITestCaseRunObserver observer ) {
        synchronized (testCaseRunObservers) {
            if(testCaseRunObservers.contains(observer)){            
                testCaseRunObservers.remove(observer);
            }
        }
    }
    
    private void notifyObservers_WarningAdded(String requestMsgID){
        synchronized (testCaseRunObservers) {
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.warningAdded(this, requestMsgID);
            }            
        }

    }
    
    private void notifyObservers_OkayAdded(){
        synchronized (testCaseRunObservers) {
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.okayAdded();
            }
        }
    }
    
    private void notifyObservers_UnknownAdded(){
        synchronized (testCaseRunObservers) {        
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.unknownAdded();
            }
        }
    }
    private void notifyObservers_EndTimestampChanged(){
        synchronized (testCaseRunObservers) {        
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.endTimestampChanged();
            }
        }
    }
    private void notifyObservers_StatusChanged(){
        synchronized (testCaseRunObservers) {        
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.statusChanged();
            }
        }
    }    
    
    private void notifyObservers_TargetUAChanged(){
        synchronized (testCaseRunObservers) {        
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.targetUAChanged();
            }
        }
    }    
    
    private void notifyObservers_CyclesChanged(){
        synchronized (testCaseRunObservers) {        
            for(ITestCaseRunObserver observer : testCaseRunObservers){
                observer.cyclesChanged();
            }
        }
    }

    public void addTestCaseExecutionObserver( ITestCaseExecutionObserver observer ) {
        synchronized (executionObservers) {
            if(!executionObservers.contains(observer)){
                executionObservers.add(observer);
            }
        }
        
    }

    public void removeTestCaseExecutionObserver( ITestCaseExecutionObserver observer ) {
        synchronized (executionObservers) {
            if(executionObservers.contains(observer)){
                executionObservers.remove(observer);
            }
        }
    }    
    private void notifyObservers_Finished(){
        synchronized (executionObservers) {

            for(ITestCaseExecutionObserver observer : executionObservers){
                observer.testRunFinished();
            }
        }
        //remove all observers since execution has finished
        executionObservers.clear();
    }

    public void delete() {
        //Delete all dependent log files
        statisticsXMLFile.delete();
        warningLog.delete();
        messagesLog.delete();
    }

    
}
 
