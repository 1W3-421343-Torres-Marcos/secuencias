import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== JUEGO DE CARTAS: MAXIMIZADOR DE PUNTAJE ===");
        System.out.print("Ingrese la cantidad de cartas que va a robar el jugador: ");
        int cantidadCartas = scanner.nextInt();

        // 1. Obtenemos las cartas
        List<Carta> cartasRobadas = simularCartas(cantidadCartas);
        System.out.println("\nSe han generado " + cartasRobadas.size() + " cartas aleatorias.\n");

        // 2. Instanciamos el motor lógico
        GestorDeCartas gestor = new GestorDeCartas();

        // 3. Le pasamos los datos para que arme su matriz interna
        gestor.registrarCartas(cartasRobadas);

        // (Opcional) Mostramos la matriz para depuración visual
        gestor.mostrarInventario();

        // 4. LA LLAMADA MAESTRA: Pedimos el resultado final
        ResultadoJugada puntajeMaximo = gestor.evaluarMejorJugada();

        System.out.println("================================================");
        System.out.println("LA MEJOR JUGADA POSIBLE OTORGA: " + puntajeMaximo + " PUNTOS");
        System.out.println("================================================");

        scanner.close();
    }

    // Método auxiliar para generar cartas al azar
    public static List<Carta> simularCartas(int cantidad) {
        List<Carta> cartas = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < cantidad; i++) {
            int elemento = rand.nextInt(4);
            int valor = rand.nextInt(9) + 1;
            cartas.add(new Carta(elemento, valor));
        }
        return cartas;
    }
}