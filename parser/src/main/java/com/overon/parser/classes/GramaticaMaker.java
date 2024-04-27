package com.overon.parser.classes;

import java.util.Arrays;

// Esta clase se encarga de generar la gramática basandose en un archivo de texto
// Utiliza la clase LRTable para generar la tabla de análisis sintáctico
// Utiliza los símbolos de la clase GramaticaLexic para leer el archivo y definir la gramática
public class GramaticaMaker {

    private LRTable table;

    public GramaticaMaker() {
        table = new LRTable();
    }

    public void make(String grammar) {
        String[] lines = grammar.split("\n");

        for (String line : lines) {
            String[] parts = line.split(GramaticaLexic.DERIVACION_ASSIGNATION);

            String left = parts[0].trim();
            String[] right = parts[1].split(GramaticaLexic.DERIVACION_OR);

            // Quita los espacios en blanco de la parte derecha

            right = Arrays.stream(right)
                    .filter(s -> !s.trim().isEmpty())
                    .toArray(String[]::new);

            for (String r : right) {
                table.addRule(left, r.trim());
            }
        }

        // Añade el simbolo de aceptacion
        table.addAction(0, GramaticaLexic.DERIVACION_FIN);
    }

    public LRTable getTable() {
        return table;
    }

    public static void main(String[] args) {
        GramaticaMaker maker = new GramaticaMaker();
        maker.make("E ::= E + T | T\n" +
                "T ::= T * F | F\n" +
                "F ::= ( E ) | I");
        LRTable table = maker.getTable();
        // Parsear la tabla con un ejemplo

        LRParser parser = new LRParser(table);
        parser.parse("E+T*F+T;"); // Cadena aceptada correctamente

        System.out.println(table);
    }

}
