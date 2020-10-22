package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            cola = new Cola<>();
            if (raiz != null) {
                cola.mete(raiz);
            }    
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return cola.esVacia() != true;

        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            Vertice v = cola.saca();
            if (v.izquierdo != null) {
                cola.mete(v.izquierdo);
            }
            if (v.derecho != null) {
                cola.mete(v.derecho);
            }
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar nulos");
        }
        elementos++;
        Vertice nuevo = nuevoVertice(elemento);
        if (this.raiz == null) {
            raiz = nuevo;
        } else {
            Cola<Vertice> bfsCola = new Cola<>();
            bfsCola.mete(raiz);
            Vertice v = null;
            while (!bfsCola.esVacia()) {
                v = bfsCola.saca();
                if (v.izquierdo == null) {
                    nuevo.padre = v;
                    v.izquierdo = nuevo;
                    break;
                
                }

                if (v.derecho == null) {
                    nuevo.padre = v;
                    v.derecho = nuevo;
                    break;
                }
                    
                
                if (v.izquierdo != null) {
                    bfsCola.mete(v.izquierdo);
                }
                if (v.derecho != null) {
                    bfsCola.mete(v.derecho);
                }
            }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminar = (Vertice) busca(elemento);
        if (eliminar != null && this.raiz != null) {
            elementos--;
            if (elementos == 0) {
                this.raiz = null;
                return;
            }

            Cola<Vertice> colita = new Cola<>();
            colita.mete(this.raiz);
            Vertice v = null;
            while (!colita.esVacia()) {
                v = colita.saca();
                if (v.izquierdo != null) {
                    colita.mete(v.izquierdo);
                }
                if (v.derecho != null) {
                    colita.mete(v.derecho);
                }
            }
            
            //Intercambiamos los valores 
            T elem = eliminar.elemento;
            eliminar.elemento = v.elemento;
            v.elemento = elem;
            //Como v es una hoja es facil desconectarlo
            if (v.padre.izquierdo == v) {
                v.padre.izquierdo = null;
                
            }  else {
                v.padre.derecho = null;
    
            } 
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        if (this.raiz == null)
            return -1;
        return (int) Math.floor(Math.log10(elementos)/ Math.log10(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        if (this.raiz != null) {
            Cola<Vertice> nuevaCola = new Cola<>();
            nuevaCola.mete(this.raiz);
            Vertice v = null;
            while (!nuevaCola.esVacia()) {
                v = nuevaCola.saca();
                accion.actua(v);
                if (v.izquierdo != null) {
                    nuevaCola.mete(v.izquierdo);
                }
                if (v.derecho != null) {
                    nuevaCola.mete(v.derecho);
                }
            }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
