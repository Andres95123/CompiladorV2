package lexico;

public class ParserLex {

    public static ParserLexOptions getParsedLex(String input) {

        switch (input) {
            case inLex.DERIVATION:
                return ParserLexOptions.DERIVATION;
            case inLex.OR:
                return ParserLexOptions.OR;
            case inLex.OPTIONAL:
                return ParserLexOptions.OPTIONAL;
            case inLex.CLOSURE:
                return ParserLexOptions.CLOSURE;
            case inLex.EOL:
                return ParserLexOptions.EOL;
            case inLex.POSITIVE_CLOSURE:
                return ParserLexOptions.POSITIVE_CLOSURE;
            case inLex.OPEN_EX:
                return ParserLexOptions.OPEN_EX;
            case inLex.CLOSE_EX:
                return ParserLexOptions.CLOSE_EX;
            default:

                if (input.matches(inLex.CONST_MATCH)) {
                    return ParserLexOptions.CONST;
                }
                if (input.matches(inLex.ID_MATCH)) {
                    return ParserLexOptions.ID;
                }
                return ParserLexOptions.ERROR;
        }

    }

}
