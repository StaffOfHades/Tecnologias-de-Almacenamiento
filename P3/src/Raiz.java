
// Faltan los import

import java.io.IOException;
import java.io.RandomAccessFile;

public class Raiz implements Constants, Nodo {

    private Nodo[] nodos = new Nodo[N + 1];
    private Raiz izq = null;
    private Raiz der = null;
    private String[] claves = new String[N];
    private int indiceMax = 0; // (N - 1) / 2 < indiceMax < N
    private Raiz padre;
    private static RandomAccessFile raf;
    private static Arbol arbol;


    public static void setArbol(Arbol arbol) {
        Raiz.arbol = arbol;
    }

    public static void setRaf(RandomAccessFile raf) {
        Raiz.raf = raf;
    }

    @Override
    public void setPadre(Raiz raiz) {
        padre = raiz;
    }

    @Override
    public void borrar() throws IOException {

        final int max = tamaño();
        for( int i = 0; i < max; i++ )
            nodos[i].borrar();

        for( int i = 0; i < max; i++ ) {

            if( i > 0 ) claves[i - 1] = null;
            nodos[i] = null;
        }

        padre = null;
        izq = null;
        der = null;
    }

    /**
     * Busca linealmente en que hoja debe ser borrado
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    @Override
    public void borrar(String clave) throws IOException {
        nodos[buscarPosicion(clave)].borrar(clave);
    }

    /**
     * Busca linealmente en cual hoja se podria encontrar la clave indicada,
     * y le pida a la hoja que lo busque.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    @Override
    public RegIndice buscar(String clave) throws IOException {
        return nodos[buscarPosicion(clave)].buscar(clave);
    }

    @Override
    public void insertar(RegIndice indice) throws IOException {
        nodos[buscarPosicion(indice.getClave())].insertar(indice);
    }

    /**
     * Busca linealmente en que hoja debe ser insertado la clave indicada 
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    @Override
    public int insertar(String clave) throws IOException {
        return nodos[buscarPosicion(clave)].insertar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(String clave, int liga) throws IOException {
        nodos[buscarPosicion(clave)].modificar( clave, liga );
    }

    /** 
     * Mostrar todos los RegIndice adminstrados por esta hoja.
     */
    @Override
    public void mostrar(int nivel) throws IOException {

        String tabs = "";
        for (int i = 0; i < nivel; i++)
            tabs += "\t";

        for (int i = 0; i < indiceMax; i++) {

            if ( i > 0 )
                System.out.println(tabs + claves[i  - 1]);
            nodos[i].mostrar(nivel + 1);
        }
    }

    public void borrar(Nodo nodo) {

        boolean encontrado = false;
        int i = -1;

        while (!encontrado && ++i < indiceMax)
            encontrado = nodo == nodos[i];

        if (encontrado)
            borrarEn(i);
        else if ( padre != null )
            padre.borrar(nodo);
        else
            System.err.println("Intento de borrar noda en el arbol fracaso.");

        if( indiceMax <= (N - 1) / 2 )
            unir();
    }

    public void cambiarClave(String vieja, String nueva) {

        final int claveBusq = Integer.parseInt( vieja.replaceAll( "\\D+","" ) );
        int posicion = -1;

        final int tamaño = tamaño() - 1;
        boolean mayor = true;
        boolean igual = false;
        while( mayor && !igual && ++posicion < tamaño ) {

            int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq > claveInd;
            if( claveInd == claveBusq ) {

                claves[posicion] = nueva;
                igual = true;
            }
        }

        if( !igual && !mayor && padre != null )
            padre.cambiarClave(vieja, nueva);
        else if ( padre == null )
            System.err.println("Intento de cambiar clave de busqueda en el arbol fracaso.");
    }

    public void insertar( Nodo nodo, String clave ) {

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int i = 0;

        if ( (indiceMax - 1) > i ) {

            System.out.println( (indiceMax - 1) + " > " + i  );

            boolean mayor = true;
            int claveInd = Integer.parseInt(claves[i].replaceAll("\\D+", ""));

            if( claveBusq >= claveInd ) {

                final int tamaño = indiceMax - 1;
                System.out.println("Claves disponibles: " + tamaño);

                i--;

                while( mayor && ++i < tamaño ) {

                    claveInd = Integer.parseInt(
                            claves[i].replaceAll("\\D+", ""));
                    mayor =  claveBusq > claveInd;
                    System.out.println( "\tclaveBusq " + claveBusq +
                            ( mayor ? " >" : " <=") +  " claveInd " + claveInd );
                }

                claveInd = Integer.parseInt(
                        claves[i - 1].replaceAll("\\D+", ""));

                if( claveInd == claveBusq )
                    return;
                else if( claveBusq > claveInd )
                    i++;
            }
        } else if( indiceMax > i )
            i++;

        System.out.println( "\tInsertando nodo con clave " + clave + " en " + i );

        insertarEn( nodo, clave, i );
        indiceMax++;
        if( indiceMax > N )
            dividir();
    }

