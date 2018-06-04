package persistence;

import java.util.Vector;

import pd.*;
import pd.modules.*;
import config.Configuration;

public class ElementFactory implements IElementFactory {

    public IModule createClearText( String value ) {
        return new ClearText(value);
    }

    public IModule createConfigValue( Configuration config, String paramName ) {
        return new ConfigValue(config, paramName);
    }

    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String algorithm ) {
        return new Digest(username, secret, realm, nonce, uri, method, algorithm);
    }

    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String qop, String noncecount, String cnonce, String algorithm ) {
        return new AuthDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, algorithm);
    }

    public IModule createDigest( String username, String secret, String realm, String nonce, String uri, String method, String qop, String noncecount, String cnonce, String body, String algorithm ) {
        return new AuthIntDigest(username, secret, realm, nonce, uri, method, qop, noncecount, cnonce, body, algorithm);
    }
    
    public IModule createNumberRangeFuzzer( long minimum, long maximum ) {
        return new NumberRangeFuzzer(minimum, maximum);
    }

    public IRequest createRequest( int requestID, String content, Vector<IResponse> responses) {
        return new Request(requestID, content, responses);
    }

    public IResponse createResponse( String regex, String category ) {
        return new Response(regex, category);
    }

    public IModule createResponseCatcher( String regex, int replacement ) {
        return new ResponseCatcher(regex, replacement);
    }

    public IResponseVar createResponseVar( String name, IModule module ) {
        return new ResponseVar(name, module);
    }

    public IModule createStringExpansionFuzzer( String expansionString, int increment ) {
        return new StringExpansionFuzzer(expansionString, increment);
    }

    public IModule createStringExpansionFuzzer( int increment ) {
        return new StringExpansionFuzzer(increment);
    }
    
    public IModule createStringExpansionFuzzer( int increment, char[] charSet ) {
        return new StringExpansionFuzzer(increment, charSet);
    }    

    public IModule createStringMutationFuzzer( int length ) {
        return new StringMutationFuzzer(length);
    }
    
    public IModule createStringMutationFuzzer( int length, char[] charSet ) {
        return new StringMutationFuzzer(length, charSet);
    }

    public ITestCase createTestCase( String name, int initialRequestMessageID, Vector<IRequest> requests ) {
        return new TestCase(name, initialRequestMessageID, requests);
    }

    public ITimeout createTimeout( int timeInMilliseconds, String message ) {
        return new Timeout(timeInMilliseconds, message);
    }

    public IModule createValueListFuzzer( String filepath, boolean repeatList ) {
        return new ValueListFuzzer(filepath, repeatList);
    }

    public IModule createValueListFuzzer( Vector<String> valueList, boolean repeatList ) {
        return new ValueListFuzzer(valueList, repeatList);
    }
    
    public IModule createHexValueListFuzzer( String filePath, boolean repeatList ) {
        return new HexValueListFuzzer(filePath, repeatList);
    }

    public IModule createHexValueListFuzzer( Vector<String> valueList, boolean repeatList ) {
        return new HexValueListFuzzer(valueList, repeatList);
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

    public IMessageModifier createLinePermutator(int startLine, int endLine) {
        return new LinePermutator(startLine, endLine);
    }  

}
