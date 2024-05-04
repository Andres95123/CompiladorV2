package com.overon.parser.classes2;

import com.overon.parser.classes.LRSymbols;

public class LRObject {

    LRSymbols action;

    int state;

    int gotoState;

    String reductionSymbol;

    int reductionPops;

    public LRObject(LRSymbols action, int state, int gotoState) {
        this.action = action;
        this.state = state;
        this.gotoState = gotoState;
    }

    public LRObject(String reductionSymbol, LRSymbols action, int state, int reduceGoto, int reductionPops) {
        this.action = action;
        this.state = state;
        this.gotoState = reduceGoto;
        this.reductionSymbol = reductionSymbol;
        this.reductionPops = reductionPops;
    }

    public String getReductionSymbol() {
        return reductionSymbol;
    }

    public LRSymbols getAction() {
        return action;
    }

    public int getReductionPops() {
        return reductionPops;
    }

    public int getState() {
        return state;
    }

    public int getGotoState() {
        return gotoState;
    }

    @Override
    public String toString() {
        return "LRObject [action=" + action + ", state=" + state + ", gotoState=" + gotoState + ", reductionSymbol="
                + reductionSymbol + "]";
    }

}
