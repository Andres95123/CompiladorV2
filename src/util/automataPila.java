package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lexico.ParserLexOptions;

public class automataPila {
    HashMap<String, String> reducciones;
    Set<String> tokensEnum = new HashSet<>();

    public automataPila() {
        reducciones = new HashMap<>();
    }

    public void addReduccion(Token[] tokens, Token token) throws Exception {
        // Comprovar si existe ya en el hashmap

        if (reducciones.containsKey(tokens)) {
            throw new Exception("Error: " + tokens + " ya existe en el hashmap");
        }

        if (!tokensEnum.contains(token.getValor())) {
            tokensEnum.add(token.getValor());
        }

        for (Token tokenEnum : tokens) {
            if (!tokensEnum.contains(tokenEnum.getValor())) {
                tokensEnum.add(tokenEnum.getValor());
            }
        }

        // Transformar tokens a un array de string con sus getValor
        String[] tokensString = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            tokensString[i] = tokens[i].getValor();
        }

        reducciones.put(String.join("", tokensString), token.getValor());
    }

    public String getReduccion(String tokens) {
        // Transformar tokens a un array de string con sus getValor

        return reducciones.get(tokens);

    }

    public String[] getEnumTokens() {
        return tokensEnum.toArray(new String[0]);
    }

    public String toString() {
        return reducciones.toString();
    }
}
