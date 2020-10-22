package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color; 
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (this.distancia > vertice.distancia) {
                return 1;
            } else if (this.distancia < vertice.distancia) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
        this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento es <code>null</code> o ya
     *         había sido agregado a la gráfica.
     */
    @Override public void agrega(T elemento) {
        //Si el elemento es null o ya esta contenido en la lista de vertices
        if (elemento == null || contiene(elemento))
            throw new IllegalArgumentException("¡Elemento nulo o ya está contenido en la gráfica!");
        
        //Agrega el elemento
        vertices.agrega(new Vertice(elemento)); 
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Si alguno de los dos no está en la gráfica
        if (!contiene(a) || !contiene(b)) 
            throw new NoSuchElementException("¡Algún elemento no está contenido en la gráfica!");

        //Si ya son Vecinos o son iguales
        if (sonVecinos(a, b) || a == b) 
            throw new IllegalArgumentException("¡No puedes agregar elementos iguales o que ya sean vecinos!");
        
        //Procedemos a conectarlos agregándolos mutuamente a sus listas de vecinos
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        primero.vecinos.agrega(new Vecino(segundo, 1));
        segundo.vecinos.agrega(new Vecino(primero, 1));
        this.aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        //Si alguno no está contenid en la gráfica 
        if (!contiene(a) || !contiene(b) )
            throw new NoSuchElementException("Elementos no contenidos en la gráfica");
        //Si ya etsán conectados o son el mismo elemento 
        if (sonVecinos(a, b) || a == b || peso < 0)
            throw new IllegalArgumentException("¡No puedes agregar elementos iguales o que ya sean vecinos!");
        
        //Procedemos a conectarlos agregándolos mutuamente a sus listas de vecinos
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        primero.vecinos.agrega(new Vecino(segundo, peso));
        segundo.vecinos.agrega(new Vecino(primero, peso));
        this.aristas++;
        
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        //Si alguno de los elementos no estan en la gráfica.
        if (!contiene(a) || !contiene(b)) 
            throw new NoSuchElementException("¡Algún elemento no está contenido en la gráfica!");

        //Si los elementos no están conectados
        if (!sonVecinos(a, b)) 
            throw new IllegalArgumentException("¡Los vertice ya están desconectados!");
        
        //Procedemos a desconectarlos eliminándolos mutuamente de su lista de vecinos
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        primero.vecinos.elimina(vecino(primero, segundo));
        segundo.vecinos.elimina(vecino(primero, segundo));
        this.aristas--;
    }

    private Vecino vecino(Vertice v, Vertice u) {
        for (Vecino vecino : v.vecinos) {
            if (vecino.vecino.equals(u)) {
                return vecino;
            }
        }
        return null;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice vertice : vertices) {
            if (vertice.elemento.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Si no está contenido en la grafica
        if (!contiene(elemento)) 
            throw new NoSuchElementException("Elemento no contenido en la gráfica");
        
        //Procedemos eliminar el vertice de la lista de vertices 
        Vertice eliminar = (Vertice) vertice(elemento);
        vertices.elimina(eliminar);

        //Procedemos a eliminar al vertice de la lista de vecinos de sus vecinos
        for (Vecino vecino : eliminar.vecinos) {
            vecino.vecino.vecinos.elimina(vecino(vecino.vecino, eliminar));
            this.aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        //Si alguno de los vertices no está en la gráfica
        if (!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Alguno de los elementos no esta contenido en la gráfica");
        
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        for (Vecino vecino : primero.vecinos)
            if (vecino.vecino.equals(segundo)) {
                return true;
            }
        return false;   
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        //Si algún elemento no está conectado
        if (!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Algunoo de los elementos no está contenido");
        //Si no están conectados
        if(!sonVecinos(a,b)) 
            throw new IllegalArgumentException("No están conectados los vértices");

        //Obtenemos los vertices de cada elemento
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        //Recorremos para obtener el peso
        double peso = 1;
        for (Vecino vecino : primero.vecinos) {
            if (vecino.vecino.equals(segundo)) {
                peso = vecino.peso;
            }
        }
        return peso;

    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        if (!contiene(a) || !contiene(b)) 
            throw new NoSuchElementException("No están contenidos en la gráfica");
        
        if (!sonVecinos(a,b) || getPeso(a,b) <= 0)
            throw new IllegalArgumentException("No son vecinos o su peso es menor o igual a 0");
        
        Vertice primero = (Vertice) vertice(a);
        Vertice segundo = (Vertice) vertice(b);
        for (Vecino vecino : primero.vecinos) {
            if (vecino.vecino.equals(segundo)) {
                vecino.peso = peso;
            }
        }
        for (Vecino vecino : segundo.vecinos) {
            if (vecino.vecino.equals(primero)) {
                vecino.peso = peso;
            }
        }
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        //Si el elemento no está en la gráfica
        if (!contiene(elemento))
            throw new NoSuchElementException("Elemento no contenido en la gráfica");

        for (Vertice vertice : vertices) {
            // == porque los elementos en G son únicos
            if (vertice.elemento.equals(elemento)) {
                return vertice;
            }
        }
        return null;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class) {
            throw new IllegalArgumentException("Vertice inválido");
        }

        if (Vertice.class == vertice.getClass()) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        } else {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
    }

    //Método para pintar a todos los vertices de la 
    //grafica de Rojo para recorrerlos.
    private void pintarTodosDeRojo() {
        for (Vertice vertice: vertices)
            vertice.color = Color.ROJO;
    }

    //Metodo para ver si todos los vertices son Negros
    //Nos permite comprobar que si es conexa al terminar la cola
    //True - Todos negros == conexa. False - Uno != Negro == Inconexa
    private boolean sonTodosNegros() {
        for (Vertice vertice : vertices) {
            if (vertice.color != Color.NEGRO) {
                return false;
            }
        }
        return true;
    }


    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        //Recorremos la gráfica con bfs
        Vertice w = vertices.getPrimero();
        pintarTodosDeRojo();
        Cola<Vertice> cola = new Cola<>();
        w.color = Color.NEGRO;
        cola.mete(w);
        Vertice u = null;
        while (!cola.esVacia()) {
            //Lo sacamos al frente
            u = cola.saca();
            //Para cada vertice vecino de u
            for (Vecino vecino : u.vecinos){
                if (vecino.getColor() == Color.ROJO) {
                    vecino.vecino.color = Color.NEGRO;
                    cola.mete(vecino.vecino);
                }
            }
        }
        //Comprobamos si todos son negros,si lo son -> es conexa
        return sonTodosNegros();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice vertice : vertices) {
            accion.actua(vertice);
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        if (!contiene(elemento))
            throw new NoSuchElementException("Elemento no contenido en la gráfica");
        
        //Vertice en que queremos empezar a recorrer
        Vertice w = (Vertice) vertice(elemento);
        pintarTodosDeRojo();
        Cola<Vertice> cola = new Cola<>();
        w.color = Color.NEGRO;
        cola.mete(w);
        Vertice u = null;
        while (!cola.esVacia()) {
            u = cola.saca();
            accion.actua(u);
            //Metemos a los vecinos de u
            for (Vecino vecino : u.vecinos) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.vecino.color = Color.NEGRO;
                    cola.mete(vecino.vecino);
                }
            }
        }
        //Al final todos los vertices tienen que ser e Color Ninguno
        for (Vertice vertice : vertices)
            vertice.color = Color.NINGUNO;
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        if (!contiene(elemento))
            throw new NoSuchElementException("Elemento no contenido en la gráfica");
        
        Vertice w = (Vertice) vertice(elemento);
        pintarTodosDeRojo();
        Pila<Vertice> pila = new Pila<>();
        w.color = Color.NEGRO;
        pila.mete(w);
        Vertice u = null;
        while (!pila.esVacia()) {
            u = pila.saca();
            accion.actua(u);
            //Metemos a los vecinos de u
            for (Vecino vecino : u.vecinos) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.vecino.color = Color.NEGRO;
                    pila.mete(vecino.vecino);
                }
            }
        }
        //Al final todos los vertices tienen que volver a ser de Color Ninguno
        for (Vertice vertice : vertices)
            vertice.color = Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        //Si la gráfica es vacía
        if (vertices.esVacia())
            return "{}, {}";
        
        //Conjunto de los vértices de la gráfica
        String cadena = "{";
        for (Vertice v : vertices) 
            cadena += v.elemento + ", ";
        
        cadena += "}, {";

        //Conjunto de Aristas
        //Cada vertice será coloreado con negro
        for (Vertice vertice : vertices) {
            vertice.color = Color.NEGRO;
            for (Vecino vecino : vertice.vecinos) {
                if (vecino.getColor() != Color.NEGRO) {
                    cadena += "(" + vertice.elemento + ", " + vecino.get() + "), ";
                }
            }
        }

        return cadena += "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        //Comparamos todos sus caracterizticas
        if (this.vertices.getLongitud() != grafica.vertices.getLongitud()) 
            return false;
        
        if (this.aristas != grafica.aristas)
            return false;

        for (Vertice vertice : this.vertices) {
            if (!grafica.contiene(vertice.elemento)) {
                return false;
            } else {
                Vertice verticeEnH = (Vertice) grafica.vertice(vertice.elemento);
                for (Vecino vecino : vertice.vecinos) {
                    if ( !grafica.sonVecinos(verticeEnH.elemento, vecino.get()) ) 
                        return false;
                }
            }
        }
        //Si no ocurre nada de eso 
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);

        if ( s == t ) {
            Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
            trayectoria.agrega(s);
            return trayectoria;
        }

        for (Vertice vertice : vertices) {
            vertice.distancia = Double.MAX_VALUE;
        }

        s.distancia = 0;
        Cola<Vertice> cola = new Cola<>();
        cola.mete(s);
        Vertice u = null;
        while (!cola.esVacia()) {
            u = cola.saca();
            for (Vecino v : u.vecinos) {
                if (v.vecino.distancia == Double.MAX_VALUE) {
                    v.vecino.distancia = u.distancia + 1;
                    cola.mete(v.vecino);
                }
            }
        }
        
        if (t.distancia == Double.MAX_VALUE) {
            return new Lista<VerticeGrafica<T>>();
        }

        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        trayectoria.agrega(t);
        while ( t != s) {
            for (Vecino vecino : t.vecinos) {
                if (vecino.vecino.distancia == t.distancia - 1) {
                    trayectoria.agrega(vecino.vecino);
                    t = vecino.vecino;
                }
            }
        }


        return trayectoria.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        if (!contiene(origen) || !contiene(destino)) {
            throw new NoSuchElementException("Alguno de los vértices no está en la gráfica");
        } 

        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);
        

        for (Vertice vertice : vertices) {
            vertice.distancia = Double.MAX_VALUE;
        }

        s.distancia = 0;
        MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<>(vertices);
        Vertice u = null;
        while (!monticulo.esVacia()) {
            u = monticulo.elimina();
            for (Vecino v : u.vecinos) {
                if (v.vecino.distancia > u.distancia + v.peso) {
                    v.vecino.distancia = u.distancia + v.peso;
                    monticulo.reordena(v.vecino);
                }
            }
        }

        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        if (t.distancia == Double.MAX_VALUE) {
            return trayectoria;
        }
        
        trayectoria.agrega(t);
        while ( t != s) {
            for (Vecino vecino : t.vecinos) {
                if (vecino.vecino.distancia  == t.distancia - vecino.peso ) {
                    trayectoria.agrega(vecino.vecino);
                    t = vecino.vecino;
                }
            }
        }
        
        return trayectoria.reversa();
    }
}
