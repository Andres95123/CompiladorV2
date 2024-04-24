package sintactico;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import lexico.ParserLexOptions;
import util.Scanner;
import util.Token;
import util.automataPila;

public class automataCompiler {
    HashMap<String, Token> tokensMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new automataCompiler().init();
    }

    public void init() throws IOException {
        Scanner scanner = new Scanner("fichero.txt");
        automataPila automataPila = new automataPila();
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
                automataPila.addReduccion(tokens, tokenReduccion);
                stack.clear();
            }
        }
        System.out.println(automataPila);
        scanner.close();
    }
}
