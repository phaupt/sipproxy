package pd.modules;

import testexec.IVariableReplacer;

public class NumberCounter implements IModule {
    
    private long numberValue;
    private long counterIncrement = 1;

    public NumberCounter(long startValue, long counterIncrement) {
        this.numberValue = startValue;
        if (counterIncrement == 0 ){
            throw new IllegalArgumentException("counterIncrement value must not be '0'");
        }
        this.counterIncrement = counterIncrement;
    }
    
    public NumberCounter(long startValue) {
        this.numberValue = startValue;
    }

    public String getValue( IVariableReplacer variableReplacer ) {
        long tmp = numberValue;
        try {
            numberValue += counterIncrement;
        } catch(Exception e){
            throw new IllegalArgumentException("The value of the NumberCounter module reached the upper limit !");
        }       
        return new Long(tmp).toString();
    }

}
