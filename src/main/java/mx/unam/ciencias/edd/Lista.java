package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            // Aquí va su código.
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if (siguiente == null) {
                throw new NoSuchElementException("No hay elemento siguiente!");
            } else {
                anterior = siguiente;
                siguiente = siguiente.siguiente;
                return anterior.elemento;
            }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if (anterior == null) {
                throw new NoSuchElementException("No hay elemento anterior!");
            } else {
                siguiente = anterior;
                anterior = anterior.anterior;
                return siguiente.elemento;
            }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            anterior = null;
            siguiente = cabeza;

        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            siguiente = null;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        if (cabeza == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        this.agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar null a la lista");
        }
        Nodo nuevo = new Nodo(elemento); //Creamos un nevo nodo para agregar  
        if (rabo == null) { //Si es vacia la lista 
            cabeza = nuevo;
            rabo = nuevo;
        } else { // Si no es vacia agregamos al final
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
        }
        longitud++; //Aumentamos la longitud
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar null a las listas! ");
        }
        Nodo nuevo = new Nodo(elemento); // Creamos un nuevo nodo para agregar 
        if (cabeza == null) { //Si la lista es vacia
            cabeza = nuevo;
            rabo = nuevo;
        } else { // Si la lista no es vacia
            cabeza.anterior = nuevo;
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        longitud++; //Aumentamos la longitud
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar un null a la lista!");
        }
        
        if (i <= 0) {
            agregaInicio(elemento);
        } else if (i >= this.longitud) {
            agregaFinal(elemento);  
        } else {
            Nodo n = new Nodo(elemento); //Nodo a agregar
            Nodo m = cabeza; //Pasamos referencia para iterar desde el inicio
            int j = 0;
            while (j++ < i) {
                m = m.siguiente;
            }
            m.anterior.siguiente = n;
            n.anterior = m.anterior;
            n.siguiente = m;
            m.anterior = n;
            longitud++; //Aumentamos la longitud
        }
    }

    


    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo eliminar = buscaNodo(cabeza, elemento);
        if(eliminar != null ) {
            if(cabeza == rabo){ //Si solo hay un elemento
                cabeza = null;
                rabo = null;
            } else if(cabeza == eliminar){ //si la cabeza es igual al nodo a eliminar
                cabeza.siguiente.anterior = null;
                cabeza = cabeza.siguiente;
            } else if(rabo == eliminar){ //si el rabo es igual al nodo a eliminar
                rabo.anterior.siguiente = null;
                rabo = rabo.anterior;
            } else { //Si es un elemento de enmedio
                eliminar.siguiente.anterior = eliminar.anterior;
                eliminar.anterior.siguiente = eliminar.siguiente; 
            }
            longitud--;
        }    
        
    }
    private Nodo buscaNodo(Nodo nodo, T elemento) {
        if(nodo == null) {
            return null;
        } 
        if(nodo.elemento.equals(elemento)) {
            return nodo;
        } 
        return buscaNodo(nodo.siguiente, elemento);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if (cabeza == null) {
            throw new NoSuchElementException("No puedes eliminar porque la lista es vacia!");
        }
        Nodo elemen = new Nodo(cabeza.elemento);//Almacenamos el elemento de Cabeza
        if (this.longitud == 1) { //Si solo hay un elemento 
            cabeza = null;
            rabo = null;
        } else { //Si hay mas de dos elementos
            cabeza.siguiente.anterior = null;
            cabeza = cabeza.siguiente;
        }
        longitud--; //Decrecemos la longitud
        return elemen.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if (rabo == null) {
            throw new NoSuchElementException("No puedes eliminar porque la lista es vacia!");
        }
        Nodo elemen = new Nodo(rabo.elemento); //Almacenamos el elemento del rabo
        if (this.longitud == 1) { //Si solo hay un elemento
            rabo = null;
            cabeza = null;
        } else { // Si hay mas de dos elementos
            rabo.anterior.siguiente = null;
            rabo = rabo.anterior;
        }
        longitud--; //Decrementamos la longitud
        return elemen.elemento;

    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return this.buscaNodo(cabeza,elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> listaReversa = new Lista<>();
        Nodo n = cabeza;
        while (n != null) {
            listaReversa.agregaInicio(n.elemento);
            n = n.siguiente;
        }
        return listaReversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> listaCopia = new Lista<>();
        Nodo n = cabeza;
        while (n != null) {
            listaCopia.agregaFinal(n.elemento);
            n = n.siguiente;
        }
        return listaCopia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if (cabeza == null) {
            throw new NoSuchElementException("No puedes obtener el primer porque la lista es vacia!");
        }
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if (rabo == null) {
            throw new NoSuchElementException("No pudes obtener el ultimo porque la lista es vacia!");
        }
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido("Indice invalido!");
        }
        Nodo m = cabeza; //Pasamos referencia para iterar desde el inicio
        int j = 0;
        while (j++ < i) {
            m = m.siguiente;
        } 
        return m.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        Nodo n = cabeza;
        int j = 0;
        while (n != null) {
            if (n.elemento.equals(elemento)) {
                return j;
            }
            n = n.siguiente;
            j++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        if (cabeza == null) {
            return "[]";
        } else {
            String cadena = "[";
            Nodo n = cabeza;
            while (n != null) {
                if (n != rabo) {
                    cadena = cadena + n.elemento + ", ";
                } else {
                    cadena = cadena + n.elemento + "]";
                }
                n = n.siguiente;
            }
            return cadena;
        }
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
        if (this.longitud != lista.longitud) {
            return false;
        } 
        if (this.rabo == null && lista.rabo == null) {
            return true;
        } else {
            Nodo n = this.cabeza;
            Nodo m = lista.cabeza;
            while (n != null) {
                if ( !(n.elemento.equals(m.elemento) )) {
                    return false;  
                } 
                n = n.siguiente;
                m = m.siguiente;
            }
            return true;
        }

    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
        if (cabeza == rabo || cabeza == null) { //Si solo tiene un elemento o ninguno  ya esta ordenada
            return this.copia();
        }


        int m = longitud / 2;
        int contador = 0;
        Lista<T> lista1 = new Lista<>();
        Lista<T> lista2 = new Lista<>();
        Nodo n = this.cabeza;

        while (contador < m) {
            lista1.agrega(n.elemento);
            n = n.siguiente;
            contador++;
        }
        while ( n != null) {
            lista2.agrega(n.elemento);
            n = n.siguiente;
        }
        
        return merge(comparador, lista1.mergeSort(comparador), lista2.mergeSort(comparador));
    
    }
    private Lista<T> merge(Comparator<T> comparador, Lista<T> lista1, Lista<T> lista2){
        Lista<T> nueva = new Lista<>();
        Nodo i = lista1.cabeza;
        Nodo j = lista2.cabeza;
        while (i != null && j != null) {
            if (comparador.compare(i.elemento, j.elemento) <= 0) {
                nueva.agrega(i.elemento);
                i = i.siguiente;
            } else {
                nueva.agrega(j.elemento);
                j = j.siguiente;
            }
        }
        //Agregamos los elementos restantes

        while (i != null) { //Si i es la que no esta vacia
            nueva.agrega(i.elemento);
            i = i.siguiente;
        }
        while (j != null) { //Si j es la que no esta vacia
            nueva.agrega(j.elemento);
            j = j.siguiente;
        }
        return nueva;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        Nodo n = cabeza;
        while (n != null) {
            if (n.elemento.equals(elemento)) {
                return true;
            } else {
                n = n.siguiente;
            }
            if (comparador.compare(n.elemento, elemento) > 0) {
                return false;
            }

        }
        return false;
        
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
