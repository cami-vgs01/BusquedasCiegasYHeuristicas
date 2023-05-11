import java.util.*;
public class Grafo {
    private final ArrayList<Nodo> nodos;
    public Grafo() {
        nodos = new ArrayList<>();
    }
    public void agregarNodo(Nodo nodo) {
        if(buscarNodo(nodo.getNombre()) == null){
            nodos.add(nodo);
        }
    }
    public Nodo buscarNodo(String nombre) {
        for (Nodo nodo : nodos) {
            if (nodo.getNombre().contains(nombre)) {
                return nodo;
            }
        }
        return null;
    }
    public Conexion buscarConexion(Nodo nodoOrigen, Nodo nodoDestino) {
        for (Conexion conexion : nodoOrigen.getConexiones()) {
            if (conexion.getDestino().getNombre().equals(nodoDestino.getNombre())) {
                return conexion;
            }
        }
        return null;
    }
    public void agregarConexion(String origend, String destinod, int peso) {
        Nodo origen = buscarNodo(origend);
        Nodo destino = buscarNodo(destinod);
        if (origen != null && destino != null) {
            Conexion verconexion = buscarConexion(origen, destino);
            if(verconexion == null){
                Conexion conexion = new Conexion(destino, peso);
                origen.agregarConexion(conexion);
            }
        }
    }
    public ArrayList<Nodo> getNodos() {
        return nodos;
    }
    public void limpiarConexiones() {
        if(!nodos.isEmpty()){
            nodos.clear();
        }
    }
    public ArrayList<Nodo> buscarPorAmplitud(String origenId, String destinoId) {
        ArrayList<Nodo> recorrido = new ArrayList<>();
        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        Nodo nodoOrigen = buscarNodo(origenId);
        Nodo nodoDest = buscarNodo(destinoId);
        int limite =nivelNodoDestino(nodoOrigen,nodoDest);
        cola.add(nodoOrigen);
        visitados.add(nodoOrigen);
        while (!cola.isEmpty()) {
            for(Nodo nodo: cola){
                System.out.print(nodo.getNombre()+" ");
            }
            System.out.println();
            Nodo nodoActual = cola.poll();
            recorrido.add(nodoActual);
            if(recorrido.contains(nodoDest)){
                break;
            }
            for (Conexion conexion : nodoActual.getConexiones()) {
                Nodo nodoDestino = conexion.getDestino();
                if (!visitados.contains(nodoDestino)) {
                    cola.add(nodoDestino);
                    visitados.add(nodoDestino);
                }
            }
        }
        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^(" + limite + "+1)) = "+ Math.pow(factorRamificacion(),limite+1));
        System.out.println("La complejidad espacial es O("+ factorRamificacion() + "^(" + limite + "+1)) = "+ Math.pow(factorRamificacion(),limite+1));
        return recorrido;
    }
    public int factorRamificacion(){
        int totalNodos = getNodos().size();
        int totalConexiones = 0;
        for(Nodo nodo: getNodos()){
            totalConexiones += nodo.getConexiones().size();
        }
        return (int) totalConexiones / totalNodos;
    }
    public int nivelNodoDestino(Nodo idOrigen, Nodo idDestino) {
        if (idOrigen.equals(idDestino)) {
            return 0;
        }
        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        int nivel = 0;
        cola.add(idOrigen);
        visitados.add(idOrigen);
        while (!cola.isEmpty()) {
            int tamanoActual = cola.size();
            for (int i = 0; i < tamanoActual; i++) {
                Nodo nodoActual = cola.poll();
                for (Conexion conexion : nodoActual.getConexiones()) {
                    Nodo vecino = conexion.getDestino();
                    if (!visitados.contains(vecino)) {
                        if (vecino.equals(idDestino)) {
                            return nivel + 1;
                        }
                        cola.add(vecino);
                        visitados.add(vecino);
                    }
                }
            }
            nivel++;
        }
        return -1; // Si no se encontró el nodo destino
    }
    public int profundidaMaxima(Nodo nodoOrigen) {
        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        int niveles = 0;
        if (nodoOrigen != null) {
            cola.add(nodoOrigen);
            visitados.add(nodoOrigen);
            while (!cola.isEmpty()) {
                int tamanoCola = cola.size();
                for (int i = 0; i < tamanoCola; i++) {
                    Nodo nodoActual = cola.poll();
                    for (Conexion conexion : nodoActual.getConexiones()) {
                        Nodo nodoDestino = conexion.getDestino();
                        if (!visitados.contains(nodoDestino)) {
                            cola.add(nodoDestino);
                            visitados.add(nodoDestino);
                        }
                    }
                }
                niveles++;
            }
        }
        return niveles;
    }
    public ArrayList<Nodo> busquedaProfundidad(String origenId, String destinoId) {
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        ArrayList<Nodo> visitados = new ArrayList<>();
        LinkedList<Nodo> visitadosSet = new LinkedList<>();
        Stack<Nodo> pila = new Stack<>();
        pila.push(origen);
        visitadosSet.add(origen);
        int limite = profundidaMaxima(origen) - 1;

        while (!pila.isEmpty()) {
            for (Nodo nodo : visitadosSet) {
                System.out.print(nodo.getNombre() + " ");
            }
            System.out.println();

            Nodo actual = pila.pop();
            if (actual.equals(destino)) {
                visitados.add(actual);
                break;
            }

            if (!visitados.contains(actual)) {
                visitados.add(actual);
                ArrayList<Nodo> aux = new ArrayList<>();

                for (Conexion conexion : actual.getConexiones()) {
                    Nodo sucesor = conexion.getDestino();
                    if (!visitados.contains(sucesor)) {
                        aux.add(sucesor);
                        if (!visitadosSet.contains(sucesor)) {
                            visitadosSet.add(sucesor);
                        }
                    }
                }

                Collections.sort(aux, Comparator.comparing(Nodo::getNombre));
                for (int i = aux.size() - 1; i >= 0; i--) {
                    Nodo nodo = aux.get(i);
                    pila.push(nodo);
                }
                visitadosSet.remove(actual);
            }
        }

        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^" + limite + ") = " + Math.pow(factorRamificacion(), limite));
        System.out.println("La complejidad espacial es O(" + factorRamificacion() + "*" + limite + ") = " + (factorRamificacion() * limite));
        return visitados;
    }

    public void busquedaBidireccional(String origen, String destino) {
        Nodo nodoOrigen = buscarNodo(origen);
        Nodo nodoDestino = buscarNodo(destino);
        if (nodoOrigen == null || nodoDestino == null) {
            System.out.println("No se encontró el nodo de origen o destino.");
        }
        // Lista de nodos explorados desde el origen y destino
        List<Nodo> exploradosOrigen = new ArrayList<>();
        List<Nodo> exploradosDestino = new ArrayList<>();
        // Colas para la búsqueda bidireccional
        Queue<Nodo> colaOrigen = new LinkedList<>();
        Queue<Nodo> colaDestino = new LinkedList<>();
        // Agregamos los nodos de origen y destino a sus respectivas colas
        colaOrigen.add(nodoOrigen);
        colaDestino.add(nodoDestino);
        // Marcamos los nodos de origen y destino como explorados en sus respectivas listas
        exploradosOrigen.add(nodoOrigen);
        exploradosDestino.add(nodoDestino);
        List<Nodo> caminoOrigen = new ArrayList<>();
        List<Nodo> caminoDestino = new ArrayList<>();
        // Bucle de búsqueda bidireccional
        float limite =nivelNodoDestino(nodoOrigen,nodoDestino);
        while (!colaOrigen.isEmpty() && !colaDestino.isEmpty()) {
            // Búsqueda desde el origen
            System.out.println("Origen");
            for (Nodo nodo : colaOrigen) {
                System.out.print(nodo.getNombre() + " ");
            }
            Nodo nodoActual = colaOrigen.poll();
            caminoOrigen.add(nodoActual);
            System.out.print("- Extraer "+nodoActual.getNombre());
            System.out.println();
            // Verificamos si el nodo actual ya fue explorado desde el otro extremo
            if (caminoDestino.contains(nodoActual)) {
                System.out.println("Nodo en comun : "+nodoActual.getNombre());
                break;
            }
            // Recorremos las conexiones del nodo actual
            LinkedList<Nodo> listOrigen = new LinkedList<>();
            for (Conexion conexion : nodoActual.getConexiones()) {
                Nodo nodoAdyacente = conexion.getDestino();
                listOrigen.add(nodoAdyacente);
            }
            Collections.sort(listOrigen, new Comparator<Nodo>() {
                @Override
                public int compare(Nodo o1, Nodo o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            for (Nodo nodoAdyacente : listOrigen) {
                if (!exploradosOrigen.contains(nodoAdyacente)) {
                    colaOrigen.add(nodoAdyacente);
                    exploradosOrigen.add(nodoAdyacente);
                }
            }
            System.out.println("Destino");
            for (Nodo nodo : colaDestino) {
                System.out.print(nodo.getNombre() + " ");
            }
            Nodo nodoActualDes = colaDestino.poll();
            caminoDestino.add(nodoActualDes);
            System.out.print("- Extraer "+nodoActualDes.getNombre());
            System.out.println();
            // Verificamos si el nodo actual ya fue explorado desde el otro extremo
            if (caminoOrigen.contains(nodoActualDes)) {
                System.out.println("Nodo en comun : "+nodoActual.getNombre());
                break;
            }
            LinkedList<Nodo> list = new LinkedList<>();
            for (Conexion conexion : nodoActualDes.getConexiones()) {
                Nodo nodoAdyacente = conexion.getDestino();
                list.add(nodoAdyacente);
            }
            Collections.sort(list, new Comparator<Nodo>() {
                @Override
                public int compare(Nodo o1, Nodo o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            for (Nodo nodoAdyacente : list) {
                if (!exploradosDestino.contains(nodoAdyacente)) {
                    colaDestino.add(nodoAdyacente);
                    exploradosDestino.add(nodoAdyacente);
                }
            }
        }
        System.out.print("Desde el origen: ");
        for (Nodo nodo : caminoOrigen) {
            System.out.print(nodo.getNombre() + " ");
        }
        System.out.println();
        System.out.print("Desde el destino: ");
        for (Nodo nodo : caminoDestino) {
            System.out.print(nodo.getNombre() + " ");
        }
        System.out.println();
        double resultado = Math.pow(factorRamificacion(),(limite/2));
        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^(" + limite + "/2)) = "+ resultado);
        System.out.println("La complejidad espacial es O("+ factorRamificacion() + "^(" + limite + "/2)) = "+ resultado);
    }
    public ArrayList<Nodo> busquedaProfundidadIterativa(String origenId, String destinoId) {
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> recorrido = new ArrayList<>();
        int limite = 0;
        while (true) {
            System.out.println("Nivel: "+limite);
            Stack<Nodo> pila = new Stack<>();
            Queue<Nodo> cola = new LinkedList<>();
            recorrido.add(origen);
            pila.push(origen);
            cola.offer(origen);
            Map<Nodo, Integer> niveles = new HashMap<>();
            niveles.put(origen, 0);
            boolean encontrado = false;
            while (!pila.isEmpty()) {
                for(int i= pila.size()-1; i>=0; i--){
                    System.out.print(pila.get(i).getNombre()+" ");
                }
                System.out.println();
                Nodo actual = pila.pop();
                visitados.add(actual);
                if (actual.equals(destino)) {
                    encontrado = true;
                    break;
                }
                int nivel = niveles.get(actual);
                if (nivel == limite) {
                    continue;
                }
                ArrayList<Conexion> conexionesOrdenadas = actual.getConexiones();
                Collections.sort(conexionesOrdenadas, new Comparator<Conexion>() {
                    @Override
                    public int compare(Conexion c1, Conexion c2) {
                        return c2.getDestino().getNombre().compareTo(c1.getDestino().getNombre());
                    }
                });
                for (Conexion conexion : conexionesOrdenadas) {
                    Nodo sucesor = conexion.getDestino();
                    if (!recorrido.contains(sucesor)) {
                        recorrido.add(sucesor);
                        pila.push(sucesor);
                        cola.offer(sucesor);
                        niveles.put(sucesor, nivel + 1);
                    }
                }
            }
            if (encontrado) {
                System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^" + limite + ") = "+ Math.pow(factorRamificacion(), limite));
                System.out.println("La complejidad espacial es O("+ factorRamificacion() +"*"+limite + ") = "+ (factorRamificacion()*limite));
                return visitados;
            } else {
                limite++;
                recorrido.clear();
                visitados.clear();
                while (!cola.isEmpty()) {
                    niveles.remove(cola.poll());
                }
            }
        }
    }
    public List<Nodo> busquedaCostoUniforme(String origenId, String destinoId, List<Integer>pesoEnlace) {
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Integer> costoMinimo = new HashMap<>();
        Map<Nodo, Integer> vistNodos = new HashMap<>();
        Map<Nodo, Nodo> padre = new HashMap<>();
        List<Nodo> camino = new ArrayList<>();
        List<Nodo> nodosVisitados = new ArrayList<>(); // lista para almacenar los nodos visitados
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        costoMinimo.put(origen, 0);
        vistNodos.put(origen, 0);
        int damePeso = 0;
        Nodo nodoActual = origen;
        while (nodoActual != null) {
            visitados.add(nodoActual);
            if (nodoActual.equals(destino)) {
                nodosVisitados.add(nodoActual);
                break;
            }
            nodosVisitados.add(nodoActual); // agregar el nodo visitado a la lista
            for (Conexion conexion : nodoActual.getConexiones()) {
                Nodo vecino = conexion.getDestino();
                int costoConexion = conexion.getPeso();
                if (!visitados.contains(vecino)) {
                    int costoAcumulado = costoMinimo.getOrDefault(nodoActual, Integer.MAX_VALUE);
                    costoAcumulado += costoConexion;
                    if (costoAcumulado < costoMinimo.getOrDefault(vecino, Integer.MAX_VALUE)) {
                        costoMinimo.put(vecino, costoAcumulado);
                        vistNodos.put(vecino, costoAcumulado);
                        padre.put(vecino, nodoActual);
                    }
                }
                vistNodos.remove(nodoActual);
            }
            int menorCosto = Integer.MAX_VALUE;
            Nodo proximoNodo = null;
            for (Map.Entry<Nodo, Integer> entry : costoMinimo.entrySet()) {
                Nodo nodo = entry.getKey();
                int costo = entry.getValue();
                if (!visitados.contains(nodo) && costo < menorCosto) {
                    menorCosto = costo;
                    proximoNodo = nodo;
                }
            }
            for(Map.Entry<Nodo, Integer> entry : vistNodos.entrySet()){
                Nodo nodo = entry.getKey();
                int costo = entry.getValue();
                System.out.print(nodo.getNombre() + "(" + costo+ ") ");
                if(nodo.equals(destino)){
                    damePeso = costo;
                }
            }
            System.out.println();
            nodoActual = proximoNodo;
        }
        if (nodoActual != null) {
            camino.add(destino);
            while (nodoActual != origen) {
                Nodo nodoPadre = padre.get(nodoActual);
                camino.add(nodoPadre);
                nodoActual = nodoPadre;
            }
            Collections.reverse(camino);
        }
        System.out.print("Nodos extraidos: ");
        for (Nodo nodo : nodosVisitados) {
            System.out.print(nodo.getNombre() + " ");
        }
        System.out.println();
        float minimo = Collections.min(pesoEnlace);
        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^(" + damePeso + "/"+minimo+")) = "+ Math.pow(factorRamificacion(), damePeso/minimo));
        System.out.println("La complejidad espacial es O(" + factorRamificacion() + "^(" + damePeso + "/"+minimo+")) = "+ Math.pow(factorRamificacion(), damePeso/minimo));
        return camino; // devolver la lista de nodos visitados
    }
    public void ascensoColina(String origen, String destino) {
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> recorrido = new ArrayList<>();
        Nodo nodoActual = buscarNodo(origen);
        Nodo nodoDestino = buscarNodo(destino);
        int mejorValor = Integer.MAX_VALUE;
        Nodo mejorNodo = null;
        visitados.add(nodoActual);
        recorrido.add(nodoActual);
        int maxHijos = 0;
        while(!recorrido.isEmpty()) {
            for(Nodo nodo: recorrido){
                System.out.print(nodo.getNombre()+ "("+nodo.getPesoNodo()+") ");
            }
            System.out.println();
            if(nodoActual.equals(nodoDestino)){
                System.out.println("Se encontro el destino");
                break;
            }
            for (Conexion conexion : nodoActual.getConexiones()) {
                recorrido.remove(nodoActual);
                if(!recorrido.contains(conexion.getDestino())){
                    recorrido.add(conexion.getDestino());
                }
                int valor = conexion.getDestino().getPesoNodo();
                if (valor < mejorValor) {
                    mejorValor = valor;
                    mejorNodo = conexion.getDestino();
                }
            }
            if(!visitados.contains(mejorNodo)){
                visitados.add(mejorNodo);
            }
            ArrayList<Conexion> con = new ArrayList<>(nodoActual.getConexiones());
            for (Conexion nodo : con) {
                int cantHijos = nodo.getDestino().getConexiones().size();
                if (cantHijos > maxHijos) {
                    maxHijos = cantHijos;
                }
            }
            nodoActual = mejorNodo;
            mejorValor = Integer.MAX_VALUE;
            mejorNodo = null;
        }
        for(Nodo nodo: visitados){
            System.out.print(nodo.getNombre()+ " ");
        }
        System.out.println();
        System.out.println("La complejidad computacional es O(" + getNodos().size() + "*" + maxHijos + ") = "+ (getNodos().size()*maxHijos));
        System.out.println("La complejidad espacial es O(1)");
    }
    public void primeroElMejor(String origen, String destino) {
        Nodo nodoOrigen = buscarNodo(origen);
        Nodo nodoDestino = buscarNodo(destino);
        PriorityQueue<Nodo> cola = new PriorityQueue<>(new Comparator<Nodo>() {
            public int compare(Nodo n1, Nodo n2) {
                return Integer.compare(n1.getPesoNodo(), n2.getPesoNodo());
            }
        });
        cola.add(nodoOrigen);
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> recorrido = new ArrayList<>();
        visitados.add(nodoOrigen);
        while (!cola.isEmpty()) {
            for (Nodo nodo : cola) {
                System.out.print(nodo.getNombre() + "-"+ nodo.getPesoNodo() + " ");
            }
            System.out.println();
            Nodo actual = cola.poll();
            recorrido.add(actual);
            if (actual == nodoDestino) {
                System.out.println("¡Destino encontrado!");
                for (Nodo nodo : recorrido){
                    System.out.print(nodo.getNombre() + "-"+ nodo.getPesoNodo() + " ");
                }
                System.out.println();
                int totalConexiones = 0;
                for(Nodo nodo: getNodos()){
                    totalConexiones += nodo.getConexiones().size();
                }
                System.out.println("La complejidad computacional es O(" + getNodos().size() + "^2"  + " log "+getNodos().size()+") = "+ Math.pow(getNodos().size(), 2)*Math.log(getNodos().size()));
                System.out.println("La complejidad espacial es O("+getNodos().size()+"+"+totalConexiones+") = "+(getNodos().size()+totalConexiones));
                return;
            }
            ArrayList<Conexion> conexiones = new ArrayList<>(actual.getConexiones());
            // Ordenar conexiones por valor de enlace
            Collections.sort(conexiones, new Comparator<Conexion>() {
                public int compare(Conexion c1, Conexion c2) {
                    return Integer.compare(c1.getPeso(), c2.getPeso());
                }
            });
            for (Conexion conexion : conexiones) {
                Nodo adyacente = conexion.getDestino();
                if (!visitados.contains(adyacente)) {
                    visitados.add(adyacente);
                    cola.add(adyacente);
                }
            }
        }
    }
    public List<Nodo> busquedaAStar(String origenId, String destinoId) {
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Integer> costoMinimo = new HashMap<>();
        Map<Nodo, Integer> mapAbierto = new HashMap<>();
        Map<Nodo, Nodo> padre = new HashMap<>();
        List<Nodo> camino = new ArrayList<>();
        List<Nodo> listaCerrada = new ArrayList<>(); // lista para almacenar los nodos visitados
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        costoMinimo.put(origen, 0);
        mapAbierto.put(origen, 0);
        Nodo nodoActual = origen;
        float limite =nivelNodoDestino(origen,destino);
        while (nodoActual != null) {
            visitados.add(nodoActual);
            if (nodoActual.equals(destino)) {
                listaCerrada.add(nodoActual);
                break;
            }
            listaCerrada.add(nodoActual); // agregar el nodo visitado a la lista
            for (Conexion conexion : nodoActual.getConexiones()) {
                Nodo vecino = conexion.getDestino();
                int costoConexion = conexion.getPeso();
                if (!visitados.contains(vecino)) {
                    int costoAcumulado = costoConexion + vecino.getPesoNodo();
                    if (costoAcumulado < costoMinimo.getOrDefault(vecino, Integer.MAX_VALUE)) {
                        costoMinimo.put(vecino, costoAcumulado);
                        mapAbierto.put(vecino, costoAcumulado);
                        padre.put(vecino, nodoActual);
                    }
                }
                mapAbierto.remove(nodoActual);
            }
            int menorCosto = Integer.MAX_VALUE;
            Nodo proximoNodo = null;
            for (Map.Entry<Nodo, Integer> entry : costoMinimo.entrySet()) {
                Nodo nodo = entry.getKey();
                int costo = entry.getValue();
                if (!visitados.contains(nodo) && costo < menorCosto) {
                    menorCosto = costo;
                    proximoNodo = nodo;
                }
            }
            for(Map.Entry<Nodo, Integer> entry : mapAbierto.entrySet()){
                Nodo nodo = entry.getKey();
                int costo = entry.getValue();
                System.out.print(nodo.getNombre() + "(" + costo+ ") ");
            }
            System.out.println();
            nodoActual = proximoNodo;
        }
        if (nodoActual != null) {
            camino.add(destino);
            while (nodoActual != origen) {
                Nodo nodoPadre = padre.get(nodoActual);
                camino.add(nodoPadre);
                nodoActual = nodoPadre;
            }
            Collections.reverse(camino);
        }
        System.out.print("Nodos extraidos: ");
        for (Nodo nodo : listaCerrada) {
            System.out.print(nodo.getNombre() + " ");
        }
        System.out.println();
        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^" + limite + ") = "+ Math.pow(factorRamificacion(), limite));
        System.out.println("La complejidad espacial es O(" + factorRamificacion() + "^" + limite + ") = "+ Math.pow(factorRamificacion(), limite));
        return camino; // devolver la lista de nodos visitados
    }
    public void BusquedaAvara(String origenId, String destinoId){
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        ArrayList<Nodo> visitados = new ArrayList<>();
        ArrayList<Nodo> camino = new ArrayList<>();
        visitados.add(origen);
        camino.add(origen);
        int limite =profundidaMaxima(origen)-1;
        while (!visitados.isEmpty()){
            for (Nodo nodo : visitados){
                System.out.print(nodo.getNombre() + "("+ nodo.getPesoNodo() + ")");
            }
            System.out.println();
            if (origen==destino){
                break;
            }
            int menorPeso = Integer.MAX_VALUE;
            Nodo mejorNodo = null;
            assert origen != null;
            for(Conexion conexion: origen.getConexiones()){
                visitados.remove(origen);
                Nodo adyacente = conexion.getDestino();
                int peso = adyacente.getPesoNodo();
                if (!visitados.contains(adyacente)) {
                    visitados.add(adyacente);
                }
                if (peso < menorPeso){
                    menorPeso = peso;
                    mejorNodo = adyacente;
                }
            }
            if(!camino.contains(mejorNodo)){
                camino.add(mejorNodo);
            }
            origen = mejorNodo;
        }
        System.out.print("Camino: ");
        for (Nodo nodo : camino){
            System.out.print(nodo.getNombre() + "("+ nodo.getPesoNodo() + ")");
        }
        System.out.println();
        System.out.println("La complejidad computacional es O(" + factorRamificacion() + "^" + limite + ") = "+ Math.pow(factorRamificacion(), limite));
        System.out.println("La complejidad espacial es O(" + factorRamificacion() + "^" + limite + ") = "+ Math.pow(factorRamificacion(), limite));
    }
}