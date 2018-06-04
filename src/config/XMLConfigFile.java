package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.SimpleLogger;

import config.transformation.XMLTransformationConfigFile;

public abstract class XMLConfigFile {

    private static final String A_IP = "ip";
    private static final String A_PORT = "port";
    private static final String A_FILEREF = "fileRef";
    private static final String A_PATH = "path";

    private static final String N_ROOT = "SIPProxyConfig";
    private static final String N_PROXYMODE = "ProxyMode";
    private static final String N_TESTCASEMODE = "TestCaseMode";
    
    //Proxy Mode Nodes
    private static final String N_PROXYSOCKET = "ProxySocket";
    private static final String N_PBX = "PBX";
    private static final String N_CLIENT = "Client";
    private static final String N_MSGTRANS = "Transformation";
    private static final String N_DYNTRANS = "DynamicTransformation";
    
    //TestCase Mode Nodes
    private static final String N_TESTCASESOCKET = "TestCaseSocket";
    private static final String N_TARGET= "Target";    
    private static final String N_TESTCASEDIR = "TestCaseDir";

    public static Configuration load( String filename ) {
        // Loads a Configuration from the file

        Configuration config = new Configuration();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }

        // Document Object for the DOM-Tree
        Document doc = null;

        if (builder != null) {
            try {
                // Try to build the DOM-Tree
                doc = builder.parse(new File(filename));
            }
            catch (Exception e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }

        if (doc != null) {
            Node root = doc.getDocumentElement();
            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node child = root.getChildNodes().item(i);
                if(child.getNodeName().equals(N_PROXYMODE)){
                    loadProxyModeNode(child, config);
                }
                else if(child.getNodeName().equals(N_TESTCASEMODE)){
                    loadTestCaseModeNode(child, config);
                }
            }
        }

