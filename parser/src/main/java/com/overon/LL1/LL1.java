package com.overon.LL1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class LL1 {

    // Control de los terminales y no terminales
    private HashSet<String> TERMINALES;
    private HashSet<String> NO_TERMINALES;

    // Fists & Follows
    private Map<String, HashSet<String>> FIRST;
    private Map<String, HashSet<String>> FOLLOW;

    // LL1 Table
    private final Map<String, Map<String, ProduccionLL>> LL1TABLE;

    // Start
    private String start;

    public LL1(HashSet<String> TERMINALES, HashSet<String> NO_TERMINALES, List<ProduccionLL> producciones,
            String start) {
        this.TERMINALES = TERMINALES;
        this.NO_TERMINALES = NO_TERMINALES;
        this.start = start;

        // Inicializar FIRST y FOLLOW
        FIRST = new HashMap<>();
        FOLLOW = new HashMap<>();

        // Inicializar la tabla LL1
        LL1TABLE = new HashMap<>();

        // Aumentar la gramática
        producciones.add(new ProduccionLL(start + "'", start + " $"));
        NO_TERMINALES.add(start + "'");

        // Inicializar conjuntos FIRST y FOLLOW
        for (String nonTerminal : NO_TERMINALES) {
            FIRST.put(nonTerminal, new HashSet<>());
            FOLLOW.put(nonTerminal, new HashSet<>());
        }
        FOLLOW.get(start).add("$");

        // Generar First y Follow
        generarFirst(producciones);
        generarFollow(producciones);

        // Generar la tabla LL1 con sus gramáticas
        generarTablaLL1(producciones);
    }

    private void generarFirst(List<ProduccionLL> producciones) {

        boolean changed = true;
        while (changed) {
            changed = false;
            for (ProduccionLL produccion : producciones) {

                String left = produccion.getLeft();
                String[] right = produccion.getRight();

                // Si el primer simbolo de la derecha es terminal, añadirlo a FIRST
                if (TERMINALES.contains(right[0])) {
                    if (!FIRST.get(left).contains(right[0])) {
                        FIRST.get(left).add(right[0]);
                        changed = true;
                    }
                } else {
                    // Si el primer simbolo de la derecha es no terminal, añadir todos los FIRST de
                    // ese no terminal

                    // Si el primer simbolo de la derecha es ε, mirar el siguiente simbolo
                    // y si es un no terminal, añadir todos los FIRST de ese no terminal

                    if (!produccion.isEpsilon) {
                        FIRST.get(left).addAll(FIRST.get(right[0]));
                    }

                }

            }
        }

    }

    private void generarFollow(List<ProduccionLL> producciones) {

        boolean changed = true;
        while (changed) {
            changed = false;
            for (ProduccionLL produccion : producciones) {

                String left = produccion.getLeft();
                String[] right = produccion.getRight();

                for (int i = 0; i < right.length; i++) {
                    String symbol = right[i];

                    // Si el simbolo es no terminal
                    if (NO_TERMINALES.contains(symbol)) {
                        // Si el simbolo es el último de la derecha, añadir todos los FOLLOW de la
                        // izquierda
                        if (i == right.length - 1) {
                            FOLLOW.get(symbol).addAll(FOLLOW.get(left));
                        } else {
                            // Si el simbolo no es el último de la derecha
                            // Si el siguiente simbolo es terminal, añadirlo a FOLLOW
                            if (TERMINALES.contains(right[i + 1])) {
                                if (!FOLLOW.get(symbol).contains(right[i + 1])) {
                                    FOLLOW.get(symbol).add(right[i + 1]);
                                    changed = true;
                                }
                            } else {
                                // Si el siguiente simbolo es no terminal, añadir todos los FIRST de ese
                                // simbolo
                                HashSet<String> first = FIRST.get(right[i + 1]);
                                if (first.contains("ε")) {
                                    first.remove("ε");
                                    FOLLOW.get(symbol).addAll(first);
                                    if (!FOLLOW.get(symbol).containsAll(FOLLOW.get(left))) {
                                        FOLLOW.get(symbol).addAll(FOLLOW.get(left));
                                        changed = true;
                                    }
                                } else {
                                    FOLLOW.get(symbol).addAll(first);
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    private void generarTablaLL1(List<ProduccionLL> producciones) {

        for (ProduccionLL produccion : producciones) {
            String left = produccion.getLeft();
            String[] right = produccion.getRight();

            // Si el primer simbolo de la derecha es terminal, añadirlo a la tabla LL1
            if (TERMINALES.contains(right[0])) {
                if (!LL1TABLE.containsKey(left)) {
                    LL1TABLE.put(left, new HashMap<>());
                }
                LL1TABLE.get(left).put(right[0], produccion);
            } else {
                // Si el primer simbolo de la derecha es no terminal, añadir todos los FIRST de
                // ese no terminal

                // Si la produccion isEpsilon, añadir todos los FOLLOW de la izquierda
                if (produccion.isEpsilon) {
                    for (String terminal : FOLLOW.get(left)) {
                        if (!LL1TABLE.containsKey(left)) {
                            LL1TABLE.put(left, new HashMap<>());
                        }
                        if (LL1TABLE.get(left).containsKey(terminal)) {
                            throw new RuntimeException(
                                    "Collision error: Multiple productions for the same terminal and non-terminal combination");
                        }
                        LL1TABLE.get(left).put(terminal, produccion);
                    }
                    continue;
                }

                for (String terminal : FIRST.get(right[0])) {
                    if (!LL1TABLE.containsKey(left)) {
                        LL1TABLE.put(left, new HashMap<>());
                    }
                    if (LL1TABLE.get(left).containsKey(terminal)) {
                        throw new RuntimeException(
                                "Collision error: Multiple productions for the same terminal and non-terminal combination");
                    }
                    LL1TABLE.get(left).put(terminal, produccion);
                }
            }

        }
    }

    public String printLL1Table() {
        StringBuilder sb = new StringBuilder();
        for (String nonTerminal : LL1TABLE.keySet()) {
            sb.append("Non-terminal: ").append(nonTerminal).append("\n");
            sb.append("First: ").append(FIRST.get(nonTerminal)).append("\n");
            sb.append("Follow: ").append(FOLLOW.get(nonTerminal)).append("\n");
            for (String terminal : LL1TABLE.get(nonTerminal).keySet()) {
                sb.append("Productions: ").append(terminal).append(" -> ");
                sb.append(LL1TABLE.get(nonTerminal).get(terminal)).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean parse(String[] tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(this.start);

        int index = 0;
        String token = tokens[index];

        while (!stack.isEmpty()) {
            String top = stack.pop();
            if (TERMINALES.contains(top)) {
                if (top.equals(token)) {
                    index++;
                    if (index < tokens.length) {
                        token = tokens[index];
                    }
                } else {
                    return false; // Error de sintaxis
                }
            } else if (LL1TABLE.containsKey(top)) {
                ProduccionLL production = LL1TABLE.get(top).get(token);
                if (production != null) {
                    String[] rhs = production.right;
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

    public static void main(String[] args) {
        /*
         * DEMO
         * E -> T Ep .
         * Ep -> ADD T Ep | SUB T Ep | .
         * T -> F Tp .
         * Tp -> MUL F Tp | DIV F Tp | .
         * F -> OBracket E CBracket | int .
         */

        HashSet<String> TERMINALES = new HashSet<>();
        TERMINALES.add("ADD");
        TERMINALES.add("SUB");
        TERMINALES.add("ID");
        TERMINALES.add("$");

        HashSet<String> NO_TERMINALES = new HashSet<>();
        NO_TERMINALES.add("E");
        NO_TERMINALES.add("T");

        List<ProduccionLL> producciones = new ArrayList<>();
        producciones.add(new ProduccionLL("E", "T ADD T E"));
        producciones.add(new ProduccionLL("E", "ε"));
        producciones.add(new ProduccionLL("T", "ID"));

        LL1 lr1 = new LL1(TERMINALES, NO_TERMINALES, producciones, "E");
        System.out.println(lr1.printLL1Table());
        if (lr1.parse(new String[] { "ID","ADD","ID","ID","ADD","ID", "$" })) {
            System.out.println("Input is valid according to the grammar.");
        } else {
            System.out.println("Input is invalid.");
        }
    }
}
