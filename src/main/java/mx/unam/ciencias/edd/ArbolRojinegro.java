package mx.unam.ciencias.edd;


/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
            if (this.color == Color.ROJO) {
                return  "R{" + this.elemento + "}"; 
            }
            return "N{" + this.elemento + "}";

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            if (this.color != vertice.color) {
                return false;
            }
            return super.equals(vertice);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        VerticeRojinegro nuevo = new VerticeRojinegro(elemento);
        return nuevo;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro v = (VerticeRojinegro) vertice;
        return v.color;
    }

    //Metodo para conseguir un tio
    private VerticeRojinegro tio(VerticeRojinegro padre, VerticeRojinegro abuelo) {
        if (abuelo == null) {
            return null;
        }
        if (abuelo.izquierdo == padre) {
            return (VerticeRojinegro) abuelo.derecho;
        }
        return (VerticeRojinegro) abuelo.izquierdo;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro ultimo = (VerticeRojinegro) this.ultimoAgregado;
        ultimo.color = Color.ROJO;
        rebalancearAlAgregar(ultimo);
    }

    private void rebalancearAlAgregar(VerticeRojinegro vertice) {
        //Si es nulo no hace nada
        if (vertice == null) {
            return;
        }
        //ENTRAMOS A LOS CASOS. Sean P,T,A de V. 
        VerticeRojinegro padre = (VerticeRojinegro) vertice.padre; //Vertice p

        //Caso 1: El padre de vertice es nulo
        if (padre == null) {
            vertice.color = Color.NEGRO;
            return;
        }

        //Caso 2: El padre del vertice es negro (Ya está balanceado) 
        if (padre.color == Color.NEGRO) {
            return;
          
        }
        //Caso 3: El tio y el padre son rojos  
        VerticeRojinegro abuelo = (VerticeRojinegro) padre.padre; //Vertice a 
        VerticeRojinegro tioo = tio(padre, abuelo);
        if ( tioo != null && (padre.color == Color.ROJO) && (tioo.color == Color.ROJO) ) {
            padre.color = Color.NEGRO;
            tioo.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalancearAlAgregar(abuelo);
            return;
           
        }
        //Caso 4: El vertice y su padre estan cruzados 
        if ( (abuelo.izquierdo == padre && padre.derecho == vertice) || (abuelo.derecho == padre && padre.izquierdo == vertice)) {
            if ( abuelo.izquierdo == padre ) {
                super.giraIzquierda(padre);
            } else {
                super.giraDerecha(padre);
            }
            //Actualizamos referencias
            VerticeRojinegro intercambio = padre;
            padre = vertice;
            vertice = intercambio;

        }
        //CAso 5
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if (padre.izquierdo == vertice) {
            super.giraDerecha(abuelo);
        } else {
            super.giraIzquierda(abuelo);
        }
    
    }

    //Metodo para obtener el hermano de un VerticeRojiNegro
    private VerticeRojinegro hermano( VerticeRojinegro vertice) {
        if (vertice.padre == null) {
            return null;
        } 
        if (vertice.padre.izquierdo == vertice) {
            return (VerticeRojinegro) vertice.padre.derecho;
        }
        return (VerticeRojinegro) vertice.padre.izquierdo;
    }

    //Metodo que nos dice si un VerticeRojiNegro es Negro
    private boolean esNegro(VerticeRojinegro v) {
        return  v == null || v.color == Color.NEGRO;
    }

    //Metodo que nos dice si un VerticeRojiNegro es Rojo
    private boolean esRojo(VerticeRojinegro v) {
        if (v == null) {
            return false;
        }
        return v.color == Color.ROJO;
    }

    //Elimina fantasma usado en el metodo elimina 
    private void eliminaFantasma(VerticeRojinegro fantasma) {
        if (fantasma == null) 
            return;

        if (fantasma.padre == null)
            raiz = ultimoAgregado = fantasma = null;
        else 
            if (fantasma.padre.izquierdo == fantasma) {
                fantasma.padre.izquierdo = null;
            } else {
                fantasma.padre.derecho = null;
            }

    }
    //Metodo para obtener hijo distinto de null de un vertice que solo tiene un unico hijo
    private VerticeRojinegro hijo (VerticeRojinegro v) {
        if (v.izquierdo != null) {
            return (VerticeRojinegro) v.izquierdo;
        }
        return (VerticeRojinegro) v.derecho;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Buscamos el vertice
        VerticeRojinegro v = (VerticeRojinegro) busca(elemento);
        //Si no esta contenida
        if (v == null)
            return;

        //Si tiene dos hijos distintos de null
        if (v.izquierdo != null && v.derecho != null) {
            v = (VerticeRojinegro) intercambiaEliminable(v);
        }

        //Si tiene dos hijos null
        VerticeRojinegro fantasma = null;
        if (v.izquierdo == null && v.derecho == null) {
            fantasma = (VerticeRojinegro) nuevoVertice(null);
            v.izquierdo = fantasma;
            fantasma.padre = v;
            fantasma.color = Color.NEGRO;
        }

        //Salvamos el hijo para checar sus colores
        VerticeRojinegro hijo = hijo(v);
        //Desconectamos el vertice v
        eliminaVertice(v);

        //Tenemos 3 posibilidades
        //hijo es rojo -> v es negro
        if (esRojo(hijo)) {
            hijo.color = Color.NEGRO;
            return;
        } 
        //Ambos son negros entonces balanceamos obre el hijo
        if ( esNegro(v) && esNegro(hijo) ) {
            rebalancearAlEliminar(hijo);
        }
        //Eliminamos el fantasma 
        eliminaFantasma(fantasma);
        
    }
    
    private void rebalancearAlEliminar(VerticeRojinegro vertice) {
        

        //Caso 1: El vertice tiene padre nulo
        if (vertice.padre == null) {
            return;
        }
                
        VerticeRojinegro padre = (VerticeRojinegro) vertice.padre;
        VerticeRojinegro hermano = hermano(vertice);
        //Caso 2: Hermano es rojo
        if (esRojo(hermano) == true) {
            padre.color = Color.ROJO;
            hermano.color = Color.NEGRO;
            if (padre.izquierdo == vertice) {
                super.giraIzquierda(padre);
            } else {
                super.giraDerecha(padre);
            }
            hermano = hermano(vertice);
        }
        //Caso 3: P,H,HI,HD son negros
        VerticeRojinegro hi = (VerticeRojinegro) hermano.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) hermano.derecho;
        if (esNegro(padre) && esNegro(hermano) && esNegro(hi) && esNegro(hd)) {
            hermano.color = Color.ROJO;
            rebalancearAlEliminar(padre);
            return;
        }
        //Caso 4: H,HI,HD son negros y P es rojo
        if (esRojo(padre) && esNegro(hermano) && esNegro(hi) && esNegro(hd) ) {
            hermano.color = Color.ROJO;
            padre.color = Color.NEGRO;
            return;
        }
        //CAso 5: Vertice es izquierdo, hi es rojo y hd es negro O vertice es derecho, hi es negro y hd es rojo
        if ( (padre.izquierdo == vertice && esRojo(hi) && esNegro(hd)) || (padre.derecho == vertice && esNegro(hi) && esRojo(hd)) ) {
            hermano.color = Color.ROJO;
            if (esRojo(hi)) {
                hi.color = Color.NEGRO;
            } else {
                hd.color = Color.NEGRO;
            }

            if (padre.izquierdo == vertice) {
                super.giraDerecha(hermano);
            } else {
                super.giraIzquierda(hermano);
            }
            hermano = hermano(vertice);
            hi = (VerticeRojinegro) hermano.izquierdo;
            hd = (VerticeRojinegro) hermano.derecho;
        }

        if ((vertice.padre.izquierdo == vertice && esRojo(hd)) || (vertice.padre.derecho == vertice && esRojo(hi))) {
            hermano.color = padre.color;
            padre.color = Color.NEGRO;
            if (vertice.padre.izquierdo == vertice) {
                hd.color = Color.NEGRO;
                super.giraIzquierda(padre);
            } else {
                hi.color = Color.NEGRO;
                super.giraDerecha(padre);
            }
        }


        
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
