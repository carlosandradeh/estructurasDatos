package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return super.altura();
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
            return elemento + " " + this.altura() + "/" + (alturaAux(this.izquierdo) - alturaAux(this.derecho) );
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            // Aquí va su código.
            return this.altura == vertice.altura && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeAVL padre = (VerticeAVL) ultimoAgregado.padre;
        rebalanceo(padre);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminar = (Vertice) busca(elemento); //Vamos a ver si esta acontenido
        if (eliminar != null) { //Si lo encuentra

            //Caso 1: Tiene dos hijos distintos de null
            if (eliminar.izquierdo != null && eliminar.derecho != null) {
                Vertice maximo = intercambiaEliminable(eliminar);
                eliminaVertice(maximo);
                VerticeAVL padre = (VerticeAVL) maximo.padre;
                rebalanceo(padre);
                return;
            }
            //Todos los demas casos xd
            eliminaVertice(eliminar);

            //Rebalanceo sobre el padre
            VerticeAVL padre = (VerticeAVL) eliminar.padre;
            rebalanceo(padre);
        }    
    
    }

    //Metodo para obtener alturas
    private int alturaAux(Vertice v) {
        if (v == null) {
            return -1;
        } 
        return 1 + Math.max( alturaAux(v.izquierdo), alturaAux(v.derecho) );
    }
    
    //Metodo para calcular el balance: b(v)
    private int calcularBalance(VerticeAVL vertice) {
        int balance = alturaAux(vertice.izquierdo) - alturaAux(vertice.derecho);
        return balance;
    }



    private void rebalanceo(VerticeAVL vertice) {
        if(vertice == null)
            return;

        //Actualizamos su altura
        vertice.altura = vertice.altura();
        
        //Calculamos el balance
        int balance = calcularBalance(vertice);
        
        //Sean p y q hijos izquierdo y derecho de vertice
        VerticeAVL p = (VerticeAVL) vertice.izquierdo;
        VerticeAVL q = (VerticeAVL) vertice.derecho;
        
        //Caso en el que el balance sea igual a -2
        if (balance == -2) {
            if (calcularBalance(q) == 1) {
                super.giraDerecha(q);
                q = (VerticeAVL) vertice.derecho;
            }
            super.giraIzquierda(vertice);
        }

        //Caso en el que el balance es 2
        if (balance == 2) {
            if (calcularBalance(p) == -1 ) {
                super.giraIzquierda(p);
                p = (VerticeAVL) vertice.izquierdo;
            }
            super.giraDerecha(vertice);
        }

        rebalanceo((VerticeAVL) vertice.padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }



}
