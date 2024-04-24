package util;

import java.util.HashMap;

public class automataPila {
    HashMap<Token[], Token> reducciones;

    public automataPila() {
        reducciones = new HashMap<>();
    }

    public void addReduccion(Token[] tokens, Token token) {
        reducciones.put(tokens, token);
    }

    public String toString() {
        return reducciones.toString();
    }
}
