package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        // Aquí va su código.
        Nodo n = this.cabeza;
        String cadena = "";
        while (n != null) {
            cadena = cadena + n.elemento  + "," ;
            n = n.siguiente;
        }
        return cadena;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException("No puedes agregar valores nulos");
        }
        Nodo nuevo = new Nodo(elemento);
        if (rabo == null) {
            cabeza = rabo = nuevo;
        } else {
            rabo.siguiente = nuevo;
            rabo = nuevo;
        }
    }
}
