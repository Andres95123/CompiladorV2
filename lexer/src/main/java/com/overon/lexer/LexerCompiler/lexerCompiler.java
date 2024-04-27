package com.overon.lexer.LexerCompiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.overon.comunes.Token;

public class lexerCompiler {

    ScannerLexer scanner;
    Token lastToken;
    List<regexContainer> regexList = new ArrayList<>();
    HashMap<String, String> specialAssigns = new HashMap<>();

    public void compilerLexer(String path, String outPath) throws IOException {

        // Obtener los elementos del archivo y procesarlos
        scanner = new ScannerLexer(path);
        obtenerStartDefinitions();
        obtenerAllRegex();
        // Reordenar los regex segun el orden de aparicion
        obtenerOrden();

        // Verificar que se ha llegado al final del archivo
        if (scanner.hasNext()) {
            throw new IllegalArgumentException("Error en la definicion del fichero, se esperaba la palabra END");
        }

        // Crear el archivo de salida
        createLexerFile(outPath + "\\Lexer.java");
        // Crear el archivo del Scanner
        createScannerFile(outPath + "\\Scanner.java");

    }

    private void obtenerOrden() {

        // El fichero debe empezar con la palabra reservada ORDER

        if (lastToken.getTipo() != lexerOptions.ORDER) {
            // El orden es opcional
            return;
        }

        // Saltar el token de asignacion

        // Ir leyendo los tokens con caracteristicas especiales hasta llegar al token
        // deffinitions
        List<String> orderList = new ArrayList<>();
        while (scanner.hasNext()) {

            lastToken = scanner.next();

            if (lastToken.getTipo() == lexerOptions.END) {
                break;
            }

            // En este apartado hay asignaciones especiales, como la de package, donde se
            // determina el package del Scanner y el lexer creados

            if (lastToken.getTipo() == lexerOptions.ID) {

                orderList.add(lastToken.getValor());

            }

        }

        // Reordenar los regex segun el orden de aparicion con la mejor complejidad
        // posible
        Map<String, regexContainer> regexMap = new HashMap<>();
        for (regexContainer regexContainer : regexList) {
            regexMap.put(regexContainer.getToken(), regexContainer);
        }

        List<regexContainer> newRegexList = new ArrayList<>();
        for (String order : orderList) {
            regexContainer regexContainer = regexMap.get(order);
            if (regexContainer != null) {
                newRegexList.add(regexContainer);
            } else {
                throw new IllegalArgumentException("Error: Token '" + order + "' no ha sido declarado.");
            }
        }
        regexList = newRegexList;

        System.out.println("Elementos en el orden: " + orderList.size() + " elementos : " + orderList.toString());
    }

    private void obtenerStartDefinitions() {

        // El fichero debe empezar con la palabra reservada START
        lastToken = scanner.next();

        if (lastToken.getTipo() != lexerOptions.START) {
            throw new IllegalArgumentException("Error en la definicion del fichero, se esperaba la palabra START");
        }

        // Saltar el token de asignacion

        // Ir leyendo los tokens con caracteristicas especiales hasta llegar al token
        // deffinitions
        while (scanner.hasNext()) {

            lastToken = scanner.next();

            if (lastToken.getTipo() == lexerOptions.DEFINITIONS) {
                break;
            }

            // En este apartado hay asignaciones especiales, como la de package, donde se
            // determina el package del Scanner y el lexer creados

            if (lastToken.getTipo() == lexerOptions.ID) {

                // Revisamos que sea una asignacion especial
                if (!reservedWords.isStartDeclaration(lastToken.getValor())) {
                    throw new IllegalArgumentException(
                            "Error con la definicion inicial, no se reconoce la palabra reservada "
                                    + lastToken.getValor()
                                    + " aproximadamente en el token "
                                    + scanner.getIndex());
                }

                scanner.next(); // Saltar el igual
                Token regex = scanner.next();

                // Si no es un valor string constante, lanzar error
                if (regex.getTipo() != lexerOptions.CONST) {
                    throw new IllegalArgumentException(
                            "Error en la definicion de los regex aproximadamente en el token " + scanner.getIndex());
                }

                String regexSTR = regex.getValor().substring(1);
                regexSTR = regexSTR.substring(0, regexSTR.length() - 1);
                specialAssigns.put(lastToken.getValor().toUpperCase(), regexSTR);
            }
        }

    }

    private void obtenerAllRegex() {

        while (scanner.hasNext()) {

            // Obtener los regex, que tendran la forma ID = REGEX

            lastToken = scanner.next();

            // Si se llega al END, terminar los regex
            if (lastToken.getTipo() == lexerOptions.END || lastToken.getTipo() == lexerOptions.ORDER) {
                break;
            }

            if (lastToken.getTipo() == lexerOptions.ID) {
                scanner.next(); // Saltar el igual
                Token regex = scanner.next();

                // Si no es un regex constante, lanzar error
                if (regex.getTipo() != lexerOptions.CONST) {
                    throw new IllegalArgumentException(
                            "Error en la definicion de los regex aproximadamente en el token " + scanner.getIndex());
                }

                regexList.add(new regexContainer(regex.getValor(), lastToken.getValor()));
            }

        }

    }

