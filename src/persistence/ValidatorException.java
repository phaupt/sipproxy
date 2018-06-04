package persistence;

import util.SimpleLogger;


public class ValidatorException extends Exception {

    public ValidatorException(String s) {
        super(s);
        SimpleLogger.log(s, SimpleLogger.ERROR_LOG);
    }

}
