
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
    private RandomAccessFile raf;
    private static Arbol arbol;

    public Raiz( Arbol arbol, Raiz padre, RandomAccessFile raf ) {
        this.arbol = arbol;
        this.padre = padre;
        this.raf = raf;
    }

    /**
     * Busca linealmente en cual hoja se podria encontrar la clave indicada,
     * y le pida a la hoja que lo busque.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    @Override
    public RegIndice buscar(String clave) throws IOException {

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int posicion = 0;
        int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );

        if( claveBusq < claveInd )
            return nodos[posicion].buscar(clave);

        final int tamaño = claves.length;
        boolean mayor = true;
        posicion = -1;
        while( mayor && ++posicion < tamaño ) {
            claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq < claveInd;
        }

        return nodos[posicion].buscar(clave);
    }

    /**
     * Busca linealmente en cual hoja se podria encontrar la clave indicada,
     * y le pida a la hoja que lo busque.
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     * @return El RegIndice si existe.
     */
    @Override
    public RegIndice buscar(int posicion_indice) throws IOException {

        if( posicion_indice >= tamaño() ) return null;

        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion_indice * indice.length() );
        indice.read(raf);

        return buscar(indice.getClave());
    }

    /**
     * Busca linealmente en que hoja debe ser insertado la clave indicada 
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    @Override
    public int insertar(String clave)
        throws IOException
    {
        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int posicion = 0;
        int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );

        if( claveBusq < claveInd )
            return nodos[posicion].insertar(clave);

        final int tamaño = claves.length;
        boolean mayor = true;
        posicion = -1;
        while( mayor && ++posicion < tamaño ) {
            claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq < claveInd;
        }

        return nodos[posicion].insertar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(String clave, int liga) throws IOException {

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int posicion = 0;
        int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );

        if( claveBusq < claveInd )
            nodos[posicion].modificar(clave, liga);

        final int tamaño = claves.length;
        boolean mayor = true;
        posicion = -1;
        while( mayor && ++posicion < tamaño ) {
            claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq < claveInd;
        }

        nodos[posicion].modificar(clave, liga);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(int posicion_indice, int liga) throws IOException {

        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion_indice * indice.length() );
        indice.read(raf);

        modificar( indice.getClave(), liga );
    }

    /** 
     * Busca linealmente en que hoja debe ser borrado
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    @Override
    public void borrar(String clave) throws IOException
    {
        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int posicion = 0;
        int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );

        if( claveBusq < claveInd )
            nodos[posicion].borrar(clave);

        final int tamaño = claves.length;
        boolean mayor = true;
        posicion = -1;
        while( mayor && ++posicion < tamaño ) {
            claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq < claveInd;
        }

        nodos[posicion].borrar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     */
    @Override
    public void borrar(int posicion_indice) throws IOException {

        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion_indice * indice.length() );
        indice.read(raf);

        borrar(indice.getClave());
    }

    /** 
     * Mostrar todos los RegIndice adminstrados por esta hoja.
     */
    @Override
    public void mostrar() throws IOException {
        // TODO

        for( Nodo n : nodos )
            n.mostrar(); 
    }

    /**
     * Cerrar el acceso de archivo para todas las hojas
     */
    @Override
    public void cerrar() throws IOException {
        for( Nodo n : nodos )
            if( n != null)
                n.cerrar();
    }

    @Override
    public Nodo getPadre() {
        return padre;
    }

    @Override
    public void setPadre(Raiz raiz) {
        padre = raiz;
    }

    private void dividir() {

        boolean sinPadre = padre == null;
        if(sinPadre) {
            padre = new Raiz(arbol, null, raf);
            arbol.setRaiz(padre);
        }

        final int mitad = indiceMax / 2;
        final String clave = claves[N / 2 - 1];
        Raiz raiz = new Raiz(arbol, padre, raf);

        this.setDer(raiz);
        raiz.setIzq(raiz);

        for( int i = mitad; i <= N; i++) {

            raiz.insertar(nodos[i], claves[i - 1]);
            nodos[i] = null;
            claves[i - 1] = null;
        }

        indiceMax = mitad;
        if(sinPadre)
            padre.insertar( this, claves[0] );
        padre.insertar(raiz, clave);
    }

    private void insertarEn(Nodo nodo, String clave, int posicion) {

        int tamaño = indiceMax - 1;

        for(; tamaño >= posicion; tamaño-- ) {

            nodos[tamaño + 1] = nodos[tamaño];
            if(tamaño > 1)
                claves[tamaño] = claves[tamaño - 1];
        }

        nodos[posicion] = nodo;
        posicion = posicion - (posicion > 0 ? -1 : 0);
        if( indiceMax > 0 )
            claves[posicion] = clave;
    }

    private void borrarEn(int posicion) {

        int i;
        for( i = posicion; i < indiceMax - 1; i++ )
            nodos[i] = nodos[i + 1];

        nodos[i] = null;

        for( i = posicion; i < indiceMax - 2; i++ )
            claves[i] = claves[i + 1];

        claves[i] = null;
        indiceMax--;
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

        final int tamaño = claves.length;
        boolean mayor = true;
        boolean igual = false;
        while( mayor && !igual && ++posicion < tamaño ) {

            int claveInd = Integer.parseInt( claves[posicion].replaceAll( "\\D+","" ) );
            mayor = claveBusq < claveInd;
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

    public void insertar(Nodo nodo, String clave) {

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int i = 0;

        if ( indiceMax > i && claves.length > 0 ) {

            System.out.println( indiceMax  + " > " + i);

            boolean mayor = true;
            int claveInd = Integer.parseInt(claves[i].replaceAll("\\D+", ""));

            if (claveInd < claveBusq) {
                System.out.println("claveInd " + claveInd + " < claveBusq " + claveBusq);

                while (mayor && ++i < indiceMax) {

                    claveInd = Integer.parseInt(
                            claves[i - 1].replaceAll("\\D+", ""));
                    mayor = claveInd < claveBusq;
                }

                if (claves[i].equals(clave))
                    return;
            }
        } else if ( indiceMax > i ) {

            claves[i] = clave;
            i++;
        }

        System.out.println("Insertando nodo con clave " + clave + " en " + i);

        insertarEn( nodo, clave, i );
        indiceMax++;
        if( indiceMax > N )
            dividir();
    }

    public String moverClave(int posicion ) {

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

    public Nodo moverNodo(int posicion) {

        if( posicion == 0 )
            return nodos[posicion];
        else if( posicion > 0 && posicion < indiceMax ) {

            final Nodo nodo = nodos[posicion];
            nodos[posicion] = null;
            return nodo;
        }

        return null;
    }

    public void setDer(Raiz raiz) {
        der = raiz;
    }

    public void setIzq(Raiz raiz) {
        izq = raiz;
    }

    public int tamaño() {
        return indiceMax;
    }

}

