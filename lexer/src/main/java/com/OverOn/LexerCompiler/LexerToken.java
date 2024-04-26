package com.OverOn.LexerCompiler;

public class LexerToken {

    private int type;
    private String value;

    public LexerToken(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public LexerToken(int type) {
        this(type, "");
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " | " + (value);
    }
}
