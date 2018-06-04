package pd.modules;

import testexec.IVariableReplacer;


public class AuthDigest extends Digest {
    protected String qop = null;
    protected String nonceCount = null;
    protected String cnonce = null;

    public AuthDigest(String username, String secret, String realm, String nonce, String uri, String method, String qop, String nonceCount, String cnonce, String algorithm) {
        super(username, secret, realm, nonce, uri, method, algorithm);
        this.qop = qop;
        this.nonceCount = nonceCount;
        this.cnonce = cnonce;
    }
    
    public String getValue(IVariableReplacer variableReplacer) {
        String username = this.username;
        String secret = this.secret;
        String realm = this.realm;
        String nonce = this.nonce;
        String uri = this.uri;
        String method = this.method;
        String nonceCount = this.nonceCount;
        String cnonce = this.cnonce;
        String qop = this.qop;
        String algorithm = this.algorithm;

        //Replace variables
        if(variableReplacer != null){
            username = variableReplacer.replaceVariables(this.username);
            secret = variableReplacer.replaceVariables(this.secret);
            realm = variableReplacer.replaceVariables(this.realm);
            nonce = variableReplacer.replaceVariables(this.nonce);
            uri =  variableReplacer.replaceVariables(this.uri);
            method =  variableReplacer.replaceVariables(this.method);
            nonceCount = variableReplacer.replaceVariables(this.nonceCount);
            cnonce =  variableReplacer.replaceVariables(this.cnonce);
            qop =  variableReplacer.replaceVariables(this.qop);  
            algorithm = variableReplacer.replaceVariables(this.algorithm);
        }
        
        String hash1 = getMD5Hash(username + ":" + realm + ":" + secret, algorithm);
        String hash2 = getMD5Hash(method + ":" + uri, algorithm);
        return getMD5Hash(hash1 + ":" + nonce + ":" + nonceCount + ":" + cnonce + ":" + qop + ":" + hash2, algorithm);
    }
}
