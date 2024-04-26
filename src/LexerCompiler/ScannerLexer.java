package LexerCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lexico.ParserLex;
import util.Token;

public class ScannerLexer {

    private File archivo;
    private BufferedReader bufferedReader;

    // Lista con los Tokens

    List<Token> tokensEnum;
    int index = 0;
    int last = 0;

    public ScannerLexer(String archivo) throws IOException {
        this.archivo = new File("data/" + archivo);
        bufferedReader = new BufferedReader(new FileReader(this.archivo));

        // Generar la lista
        List<String> tokens = new ArrayList<>();
        tokensEnum = new ArrayList<>();

        // Añadir todos los tokens a la lista
        String res;

        do {
            res = bufferedReader.readLine();
            if (res != null) {
                for (String token : res.split(" ")) {
                    if (token.equals("")) {
                        continue;
                    }
                    tokens.add(token);
                    last++;
                }
            }
        } while (res != null);

        // Convertir los tokens a Enum
        for (String token : tokens) {
            tokensEnum.add(new Token(lexerTokenizer.getParsedLex(token), token));
        }
    }

    public Token next() {
        return tokensEnum.get(index++);
    }

    public boolean hasNext() {
        return index < last;
    }

    public void reset() {
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void close() {
        try {
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error al cerrar el archivo");
        }
    }

}
