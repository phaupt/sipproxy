package transforming;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proxy.UDPDatagram;
import config.Configuration;
import config.transformation.RegexRule;

public class PacketRegexTransformer implements ITransformer {

    private Configuration config;

    public PacketRegexTransformer(Configuration config) {
        this.config = config;
    }

    public void transformMessage( UDPDatagram packet ) {
        // Transforms Message

        if (packet.getSrcIP().equals(config.getPbxIP())) {
            // Message has to be transformed and forwarded to Client
            transformPbxToClientMessage(packet);
        }
        else {
            // Message has to be transformed and forwarded to PBX
            transformClientToPbxMessage(packet);
        }

    }

    private void transformClientToPbxMessage( UDPDatagram packet ) {
        packet.setSrcIP(config.getProxySocketIP());
        packet.setSrcPort(config.getProxySocketPort());
        packet.setDstIP(config.getPbxIP());
        packet.setDstPort(config.getPbxPort());
        packet.setData(transformData(packet.getData(), config.getTransformationConfig()
                .getReplacedClient2PbxRules()));
        packet.setData(transformData(packet.getData(), config
                .getDynamicTransformationConfig().getReplacedClient2PbxRules()));

    }

    private void transformPbxToClientMessage( UDPDatagram packet ) {
        packet.setSrcIP(config.getProxySocketIP());
        packet.setSrcPort(config.getProxySocketPort());
        packet.setDstIP(config.getClientIP());
        packet.setDstPort(config.getClientPort());
        packet.setData(transformData(packet.getData(), config.getTransformationConfig()
                .getReplacedPbx2ClientRules()));
        packet.setData(transformData(packet.getData(), config
                .getDynamicTransformationConfig().getReplacedPbx2ClientRules()));
    }

    public static byte[] transformData( byte[] data, Vector<RegexRule> rules ) {
        // Apply rules to Data
        byte[] modifiedData = data;
        for (RegexRule rule : rules) {
            if (rule.isActive()) {
                modifiedData = transform(modifiedData, rule.getRegex(), rule
                        .getReplacement());
            }
        }
        return modifiedData;
    }

    private static byte[] transform( byte[] data, String regex, String replacement ) {
        // Search for matching regex and append replacement on match

        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(new String(data));
        StringBuffer stb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(stb, replacement);
        }
        m.appendTail(stb); // Append the rest of the data (after the regex)
        return stb.toString().getBytes();
    }
}
