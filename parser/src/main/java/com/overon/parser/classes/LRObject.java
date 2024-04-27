package com.overon.parser.classes;

public class LRObject {

    private LRSymbols action;

    private int gotoState;

    private String rule;
    private int ruleReduction;

    public LRObject(LRSymbols action, int gotoState, String rule, int ruleReduction) {
        this.action = action;
        this.gotoState = gotoState;
        this.rule = rule;
        this.ruleReduction = ruleReduction;
    }

    public LRSymbols getAction() {
        return action;
    }

    public int getGotoState() {
        return gotoState;
    }

    public String getRule() {
        return rule;
    }

    public int getRuleLength() {
        return ruleReduction;
    }

    @Override
    public String toString() {
        return "LRObject{" +
                "action=" + action +
                ", gotoState=" + gotoState +
                ", rule='" + rule + '\'' +
                ", ruleReduction=" + ruleReduction +
                '}';
    }

}
