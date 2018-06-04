package pd.modules;

import testexec.IVariableReplacer;
import config.Configuration;

public class ConfigValue implements IModule {
    
    private Configuration config;
    private String paramName;

    public ConfigValue(Configuration config, String paramName) {
        this.paramName = paramName;
        this.config = config;
    }

    public String getValue(IVariableReplacer variableReplacer) {
        if (paramName.equals(Configuration.varClientIP)){
            return config.getClientIP().getHostAddress();
        }
        else if (paramName.equals(Configuration.varClientPort)){
            return new Integer(config.getClientPort()).toString();
        }
        else if (paramName.equals(Configuration.varPbxIP)){
            return config.getPbxIP().getHostAddress();
        }
        else if (paramName.equals(Configuration.varPbxPort)){
            return new Integer(config.getPbxPort()).toString();
        }
        else if (paramName.equals(Configuration.varProxySocketIP)){
            return config.getProxySocketIP().getHostAddress();
        }
        else if (paramName.equals(Configuration.varProxySocketPort)){
            return new Integer(config.getProxySocketPort()).toString();
        }
        else if (paramName.equals(Configuration.varTargetIP)){
            return config.getTargetIP().getHostAddress();
        }
        else if (paramName.equals(Configuration.varTargetPort)){
            return new Integer(config.getTargetPort()).toString();
        }
        else if (paramName.equals(Configuration.varTestCaseSocketIP)){
            return config.getTestCaseSocketIP().getHostAddress();
        }
        else if (paramName.equals(Configuration.varTestCaseSocketPort)){
            return new Integer(config.getTestCaseSocketPort()).toString();
        }
        return null;
    }

}