        return config;
    }
    private static void loadProxyModeNode(Node parent, Configuration config){
        // Handle all Child-Nodes of the ProxyMode-Node
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node child = parent.getChildNodes().item(i);
            if (child.getNodeName().equals(N_PROXYSOCKET)) {
                // PROXY Element
                try {
                    config.setProxySocketIP(InetAddress.getByName(child.getAttributes()
                            .getNamedItem(A_IP).getNodeValue()));
                    config.setProxySocketPort(Integer.parseInt(child.getAttributes()
                            .getNamedItem(A_PORT).getNodeValue()));
                }
                catch (UnknownHostException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                catch (DOMException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
            }
            else if (child.getNodeName().equals(N_PBX)) {
                // PBX Element
                try {
                    config.setPbxIP(InetAddress.getByName(child.getAttributes()
                            .getNamedItem(A_IP).getNodeValue()));
                    config.setPbxPort(Integer.parseInt(child.getAttributes()
                            .getNamedItem(A_PORT).getNodeValue()));
                }
                catch (UnknownHostException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                catch (DOMException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
            }
            else if (child.getNodeName().equals(N_CLIENT)) {
                // CLIENT Element
                try {
                    config.setClientIP(InetAddress.getByName(child.getAttributes()
                            .getNamedItem(A_IP).getNodeValue()));
                    config.setClientPort(Integer.parseInt(child.getAttributes()
                            .getNamedItem(A_PORT).getNodeValue()));
                }
                catch (UnknownHostException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                catch (DOMException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
            }
            else if (child.getNodeName().equals(N_MSGTRANS)) {
                // TRANSFORMATION Element
                config.setTransormationConfigFileRef(child.getAttributes()
                        .getNamedItem(A_FILEREF).getNodeValue());
            }
            else if (child.getNodeName().equals(N_DYNTRANS)) {
                // DYNAMIC TRANSFORMATION Element
                config.setDynamicTransformationFileRef(child.getAttributes()
                        .getNamedItem(A_FILEREF).getNodeValue());
            }
        }
    }
    private static void loadTestCaseModeNode(Node parent, Configuration config){
        // Handle all Child-Nodes of the TestCase-Node
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node child = parent.getChildNodes().item(i);
            if (child.getNodeName().equals(N_TESTCASESOCKET)) {
                // TEST CASE SOCKET Element
                try {
                    config.setTestCaseSocketIP(InetAddress.getByName(child.getAttributes()
                            .getNamedItem(A_IP).getNodeValue()));
                    config.setTestCaseSocketPort(Integer.parseInt(child.getAttributes()
                            .getNamedItem(A_PORT).getNodeValue()));
                }
                catch (UnknownHostException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                catch (DOMException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
            }
            else if (child.getNodeName().equals(N_TARGET)){
                // TARGET Element
                try {
                    config.setTargetIP(InetAddress.getByName(child.getAttributes()
                            .getNamedItem(A_IP).getNodeValue()));
                    config.setTargetPort(Integer.parseInt(child.getAttributes()
                            .getNamedItem(A_PORT).getNodeValue()));
                }
                catch (UnknownHostException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
                catch (DOMException e) {
                    SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
                }
            }
            else if (child.getNodeName().equals(N_TESTCASEDIR)){
                //TEST CASE DIR Element
                config.setTestCaseDirPath(child.getAttributes()
                        .getNamedItem(A_PATH).getNodeValue());
            }
        }
    }
    
    public static void save( Configuration config, String filename ) {
        // Method to save a configuration in a File
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Document Object for DOM-Tree
            Document doc = builder.newDocument();
            Node root = doc.createElement(N_ROOT);

            //PROXY Mode
            Node n_proxyMode =doc.createElement(N_PROXYMODE);
            appendProxyModeChildNodes(doc, n_proxyMode, config);
            
            //TESTCASE Mode
            Node n_testCaseMode = doc.createElement(N_TESTCASEMODE);
            appendTestCaseModeChildNodes(doc,n_testCaseMode, config);
            
            //Append Child nodes of Root
            root.appendChild(n_proxyMode);
            root.appendChild(n_testCaseMode);
            doc.appendChild(root);

            // Settings for the file output
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            // Save DOM-Tree to file
            FileOutputStream os = new FileOutputStream(new File(filename));
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
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
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
    }
    private static void appendProxyModeChildNodes(Document doc, Node parent, Configuration config ) {
        // PROXYSOCKET ELEMENT
        Node n_proxy = doc.createElement(N_PROXYSOCKET);
        Attr proxyIP = doc.createAttribute(A_IP);
        proxyIP.setTextContent(config.getProxySocketIP().getHostAddress());
        Attr proxyPort = doc.createAttribute(A_PORT);
        proxyPort.setTextContent("" + config.getProxySocketPort());
        n_proxy.getAttributes().setNamedItem(proxyIP);
        n_proxy.getAttributes().setNamedItem(proxyPort);

        // PBX ELEMENT
        Node n_pbx = doc.createElement(N_PBX);
        Attr pbxIP = doc.createAttribute(A_IP);
        pbxIP.setTextContent(config.getPbxIP().getHostAddress());
        Attr pbxPort = doc.createAttribute(A_PORT);
        pbxPort.setTextContent("" + config.getPbxPort());
        n_pbx.getAttributes().setNamedItem(pbxIP);
        n_pbx.getAttributes().setNamedItem(pbxPort);

        // Client ELEMENT
        Node n_client = doc.createElement(N_CLIENT);
        Attr clientIP = doc.createAttribute(A_IP);
        clientIP.setTextContent(config.getClientIP().getHostAddress());
        Attr clientPort = doc.createAttribute(A_PORT);
        clientPort.setTextContent("" + config.getClientPort());
        n_client.getAttributes().setNamedItem(clientIP);
        n_client.getAttributes().setNamedItem(clientPort);

        // MESSAGETRANSFORMER ELEMENT
        Node n_msgTrans = doc.createElement(N_MSGTRANS);
        Attr fileRef = doc.createAttribute(A_FILEREF);
        fileRef.setTextContent(config.getTransformationConfigFileRef());
        n_msgTrans.getAttributes().setNamedItem(fileRef);

        // DYNAMICTRANSFORMER ELEMENT
        Node n_dynTrans = doc.createElement(N_DYNTRANS);
        Attr dynFileRef = doc.createAttribute(A_FILEREF);
        dynFileRef.setTextContent(config.getDynamicTransformationConfigFileRef());
        n_dynTrans.getAttributes().setNamedItem(dynFileRef);

        // Save Dynamic Transformation-Rules to File
        XMLTransformationConfigFile.save(config.getDynamicTransformationConfig(),
                config.getDynamicTransformationConfigFileRef());

        // Append Child-Objects to ROOT-Element
        parent.appendChild(n_pbx);
        parent.appendChild(n_proxy);
        parent.appendChild(n_client);
        parent.appendChild(n_msgTrans);
        parent.appendChild(n_dynTrans);
        
    }
    
    private static void appendTestCaseModeChildNodes( Document doc, Node parent, Configuration config ) {
        // TESTFCASESOCKET ELEMENT
        Node n_testCaseSocket = doc.createElement(N_TESTCASESOCKET);
        Attr testCaseSocketIP = doc.createAttribute(A_IP);
        testCaseSocketIP.setTextContent(config.getTestCaseSocketIP().getHostAddress());
        Attr testCaseSocketPort = doc.createAttribute(A_PORT);
        testCaseSocketPort.setTextContent("" + config.getTestCaseSocketPort());
        n_testCaseSocket.getAttributes().setNamedItem(testCaseSocketIP);
        n_testCaseSocket.getAttributes().setNamedItem(testCaseSocketPort);

        // TARGET ELEMENT
        Node n_target = doc.createElement(N_TARGET);
        Attr targetIP = doc.createAttribute(A_IP);
        targetIP.setTextContent(config.getTargetIP().getHostAddress());
        Attr targetPort = doc.createAttribute(A_PORT);
        targetPort.setTextContent("" + config.getTargetPort());
        n_target.getAttributes().setNamedItem(targetIP);
        n_target.getAttributes().setNamedItem(targetPort);
        
        // TESTCASEDIR ELEMENT
        Node n_testCaseDir = doc.createElement(N_TESTCASEDIR);
        Attr path = doc.createAttribute(A_PATH);
        path.setTextContent(config.getTestCaseDirPath());
        n_testCaseDir.getAttributes().setNamedItem(path);

        // Append Child-Objects to ROOT-Element
        parent.appendChild(n_testCaseSocket);
        parent.appendChild(n_target);
        parent.appendChild(n_testCaseDir);
    }

}
