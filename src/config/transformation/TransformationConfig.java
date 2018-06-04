package config.transformation;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class TransformationConfig {

    public static final String DEF_OPEN = "/'";
    public static final String DEF_END = "'/";

    protected Vector<RegexRule> pbx2ClientRules = new Vector<RegexRule>();
    protected Vector<RegexRule> client2PbxRules = new Vector<RegexRule>();

    protected Map<String, String> defaultDefinitions = new Hashtable<String, String>();
    protected Map<String, String> definitions = new Hashtable<String, String>();

    public TransformationConfig() {
    }

    public TransformationConfig(Map<String, String> defaultDefinitions) {
        // this.defaultDefinitions.putAll(defaultDefinitions);
        this.defaultDefinitions = defaultDefinitions;
    }

    public void addPbx2ClientRule( RegexRule rule ) {
        // Add a Rule which can be applied for Messages from the Pbx to the Client
        pbx2ClientRules.add(rule);
    }

    public void addClient2PbxRule( RegexRule rule ) {
        // Add a Rule which can be applied for Messages from the Cient to the Pbx
        client2PbxRules.add(rule);
    }

    public void addDefinition( String name, String value ) {
        // Add a Definition which will be replaced in added Rules
        definitions.put(name, value);
    }

    public int getDefinitionsSize() {
        return defaultDefinitions.size() + definitions.size();
    }

    public String getDefinition( String name ) {
        String value = null;
        value = definitions.get(name);
        if (value == null) {
            value = defaultDefinitions.get(name);
        }
        return value;
    }

    public Vector<RegexRule> getClient2PbxRules() {
        return client2PbxRules;
    }

    public Vector<RegexRule> getReplacedClient2PbxRules() {
        // Return Rules with replacements
        Vector<RegexRule> replacedClient2PbxRules = new Vector<RegexRule>();
        for (RegexRule rule : client2PbxRules) {
            replacedClient2PbxRules.add(getReplacedRule(rule));
        }
        return replacedClient2PbxRules;
    }

    public Vector<RegexRule> getPbx2ClientRules() {
        return pbx2ClientRules;
    }

    public Vector<RegexRule> getReplacedPbx2ClientRules() {
        // Return Rules with replacements
        Vector<RegexRule> replacedPbx2ClientRules = new Vector<RegexRule>();
        for (RegexRule rule : pbx2ClientRules) {
            replacedPbx2ClientRules.add(getReplacedRule(rule));
        }
        return replacedPbx2ClientRules;
    }

    private RegexRule getReplacedRule( RegexRule rule ) {
        // Returns a new Rule with replaced Regex and Replacement
        String regex = rule.getRegex();
        String replacement = rule.getReplacement();
        for (String defName : definitions.keySet()) {
            regex = regex.replaceAll(TransformationConfig.DEF_OPEN + defName
                    + TransformationConfig.DEF_END, definitions.get(defName));
            replacement = replacement.replaceAll(TransformationConfig.DEF_OPEN + defName
                    + TransformationConfig.DEF_END, definitions.get(defName));
        }
        for (String defName : defaultDefinitions.keySet()) {
            regex = regex.replaceAll(TransformationConfig.DEF_OPEN + defName
                    + TransformationConfig.DEF_END, defaultDefinitions.get(defName));
            replacement = replacement.replaceAll(TransformationConfig.DEF_OPEN + defName
                    + TransformationConfig.DEF_END, defaultDefinitions.get(defName));
        }
        return new RegexRule(regex, replacement, rule.isActive());
    }

}
