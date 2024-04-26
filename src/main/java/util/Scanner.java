package main.java.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import main.java.lexico.ParserLex;
import main.java.lexico.ParserLexOptions;

public class Scanner {

    private File archivo;
    private BufferedReader bufferedReader;

    // Lista con los Tokens

    List<Token> tokensEnum;
    int index = 0;
    int last = 0;

    public Scanner(String archivo) throws IOException {
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
                    tokens.add(token);
                    last++;
                }
            }
        } while (res != null);

        // Convertir los tokens a Enum
        for (String token : tokens) {
            tokensEnum.add(new Token(ParserLex.getParsedLex(token), token));
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