    private void borrarEn(int posicion) {

        final int max = indiceMax - 2;

        int i;
        for( i = posicion; i <= max; i++ )
            nodos[i] = nodos[i + 1];

        nodos[i] = null;

        for( i = posicion; i < max; i++ )
            claves[i] = claves[i + 1];

        claves[i] = null;
        indiceMax--;
    }

    private int buscarPosicion(String clave) {

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int posicion = 0;
        int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );

        if( claveBusq < claveInd ) {

            System.out.println("Insertando en primera posicion clave " + clave);
            return posicion;
        }

        final int tamaño = indiceMax - 1;
        System.out.println("Claves disponibles: " + tamaño);
        boolean mayor = true;
        posicion = -1;
        while( mayor && ++posicion < tamaño ) {

            claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq > claveInd;
            System.out.println( "\tclaveBusq " + claveBusq + ( mayor ? " >" : " <=") +  " claveInd " + claveInd );
        }

        System.out.println("Insertando en posicion " + posicion + " clave " + clave);

        return posicion;
    }

    private void dividir() {

        System.out.println("Dividiendo raiz");

        boolean sinPadre = padre == null;
        if(sinPadre) {

            System.out.println("Creando nueva raiz");
            padre = new Raiz();
            arbol.setRaiz(padre);
        }

        final int mitad = indiceMax / 2;
        System.out.println("Moviendo nodos de " + mitad + " hasta " + N);

        final String clave = claves[N / 2 - 1];
        Raiz raiz = new Raiz();
        raiz.setPadre(padre);

        this.setDer(raiz);
        raiz.setIzq(raiz);

        for( int i = mitad; i <= N; i++) {

            raiz.insertar(nodos[i], claves[i - 1]);
            nodos[i].setPadre(raiz);
            nodos[i] = null;
            claves[i - 1] = null;
        }

        System.out.println("Añandiendo raiz al padre");

        indiceMax = mitad;
        if(sinPadre)
            padre.insertar( this, claves[0] );
        padre.insertar(raiz, clave);
    }

    private void insertarEn(Nodo nodo, String clave, int posicion) {

        int tamaño = indiceMax - 1;

        for(; tamaño >= posicion; tamaño-- ) {

            System.out.println( "\tMoviendo " + nodos[tamaño] +" a " + ( tamaño + 1));
            nodos[tamaño + 1] = nodos[tamaño];
            if(tamaño > 1) {
                System.out.println( "\tMoviendo " + claves[tamaño - 1] +" a " + tamaño);
                claves[tamaño] = claves[tamaño - 1];
            }
        }

        nodos[posicion] = nodo;
        posicion--;
        if( indiceMax > 0 ) {

            System.out.println( "\tInsertando clave " + clave + " en " + posicion  );
            claves[posicion] = clave;
        }
    }

    private String moverClave(int posicion) {

        if(posicion == 0) {

            final String clave = claves[posicion];
            borrarEn(posicion);
            return clave;
        } else if( posicion > 0 && posicion < indiceMax ) {

            final String clave = claves[--posicion];
            claves[posicion] = null;
            indiceMax--;
            return clave;
        }

        return null;
    }

    private Nodo moverNodo(int posicion) {

        if( posicion == 0 )
            return nodos[posicion];
        else if( posicion > 0 && posicion < indiceMax ) {

            final Nodo nodo = nodos[posicion];
            nodos[posicion] = null;
            return nodo;
        }

        return null;
    }

    private void setDer(Raiz raiz) {
        der = raiz;
    }

    private void setIzq(Raiz raiz) {
        izq = raiz;
    }

    private int tamaño() {
        return indiceMax;
    }

    private void unir() {

        if( izq != null && izq.tamaño() > (N - 1) / 2 )
            insertar( izq.moverNodo( izq.tamaño() - 1 ),
                    izq.moverClave( izq.tamaño() - 1 ) );
        else if ( der != null && der.tamaño() > (N - 1) / 2 )
            insertar( der.moverNodo(0), der.moverClave(0) );
        else {

            boolean raizIzq = izq != null;
            Raiz raiz = raizIzq ? izq : der;
            final int tamaño = raiz != null ? raiz.tamaño() : 0;
            for(int i = 0; i < tamaño; i++)
                insertar( raiz.moverNodo(i), raiz.moverClave(i) );

            if(raizIzq) {

                padre.borrar(this.izq);
                this.izq.der = null;
                this.izq = null;
            } else if( der != null ) {

                padre.borrar(this.der);
                this.der.izq = null;
                this.der = null;
            } else
                System.err.println("La hoja no tiene parientes con quien unirse");
        }
    }
}