    // Escribir el fichero

    private void createScannerFile(String outPath) {

        StringBuilder stringBuilder = new StringBuilder();

        // Creamos el scanner que utilizara el lexer creado para analizar el texto y
        // devolver tokens segun los regex definidos

        StringBuilder sb = new StringBuilder();
        if (specialAssigns.containsKey("PACKAGE")) {
            sb.append("package " + specialAssigns.get("PACKAGE") + ";\n");
        }
        sb.append("\n");
        sb.append("import java.io.BufferedReader;\n");
        sb.append("import java.io.IOException;\n");
        sb.append("import java.io.StringReader;\n");
        sb.append("import LexerCompiler.LexerToken;\n");
        sb.append("\n");
        sb.append("public class Scanner {\n");
        sb.append("\n");
        sb.append("    private BufferedReader reader;\n");
        sb.append("    private int currentChar;\n");
        sb.append("    private boolean eof;\n");
        sb.append("\n");
        sb.append("    public Scanner(String input) {\n");
        sb.append("        reader = new BufferedReader(new StringReader(input));\n");
        sb.append("        currentChar = 0;\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    public LexerToken nextToken() {\n");
        sb.append("        StringBuilder sb = new StringBuilder();\n");
        sb.append("\n");
        sb.append("        // Obtenemos la palabra hasta el siguiente espacio\n");
        sb.append("        try {\n");
        sb.append("            currentChar = reader.read();\n");
        sb.append("            while (currentChar != -1 && currentChar != 32) {\n");
        sb.append("                sb.append((char) currentChar);\n");
        sb.append("                currentChar = reader.read();\n");
        sb.append("            }\n");
        sb.append("\n");
        sb.append("            if (sb.length() == 0 && currentChar == -1) {\n");
        sb.append("                eof = true;\n");
        sb.append("                return new LexerToken(Lexer.EOF);\n");
        sb.append("            }\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            return new LexerToken(Lexer.EOF);\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        // Comprobamos si es algun token de los definidos\n");

        StringBuilder patternsBuilder = new StringBuilder();
        StringBuilder tokensBuilder = new StringBuilder();
        for (regexContainer regexContainer : regexList) {
            patternsBuilder.append("Lexer." + regexContainer.getToken().toUpperCase() + "_MATCH, ");
            tokensBuilder.append("Lexer." + regexContainer.getToken().toUpperCase() + ", ");
        }
        String patterns = patternsBuilder.toString();
        String tokens = tokensBuilder.toString();
        patterns = patterns.substring(0, patterns.length() - 2); // Borrar ultima come y espacios
        tokens = tokens.substring(0, tokens.length() - 2); // Borrar ultima come y espacios

        sb.append("        String[] patterns = { " + patterns + " };\n");
        sb.append("        int[] tokens = { " + tokens + " };\n");
        sb.append("\n");
        sb.append("        for (int i = 0; i < patterns.length; i++) {\n");
        sb.append("            if (sb.toString().matches(patterns[i])) {\n");
        sb.append("                return new LexerToken(tokens[i],sb.toString());\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("\n");
        sb.append("        throw new RuntimeException(\"Error : Token no reconocido {\" + sb.toString() + \"}\");\n");
        sb.append("    }\n");
        // Has next
        sb.append("\n");
        sb.append("    public boolean hasNext() {\n");
        sb.append("        try {\n");
        sb.append("            return !eof && reader.ready();\n");
        sb.append("        } catch (IOException e) {\n");
        sb.append("            return false;\n");
        sb.append("        }\n");
        sb.append("    }\n");

        sb.append("}\n");

        // Guardamos el archivo
        try {
            java.io.FileWriter myWriter = new java.io.FileWriter(outPath);
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Ha habido un error generando la clase Scanner. " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void createLexerFile(String outPath) {

        StringBuilder stringBuilder = new StringBuilder();

        if (specialAssigns.containsKey("PACKAGE")) {
            stringBuilder.append("package " + specialAssigns.get("PACKAGE") + ";\n\n");
        }
        stringBuilder.append("public class Lexer {\n\n");
        stringBuilder.append("    public static final int EOF = -1;\n");
        stringBuilder.append("    public static final int ERROR = -2;\n\n");

        for (regexContainer regexContainer : regexList) {
            stringBuilder.append("    public static final int " + regexContainer.getToken().toUpperCase() + " = "
                    + regexContainer.getToken().hashCode() + ";\n");
        }

        stringBuilder.append("\n");

        for (regexContainer regexContainer : regexList) {
            stringBuilder.append("    public static final String " + regexContainer.getToken().toUpperCase()
                    + "_MATCH = " + regexContainer.getRegex() + ";\n");
        }

        stringBuilder.append("}");

        // Escribir el archivo con un buffer

        try {
            java.io.FileWriter myWriter = new java.io.FileWriter(outPath);
            myWriter.write(stringBuilder.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}

class regexContainer {

    private String regex;
    private String token;

    public regexContainer(String regex, String token) {
        this.regex = regex;
        this.token = token;
    }

    public String getRegex() {
        return regex;
    }

    public String getToken() {
        return token;
    }

}
