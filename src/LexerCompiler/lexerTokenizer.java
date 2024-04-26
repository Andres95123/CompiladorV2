package LexerCompiler;

public class lexerTokenizer {

    public static lexerOptions getParsedLex(String input) {

        switch (input) {
            case lexerOptions.START_MATCH:
                return lexerOptions.START;
            case lexerOptions.ASSIGN_MATCH:
                return lexerOptions.ASSIGN;
            case lexerOptions.DEFINITIONS_MATCH:
                return lexerOptions.DEFINITIONS;
            case lexerOptions.END_MATCH:
                return lexerOptions.END;
            case lexerOptions.ORDER_MATCH:
                return lexerOptions.ORDER;
            default:

                if (input.matches(lexerOptions.CONST_MATCH)) {
                    return lexerOptions.CONST;
                }
                if (input.matches(lexerOptions.ID_MATCH)) {
                    return lexerOptions.ID;
                }
                throw new RuntimeException("Error : Token no reconocido {" + input + "}");

        }

    }

}
