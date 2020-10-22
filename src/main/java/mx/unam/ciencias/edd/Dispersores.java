package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    //Big Endian
    private static int bigEndian(byte a, byte b, byte c,byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    //Metodo para llnar de ceros algún arreglo que no sea de longitud multiplo de 4
    private static byte[] llenaCeros(byte[] llave) {
        byte[] nuevo;
        if (llave.length % 4 == 1) {
            nuevo = new byte[llave.length + 3];
            for (int i = 0; i < llave.length; i++) {
                nuevo[i] = llave[i];
            }
            nuevo[nuevo.length-3] = 0;
            nuevo[nuevo.length-2] = 0;
            nuevo[nuevo.length-1] = 0;
            return nuevo;  
        } else if (llave.length % 4 == 2) {
            nuevo = new byte[llave.length+2];
            for (int i = 0; i < llave.length; i++) {
                nuevo[i] = llave[i];
            }
            nuevo[nuevo.length-2] = 0;
            nuevo[nuevo.length-1] = 0;
            return nuevo;
        } else {
            nuevo = new byte[llave.length+1];
            for (int i = 0; i < llave.length; i++) {
                nuevo[i] = llave[i];
            }
            nuevo[nuevo.length-1] = 0;
            return nuevo;
        }
    }

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        //Si el numero de elementos en el arreglo no es multiplo de 4
        if (llave.length % 4 != 0) {
            llave = llenaCeros(llave);
        }
        //Procedemos a XOR 
        int r = 0;
        for (int i = 0; i < llave.length-3; i = i+4) {
            r ^=  bigEndian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
        }
        return r;
    }

    //Método Little Endian
    private static int littleEndian(byte a, byte b, byte c, byte d) {
        return ((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | ((a & 0xFF));
    }

    //Método para mezclar enteros
    private static int[] mezcla(int a, int b, int c, int[] arreglo) {
        a -= b; a -= c; a ^= (c>>>13); 
        b -= c; b -= a; b ^= (a<<8); 
        c -= a; c -= b; c ^= (b>>>13); 
        a -= b; a -= c; a ^= (c>>>12);  
        b -= c; b -= a; b ^= (a<<16); 
        c -= a; c -= b; c ^= (b>>>5); 
        a -= b; a -= c; a ^= (c>>>3);  
        b -= c; b -= a; b ^= (a<<10); 
        c -= a; c -= b; c ^= (b>>>15);
        arreglo[0] = a;
        arreglo[1] = b;
        arreglo[2] = c;
        return arreglo;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9e3779b9, b = a, c = 0xFFFFFFFF;
        int[] mezclado = new int[3];
        //indice
        int i = 0;
        //longitud
        int n = llave.length;
        //Mientras tenga 12 bytes para trabajar
        while (n >= 12) {
            a += littleEndian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            b += littleEndian(llave[i+4], llave[i+5], llave[i+6], llave[i+7]);
            c += littleEndian(llave[i+8], llave[i+9], llave[i+10], llave[i+11]);
            mezclado = mezcla(a, b, c, mezclado);
            a = mezclado[0];
            b = mezclado[1];
            c = mezclado[2];
            n -= 12;
            i += 12;
        }
        //Sumamos a C la longitud del arreglo
        c += llave.length;

        //Si sobran menos de 12 bytes
        switch (n) {
            case 11: c += ( (llave[i+10] & 0xFF) << 24);
            case 10: c += ( (llave[i+9] & 0xFF)  << 16);
            case  9: c += ( (llave[i+8] & 0xFF)  << 8);

            case  8: b += ( (llave[i+7] & 0xFF)  << 24);
            case  7: b += ( (llave[i+6] & 0xFF)  << 16);
            case  6: b += ( (llave[i+5] & 0xFF)  << 8);
            case  5: b +=  ( llave[i+4] & 0xFF);

            case  4: a += ( (llave[i+3] & 0xFF)  << 24); 
            case  3: a += ( (llave[i+2] & 0xFF)  << 16);
            case  2: a += ( (llave[i+1] & 0xFF)  << 8);
            case  1: a += ( llave[i] & 0xFF);
        }
        //Mezclamos y regresamos C
        mezclado = mezcla(a, b, c, mezclado);
        return mezclado[2];
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for (int i = 0; i < llave.length; i++) {
            h = h * 33 + (llave[i] & 0xFF);
        }
        return h;
    }
}
