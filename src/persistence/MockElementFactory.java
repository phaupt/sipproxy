package persistence;

import java.util.Vector;

import pd.*;
import pd.modules.*;
import config.Configuration;


public class MockElementFactory implements IElementFactory {

    public IModule createClearText( String value ) {
        return new ClearText(value);
    }

    public IModule createConfigValue( Configuration config, String paramName ) {
        return new MockConfigValue(config, paramName);
    }

    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String algorithm ) {
        return new MockDigest(username, secret, realm, nonce, uri, method, algorithm);
    }

    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String qop, String noncecount, String cnonce, String algorithm ) {
        return new MockDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, null, algorithm);
    }
    
    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String qop, String noncecount, String cnonce, String body, String algorithm ) {
        return new MockDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, body, algorithm);
    }

    public IModule createNumberRangeFuzzer( long minimum, long maximum ) {
        return new MockNumberRangeFuzzer(minimum, maximum);
    }

    public IRequest createRequest( int requestID, String content, Vector<IResponse> responses) {
        return new Request(requestID, content, responses);
    }

    public IResponse createResponse( String regex, String category ) {
        return new Response(regex, category);
    }

    public IModule createResponseCatcher( String regex, int replacement ) {
        return new MockResponseCatcher(regex, replacement);
    }

    public IResponseVar createResponseVar( String name, IModule module ) {
        return new ResponseVar(name, module);
    }

    public IModule createStringExpansionFuzzer( String expansionString, int increment ) {
        return new MockStringExpansionFuzzer(expansionString, increment);
    }

    public IModule createStringExpansionFuzzer( int increment ) {
        return new MockStringExpansionFuzzer(increment);
    }
    public IModule createStringExpansionFuzzer( int increment, char[] charSet ) {
        return new MockStringExpansionFuzzer(increment, charSet);
    }

    public IModule createStringMutationFuzzer( int length ) {
        return new MockStringMutationFuzzer(length);
    }
    
    public IModule createStringMutationFuzzer( int length, char[] charSet ) {
        return new MockStringMutationFuzzer(length, charSet);
    }

    public ITestCase createTestCase( String name, int initialRequestMessageID, Vector<IRequest> requests ) {
        return new TestCase(name, initialRequestMessageID, requests);
    }

    public ITimeout createTimeout( int timeInMilliseconds, String message ) {
        return new Timeout(timeInMilliseconds, message);
    }

    public IModule createValueListFuzzer( String filepath, boolean repeatList ) {
        return new MockValueListFuzzer(filepath, repeatList);
    }

    public IModule createValueListFuzzer( Vector<String> valueList, boolean repeatList ) {
        return new MockValueListFuzzer(valueList, repeatList);
    }
    public IModule createHexValueListFuzzer( String filePath, boolean repeatList ) {
        return new MockHexValueListFuzzer(filePath, repeatList);
    }

    public IModule createHexValueListFuzzer( Vector<String> valueList, boolean repeatList ) {
        return new MockHexValueListFuzzer(valueList, repeatList);
    }

    public IVar createVar( String name, IModule module ) {
        return new Var(name, module);
    }

    public IMessageModifier createContentLength() {
        return new ContentLength();
    }

    public IModule createNumberCounter( long startValue, long increment ) {
        return new NumberCounter(startValue, increment);
    }

    public IModule createNumberCounter( long startValue ) {
        return new NumberCounter(startValue);
    }

    public IMessageModifier createLinePermutator( int startLine, int endLine ) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
