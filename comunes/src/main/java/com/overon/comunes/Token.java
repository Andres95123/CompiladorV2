package com.overon.comunes;

public class Token<Alfabeto extends Enum<Alfabeto>> {

    private Alfabeto tipo;
    private String valor;

    public Token(Alfabeto tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Token(Alfabeto tipo) {
        this(tipo, "");
    }

    public Alfabeto getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

}
