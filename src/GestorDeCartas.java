import java.util.List;

public class GestorDeCartas {

    private int[] mazoUnico = new int[4];
    private final String[] nombresElementos = {"Fuego", "Agua", "Aire", "Tierra"};

    public void registrarCartas(List<Carta> mazoRobado) {
        for (Carta carta : mazoRobado) {
            int elemento = carta.elemento;
            int valor = carta.valor - 1;
            this.mazoUnico[elemento] |= (1 << valor);
        }
    }

    public void mostrarInventario() {
        System.out.println("=== INVENTARIO DE CARTAS (BITWISE) ===");
        System.out.println("Valores:    1  2  3  4  5  6  7  8  9");
        System.out.println("----------------------------------------");
        for (int f = 0; f < 4; f++) {
            System.out.print(nombresElementos[f] + "| ");
            for (int c = 0; c < 9; c++) {
                String marca = ((this.mazoUnico[f] & (1 << c)) != 0) ? "1" : "0";
                System.out.print(marca + "  ");
            }
            System.out.println();
        }
        System.out.println("========================================\n");
    }

    // EL ORQUESTADOR PRINCIPAL DEVUELVE AHORA EL OBJETO COMPLETO
    public ResultadoJugada evaluarMejorJugada() {
        ResultadoJugada mejor = buscarMejorCadena();

        if (mejor.puntos > 0) {
            return mejor;
        }

        ResultadoJugada sec = buscarMejorSecuencia();
        if (sec.puntos > mejor.puntos) {
            mejor = sec;
        }

        if (mejor.puntos > 27) {
            return mejor;
        }

        ResultadoJugada tri = buscarMejorTriada();
        if (tri.puntos > mejor.puntos) {
            mejor = tri;
        }

        return mejor;
    }

    private ResultadoJugada buscarMejorCadena() {
        for (int c = 4; c >= 0; c--) {
            boolean fuego1 = (this.mazoUnico[0] & (1 << c)) != 0;
            boolean aire   = (this.mazoUnico[2] & (1 << (c + 1))) != 0;
            boolean tierra = (this.mazoUnico[3] & (1 << (c + 2))) != 0;
            boolean agua   = (this.mazoUnico[1] & (1 << (c + 3))) != 0;
            boolean fuego2 = (this.mazoUnico[0] & (1 << (c + 4))) != 0;

            if (fuego1 && aire && tierra && agua && fuego2) {
                int sumaValores = (c + 1) + (c + 2) + (c + 3) + (c + 4) + (c + 5);
                String detalle = "Fuego " + (c+1) + " -> Aire " + (c+2) + " -> Tierra " + (c+3) + " -> Agua " + (c+4) + " -> Fuego " + (c+5);
                return new ResultadoJugada("Cadena", sumaValores * 2, detalle);
            }
        }
        return new ResultadoJugada("Ninguna", 0, "");
    }

    private ResultadoJugada buscarMejorSecuencia() {
        ResultadoJugada mejor = new ResultadoJugada("Ninguna", 0, "");
        int sumaRachaActual = 0;
        int longitudRacha = 0;
        int filaAnterior = 0;

        for (int i = 0; i < 36; i++) {
            int f = i / 9;
            int c = i % 9;

            if (f != filaAnterior) {
                sumaRachaActual = 0;
                longitudRacha = 0;
                filaAnterior = f;
            }

            if ((this.mazoUnico[f] & (1 << c)) != 0) {
                int valorDeLaCarta = c + 1;
                sumaRachaActual += valorDeLaCarta;
                longitudRacha++;

                if (longitudRacha > 4) {
                    sumaRachaActual -= (valorDeLaCarta - 4);
                    longitudRacha = 4;
                }

                if (longitudRacha >= 2 && sumaRachaActual > mejor.puntos) {
                    // Armamos el detalle dinámico de las cartas de la secuencia
                    StringBuilder detalle = new StringBuilder();
                    int valorInicio = valorDeLaCarta - longitudRacha + 1;
                    for(int v = valorInicio; v <= valorDeLaCarta; v++) {
                        detalle.append(nombresElementos[f]).append(" ").append(v).append(v == valorDeLaCarta ? "" : ", ");
                    }
                    mejor = new ResultadoJugada("Secuencia", sumaRachaActual, detalle.toString());
                }
            } else {
                sumaRachaActual = 0;
                longitudRacha = 0;
            }
        }
        return mejor;
    }

    private ResultadoJugada buscarMejorTriada() {
        for (int c = 8; c >= 0; c--) {
            int elementosConEstaCarta = 0;
            StringBuilder detalle = new StringBuilder();

            for (int f = 0; f < 4; f++) {
                if ((this.mazoUnico[f] & (1 << c)) != 0) {
                    elementosConEstaCarta++;
                    detalle.append(nombresElementos[f]).append(" ").append(c + 1).append(" | ");
                }
            }

            if (elementosConEstaCarta >= 3) {
                // Removemos el último " | " que sobra al final
                String detalleFinal = detalle.toString().substring(0, detalle.length() - 3);
                int valorDeLaCarta = c + 1;
                return new ResultadoJugada("Tríada", valorDeLaCarta * 3, detalleFinal);
            }
        }
        return new ResultadoJugada("Ninguna", 0, "");
    }
}