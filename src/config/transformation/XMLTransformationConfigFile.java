package config.transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Vector;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.SimpleLogger;

public abstract class XMLTransformationConfigFile {

    private static final String A_NAME = "name";
    private static final String A_VALUE = "value";
    private static final String A_REG = "regex";
    private static final String A_REP = "replacement";
    private static final String A_ACTIVE = "isActive";

    private static final String N_ROOT = "TransformationConfig";
    private static final String N_DEF = "Definitions";
    private static final String N_VAR = "Var";
    private static final String N_TRANSFORMATION_RULES = "TransformationRules";
    private static final String N_P2C = "Pbx2Client";
    private static final String N_C2P = "Client2Pbx";
    private static final String N_RULE = "Rule";

    public static TransformationConfig load( String file ) {
        return load(file, null);
    }

    public static TransformationConfig load( String file,
            Map<String, String> defaultDefinitions ) {

        //Load Transformation Config from File

        TransformationConfig transformationConfig;
        if (defaultDefinitions != null) {
            transformationConfig = new TransformationConfig(defaultDefinitions);
        }
        else {
            transformationConfig = new TransformationConfig();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }

        //Document Object for the DOM-Tree
        Document doc = null;

        if (builder != null) {
            try {
                //Try to build the DOM-Tree                
                doc = builder.parse(new File(file));
            }
            catch (Exception e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            }
        }

        if (doc != null) {
            Node root = doc.getDocumentElement();

            //Handle all Child-Nodes of the Root-element
            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node child = root.getChildNodes().item(i);
                if (child.getNodeName().equals(N_DEF)) {
                    //DEFINITON Element
                    loadDefinitions(child, transformationConfig);
                }
                else if (child.getNodeName().equals(N_TRANSFORMATION_RULES)) {
                    //TRANSFORMATION RULE Element
                    loadTransformationRules(child, transformationConfig);
                }
            }
        }

        return transformationConfig;
    }

    private static void loadDefinitions( Node definitions,
            TransformationConfig transformationConfig ) {
        //Load Definiton from Node
        for (int i = 0; i < definitions.getChildNodes().getLength(); i++) {
            Node variable = definitions.getChildNodes().item(i);
            if (variable.getNodeName().equals(N_VAR)) {
                String name = variable.getAttributes().getNamedItem(A_NAME)
                        .getNodeValue();
                String value = variable.getAttributes().getNamedItem(A_VALUE)
                        .getNodeValue();
                transformationConfig.addDefinition(name, value);
            }
        }
    }

    private static void loadTransformationRules( Node transformationRules,
            TransformationConfig transformationConfig ) {
        //For all Rules (Child-Nodes)
        for (int j = 0; j < transformationRules.getChildNodes().getLength(); j++) {
            Node transformationChild = transformationRules.getChildNodes().item(j);
            if (transformationChild.getNodeName().equals(N_P2C)) {
                //PBX to Client Rule
                loadPbx2ClientRules(transformationChild, transformationConfig);
            }
            else if (transformationChild.getNodeName().equals(N_C2P)) {
                //Client to PBX Rule
                loadClient2PbxRules(transformationChild, transformationConfig);
            }
        }
    }

    private static void loadPbx2ClientRules( Node pbx2ClientRules,
            TransformationConfig transformationConfig ) {
        Vector<RegexRule> rules = getRulesFromNode(pbx2ClientRules);
        for (RegexRule rule : rules) {
            transformationConfig.addPbx2ClientRule(rule);
        }
    }

    private static void loadClient2PbxRules( Node client2PbxRules,
            TransformationConfig transformationConfig ) {
        Vector<RegexRule> rules = getRulesFromNode(client2PbxRules);
        for (RegexRule rule : rules) {
            transformationConfig.addClient2PbxRule(rule);
        }

    }

    private static Vector<RegexRule> getRulesFromNode( Node parent ) {
        //Load Rule from Node
        Vector<RegexRule> rules = new Vector<RegexRule>();
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node rule = parent.getChildNodes().item(i);
            if (rule.getNodeName().equals(N_RULE)) {
                //Get regex and replacement from Node-Attributes
                String regex = rule.getAttributes().getNamedItem(A_REG).getNodeValue();
                String replacement = rule.getAttributes().getNamedItem(A_REP)
                        .getNodeValue();
                boolean isActive = Boolean.parseBoolean(rule.getAttributes()
                        .getNamedItem(A_ACTIVE).getNodeValue());
                rules.add(new RegexRule(regex, replacement, isActive));
            }
        }
        return rules;
    }

    public static void save( TransformationConfig transformationConfig, String file ) {
        //Save Transformation-Config to file
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Node root = doc.createElement(N_ROOT);

            //PROXY ELEMENT
            Node n_def = doc.createElement(N_DEF);
            Node n_rules = doc.createElement(N_TRANSFORMATION_RULES);

            appendDefinitions(doc, transformationConfig, n_def);
            appendTransformationRules(doc, transformationConfig, n_rules);

            root.appendChild(n_def);
            root.appendChild(n_rules);

            doc.appendChild(root);

            Transformer transformer;
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            FileOutputStream os = new FileOutputStream(new File(file));
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);

        }
        catch (ParserConfigurationException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
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
    }

    private static void appendDefinitions( Document doc,
            TransformationConfig transformationConfig, Node definitions ) {
        //Append all definitions to definitons-Node
        for (String key : transformationConfig.definitions.keySet()) {
            Node definitionNode = doc.createElement(N_VAR); //new Node for definition

            //Add Attributes for name and value of definition to new Node
            Attr aName = doc.createAttribute(A_NAME);
            aName.setTextContent(key);

            Attr aValue = doc.createAttribute(A_VALUE);
            aValue.setTextContent(transformationConfig.getDefinition(key));

            definitionNode.getAttributes().setNamedItem(aName);
            definitionNode.getAttributes().setNamedItem(aValue);

            //Attach new definiton to definitions-Node
            definitions.appendChild(definitionNode);
        }
    }

    private static void appendTransformationRules( Document doc,
            TransformationConfig transformationConfig, Node n_rules ) {
        //Add all Ruels to rules-Node
        Node n_p2c = doc.createElement(N_P2C);
        appendRules(doc, transformationConfig.getPbx2ClientRules(), n_p2c);
        Node n_c2p = doc.createElement(N_C2P);
        appendRules(doc, transformationConfig.getClient2PbxRules(), n_c2p);

        n_rules.appendChild(n_p2c);
        n_rules.appendChild(n_c2p);

    }

    private static void appendRules( Document doc, Vector<RegexRule> rules, Node rulesNode ) {
        for (RegexRule rule : rules) {
            Node n_rule = doc.createElement(N_RULE); //Create new Node for Rule

            //Add Attributes for regex and replacement of Rule to new Node
            Attr aRegex = doc.createAttribute(A_REG);
            aRegex.setTextContent(rule.getRegex());

            Attr aReplacement = doc.createAttribute(A_REP);
            aReplacement.setTextContent(rule.getReplacement());

            Attr aIsActive = doc.createAttribute(A_ACTIVE);
            aIsActive.setTextContent(Boolean.toString(rule.isActive()));

            n_rule.getAttributes().setNamedItem(aRegex);
            n_rule.getAttributes().setNamedItem(aReplacement);
            n_rule.getAttributes().setNamedItem(aIsActive);

            //Attach rule to rules-Node
            rulesNode.appendChild(n_rule);
        }

    }

}
