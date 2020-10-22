package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            pila = new Pila<>();
            if (raiz != null) {
                pila.mete(raiz);
                Vertice izquierdoV = raiz.izquierdo;
                while (izquierdoV != null) {
                    pila.mete(izquierdoV);
                    izquierdoV = izquierdoV.izquierdo;
                }
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return pila.esVacia() != true;
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
            Vertice v = pila.saca();
            T elemento = v.elemento;
            if (v.derecho != null) {
                pila.mete(v.derecho);
                Vertice izquierdoV = v.derecho.izquierdo; //Agregamos toda la rama izquierda
                while (izquierdoV != null) {
                    pila.mete(izquierdoV);
                    izquierdoV = izquierdoV.izquierdo;
                }
            }
            return elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar null");
        } else {
            elementos++;
            if (raiz == null) {
                raiz = ultimoAgregado = nuevoVertice(elemento);
            } else {
                agregaAux(raiz, elemento);
            }
        }
    }
    private void agregaAux(Vertice actual, T elemento) { 
        if (elemento.compareTo(actual.elemento) <= 0) {
            if (actual.izquierdo == null) {
                Vertice nuevo = nuevoVertice(elemento);
                actual.izquierdo = nuevo;
                nuevo.padre = actual;
                ultimoAgregado = nuevo;
            } else {
                agregaAux(actual.izquierdo, elemento);
            }
        } else {
            if (actual.derecho == null) {
                Vertice nuevo = nuevoVertice(elemento);
                actual.derecho = nuevo;
                nuevo.padre = actual;
                ultimoAgregado = nuevo;
            } else {
                agregaAux(actual.derecho, elemento);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminar = (Vertice) busca(elemento); //Vamos a ver si esta acontenido
        if (eliminar != null) { //Si lo encuentra

            //Caso 1: Tiene dos hijos distintos de null
            if (eliminar.izquierdo != null && eliminar.derecho != null) {
                Vertice maximo = intercambiaEliminable(eliminar);
                eliminaVertice(maximo);
                return;
            }

            eliminaVertice(eliminar);

        }    
    }

    private Vertice maximoDerecho(Vertice vertice) {
        //Si no existe el vertice entonces pues ni es el maximo ni hay maximo consecuente
        if (vertice == null) {
            return null;
        }
        //Si ya es el maximo porque ya no hay derecho entonces lo retorna 
        if (vertice.derecho == null) {
            return vertice;
        } else { //Si no pues no es el maximo, y hace recuersion hasta encontrarlo
            return maximoDerecho(vertice.derecho);
        }
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice maximo = maximoDerecho(vertice.izquierdo);
        T elemento = vertice.elemento;
        vertice.elemento = maximo.elemento;
        maximo.elemento = elemento;
        return maximo;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        //Caso en el que sean los dos hijos null
        if (vertice.izquierdo == null && vertice.derecho == null) {
            //Si es la raiz
            if (vertice.padre == null) {
                raiz = null;
                elementos--;
                return;
            } 
            //Si no lo es
            if (vertice.padre.izquierdo == vertice) {
                vertice.padre.izquierdo = null;
                elementos--;
                return;
            } else {
                vertice.padre.derecho = null;
                elementos--;
                return;
            }
        }

        //Caso en el que tenga a lo mas un vertice distinto de null:

        //Si el que es distinto de null es izquierdo
        if (vertice.izquierdo != null) { 
            //Si el que queremos eliminar es la raiz
            if (vertice.padre == null) {
                raiz = vertice.izquierdo;
                vertice.izquierdo.padre = null;
                elementos--;
                return;
            } else {
                //Si no es la raiz el vertice a eliminar
                if (vertice.padre.izquierdo == vertice) {
                    vertice.padre.izquierdo = vertice.izquierdo;
                } else {
                    vertice.padre.derecho = vertice.izquierdo;
                }
                vertice.izquierdo.padre = vertice.padre;
                elementos--;
                return;
            }
        }

        //Si el que es distinto de null es derecho
        if (vertice.derecho != null) {
            //Si es la raiz la que queremos eliminar
            if (vertice.padre == null) {
                raiz = vertice.derecho;
                vertice.derecho.padre = null;
                elementos--;
                return;
            } else {
                if (vertice.padre.izquierdo == vertice) {
                    vertice.padre.izquierdo = vertice.derecho;
                } else {
                    vertice.padre.derecho = vertice.derecho;
                }
                vertice.derecho.padre = vertice.padre;
                elementos--;
                return;
            }
        }
    }
    

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return buscaAux(this.raiz, elemento);
    }
    private Vertice buscaAux(Vertice v, T elemento) {
        if (v == null || elemento == null) {
            return null;
        }
        if (elemento.compareTo(v.elemento) == 0) {
            return v;
        }
        if (elemento.compareTo(v.elemento) < 0) {
            return buscaAux(v.izquierdo, elemento);
        }
        return buscaAux(v.derecho, elemento);

    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        // Aquí va su código.
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice v = (Vertice) vertice;
        if (v != null && v.izquierdo != null) {
            Vertice izquierdoV = v.izquierdo;
            izquierdoV.padre = v.padre; //Hacemos que se pasen referencias de padres
            if (v == raiz) {
                raiz = izquierdoV;
            } else {
                //Vamos a terminar de conectar al padre si no es la raiz
                if (v.padre.izquierdo == v) {
                    izquierdoV.padre.izquierdo = izquierdoV;
                } else {
                    izquierdoV.padre.derecho = izquierdoV;
                }
            }
            v.izquierdo = izquierdoV.derecho;
            if (izquierdoV.derecho != null) {
                izquierdoV.derecho.padre = v;
            }
            izquierdoV.derecho = v;
            v.padre = izquierdoV;
        }
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código
        Vertice p = (Vertice) vertice;
        if (p != null && p.derecho != null) {
            Vertice q = p.derecho;
            q.padre = p.padre;
            if (p == raiz) {
                raiz = q;
            } else {
                if (p.padre.izquierdo == p ) {
                    q.padre.izquierdo = q;
                } 
                if (p.padre.derecho == p) {
                    q.padre.derecho = q;
                }
            }
            p.derecho = q.izquierdo;
            if (q.izquierdo != null) {
                q.izquierdo.padre = p;
            }
            q.izquierdo = p;
            p.padre = q;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrderAux(this.raiz, accion);
    }
    private void dfsPreOrderAux(Vertice actual, AccionVerticeArbolBinario<T> accion){
        if (actual == null) {
            return ;
        }
        accion.actua(actual);
        dfsPreOrderAux(actual.izquierdo, accion);
        dfsPreOrderAux(actual.derecho, accion);

    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrderAux(this.raiz, accion);
    }
    private void dfsInOrderAux(Vertice actual, AccionVerticeArbolBinario<T> accion) {
        if (actual == null) {
            return;
        }
        dfsInOrderAux(actual.izquierdo, accion);
        accion.actua(actual);
        dfsInOrderAux(actual.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrderAux(this.raiz, accion);
    }
    private void dfsPostOrderAux(Vertice actual, AccionVerticeArbolBinario<T> accion) {
        if (actual == null) {
            return;
        } 
        dfsPostOrderAux(actual.izquierdo,accion);
        dfsPostOrderAux(actual.derecho, accion);
        accion.actua(actual);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

}
