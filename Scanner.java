package out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Scanner {

    private BufferedReader reader;
    private int currentChar;

    public Scanner(String input) {
        reader = new BufferedReader(new StringReader(input));
        currentChar = 0;
    }

    public int nextToken() {
        StringBuilder sb = new StringBuilder();

        // Obtenemos la palabra hasta el siguiente espacio
        try {
            currentChar = reader.read();
            while (currentChar != -1 && currentChar != 32) {
                sb.append((char) currentChar);
                currentChar = reader.read();
            }

            if (sb.length() == 0 && currentChar == -1) {
                return Lexer.EOF;
            }
        } catch (IOException e) {
            return Lexer.EOF;
        }

        // Comprobamos si es algun token de los definidos
        String[] patterns = { Lexer.ID_MATCH, Lexer.NUMERO_MATCH, Lexer.OPERADOR_MATCH };
        int[] tokens = { Lexer.ID, Lexer.NUMERO, Lexer.OPERADOR };

        for (int i = 0; i < patterns.length; i++) {
            if (sb.toString().matches(patterns[i])) {
                return tokens[i];
            }
        }

        throw new RuntimeException("Error : Token no reconocido {" + sb.toString() + "}");
    }
}
