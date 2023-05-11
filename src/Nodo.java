import java.util.ArrayList;

public class Nodo implements Comparable<Nodo>{
    private String nombre;
    private int pesoNodo;
    private ArrayList<Conexion> conexiones;

    public Nodo(String nombre, int pesoNodo) {
        this.nombre = nombre;
        this.pesoNodo = pesoNodo;
        this.conexiones = new ArrayList<>();
    }
    public Nodo(){
    }

    public String getNombre() {
        return nombre;
    }

    public int getPesoNodo() {
        return pesoNodo;
    }

    public ArrayList<Conexion> getConexiones() {
        return conexiones;
    }
    public void limpiarConexiones() {
        if(conexiones != null){
            conexiones.clear();
        }
    }

    public void agregarConexion(Conexion conexion) {
        conexiones.add(conexion);
    }

    @Override
    public int compareTo(Nodo o) {
        return Integer.compare(this.getPesoNodo(), o.getPesoNodo());
    }

    /*@Override
    public int compareTo(Nodo otro) {
        return this.getNombre().compareTo(otro.getNombre());
    }*/
}

