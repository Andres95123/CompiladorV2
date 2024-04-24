package sintactico;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import lexico.ParserLexOptions;
import util.Scanner;
import util.Token;
import util.automataPila;

public class automataCompiler {
    private automataPila automataPila = new automataPila();
    private Scanner scanner;

    public static void main(String[] args) throws Exception {
        new automataCompiler().init();
    }

    public void init() throws Exception {
        scanner = new Scanner("fichero.txt");

        Stack<Token> stack = new Stack<>();
        while (scanner.hasNext()) {
            Token token = scanner.next();

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
                        automataPila.addReduccion(tokensCorte, tokenReduccion);
                        ultimoCorte = i + 1;
                    }
                }
                // Subimos el ultimo corte, sin el token de EOL (length-1)
                automataPila.addReduccion(Arrays.copyOfRange(tokens, ultimoCorte, tokens.length - 1), tokenReduccion);
                stack.clear();
            }
        }
        new fileMaker(automataPila.getEnumTokens(), "TokensEnum");

        scanner = new Scanner("programa.txt");

        // Vaciamos la pila para empezar a usarla como almacenaje de tokens
        stack.clear();

        Stack stack2 = new Stack();

        // Vamos leyendo tokens y vamos revisando con el automataPila si hay alguna
        // reduccion
        while (scanner.hasNext()) {
            stack2.add(scanner.next());

            // Ir reduciendo poco a poco, sacando uno a uno los elementos de la pila
            // Y comparando para ver si hay alguna reduccion hasta que se ha revisado toda
            // la pila

            for (int i = 0; i < stack2.size(); i++) {
                Token[] tokens = new Token[stack2.size()];
                stack2.toArray(tokens);
                String tokensString = "";
                Token[] tokensTmp = Arrays.copyOfRange(tokens, tokens.length - i - 1, tokens.length);
                for (int j = 0; j < tokensTmp.length; j++) {
                    tokensString += tokensTmp[j].getValor();
                }
                String redu = automataPila.getReduccion(tokensString);
                if (redu != null) {
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
