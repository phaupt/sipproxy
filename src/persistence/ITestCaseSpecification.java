package persistence;

import java.io.File;

import pd.ITestCase;

public interface ITestCaseSpecification {
    
    public ITestCase getTestCase(File testSpecFile) throws ParserException, ValidatorException;
    
    //XML Schema Location
    public static final String SCHEMA_FILE = "TestCases/TestCaseSchema.xsd";
    
    //Attributes
    public static final String ATT_NAME = "name";
    public static final String ATT_CYCLES = "cycles";
    public static final String ATT_INITIALID = "initialRequestMessageID";
    public static final String ATT_TIME = "timeInMilliseconds";
    public static final String ATT_MESSAGE = "message";
    public static final String ATT_CATEGORY = "category";
    public static final String ATT_REQUESTID = "requestID";
    public static final String ATT_FOLLREQID = "followingRequestID";
    public static final String ATT_WAITINGTIME = "waitingTimeInMilliseconds";
    public static final String ATT_STARTLINE = "startLine";
    public static final String ATT_ENDLINE = "endLine";

    //Module-Attribuets
    public static final String MODATT_USERNAME = "username";
    public static final String MODATT_SECRET = "secret";
    public static final String MODATT_REALM = "realm";
    public static final String MODATT_NONCE = "nonce";
    public static final String MODATT_URI = "uri";
    public static final String MODATT_METHOD = "method";
    public static final String MODATT_QPOP = "qop";
    public static final String MODATT_NONCECOUNT = "noncecount";
    public static final String MODATT_CNONCE = "cnonce";
    public static final String MODATT_BODY = "body";
    public static final String MODATT_MIN = "minimum";
    public static final String MODATT_MAX = "maximum";
    public static final String MODATT_INCREMENT = "increment";
    public static final String MODATT_LENGTH = "length";
    public static final String MODATT_FILEPATH = "filePath";
    public static final String MODATT_PARAMNAME = "paramName";
    public static final String MODATT_CATCHGROUP = "catchGroupIndex";
    public static final String MODATT_REPEATLIST = "repeatValueList";
    public static final String MODATT_COUNTERINCREMENT = "counterIncrement";
    public static final String MODATT_STARTVALUE = "startValue";
    
    
    //Nodes
    public static final String NODE_ROOT = "TestCase";
    public static final String NODE_REQ = "Request";
    public static final String NODE_CONTENT = "Content";
    public static final String NODE_RESP = "Response";
    public static final String NODE_REGEX = "Regex";    
    public static final String NODE_VARIABLES = "Variables";
    public static final String NODE_RESPVARIABLES = "ResponseVariables";
    public static final String NODE_VAR = "Var";
    public static final String NODE_RESPVAR = "ResponseVar";
    public static final String NODE_TIME = "Timeout";
    public static final String NODE_RESPCATCHER = "ResponseCatcher";
    public static final String NODE_EXPSTRING = "ExpansionString";
    public static final String NODE_CHARSET = "CharacterSet";
    public static final String NODE_MESSAGEMODIFIER = "MessageModifier";
    public static final String NODE_CONTENTLENGTH = "ContentLength";
    public static final String NODE_LINEPERMUTATOR = "LinePermutator";
    
    //Module-Nodes
    public static final String MODNODE_DIGEST = "Digest";
    public static final String MODNODE_NUMBER_RANGE_FUZZER = "NumberRangeFuzzer";
    public static final String MODNODE_STRING_EXP_FUZZER = "StringExpansionFuzzer";
    public static final String MODNODE_STRING_MUT_FUZZER = "StringMutationFuzzer";
    public static final String MODNODE_VALUE_LIST_FUZZER = "ValueListFuzzer";
    public static final String MODNODE_HEX_VALUE_LIST_FUZZER = "HexValueListFuzzer";
    public static final String MODNODE_CLEARTEXT = "ClearText";
    public static final String MODNODE_CONFIGVALUE = "ConfigValue";
    public static final String MODNODE_NUMBERCOUNTER = "NumberCounter";

}
