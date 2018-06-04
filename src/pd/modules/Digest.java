package pd.modules;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import testexec.IVariableReplacer;
import util.SimpleLogger;

public class Digest implements IModule{
    protected String username = null;
    protected String secret = null;
    protected String realm = null;
    protected String nonce = null;
    protected String uri = null;
    protected String method = null;
    protected String algorithm = null;
    
    public Digest(String username, String secret, String realm, String nonce, String uri, String method, String algorithm) {
        this.username = username;
        this.secret = secret;
        this.realm = realm;
        this.nonce = nonce;
        this.uri = uri;
        this.method = method;
        this.algorithm = algorithm;
    }

    protected String getMD5Hash(String content, String algorithm){
        MessageDigest md = null;
        StringBuffer sb = new StringBuffer();        
        try {
            md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(content.getBytes());
            
            //get MD5 hash of content
            byte[] hash = md.digest();
            

            //Base 16 Codierung to represent byte-array in string 
            String hexValue;
            for (int i = 0; i < hash.length; i++) {
                //Logical AND to make negative values positive
                hexValue = Integer.toHexString( hash[i] & 0xFF);
                
                //if Integer.toHexString return just one character,
                //put '0' in front of it
                if(hexValue.length() == 1){
                    sb.append('0');
                }
                sb.append(hexValue);
            }
        }
        catch (NoSuchAlgorithmException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        return sb.toString();            
        
    }
    public String getValue(IVariableReplacer variableReplacer) {
        String username = this.username;
        String secret = this.secret;
        String realm = this.realm;
        String nonce = this.nonce;
        String uri = this.uri;
        String method = this.method;
        String algorithm = this.algorithm;
        
        //Replace variables
        if(variableReplacer != null){
            username = variableReplacer.replaceVariables(this.username);
            secret = variableReplacer.replaceVariables(this.secret);
            realm = variableReplacer.replaceVariables(this.realm);
            nonce = variableReplacer.replaceVariables(this.nonce);
            uri =  variableReplacer.replaceVariables(this.uri);
            method =  variableReplacer.replaceVariables(this.method);
            algorithm = variableReplacer.replaceVariables(this.algorithm);
        }
        
        //Calculate hash
        String hash1 = getMD5Hash(username + ":" + realm + ":" + secret, algorithm);
        String hash2 = getMD5Hash(method + ":" + uri, algorithm);
        
        return getMD5Hash(hash1 + ":" + nonce + ":" + hash2, algorithm);
        
    }

}
