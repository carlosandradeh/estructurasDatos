package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        quicksort(arreglo, comparador, 0, arreglo.length-1);
    }
    private static<T> void quicksort(T[] arreglo, Comparator<T> comparador, int a, int b) {
        if (b <= a ) { // En este caso ya etsá ordenado el arreglo, ya que o es vacio o es d longitud 1
            return;
        }
        int i = a+1, j = b;
        while (i < j) {
            if (comparador.compare(arreglo[i],arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0) {
                intercambia(arreglo, i++, j--);
            } else if (comparador.compare(arreglo[i], arreglo[a]) <= 0) {
                i++;
            } else {
                j--;
            }    
        }
        if (comparador.compare(arreglo[a], arreglo[i]) < 0) {
            i--;   
        }
        intercambia(arreglo, i, a);
        quicksort(arreglo, comparador, a, i-1);
        quicksort(arreglo, comparador, i+1, b); 
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        for (int i = 0; i < arreglo.length; i++) {
            int m = i;
            for (int j = i+1; j < arreglo.length; j++)
                if (comparador.compare(arreglo[j], arreglo[m]) < 0)
                    m = j;
            intercambia(arreglo, i, m);
        }
    }
    private static <T> void intercambia(T[] arreglo, int i, int j) {
        T aux = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = aux;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        return binaria(arreglo, comparador, elemento, 0, arreglo.length-1);
    }

    private static<T> int binaria(T[] arreglo, Comparator<T> comparador,T elementoBuscar, int a, int b) {
        
        if (b >= a ) {
            int m = (a+b)/2;
            if (elementoBuscar.equals(arreglo[m])) //Cuando sea el mismo elemento
                return m;  
        
            if (comparador.compare(elementoBuscar, arreglo[m]) < 0) {
                return binaria(arreglo, comparador, elementoBuscar, a, m-1);
            } 
            if (comparador.compare(elementoBuscar, arreglo[m]) > 0) {
                return binaria(arreglo, comparador, elementoBuscar, m+1, b);
            }
        }
        return -1;
        
        
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
