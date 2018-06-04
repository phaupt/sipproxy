package junit.config.transformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import junit.framework.TestCase;
import config.Configuration;
import config.XMLConfigFile;
import config.transformation.RegexRule;
import config.transformation.TransformationConfig;
import config.transformation.XMLTransformationConfigFile;

public class TestXMLTransformationConfigFile extends TestCase {

    String testFilename = "testTConfig.xml";
    String testFilename2 = "testTConfig2.xml";
    String testConfigfile = "src/junit/config/transformation/testConfig.xml";

    private static String varName1 = "pbxDomainname";
    private static String varValue1 = "proxy.mysipnet.ch";
    private static String varName2 = "ProxyIP";
    private static String varValue2 = "192.168.0.10";

    private static String regex1 = "(From:\\s)(.*)(sip:)(.*)(@)(/'ProxyIP'/)";
    private static String replaced_regex1 = "(From:\\s)(.*)(sip:)(.*)(@)(" + varValue2
            + ")";
    private static String replacement1 = "$1$2$3$4$5/'PbxIP'/$7";
    private static String regex2 = "(From: )(.*)(sip:)(.*)(@)(/'PbxIP'/)";
    private static String replacement2 = "$1$2$3$4$5/'ProxyIP'/$7";
    private static String replaced_replacement2 = "$1$2$3$4$5" + varValue2 + "$7";

    public static String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<TransformationConfig>\r\n" + "<Definitions>\r\n" + "<Var name=\""
            + varName2 + "\" value=\"" + varValue2 + "\"/>\r\n" + "<Var name=\""
            + varName1 + "\" value=\"" + varValue1 + "\"/>\r\n" + "</Definitions>\r\n"
            + "<TransformationRules>\r\n" + "<Pbx2Client>\r\n"
            + "<Rule isActive=\"true\" regex=\"" + regex1 + "\" replacement=\""
            + replacement1 + "\"/>\r\n" + "</Pbx2Client>\r\n" + "<Client2Pbx>\r\n"
            + "<Rule isActive=\"true\" regex=\"" + regex2 + "\" replacement=\""
            + replacement2 + "\"/>\r\n" + "<Rule isActive=\"false\" regex=\"" + regex2
            + "\" replacement=\"" + replacement2 + "\"/>\r\n" + "</Client2Pbx>\r\n"
            + "</TransformationRules>\r\n" + "</TransformationConfig>\r\n";

    public void setUp() {
        writeToFile(testData, testFilename);
    }

    public void testDefaultDefinitions() {
        Configuration config = XMLConfigFile.load(testConfigfile);
        TransformationConfig tConfig = config.getTransformationConfig();
        assertEquals(config.getClientIP().getHostAddress(), tConfig
                .getDefinition("ClientIP"));
        assertEquals(config.getClientPort(), Integer.parseInt(tConfig
                .getDefinition("ClientPort")));
        assertEquals(config.getPbxIP().getHostAddress(), tConfig.getDefinition("PbxIP"));
        assertEquals(config.getPbxPort(), Integer.parseInt(tConfig
                .getDefinition("PbxPort")));
        assertEquals(config.getProxySocketIP().getHostAddress(), tConfig
                .getDefinition("ProxyIP"));
        assertEquals(config.getProxySocketPort(), Integer.parseInt(tConfig
                .getDefinition("ProxyPort")));
    }

    public void testDefinitions() {
        String n1 = "nameDef1";
        String v2 = "value 2";

        TransformationConfig tConfig = XMLConfigFile.load(testConfigfile)
                .getTransformationConfig();
        assertEquals(6, tConfig.getDefinitionsSize());
        tConfig.addDefinition(n1, v2);
        assertEquals(7, tConfig.getDefinitionsSize());
        assertEquals(v2, tConfig.getDefinition(n1));
    }

    public void testLoad() {
        TransformationConfig transformationConfig = XMLTransformationConfigFile
                .load(testFilename);

        assertEquals(2, transformationConfig.getDefinitionsSize());
        String value1 = transformationConfig.getDefinition(varName1);
        assertEquals(varValue1, value1);
        String value2 = transformationConfig.getDefinition(varName2);
        assertEquals(varValue2, value2);

        Vector<RegexRule> rules = transformationConfig.getReplacedPbx2ClientRules();
        assertEquals(1, rules.size());
        RegexRule rule = rules.get(0);
        assertEquals(replaced_regex1, rule.getRegex());
        assertEquals(replacement1, rule.getReplacement());
        assertEquals(true, rule.isActive());

        rules = transformationConfig.getReplacedClient2PbxRules();
        assertEquals(2, rules.size());
        rule = rules.get(0);
        assertEquals(regex2, rule.getRegex());
        assertEquals(replaced_replacement2, rule.getReplacement());
        assertEquals(true, rule.isActive());

        rule = rules.get(1);
        assertEquals(regex2, rule.getRegex());
        assertEquals(replaced_replacement2, rule.getReplacement());
        assertEquals(false, rule.isActive());
    }

    public void testSave() {
        TransformationConfig config = new TransformationConfig();
        config.addDefinition(varName2, varValue2);
        config.addDefinition(varName1, varValue1);
        config.getPbx2ClientRules().add(new RegexRule(regex1, replacement1, true));
        config.getClient2PbxRules().add(new RegexRule(regex2, replacement2, true));
        config.getClient2PbxRules().add(new RegexRule(regex2, replacement2, false));

        XMLTransformationConfigFile.save(config, testFilename2);
        compareContent(testFilename, testFilename2);
    }

    public static void writeToFile( String data, String filename ) {
        BufferedWriter resWriter;
        try {
            resWriter = new BufferedWriter(new FileWriter(new File(filename), false));
            resWriter.write(data);
            resWriter.flush();
            resWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void compareContent( String file1, String file2 ) {
        File f1 = new File(file1);
        File f2 = new File(file2);
        assertEquals(f1.length(), f2.length());
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            try {
                String s = null;
                while ((s = br1.readLine()) != null) {
                    assertEquals(s, br2.readLine());
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
