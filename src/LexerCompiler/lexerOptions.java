package LexerCompiler;

public enum lexerOptions {
    ID, CONST, ASSIGN, START, DEFINITIONS, END;

    public static final String ID_MATCH = "[a-zA-Z_]+";
    public static final String CONST_MATCH = "\"[^\"]+\"";
    public static final String ASSIGN_MATCH = "=";
    public static final String START_MATCH = "::START::";
    public static final String DEFINITIONS_MATCH = "::DEFINITIONS::";
    public static final String END_MATCH = "::END::";

}