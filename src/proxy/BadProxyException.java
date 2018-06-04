package proxy;

public class BadProxyException extends IllegalArgumentException {

    public BadProxyException() {
        super();
    }

    public BadProxyException(String s) {
        super(s);
    }
}
