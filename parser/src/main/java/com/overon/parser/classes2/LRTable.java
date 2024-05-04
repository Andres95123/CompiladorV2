package com.overon.parser.classes2;

import java.util.HashMap;

import com.overon.parser.classes.LRSymbols;

public class LRTable {

    private HashMap<Integer, HashMap<String, LRObject>> table;
    private HashMap<String, Integer> reductionGoto;
    private int states;

    public LRTable( ) {
        table = new HashMap<>();
        reductionGoto = new HashMap<>();

        states = 0;
    }

    public boolean contains(String symbol, int state) {
        return table.get(state).containsKey(symbol);
    }

    public LRObject get(String symbol, int state) {
        return table.get(state).get(symbol);
    }

    // Getter

    public int getStates() {
        return states;
    }

    public int getReductionGoto(String symbol) {
        // Si no existe, se añade
        if (!reductionGoto.containsKey(symbol)) {
            addReductionGoto(symbol, generateState());
        }

        return reductionGoto.get(symbol);
    }

    public boolean containsReductionGoto(String symbol) {
        return reductionGoto.containsKey(symbol);
    }

    public int add(String symbol, LRObject object) {
        int state;
        if (table.containsKey(object.getState())) {
            state = object.getState();
        } else {
            state = generateState();
        }
        if (table.get(state).containsKey(symbol)) {
            throw new RuntimeException("Conflict in LRTable : Type " + object.getAction() + "-"
                    + table.get(state).get(symbol).getAction() + " in state " + state + " with symbol " + symbol);
        }

        table.get(state).put(symbol, object);
        return state;
    }

    private void addReductionGoto(String symbol, int state) {
        reductionGoto.put(symbol, state);
    }

    public int getNextGeneratedState() {
        return states + 1;
    }

    private int generateState() {
        int state = states++;
        table.put(state, new HashMap<>());

        return state;
    }

    @Override
    public String toString() {
        return "LRTable [states=" + states + ", table=" + table + "]";
    }

}
