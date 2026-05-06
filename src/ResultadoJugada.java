public class ResultadoJugada {
    public String tipoJugada;
    public int puntos;
    public String detalleCartas;

    public ResultadoJugada(String tipoJugada, int puntos, String detalleCartas) {
        this.tipoJugada = tipoJugada;
        this.puntos = puntos;
        this.detalleCartas = detalleCartas;
    }

    @Override
    public String toString() {
        if (puntos == 0) {
            return "No se pudo armar ninguna jugada con estas cartas.";
        }
        return "================================================\n" +
                "🌟 JUGADA GANADORA: " + tipoJugada + "\n" +
                "🏆 PUNTOS OBTENIDOS: " + puntos + "\n" +
                "🃏 CARTAS USADAS: " + detalleCartas + "\n" +
                "================================================";
    }
}