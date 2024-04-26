package out;

public class Lexer {

    public static final int EOF = -1;
    public static final int ERROR = -2;

    public static final int WS = 2780;
    public static final int ENDLINE = -888545489;
    public static final int IF = 2333;
    public static final int ELSE = 2131257;
    public static final int NUMERO_HEXA = -1874733335;
    public static final int NUMERO = -1981031396;
    public static final int OPERADOR = 282057876;
    public static final int ID = 2331;

    public static final String WS_MATCH = "['\u0020'\t\n]+";
    public static final String ENDLINE_MATCH = "[\n]+";
    public static final String IF_MATCH = "if";
    public static final String ELSE_MATCH = "else";
    public static final String NUMERO_HEXA_MATCH = "0x[0-9]+";
    public static final String NUMERO_MATCH = "[0-9]+";
    public static final String OPERADOR_MATCH = "[+\\-*/]";
    public static final String ID_MATCH = "[a-zA-Z]+";
}