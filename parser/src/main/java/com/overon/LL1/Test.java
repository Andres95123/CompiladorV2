package com.overon.LL1;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Test {

    // Definimos las tablas LL(1)
    private static final Map<String, Map<String, String>> ll1Table = new HashMap<>();

    static {
        Map<String, String> sRow = new HashMap<>();
        sRow.put("id", "S -> E");
        ll1Table.put("S", sRow);

        Map<String, String> eRow = new HashMap<>();
        eRow.put("id", "E -> T + T");
        ll1Table.put("E", eRow);

        Map<String, String> tRow = new HashMap<>();
        tRow.put("id", "T -> id");
        ll1Table.put("T", tRow);
    }

    // Simbolos terminales
    private static final String[] terminals = { "id", "+", "$" };

    public static void main(String[] args) {
        String[] tokens = { "id", "+", "id", "$" };
        if (parse(tokens)) {
            System.out.println("Input is valid according to the grammar.");
        } else {
            System.out.println("Input is invalid.");
        }

    }

    public static boolean parse(String[] tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("S");

        int index = 0;
        String token = tokens[index];

        while (!stack.isEmpty()) {
            String top = stack.pop();
            if (isTerminal(top)) {
                if (top.equals(token)) {
                    index++;
                    if (index < tokens.length) {
                        token = tokens[index];
                    }
                } else {
                    return false; // Error de sintaxis
                }
            } else if (ll1Table.containsKey(top)) {
                String production = ll1Table.get(top).get(token);
                if (production != null) {
                    String[] rhs = production.split(" -> ")[1].split(" ");
                    for (int i = rhs.length - 1; i >= 0; i--) {
                        if (!rhs[i].equals("ε")) {
                            stack.push(rhs[i]);
                        }
                    }
                } else {
                    return false; // Error de sintaxis
                }
            }
        }

        return token.equals("$");
    }

    private static boolean isTerminal(String symbol) {
        for (String terminal : terminals) {
            if (symbol.equals(terminal)) {
                return true;
            }
        }
        return false;
    }
}
