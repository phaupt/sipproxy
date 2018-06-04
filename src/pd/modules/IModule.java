package pd.modules;

import testexec.IVariableReplacer;

public interface IModule {
    
	String getValue(IVariableReplacer variableReplacer);
    
    //Default Character Set: a-z,A-Z,0-9
    public static final char[] DEFAULT_CHARACTER_SET  = new char[]{ 
            'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '0','1','2','3','4','5','6','7','8','9'};
    
}
