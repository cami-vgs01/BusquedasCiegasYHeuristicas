import javax.swing.*;
import java.io.File;
import java.util.*;

public class Principal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grafo grafo = new Grafo();
        Nodo nod = new Nodo();
        JFileChooser fileChooser = new JFileChooser();
        List<List<String>> graph = new ArrayList<>();
        List<Integer> pesoEnlace = new ArrayList<>();
        Map<String, Double> tiempo = new HashMap<>();
        int opcion = 0;
        System.out.println("****************************+GRAFO*****************************");
        while (opcion!=13) {
            try {
                System.out.println("1. Escoger archivo .csv");
                System.out.println("2. Mostrar hijos");
                System.out.println("3. Búsqueda por amplitud");
                System.out.println("4. Búsqueda por profundidad");
                System.out.println("5. Búsqueda Bidireccional");
                System.out.println("6. Búsqueda Profundidad Iterativa");
                System.out.println("7. Búsqueda de Costo Uniforme");
                System.out.println("8. Búsqueda del gradiente (Ascenso a la colina)");
                System.out.println("9. Búsqueda Primero el Mejor");
                System.out.println("10. Búsqueda de A*");
                System.out.println("11. Búsqueda Avara");
                System.out.println("12. Tiempo Comparativo");
                System.out.println("13. Salir");
                System.out.print("Ingrese una opción: ");
                opcion = sc.nextInt();
                switch (opcion) {
                    case 1 -> {
                        try{
                            graph.clear();
                            pesoEnlace.clear();
                            nod.limpiarConexiones();
                            grafo.limpiarConexiones();
                            int result = fileChooser.showOpenDialog(null);
                            if (result == JFileChooser.APPROVE_OPTION) {
                                File selectedFile = fileChooser.getSelectedFile();
                                Scanner scanner = new Scanner(selectedFile);
                                List<String> lines = new ArrayList<>();
                                while (scanner.hasNext()) {
                                    lines.add(scanner.nextLine());
                                }
                                scanner.close();
                                for (String line : lines) {
                                    String[] values = line.split(",");
                                    List<String> row = Arrays.asList(values);
                                    graph.add(row);
                                }

                                for(List<String> row : graph){
                                    Nodo nodo = new Nodo(row.get(0), Integer.parseInt(row.get(2)));
                                    Nodo nodo1 = new Nodo(row.get(1), Integer.parseInt(row.get(3)));
                                    grafo.agregarNodo(nodo);
                                    grafo.agregarNodo(nodo1);
                                    grafo.agregarConexion(row.get(0), row.get(1), Integer.parseInt(row.get(4)));
                                    pesoEnlace.add(Integer.parseInt(row.get(4)));
                                }
                            } else {
                                System.out.println("No se seleccionó ningún archivo.");
                            }
                        }catch (Exception e){
                            System.out.println("No se pudo abrir el archivo.");
                        }
                    }
                    case 2 -> {
                        ArrayList<Nodo> nodos;
                        nodos = grafo.getNodos();
                        for (Nodo nodo : nodos) {
                            System.out.print("Nodo " + nodo.getNombre() + " : ");
                            for (Conexion conexion : nodo.getConexiones()) {
                                Nodo nodoDestino = conexion.getDestino();
                                int pesoConexion = conexion.getPeso();
                                System.out.print(" Nodo " + nodoDestino.getNombre() + " -> peso " + pesoConexion + ", ");
                            }
                            System.out.println();
                        }
                    }
                    case 3 -> {
                        try {
                            System.out.print("Ingrese nombre de origen: ");
                            String origenId = sc.next();
                            System.out.print("Ingrese nombre de destino: ");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            ArrayList<Nodo> recorrido = grafo.buscarPorAmplitud(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            if (!recorrido.isEmpty()) {
                                System.out.println("Busqueda por amplitud desde el nodo " + origenId + ":");
                                for (Nodo nodo : recorrido) {
                                    System.out.print(nodo.getNombre() + " ");
                                }
                                System.out.println();
                                System.out.println("Tiempo de ejecución: " + duration + " segundos");
                                tiempo.put("Amplitud: ",duration);
                            } else {
                                System.out.println("No se encontró el nodo " + origenId);
                            }
                        }catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 4 -> {
                        try {
                            System.out.print("Ingrese nombre de origen: ");
                            String origenId = sc.next();
                            System.out.print("Ingrese nombre de destino: ");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            ArrayList<Nodo> recorrido = grafo.busquedaProfundidad(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            if (!recorrido.isEmpty()) {
                                System.out.println("Busqueda por profundidad desde el nodo " + origenId + ":");
                                for (Nodo nodo : recorrido) {
                                    System.out.print(nodo.getNombre() + " ");
                                }
                                System.out.println();
                                System.out.println("Tiempo de ejecución: " + duration + " segundos");
                                tiempo.put("Profundidad: ",duration);
                            } else {
                                System.out.println("No se encontró el nodo " + origenId);
                            }
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 5 ->{
                        try {
                            System.out.println("Ingrese nombre de origen:");
                            String origenId = sc.next();
                            System.out.println("Ingrese nombre de destino:");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            grafo.busquedaBidireccional(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            System.out.println("Tiempo de ejecución: " + duration + " segundos");
                            tiempo.put("Bidireccional: ",duration);
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 6 -> {
                        try {
                            System.out.print("Ingrese nombre de origen: ");
                            String idOrigen = sc.next();
                            System.out.print("Ingrese nombre de destino: ");
                            String idDestino = sc.next();
                            double startTime = System.nanoTime();
                            ArrayList<Nodo> camino =grafo.busquedaProfundidadIterativa(idOrigen, idDestino);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;

                            if (camino.isEmpty()) {
                                System.out.println("No se encontró un camino entre los nodos.");
                            } else {
                                System.out.print("El camino es: ");
                                for (Nodo nodo : camino) {
                                    System.out.print(nodo.getNombre() + " ");
                                }
                                System.out.println();
                                System.out.println("Tiempo de ejecución: " + duration + " segundos");
                                tiempo.put("Profundidad Iterativa: ",duration);
                            }
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 7 ->{
                        try {
                            System.out.println("Ingrese nombre de origen:");
                            String origenId = sc.next();
                            System.out.println("Ingrese nombre de destino:");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            List<Nodo> caminoBidi = grafo.busquedaCostoUniforme(origenId, destinoId, pesoEnlace);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            if (caminoBidi.isEmpty()) {
                                System.out.println("No se encontró camino.");
                            } else {
                                System.out.print("Camino: ");
                                for (Nodo nodo : caminoBidi) {
                                    System.out.print(nodo.getNombre() + " ");
                                }
                                System.out.println();
                                System.out.println("Tiempo de ejecución: " + duration + " segundos");
                                tiempo.put("Costo Uniforme: ",duration);
                            }
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 8 -> {
                        try {
                            System.out.println("Ingrese nombre de origen:");
                            String origenId = sc.next();
                            System.out.println("Ingrese nombre de destino:");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            grafo.ascensoColina(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            System.out.println();
                            System.out.println("Tiempo de ejecución: " + duration + " segundos");
                            tiempo.put("Ascenso a la colina: ",duration);
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 9 ->{
                        try {
                            System.out.println("Ingrese nombre de origen:");
                            String origenId = sc.next();
                            System.out.println("Ingrese nombre de destino:");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            grafo.primeroElMejor(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            System.out.println("Tiempo de ejecución: " + duration + " segundos");
                            tiempo.put("Primero el Mejor: ",duration);
                        } catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 10 ->{
                        try {
                            System.out.print("Ingrese nombre de origen: ");
                            String origenId = sc.next();
                            System.out.print("Ingrese nombre de destino: ");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            List<Nodo> recorrido = grafo.busquedaAStar(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            if (!recorrido.isEmpty()) {
                                System.out.println("Busqueda A* desde el nodo " + origenId + ":");
                                for (Nodo nodo : recorrido) {
                                    System.out.print(nodo.getNombre() + "-"+nodo.getPesoNodo()+" ");
                                }
                                System.out.println();
                                System.out.println("Tiempo de ejecución: " + duration + " segundos");
                                tiempo.put("A*: ",duration);
                            } else {
                                System.out.println("No se encontró el nodo " + origenId);
                            }
                        }catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 11 ->{
                        try {
                            System.out.print("Ingrese nombre de origen: ");
                            String origenId = sc.next();
                            System.out.print("Ingrese nombre de destino: ");
                            String destinoId = sc.next();
                            double startTime = System.nanoTime();
                            grafo.BusquedaAvara(origenId, destinoId);
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime)/1000000000.0;
                            System.out.println("Tiempo de ejecución: " + duration + " segundos");
                            tiempo.put("Avara: ",duration);
                        }catch (Exception e) {
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 12 ->{
                        try{
                            //Mostrar la lista tiempo
                            System.out.println("Lista de tiempos: ");
                            for (Map.Entry<String, Double> entry : tiempo.entrySet()) {
                                System.out.println("| "+entry.getKey() +" | "+ entry.getValue()+" |");
                            }
                        }catch (Exception e){
                            System.out.println("Mal ingreso de datos.");
                        }
                    }
                    case 13 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Opcion invalida");
                sc.next();
            }
        }
    }
}

