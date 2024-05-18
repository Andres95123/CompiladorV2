package com.overon.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.foreign.SymbolLookup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.overon.LL1.LL1;
import com.overon.LL1.ProduccionLL;
import com.overon.interfaces.SymbolTables;

public class Parser {

    private String[] lines;
    private int actualLine;
    private SymbolTables table;

    // Atributos para la tabla de simbolos
    private HashSet<String> terminals;
    private HashSet<String> nonTerminals;
    private String start;
    private String tableClass;
    private List<ProduccionLL> productions;

    public Parser(String path) throws IOException {
        BufferedReader filReader = new BufferedReader(new FileReader(path));

        // Cargar las lineas del archivo
        lines = filReader.lines().toArray(String[]::new);
        actualLine = 0;
        getSettings();
        productions = new ArrayList<>();
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
        obtainTerminalsAndNonTerminalsAndProductions();
        createTable();
        filReader.close();
    }

    private String nextLine() {
        return lines[actualLine++];
    }

    private void createTable() {

        if (tableClass.equals("LL1")) {
            table = new LL1(terminals, nonTerminals, productions, start);
        } else if (tableClass.equals("LR1")) {

        } else {
            throw new IllegalArgumentException("Tabla de simbolos no reconocida");
        }
    }

    private void getSettings() {
        // Obtenemos los primeros settings del archivo dentro de ::SETTINGS::, START: S,
        // TABLE: LL1
        String line = nextLine();
        if (!line.equals("::SETTINGS::")) {
            throw new IllegalArgumentException("No se encontraron los settings");
        }
        // Seguir leyendo hasta encontrar el final de los settings
        while (!(line = nextLine()).equals("::PRODUCTIONS::")) {
            String[] setting = line.trim().split(":");
            if (setting[0].equals("START")) {
                // Inicializar el simbolo de inicio
                start = setting[1];
                System.out.println("Simbolo de inicio: " + start);
            } else if (setting[0].equals("TABLE")) {
                // Inicializar la tabla de simbolos
                if (setting[1].equals("LL1")) {
                    tableClass = "LL1";
                } else if (setting[1].equals("LR1")) {
                    tableClass = "LR1";
                    throw new IllegalArgumentException("LR1 no implementado, solo se soporta LL1 de momento");
                } else {
                    throw new IllegalArgumentException("Tabla de simbolos no reconocida");
                }

            }
        }

    }

    // Leer las lineas y obtener los Terminales y No Terminales
    private void obtainTerminalsAndNonTerminalsAndProductions() {

        int startLine = actualLine;
        String line;

        while (!(line = nextLine()).equals("::END::") && line != null) {

            // Si la linea es vacia, no hacer nada
            if (line.trim().length() == 0) {
                continue;
            }

            String[] input = line.split("->");
            if (input.length == 2) {
                String left = input[0].trim();
                nonTerminals.add(left);
                String right = input[1].trim();

                // Si right no tiene nada, lo consideramos un epsilon
                if (right.length() == 0) {
                    right = "ε";
                    System.out.println("Epsilon encontrado");
                }

                productions.add(new ProduccionLL(left, right));
            } else {
                throw new IllegalArgumentException("Error en la definicion de los simbolos");
            }

        }

        actualLine = startLine;
        while (!(line = nextLine()).equals("::END::") && line != null) {

            // Si la linea es vacia, no hacer nada
            if (line.trim().length() == 0) {
                continue;
            }

            String[] input = line.split("->");
            if (input.length == 2) {
                String right = input[1].trim();

                String[] rightSymbols = right.split(" ");
                for (String symbol : rightSymbols) {
                    if (!symbol.equals("ε") && !nonTerminals.contains(symbol)) {
                        terminals.add(symbol);
                    }
                }
            } else {
                throw new IllegalArgumentException("Error en la definicion de los simbolos");
            }

        }

        // Añadir el simbolo de fin de cadena
        terminals.add("$");

    }

    public void parse(String[] input) {
        boolean acc = table.parse(input);
        if (acc) {
            System.out.println("Cadena aceptada");
        } else {
            System.out.println("Cadena no aceptada");
        }
    }

    public static void main(String[] args) {
        try {
            Parser parser = new Parser("data/input.txt");
            String input = "id $";
            parser.parse(input.split(" "));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
