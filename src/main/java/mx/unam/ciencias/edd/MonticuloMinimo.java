package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.Coleccion;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (this.indice >= elementos || this.indice < 0 ) {
                throw new NoSuchElementException("No hay siguiente");
            }

            this.indice++;
            return arbol[indice-1];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
        this.elementos = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
        int i = 0;
        for (T elemento : iterable) {
            elemento.setIndice(i);
            arbol[i] = elemento;
            i++;
        }
        this.elementos = n;
        for (int j = ((n-1)/2); j >= 0; j--) {
            acomodaHaciaAbajo(j);
        }
    }

    private void intercambia(T elemento, T elemento2) {
        //Auxiliares
        int auxIndice = elemento.getIndice();
        //INtercambiamos valores
        arbol[elemento.getIndice()] = elemento2;
        arbol[elemento2.getIndice()] = elemento;
        //Intercambiamos indices
        elemento.setIndice(elemento2.getIndice());
        elemento2.setIndice(auxIndice);
    }

    private void acomodaHaciaArriba(int i) {
        //El padre debe ser menor que sus hijos
        //Si el vertice es menor que su padre sucede esto

        //Si ya es la raiz O si es un indice inválido
        if (i == 0 || i < 0 || i >= this.elementos)
            return;

        //Si el padre es null O Si el elemento actual es mayor al padre
        if (arbol[(i-1)/2] == null || arbol[i].compareTo(arbol[(i-1)/2]) > 0 )
            return;

        //Intercambiamos valores y hacemos recursión 
        intercambia(arbol[i], arbol[(i-1)/2]);
        acomodaHaciaArriba((i-1)/2);
    }
    
    private void acomodaHaciaAbajo(int i) {
        if (i < 0 || 2*i+1 >= elementos) {
            return;
        }
        int izquierdo = 2*i+1;
        int derecho = 2*i+2;
        if (elementos <= derecho || arbol[izquierdo].compareTo(arbol[derecho]) <= 0) {
            if (arbol[i].compareTo(arbol[izquierdo]) > 0) {
                intercambia(arbol[i], arbol[izquierdo]);
            }
            acomodaHaciaAbajo(izquierdo);
        } else {
            if (arbol[i].compareTo(arbol[derecho]) > 0) {
                intercambia(arbol[i], arbol[derecho]);
            }
            acomodaHaciaAbajo(derecho);
        }

    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if ( this.elementos == arbol.length ) {
            T[] nuevo = nuevoArreglo(this.elementos*2);
            for (int i = 0; i < elementos; i++) {
                nuevo[i] = arbol[i];
            }
            arbol = nuevo;
        }
        elemento.setIndice(this.elementos);
        arbol[this.elementos] = elemento;
        this.elementos++;
        //Acomodamos hacia arriba el ultimo agregado
        acomodaHaciaArriba(this.elementos-1);

    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if (this.elementos == 0)
            throw new IllegalStateException("Monticulo Vacio");
        
        T eliminado = arbol[0];
        intercambia(arbol[0], arbol[this.elementos-1]);
        arbol[this.elementos-1].setIndice(-1);
        arbol[this.elementos-1] = null;
        this.elementos--;
        //Reacomodamos la raiz, ya que era el ultimo antes entonces es mayor que todos sus hijos
        acomodaHaciaAbajo(0);
        return eliminado;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= this.elementos)
            return; 
        int i = elemento.getIndice();
        intercambia(arbol[i], arbol[this.elementos - 1]);
        arbol[this.elementos - 1].setIndice(-1);
        this.elementos--;
        acomodaHaciaAbajo(i);
        acomodaHaciaArriba(i);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= this.elementos)
            return false;
        
        return arbol[elemento.getIndice()].equals(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return this.elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for (int i = 0; i < this.elementos; i++) {
            arbol[i] = null;
        }
        this.elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        acomodaHaciaAbajo(elemento.getIndice());
        acomodaHaciaArriba(elemento.getIndice());
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return this.elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= this.elementos)
            throw new NoSuchElementException("Indice inválido");
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String cadena = "";
        for (int i = 0; i < this.elementos; i++) {
            cadena += arbol[i] + ", ";
        }
        return cadena;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        
        for (int i = 0; i < this.elementos; i++) {
            if ( !this.arbol[i].equals(monticulo.arbol[i])) 
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> l1 = new Lista<>();
        
        for (T elemento : coleccion) {
            Adaptador<T> adaptador = new Adaptador<>(elemento);
            l1.agrega(adaptador);
        }

        Lista<T> l2 = new Lista<>();
        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<>(l1);
        while(!monticulo.esVacia()) {
            T elem = monticulo.elimina().elemento;
            l2.agrega(elem);
        }
        return l2;
    }
}