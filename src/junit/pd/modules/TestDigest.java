package junit.pd.modules;

import pd.modules.AuthDigest;
import pd.modules.AuthIntDigest;
import pd.modules.Digest;

public class TestDigest extends junit.framework.TestCase {
    
    /*
    DIGEST
    ******
    A1 : H(343434:asterisk:123456) = 5bf93aa0766390202fcaf4b26bc44802
    A2 : H(REGISTER:sip:192.168.0.18) = 91a4fe43739f58c750cd35f91b984006
    H  : H(A1:30419c6c:A2) = H(5bf93aa0766390202fcaf4b26bc44802:30419c6c:91a4fe43739f58c750cd35f91b984006)
                           = 758d52087539766dab8eed5e1f190cd2

    DIGESTAUTH
    **********
    A1 : H(343434:asterisk:123456) = 5bf93aa0766390202fcaf4b26bc44802
    A2 : H(REGISTER:sip:192.168.0.18) = 91a4fe43739f58c750cd35f91b984006
    H  : H(A1:30419c6c:0a1b2c3d4e:abcdef012345:AUTH:A2)
         = H(5bf93aa0766390202fcaf4b26bc44802:30419c6c:0a1b2c3d4e:abcdef012345:AUTH:91a4fe43739f58c750cd35f91b984006)
         = 1f5d442d64dc0d9ca069b4827130866a

    DIGESTAUTHINT
    *************
    H(body) : H(BLA BLA) = a204731fbb63f836bc26aeebfd406b23
    A1 : H(343434:asterisk:123456) = 5bf93aa0766390202fcaf4b26bc44802
    A2 : H(REGISTER:sip:192.168.0.18:H(body) = H(REGISTER:sip:192.168.0.18:a204731fbb63f836bc26aeebfd406b23)
                                             = 7ac101c88072acff43d1365c4b10afab
    H  : H(A1:30419c6c:0a1b2c3d4e:abcdef012345:AUTH-INT:A2)
         = H(5bf93aa0766390202fcaf4b26bc44802:30419c6c:0a1b2c3d4e:abcdef012345:AUTH-INT:7ac101c88072acff43d1365c4b10afab
         = f2b5b7dffacc3dd8481e0c0fa9e87207
    */
    
    private Digest digest;
    private Digest digestAuth;
    private Digest digestAuthInt;
    
    private String username = "343434";
    private String secret = "123456";    
    private String realm = "asterisk";
    private String nonce = "30419c6c";
    private String uri = "sip:192.168.0.18";
    private String method = "REGISTER";
    private String qopAuth = "AUTH";
    private String qopAuthInt = "AUTH-INT";
    private String noncecount = "0a1b2c3d4e";
    private String cnonce = "abcdef012345";
    private String body = "BLA BLA";
    private String algorithm = "md5";

    public TestDigest(){
    	digest = new Digest(username, secret, realm, nonce, uri, method, algorithm);
        digestAuth = new AuthDigest(username, secret, realm, nonce, uri, method, qopAuth, noncecount, cnonce, algorithm);
        digestAuthInt = new AuthIntDigest(username, secret, realm, nonce, uri, method, qopAuthInt, noncecount, cnonce, body, algorithm);
    }
    
    public void testInstances(){      
        assertNotNull(digest);
        assertNotNull(digestAuth);
        assertNotNull(digestAuthInt);
    }
    
    public void testDigest(){
        assertEquals("758d52087539766dab8eed5e1f190cd2",digest.getValue(null));
        assertEquals("1f5d442d64dc0d9ca069b4827130866a",digestAuth.getValue(null));
        assertEquals("f2b5b7dffacc3dd8481e0c0fa9e87207",digestAuthInt.getValue(null));
    }  
}
