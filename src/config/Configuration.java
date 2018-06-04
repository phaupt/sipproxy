package config;

import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Map;

import config.transformation.TransformationConfig;
import config.transformation.XMLTransformationConfigFile;

public class Configuration {

    //Proxy Mode
    public static final String varProxySocketIP = "ProxySocketIP";
    private InetAddress proxySocketIP = null;
    public static final String varProxySocketPort = "ProxySocketPort";
    private int proxySocketPort = -1;
    public static final String varPbxIP = "PbxIP";
    private InetAddress pbxIP = null;
    public static final String varPbxPort = "PbxPort";
    private int pbxPort = -1;
    public static final String varClientIP = "ClientIP";
    private InetAddress clientIP = null;
    public static final String varClientPort = "ClientPort";
    private int clientPort = -1;
    private String transformationConfigFileRef = null;
    private TransformationConfig transformationConfig = new TransformationConfig();
    private String dynamicTransformationConfigFileRef = null;
    private TransformationConfig dynamicTransformationConfig = new TransformationConfig();
    
    //TestCase Mode
    public static final String varTestCaseSocketIP = "TestCaseSocketIP";
    private InetAddress testCaseSocketIP = null;
    public static final String varTestCaseSocketPort = "TestCaseSocketPort";
    private int testCaseSocketPort = -1;
    public static final String varTargetIP = "TargetIP";
    private InetAddress targetIP = null;
    public static final String varTargetPort = "TargetPort";
    private int targetPort = -1;
    private String testCaseDirPath = null;

    private Map<String, String> defaultDefinitions = new Hashtable<String, String>();
    
    
    public Configuration() {
    }

    public Configuration(InetAddress proxyIP, int proxyPort, InetAddress pbxIP,
            int pbxPort, InetAddress clientIP, int clientPort,
            String transformationConfigFileRef, String dynamicTransformationConfigFileRef,
            InetAddress testCaseSocketIP, int testCaseSocketPort, InetAddress targetIP,
            int targetPort, String testCaseDirPath) {
        //Proxy Mode
        setProxySocketIP(proxyIP);
        setProxySocketPort(proxyPort);
        setPbxIP(pbxIP);
        setPbxPort(pbxPort);
        setClientIP(clientIP);
        setClientPort(clientPort);

        setTransormationConfigFileRef(transformationConfigFileRef);
        setDynamicTransformationFileRef(dynamicTransformationConfigFileRef);
        
        //TestCase Mode
        setTestCaseSocketIP(testCaseSocketIP);
        setTestCaseSocketPort(testCaseSocketPort);
        setTargetIP(targetIP);
        setTargetPort(targetPort);
        setTestCaseDirPath(testCaseDirPath);
    }

    public InetAddress getPbxIP() {
        return pbxIP;
    }

    public int getPbxPort() {
        return pbxPort;
    }

    public InetAddress getProxySocketIP() {
        return proxySocketIP;
    }

    public int getProxySocketPort() {
        return proxySocketPort;
    }

    public void setTransormationConfigFileRef( String fileRef ) {
        transformationConfigFileRef = fileRef;
        if (transformationConfigFileRef != null) {
            //Load the new TransformationConfig-File
            transformationConfig = XMLTransformationConfigFile.load(
                    transformationConfigFileRef, defaultDefinitions);
        }
    }

    public TransformationConfig getTransformationConfig() {
        return transformationConfig;
    }

    public String getTransformationConfigFileRef() {
        return transformationConfigFileRef;
    }

    public void setDynamicTransformationFileRef( String fileRef ) {
        dynamicTransformationConfigFileRef = fileRef;
        if (dynamicTransformationConfigFileRef != null) {
            //Load the new DynamicTransformationConfig-File         
            dynamicTransformationConfig = XMLTransformationConfigFile.load(
                    dynamicTransformationConfigFileRef, defaultDefinitions);
        }
    }

    public TransformationConfig getDynamicTransformationConfig() {
        return dynamicTransformationConfig;
    }

    public String getDynamicTransformationConfigFileRef() {
        return dynamicTransformationConfigFileRef;
    }

    public void setPbxIP( InetAddress pbxIP ) {
        defaultDefinitions.put(varPbxIP, pbxIP.getHostAddress());
        this.pbxIP = pbxIP;
    }

    public void setPbxPort( int pbxPort ) {
        defaultDefinitions.put(varPbxPort, new Integer(pbxPort).toString());
        this.pbxPort = pbxPort;
    }

    public void setProxySocketIP( InetAddress proxyIP ) {
        defaultDefinitions.put(varProxySocketIP, proxyIP.getHostAddress());
        this.proxySocketIP = proxyIP;
    }

    public void setProxySocketPort( int proxyPort ) {
        defaultDefinitions.put(varProxySocketPort, new Integer(proxyPort).toString());
        this.proxySocketPort = proxyPort;
    }

    public InetAddress getClientIP() {
        return clientIP;
    }

    public void setClientIP( InetAddress clientIP ) {
        defaultDefinitions.put(varClientIP, clientIP.getHostAddress());
        this.clientIP = clientIP;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort( int clientPort ) {
        defaultDefinitions.put(varClientPort, new Integer(clientPort).toString());
        this.clientPort = clientPort;
    }

    
    public InetAddress getTargetIP() {
        return targetIP;
    }

    
    public void setTargetIP( InetAddress targetIP ) {
        defaultDefinitions.put(varTargetIP, targetIP.getHostAddress());
        this.targetIP = targetIP;
    }

    
    public int getTargetPort() {
        return targetPort;
    }

    
    public void setTargetPort( int targetPort ) {
        defaultDefinitions.put(varTargetPort, new Integer(targetPort).toString());
        this.targetPort = targetPort;
    }

    
    public String getTestCaseDirPath() {
        return testCaseDirPath;
    }

    
    public void setTestCaseDirPath( String testCaseDirPath ) {
        this.testCaseDirPath = testCaseDirPath;
    }

    
    public InetAddress getTestCaseSocketIP() {
        return testCaseSocketIP;
    }

    
    public void setTestCaseSocketIP( InetAddress testCaseSocketIP ) {
        defaultDefinitions.put(varTestCaseSocketIP, testCaseSocketIP.getHostAddress());
        this.testCaseSocketIP = testCaseSocketIP;
    }

    
    public int getTestCaseSocketPort() {
        return testCaseSocketPort;
    }

    
    public void setTestCaseSocketPort( int testCaseSocketPort ) {
        defaultDefinitions.put(varTestCaseSocketPort, new Integer(testCaseSocketPort).toString());
        this.testCaseSocketPort = testCaseSocketPort;
    }

}
