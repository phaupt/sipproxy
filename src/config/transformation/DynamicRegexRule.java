package config.transformation;

public class DynamicRegexRule {

    private RegexRule rule;
    private String direction;

    public DynamicRegexRule(RegexRule rule, String direction) {
        this.rule = rule;
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection( String direction ) {
        this.direction = direction;
    }

    public RegexRule getRule() {
        return rule;
    }

    public void setRule( RegexRule rule ) {
        this.rule = rule;
    }

}
