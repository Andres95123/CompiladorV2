package com.overon.LL1;

public class ProduccionLL {

    String left;
    String[] right;
    boolean isEpsilon = false;

    public ProduccionLL(String left, String right) {
        this.left = left;
        this.right = right.split(" ");
        isEpsilon = right.equals("ε");
    }

    public ProduccionLL(String left, String[] right, boolean isEpsilon) {
        this.left = left;
        this.right = right;
        this.isEpsilon = isEpsilon;
    }

    public String getLeft() {
        return left;
    }

    public String getRight(int index) {
        if (index >= right.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return right[index];
    }

    public String[] getRight() {

        return right;
    }

    public String getLast() {
        return right[right.length - 1];
    }

    @Override
    public ProduccionLL clone() {
        return new ProduccionLL(left, right, isEpsilon);
    }

    @Override
    public String toString() {
        // Devuelve la produccion con el puntero en la posicion correcta
        return left + " -> " + right;
    }

}
