package util;

public class automataFD<Alfabeto extends Enum<Alfabeto>> {

    private Alfabeto[] E;
    private int q0;
    private int[] F;
    private int[][] transiciones;

    private int estadoActual;

    public automataFD(Alfabeto[] E, int q0, int[] F, int[][] transiciones) {
        this.E = E;
        this.q0 = q0;
        this.estadoActual = q0;
        this.F = F;
        this.transiciones = transiciones;
    }

    public void reset() {
        this.estadoActual = q0;
    }

    public boolean isFinal() {
        for (int i = 0; i < F.length; i++) {
            if (estadoActual == F[i]) {
                return true;
            }
        }
        return false;
    }

    public int procesar(Alfabeto simbolo) {
        estadoActual = transiciones[estadoActual][simbolo.ordinal()];
        return estadoActual;
    }

}
