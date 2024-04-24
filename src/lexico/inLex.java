package lexico;

public class inLex {
    public final static int EOF = -1;
    public final static int ERROR = -2;

    public final static String DERIVATION = "::=";
    public final static String ENTER = "\n";
    public final static String OR = "|";
    public final static String OPTIONAL = "?";
    public final static String CLOSURE = "*";
    public final static String POSITIVE_CLOSURE = "+";
    public final static String EOL = ";";

    public final static String ID_MATCH = "[a-zA-Z_][a-zA-Z0-9_]*";
    public final static String CONST_MATCH = "[\"'][^\"']*" + ID_MATCH + "[^\"']*['\"]";
}
