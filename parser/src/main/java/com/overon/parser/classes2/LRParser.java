package com.overon.parser.classes2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.overon.parser.classes.LRSymbols;

public class LRParser {

    private LRTable table;
    private Stack<Integer> states;
    private boolean accept;

    public LRParser() {
        this.table = new LRTable();
        states = new Stack<>();
        states.push(0);
    }

    public void parse(String input) {
        // Input = Expresion regular "E ::= E + T | T" por ejemplo

        // Dividimos la entrada en tokens
        String[] tokens = input.split("::=");
        String[] symbols = tokens[1].split("\\|");

        // Añadimos los tokens como array de Strings

        List<TokenParser> symbolsList = new ArrayList<>();
        for (String symbol : symbols) {
            String[] symbolsArray = symbol.trim().split(" ");
            symbolsList.add(new TokenParser(symbolsArray));
        }

        // Vamos añadiendo los tokens a la par
        String leftE = tokens[0].trim();
        int expressionState = table.getReductionGoto(leftE);

        for (TokenParser symbol : symbolsList) {

            int state = expressionState;

            while (symbol.getAccion() == LRSymbols.SHIFT) {
                // Si es un no terminal

                // Si es shift, añadimos el token a la tabla
                state = table.add(symbol.getSymbol(),
                        new LRObject(LRSymbols.SHIFT, table.getStates(), table.getNextGeneratedState()));

                symbol.reduceLongitud();
            }

            // Al terminar de hacer shift es reduce obligatoriamente
            table.add(symbol.getSymbol(),
                    new LRObject(leftE, LRSymbols.REDUCE, state + 1, expressionState + 1, symbol.getPops()));
            // +
            // 1
            // Por
            // que
            // es
            // este
            // token

        }

        // Si expressionState es el primero, añadimos el estado de acceptacion
        if (expressionState == 0 && !accept) {
            table.add("$", new LRObject(LRSymbols.ACCEPT, expressionState + 1, -1));
            accept = true;
        }

        System.out.println("Tabla LR" + table.toString());
    }

    public void parseFile(String file) {
        // Para cada linea del fichero, parseamos
        String[] lines = file.split("\n");
        for (String line : lines) {
            parse(line);
        }
    }

    // Reconocedor del lenguaje
    public boolean recognize(String input, int stateInicial) {
        // Usamos una pila para ir guardando los estados
        Stack<Integer> states = new Stack<>();
        Stack<String> symbols = new Stack<>();
        // Subimos el estado aceptador
        states.push(stateInicial);
        symbols.push("$");

        int profundidad = 0;
        // Dividimos la entrada en tokens
        String[] tokens = input.split(" ");
        for (int j = 0; j < tokens.length; j++) {
            String token = tokens[j];

            // Si el token es reconocido, lo añadimos a la pila
            LRObject object = table.get(token, states.peek());
            if (object == null) {
                if (profundidad > 0) {
                    profundidad--;
                    j--;
                    continue;
                }
                return false;
            }

            // Si es no terminal, nos transportamos al estado correspondiente
            if (table.containsReductionGoto(token)) {
                states.push(table.getReductionGoto(token) + 1);
                profundidad++;
                object = table.get(token, states.peek());
                while (object == null) {
                    states.pop();
                    object = table.get(token, states.peek());
                }
            }

            // Si es shift, añadimos el estado a la pila
            if (object.getAction() == LRSymbols.SHIFT) {
                states.push(object.getGotoState());
                symbols.push(token);

            } else if (object.getAction() == LRSymbols.REDUCE) {
                // Si es reduce, sacamos tantos elementos de la pila como símbolos haya en el
                // lado derecho
                // de la regla
                for (int i = 0; i < object.getReductionPops(); i++) {
                    states.pop();
                    symbols.pop();
                }

                // Añadimos el símbolo de la regla
                symbols.push(object.getReductionSymbol());
                // Añadimos el estado de la regla
                states.push(table.getReductionGoto(object.getReductionSymbol()) + 1);

                if (table.containsReductionGoto(object.getReductionSymbol())) {
                    // Cuando tengan el mismo goto, borrar de estados
                    while (states.peek() == table.getReductionGoto(object.getReductionSymbol()) + 1) {
                        states.pop();
                    }
                    object = table.get(object.getReductionSymbol(), states.peek());
                    while (object == null) {
                        states.pop();
                        object = table.get(object.getReductionSymbol(), states.peek());
                    }

                    // Reaemos el reduce
                    if (object.getAction() == LRSymbols.REDUCE) {
                        for (int i = 0; i < object.getReductionPops() + 1; i++) {
                            states.pop();
                            symbols.pop();
                        }

                        // Añadimos el símbolo de la regla
                        symbols.push(object.getReductionSymbol());
                        // Añadimos el estado de la regla
                        states.push(table.getReductionGoto(object.getReductionSymbol()) + 1);
                    }
                }
            } else if (object.getAction() == LRSymbols.ACCEPT) {
                // Si es aceptar, hemos terminado
                return true;
            }
        }

        if (states.peek() == stateInicial + 1) {
            return true;
        }

        return false;
    }

    private static String[] getAllLeftProductions(String input) {
        String[] tokens = input.split("\n");
        // Optemeos el lado izquierdo
        List<String> leftProductions = new ArrayList<>();
        for (String token : tokens) {
            leftProductions.add(getLeftProduction(token));
        }
        return leftProductions.toArray(new String[0]);
    }

    private static String getLeftProduction(String input) {
        String[] tokens = input.split("::=");
        return tokens[0].trim();
    }

    public static void main(String[] args) {
        String productions = "E ::= E + T | F\nT ::= T * F | F\nF ::= ( E ) | id\n";
        LRParser parser = new LRParser();
        parser.parseFile(productions);
        System.out.println(parser.recognize("E + T * F * F $", 1));
    }

}

class TokenParser {
    static int ids = 0;
    private String[] symbol;
    int longitud;
    int id;

    public TokenParser(String[] symbol) {
        this.symbol = symbol;
        this.longitud = 0;
        this.id = ids++;
    }

    // Getters
    public String getSymbol() {
        return symbol[longitud];
    }

    public int getID() {
        return id;
    }

    public int reduceLongitud() {
        longitud++;
        return longitud;
    }

    public int getPops() {
        return longitud;
    }

    public LRSymbols getAccion() {
        // Si no se han acabado los simbolos, shift, sino reduce
        return longitud < symbol.length - 1 ? LRSymbols.SHIFT : LRSymbols.REDUCE;
    }

}
