package config.transformation;

public class RegexRule implements Comparable {

    private String regex;
    private String replacement;
    private boolean isActive;

    public RegexRule(String regex, String replacement, boolean isActive) {
        this.regex = regex;
        this.replacement = replacement;
        this.isActive = isActive;
    }

    public String getRegex() {
        return regex;
    }

    public String getReplacement() {
        return replacement;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive( boolean isActive ) {
        this.isActive = isActive;
    }

    public void setRegex( String regex ) {
        this.regex = regex;
    }

    public void setReplacement( String replacement ) {
        this.replacement = replacement;
    }

    public int compareTo( Object o ) {
        RegexRule rule2 = (RegexRule) o;
        int res = getRegex().compareTo(rule2.getRegex());
        if (res == 0) {
            res = getReplacement().compareTo(rule2.getReplacement());
        }
        return res;
    }

}
