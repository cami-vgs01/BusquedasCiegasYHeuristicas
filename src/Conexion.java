public class Conexion {
    private final Nodo destino;
    private final int peso;

    public Conexion(Nodo destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Nodo getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
}

