package main.java.sintactico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import main.java.lexico.ParserLexOptions;
import main.java.util.Scanner;
import main.java.util.Token;
import main.java.util.automataPila;

public class automataCompiler {
    private automataPila automataPila = new automataPila();
    private Scanner scanner;
    private List<String> codeList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new automataCompiler().init();
    }

    public void init() throws Exception {
        scanner = new Scanner("fichero.txt");

        Stack<Token> stack = new Stack<>();

        while (scanner.hasNext()) {
            Token token = scanner.next();

            // Si el token es un EOL, entonces se ha terminado la linea y se procede a
            // obtener el codigo ejecutable entre {}

            if (token.getTipo() == ParserLexOptions.OPEN_EX) {
                String codigo = "";
                while (scanner.hasNext()) {
                    if (token.getTipo() == ParserLexOptions.CLOSE_EX) {
                        codigo += token.getValor();
                        token = scanner.next();
                        codeList.add(codigo);
                        break;
                    }
                    codigo += token.getValor();
                    token = scanner.next();
                }

            }
            // Añadimos los tokens a la pila
            stack.push(token);
            // Esperamos hasta que termine la reduccion y luego la añadimos al automata
            if (token.getTipo() == ParserLexOptions.EOL) {
                Token[] tokens = new Token[stack.size()];
                stack.toArray(tokens);
                Token tokenReduccion = tokens[0];
                tokens = Arrays.copyOfRange(tokens, 2, tokens.length);
                // Dividimos los tokens en N partes separando los OR
                int ultimoCorte = 0;
                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].getTipo() == ParserLexOptions.OR) {
                        Token[] tokensCorte = Arrays.copyOfRange(tokens, ultimoCorte, i);
                        if (codeList.size() >= 1) {
                            automataPila.addExecute(tokensCorte, codeList.remove(0));
                        }
                        automataPila.addReduccion(tokensCorte, tokenReduccion);
                        ultimoCorte = i + 1;
                    }
                }
                // Subimos el ultimo corte, sin el token de EOL (length-1)
                Token[] tokensCorte = Arrays.copyOfRange(tokens, ultimoCorte, tokens.length - 1);
                if (codeList.size() >= 1) {
                    automataPila.addExecute(tokensCorte, codeList.remove(0));
                }

                automataPila.addReduccion(tokensCorte, tokenReduccion);
                stack.clear();
            }
        }
        new fileMaker(automataPila.getEnumTokens(), "TokensEnum");

        scanner = new Scanner("programa.txt");

        // Vaciamos la pila para empezar a usarla como almacenaje de tokens
        stack.clear();

        Stack<Token> stack2 = new Stack();

        // Vamos leyendo tokens y vamos revisando con el automataPila si hay alguna
        // reduccion
        while (scanner.hasNext()) {
            stack2.add(scanner.next());

            // Ir reduciendo poco a poco, sacando uno a uno los elementos de la pila
            // Y comparando para ver si hay alguna reduccion hasta que se ha revisado toda
            // la pila

            for (int i = 0; i < stack2.size(); i++) {
                Token[] tokens = stack2.toArray(new Token[0]);
                String tokensString = "";
                // Transformamos los tokens en strings
                Token[] tokensTmp = Arrays.copyOfRange(tokens, i, tokens.length);
                for (int j = 0; j < tokensTmp.length; j++) {
                    tokensString += tokensTmp[j].getValor();
                }
                // Revisamos todas las combinaciones de reducciones hasta que ya no queden
                String redu = automataPila.getReduccion(tokensString);
                if (redu != null) {
                    // Invertimos la pila
                    // Sacamos los tokens que se van a reducir
                    for (int j = 0; j < tokensTmp.length; j++) {
                        stack2.pop();
                    }

                    stack2.add(new Token(ParserLexOptions.ID, redu));
                    i = -1;
                }
            }

        }

        // Hacemos print final de la reduccion
        System.out.println(stack2.toString());

        scanner.close();
    }

}
