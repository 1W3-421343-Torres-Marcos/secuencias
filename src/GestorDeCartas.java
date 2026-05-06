import java.util.List;
import java.util.StringJoiner;

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
        int mejorSuma = 0;
        int mejorFila = -1;
        int mejorFin = -1;   // índice 'c' de la última carta de la mejor ventana
        int mejorLong = 0;

        for (int f = 0; f < 4; f++) {
            int mascara = this.mazoUnico[f];
            int inicioRacha = -1;

            for (int c = 0; c < 9; c++) {
                if ((mascara & (1 << c)) != 0) {
                    if (inicioRacha == -1) inicioRacha = c;

                    int longActual = c - inicioRacha + 1;
                    int ventana = Math.min(longActual, 4); // máximo de 4 cartas

                    // Como los valores son crecientes, la ventana óptima siempre
                    // termina en 'c'. Calculamos la suma por fórmula, sin acumulador.
                    int valorFin = c + 1;
                    int valorIni = valorFin - ventana + 1;
                    int suma = ventana * (valorIni + valorFin) / 2;

                    if (ventana >= 2 && suma > mejorSuma) {
                        // Solo guardamos coordenadas, nada de Strings todavía
                        mejorSuma = suma;
                        mejorFila = f;
                        mejorFin = c;
                        mejorLong = ventana;
                    }
                } else {
                    inicioRacha = -1; // se cortó la racha
                }
            }
        }

        if (mejorFila == -1) return new ResultadoJugada("Ninguna", 0, "");

        // Construimos el String UNA sola vez, con el resultado ya definitivo
        StringBuilder detalle = new StringBuilder();
        int valorFin = mejorFin + 1;
        int valorIni = valorFin - mejorLong + 1;
        for (int v = valorIni; v <= valorFin; v++) {
            detalle.append(nombresElementos[mejorFila]).append(" ").append(v);
            if (v < valorFin) detalle.append(", ");
        }

        return new ResultadoJugada("Secuencia", mejorSuma, detalle.toString());
    }

    private ResultadoJugada buscarMejorTriada() {
        for (int c = 8; c >= 0; c--) {
            // Fase 1: solo contamos, sin construir texto todavía
            int elementosConEstaCarta = Integer.bitCount(
                    ((this.mazoUnico[0] >> c) & 1) |
                            (((this.mazoUnico[1] >> c) & 1) << 1) |
                            (((this.mazoUnico[2] >> c) & 1) << 2) |
                            (((this.mazoUnico[3] >> c) & 1) << 3)
            );
            // Forma más legible de lo mismo:
            // contar cuántos de los 4 mazos tienen el bit 'c' encendido

            if (elementosConEstaCarta >= 3) {
                // Fase 2: recién ahora construimos el detalle, una sola vez
                StringJoiner detalle = new StringJoiner(" | ");
                for (int f = 0; f < 4; f++) {
                    if ((this.mazoUnico[f] & (1 << c)) != 0) {
                        detalle.add(nombresElementos[f] + " " + (c + 1));
                    }
                }
                // Puntaje correcto: usa la cantidad real de elementos
                return new ResultadoJugada("Tríada", (c + 1) * elementosConEstaCarta, detalle.toString());
            }
        }
        return new ResultadoJugada("Ninguna", 0, "");
    }
}