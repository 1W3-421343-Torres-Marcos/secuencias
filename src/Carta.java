public class Carta {
    // 0: Fuego, 1: Agua, 2: Aire, 3: Tierra
    public int elemento;

    // Valores del 1 al 9
    public int valor;

    public Carta(int elemento, int valor) {
        this.elemento = elemento;
        this.valor = valor;
    }

    @Override
    public String toString() {
        String[] nombresElementos = {"Fuego", "Agua", "Aire", "Tierra"};
        return nombresElementos[elemento] + " " + valor;
    }
}