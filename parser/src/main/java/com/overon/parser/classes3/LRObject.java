package com.overon.parser.classes3;

public class LRObject implements Comparable<LRObject> {
    private String left;
    private String produccion;
    private String lookahead;

    private int pointer;

    public String getProduccion() {
        return produccion;
    }

    public String getNext() {
        return produccion.toCharArray()[pointer] + "";
    }

    public String getLookahead() {
        return lookahead + "";
    }

    public String getLeft() {
        return left;
    }

    public boolean isFinal() {
        return pointer >= produccion.length();
    }

    public void getNextPointer() {
        pointer++;

    }

    public LRObject(String left, String produccion, String lookahead) {
        this.produccion = produccion; // Añadimos el simbolo de fin de cadena
        this.pointer = 0;
        // Si el puntero es menor que la longitud de la produccion, lookahead es $
        this.lookahead = pointer < produccion.length() ? lookahead : "$";
        this.left = left;
    }

    public LRObject(String left, String produccion, String lookahead, int pointer) {
        this.produccion = produccion;
        this.pointer = pointer;
        this.lookahead = lookahead;
        this.left = left;
    }

    @Override
    public int compareTo(LRObject o) {
        // Comparamos las producciones y los lookahead
        if (pointer != o.pointer) {
            return -1;
        }
        if (!left.equals(o.left)) {
            return -1;
        }
        if (lookahead != o.lookahead) {
            return -1;
        }

        return produccion.equals(o.produccion) ? 0 : -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left).append(" -> ");
        sb.append(produccion.substring(0, pointer));
        sb.append(" · ");
        sb.append(produccion.substring(pointer));
        sb.append(" , ").append(lookahead);
        return sb.toString();
    }

    public LRObject clone() {
        return new LRObject(left, produccion, lookahead, pointer);
    }
}
