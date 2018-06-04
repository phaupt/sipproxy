package pd.modules;

import testexec.IVariableReplacer;



public class MockDigest implements IModule {
    
    public String username;
    public String secret;
    public String realm;
    public String nonce;
    public String uri;
    public String method;
    public String qop;
    public String noncecount;
    public String cnonce;
    public String body;
    public String algorithm;

    public MockDigest(String username, String secret, String realm, 
            String nonce, String uri, String method, String algorithm) {
        this.username = username;
        this.secret = secret;
        this.realm = realm;
        this.nonce = nonce;
        this.uri = uri;
        this.method = method;
        this.algorithm = algorithm;
    }
    
    public MockDigest(String username, String secret, String realm,
            String nonce, String uri, String method, 
            String qop, String noncecount, String cnonce, String body,
            String algorithm) {
        this.username = username;
        this.secret = secret;
        this.realm = realm;
        this.nonce = nonce;
        this.uri = uri;
        this.method = method;
        this.qop = qop;
        this.noncecount = noncecount;
        this.cnonce = cnonce;
        this.body = body;
        this.algorithm = algorithm;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        // TODO Auto-generated method stub
        return null;
    }
 

}
