package com.overon.parser.classes;

import java.util.Stack;

public class LRParser {

    private LRTable table;
    private Stack<Integer> states;
    private Stack<String> symbols;

    public LRParser(LRTable table) {
        this.table = table;
        states = new Stack<>();
        symbols = new Stack<>();
    }

    public void parse(String input) {
        states.push(0);
        symbols.push("$");

        int i = 0;
        int profundidad = 0;

        while (i < input.length()) {
            String symbol = String.valueOf(input.charAt(i));
            int state = states.peek();
            LRSymbols action = table.getAction(state, symbol);

            switch (action) {
                case SHIFT:
                    states.push(table.getGotoState(state, symbol));
                    symbols.push(symbol);
                    // Si es un terminal, se añade a la pila
                    int derivationId = table.getDerivationId(symbol);
                    if (derivationId != -1) {
                        profundidad++;
                        states.push(derivationId);
                    }
                    break;
                case REDUCE:

                    // Elimina de la pila los estados y símbolos que se van a reducir
                    int ruleLength = table.getRuleLength(state, symbol) - 1;
                    for (int j = 0; j < ruleLength; j++) {
                        symbols.pop();
                        states.pop();
                    }

                    // Añade a la pila la reduccion
                    states.push(table.getGotoState(state, symbol));
                    symbols.push(table.getRule(state, symbol));

                    states.push(table.getGotoState(states.peek(), symbols.peek()));
                    

                    break;

                case ACCEPT:
                    System.out.println("Cadena aceptada correctamente" + symbols.reversed());
                    return;
                case ERROR:
                    if (profundidad == 0) {
                        System.out.println("Error en la cadena, no se reconoce el simbolo " + symbol);
                        return;
                    } else {
                        // Eliminamos dicha profundidad
                        profundidad--;
                        states.pop();
                        i--;
                    }
            }
            i++;
        }
    }

}
