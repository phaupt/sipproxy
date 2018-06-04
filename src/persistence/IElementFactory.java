package persistence;

import java.util.Vector;

import config.Configuration;
import pd.IRequest;
import pd.IResponse;
import pd.IResponseVar;
import pd.ITestCase;
import pd.ITimeout;
import pd.IVar;
import pd.modules.IMessageModifier;
import pd.modules.IModule;

public interface IElementFactory {
    ITestCase createTestCase(String name, int initialRequestMessageID, Vector<IRequest> requests);
    IRequest createRequest(int requestID, String content, Vector<IResponse> responses);
    ITimeout createTimeout(int timeInMilliseconds, String message);
    IResponse createResponse(String regex, String category);
    IVar createVar(String name, IModule module);
    IResponseVar createResponseVar(String name, IModule module);
    IModule createResponseCatcher(String regex, int replacement);
    IModule createNumberRangeFuzzer(long minimum, long maximum);
    IModule createStringMutationFuzzer(int length);
    IModule createStringMutationFuzzer(int length, char[] charSet);
    IModule createStringExpansionFuzzer(String expansionString, int increment);
    IModule createStringExpansionFuzzer(int increment);
    IModule createStringExpansionFuzzer(int increment, char[] charSet);    
    IModule createHexValueListFuzzer(String filePath, boolean repeatList);
    IModule createHexValueListFuzzer(Vector<String> valueList, boolean repeatList);
    IModule createValueListFuzzer(String filepath, boolean repeatList);
    IModule createValueListFuzzer(Vector<String> valueList, boolean repeatList);
    IModule createDigest(String username, String secret, String realm, 
                         String nonce, String uri, String method, 
                         String algorithm);
    IModule createDigest(String username, String secret, String realm,
            String nonce, String uri, String method, 
            String qop, String noncecount, String cnonce,
            String algorithm);
    IModule createDigest(String username, String secret, String realm,
                         String nonce, String uri, String method, 
                         String qop, String noncecount, String cnonce,
                         String body, String algorithm);
    IModule createConfigValue(Configuration config, String paramName);
    IModule createClearText(String value);
    IModule createNumberCounter(long startValue, long increment);
    IModule createNumberCounter(long startValue);
    IMessageModifier createContentLength();
    IMessageModifier createLinePermutator(int startLine, int endLine);
}
