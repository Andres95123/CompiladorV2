package com.overon.parser.classes;

// Esta clase controla una tabla LR0 que se utiliza para el análisis sintáctico
import java.util.HashMap;
import java.util.Map;

public class LRTable {
    private Map<Integer, Map<String, LRObject>> table;
    private Map<String, Integer> derivations;
    private int id;

    public LRTable() {
        table = new HashMap<>();
        derivations = new HashMap<>();
        id = 0;
    }

    private void addAction(int state, String symbol, String rule, int ruleReduction, LRSymbols action, int gotoState) {
        // Si la accion ya existe, llamamos a una excepcion e indicamos las acciones
        // como error, por ejemplo shift-reduce
        if (table.containsKey(state) && table.get(state).containsKey(symbol)) {
            // Obtenemos la accion que ya existe en la tabla
            LRSymbols oldAction = table.get(state).get(symbol).getAction();
            throw new RuntimeException(oldAction.name() + "-" + action.name() + " conflict");
        }
        table.computeIfAbsent(state, k -> new HashMap<>()).put(symbol,
                new LRObject(action, gotoState, rule, ruleReduction));
    }

    /* Añade una accion de reduccion */
    public void addAction(int state, String symbol, String rule, int ruleReduction, int gotoState) {
        addAction(state, symbol, rule, ruleReduction, LRSymbols.REDUCE, gotoState);
    }

    /* Añade una accion de desplazamiento */
    public void addAction(int state, String symbol, int gotoState) {
        addAction(state, symbol, null, -1, LRSymbols.SHIFT, gotoState);
    }

    /* Añade una accion de aceptacion */
    public void addAction(int state, String symbol) {
        addAction(state, symbol, null, -1, LRSymbols.ACCEPT, -1);
    }

    /* Añade una accion de error */
    public void addActionError(int state, String symbol) {
        addAction(state, symbol, null, -1, LRSymbols.ERROR, -1);
    }

    public void addRule(String left, String right) {

        // Controlamos epsilon
        if (right.equals(GramaticaLexic.SIMBOLO_EPSILON)) {
            addAction(derivations.computeIfAbsent(left, k -> derivations.size()), "$", left, 0, 0);
            return;
        }

        // Revisa si la derivacion ya existe, si no, la añade

        int OrleftId = derivations.computeIfAbsent(left, k -> id++);
        int leftId = OrleftId;
        // Divide la derecha en sus partes
        String[] rightParts = right.split(" ");

        int rightId;
        for (int i = 0; i < rightParts.length - 1; i++) {
            rightId = id++;
            addAction(leftId, rightParts[i], rightId);
            leftId = rightId;
        }
        // Añadimos la reduccion
        rightId = id++;
        addAction(leftId, rightParts[rightParts.length - 1], left, rightParts.length,
                OrleftId);

    }

    public LRSymbols getAction(int state, String symbol) {
        // Si no existe la accion, se devuelve ERROR
        if (!table.containsKey(state) || !table.get(state).containsKey(symbol)) {
            return LRSymbols.ERROR;
        }
        return table.get(state).get(symbol).getAction();
    }

    public int getGotoState(int state, String symbol) {
        return table.get(state).get(symbol).getGotoState();
    }

    public int getRuleLength(int state, String symbol) {
        return table.get(state).get(symbol).getRuleLength();
    }

    public String getRule(int state, String symbol) {
        return table.get(state).get(symbol).getRule();
    }

    public int getDerivationId(String derivation) {
        // Si no existe la derivacion, se devuelve -1
        if (!derivations.containsKey(derivation)) {
            return -1;
        }
        return derivations.get(derivation);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Map<String, LRObject>> entry : table.entrySet()) {
            builder.append("State ").append(entry.getKey()).append("\n");
            for (Map.Entry<String, LRObject> e : entry.getValue().entrySet()) {
                builder.append("\t").append(e.getKey()).append(" -> ").append(e.getValue()).append("\n");
            }
        }
        return builder.toString();
    }

}
