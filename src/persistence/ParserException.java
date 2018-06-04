package persistence;

import util.SimpleLogger;


public class ParserException extends Exception {

    public ParserException(String s) {
        super(s);
        SimpleLogger.log(s, SimpleLogger.ERROR_LOG);
    }

}
