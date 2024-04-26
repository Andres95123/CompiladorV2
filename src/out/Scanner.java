package out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import LexerCompiler.LexerToken;

public class Scanner {

    private BufferedReader reader;
    private int currentChar;
    private boolean eof;

    public Scanner(String input) {
        reader = new BufferedReader(new StringReader(input));
        currentChar = 0;
    }

    public LexerToken nextToken() {
        StringBuilder sb = new StringBuilder();

        // Obtenemos la palabra hasta el siguiente espacio
        try {
            currentChar = reader.read();
            while (currentChar != -1 && currentChar != 32) {
                sb.append((char) currentChar);
                currentChar = reader.read();
            }

            if (sb.length() == 0 && currentChar == -1) {
                eof = true;
                return new LexerToken(Lexer.EOF);
            }
        } catch (IOException e) {
            return new LexerToken(Lexer.EOF);
        }

        // Comprobamos si es algun token de los definidos
        String[] patterns = { Lexer.WS_MATCH, Lexer.ENDLINE_MATCH, Lexer.IF_MATCH, Lexer.ELSE_MATCH, Lexer.NUMERO_HEXA_MATCH, Lexer.NUMERO_MATCH, Lexer.OPERADOR_MATCH, Lexer.ID_MATCH };
        int[] tokens = { Lexer.WS, Lexer.ENDLINE, Lexer.IF, Lexer.ELSE, Lexer.NUMERO_HEXA, Lexer.NUMERO, Lexer.OPERADOR, Lexer.ID };

        for (int i = 0; i < patterns.length; i++) {
            if (sb.toString().matches(patterns[i])) {
                return new LexerToken(tokens[i],sb.toString());
            }
        }

        throw new RuntimeException("Error : Token no reconocido {" + sb.toString() + "}");
    }

    public boolean hasNext() {
        try {
            return !eof && reader.ready();
        } catch (IOException e) {
            return false;
        }
    }
}
