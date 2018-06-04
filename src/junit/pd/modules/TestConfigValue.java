package junit.pd.modules;

import config.Configuration;
import config.XMLConfigFile;
import pd.modules.ConfigValue;

public class TestConfigValue extends junit.framework.TestCase {
        
    private  ConfigValue configValue1;
    private  ConfigValue configValue2;
    private  ConfigValue configValue3;
    private  ConfigValue configValue4;
    private  ConfigValue configValue5;
    private  ConfigValue configValue6;
    private  ConfigValue configValue7;
    private  ConfigValue configValue8;
    private  ConfigValue configValue9;
    private  ConfigValue configValue10;

    public TestConfigValue(){
        configValue1 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varClientIP);
        configValue2 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varClientPort);
        configValue3 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varPbxIP);
        configValue4 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varPbxPort);
        configValue5 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varProxySocketIP);
        configValue6 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varProxySocketPort);
        configValue7 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varTargetIP);
        configValue8 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varTargetPort);
        configValue9 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varTestCaseSocketIP);
        configValue10 = new ConfigValue(XMLConfigFile.load("src/junit/pd/modules/testConfig.xml"),Configuration.varTestCaseSocketPort);
    }
    
    public void testValue(){
        assertEquals("192.168.0.3",configValue1.getValue(null));
        assertEquals("5063",configValue2.getValue(null));
        assertEquals("192.168.0.1",configValue3.getValue(null));
        assertEquals("5061",configValue4.getValue(null));
        assertEquals("192.168.0.2",configValue5.getValue(null));
        assertEquals("5062",configValue6.getValue(null));
        assertEquals("192.168.0.5",configValue7.getValue(null));
        assertEquals("5065",configValue8.getValue(null));
        assertEquals("192.168.0.4",configValue9.getValue(null));
        assertEquals("5064",configValue10.getValue(null));

    }  
    
}
