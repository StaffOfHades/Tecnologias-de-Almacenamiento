

import java.io.IOException;
import java.io.RandomAccessFile;

public class Hoja implements Constants, Nodo {

    private Hoja der;
    private Hoja izq;
    private Raiz padre;
    private RegIndice[] indices = new RegIndice[N + 1];
    private int indiceMax = 0; // (N - 1) / 2 =< indiceMax =< N
    private RandomAccessFile raf;
    private static Arbol arbol;

    public Hoja( Arbol arbol, Raiz padre, RandomAccessFile raf ) {
        this.arbol = arbol;
        this.padre = padre;
        this.raf = raf;
    }

    /**
     * Busca linealmente dentro de los limites si existe el registro bajo 
     * la clave indicada, y lo regresa.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    @Override
    public RegIndice buscar(String clave) throws IOException {

        boolean parar = false;
        int i = -1;
        while( !parar && ++i < indiceMax )
            parar = indices[i].getClave().equals(clave);
        return parar ? indices[i] : null;
    }

    /**
     *  Si la posicion existe, regresa el registro.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @return El RegIndice si existe.
     */
    @Override
    public RegIndice buscar(int posicion) throws IOException {
        return ( indiceMax > 0 && posicion >= 0 && posicion <= indiceMax ) ? indices[posicion] : null;
    }

    /**
     * Busca linealmente dentro de los limites si existe un registro bajo 
     * la clave indicada, y lo crea si no.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    @Override
    public int insertar(String clave)
        throws IOException
    {
        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );
        int i = -1;
        boolean mayor = true;
        while( mayor && ++i < indiceMax ) {

            int claveInd = Integer.parseInt(
                    indices[i].getClave().replaceAll( "\\D+","" ) );
            mayor = claveInd <= claveBusq;
        }

        String comp = null;
        if (indiceMax > 0)
            comp = i > 0 ? indices[i - 1].getClave() : indices[0].getClave();

        //if (comp != null) System.out.println( "Claves: " + comp + " vs " + clave );

        if( comp != null && comp.equals(clave) )
            return i + offset(izq);

        int posicion = i + offset(izq);

        indices[i] = insertarEn( clave, posicion );

        indiceMax++;

        if( indiceMax > N )
            dividir();

        return posicion;
    }

    /**
     * Busca linealmente dentro de los limites si existe un registro bajo
     * la clave indicada, y lo modifica si si.
     * @param clave Clave bajo la cual esta el RegIndice
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar( String clave, int liga ) throws IOException {

        boolean parar = false;
        int i = -1;
        while( !parar && ++i < indiceMax )
            parar = indices[i].getClave().equals(clave);

        if(parar)
           modificar( i + offset(izq), liga );
        else
            System.err.println( "Clave " + clave + " no encontrada" );
    }

    /**
     * Si la posicion existe, modifica la liga.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar( int posicion, int liga ) throws IOException {

        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion * indice.length() );
        indice.read(raf);

        final String clave = indice.getClave();
        int i = -1;
        while(!indices[++i].getClave().equals(clave)) { }

        System.out.println( "Se modifico " + clave + " con liga " + liga );

        indice.setLiga( liga );

        raf.seek( posicion * indice.length() );
        indice.write(raf);

        indices[i] = indice;
    }

    /**
     * Borrar el RegIndice si existe bajo una clave.
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    @Override
    public void borrar(String clave) throws IOException {

        boolean parar = false;
        int i = -1;
        while( !parar && ++i < indiceMax )
            parar = indices[i].getClave().equals(clave);

        if(parar)
            borrar( i + offset(izq) );
        else
            System.err.println( "Clave " + clave + " no encontrada" );
    }

    /**
     * Borrar el RegIndice en la posicion especificada
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    @Override
    public void borrar(int posicion) throws IOException {
        borrarEn(posicion);

        if( padre == null ) // Si es raiz
            return;

        if( (N - 1) / 2 > indiceMax )
            unir();
    }

    /**
     * Mostrar todos los RegIndice adminstrados por esta hoja.
     */
    @Override
    public void mostrar() throws IOException {
        // TODO
    }

    /**
     * Cerrar el acceso de archivo para esta hoja.
     */
    @Override
    public void cerrar() throws IOException {
        raf.close();
    }

    @Override
    public Nodo getPadre() {
        return padre;
    }

    @Override
    public void setPadre(Raiz raiz) {
        padre = raiz;
    }

    /**
     * Inserte la clave en la posicion dada, agregando la liga,
     * y moviendo los registro para crear espacio.
     * @param clave Clave bajo la cual esta el RegIndice
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    private RegIndice insertarEn( String clave, int posicion )
            throws IOException
    {
        System.out.println( "Se creo indice para posicion: " + posicion );
        //System.out.println( "Total: " + ( tamaño() + 1 ) + "\n" );

        final int tamaño = tamaño() - 1;
        RegIndice indice = new RegIndice();
        for( int i = tamaño; i >= posicion; i-- ) {

            raf.seek( i * indice.length() );
            indice.read( raf );

            raf.seek( (i + 1) * indice.length() );
            indice.write( raf );
        }

        raf.seek( posicion * indice.length() );
        indice.setClave( clave );
        indice.setLiga( SIN_ASIGNAR );
        indice.write( raf );

        return indice;
    }

    private void insertarEn(RegIndice indice, int posicion) {

        int tamaño = indiceMax - 1;

        for(; tamaño >= posicion; tamaño-- )
            indices[tamaño + 1] = indices[tamaño];

        indices[posicion] = indice;
    }

    /**
     * Borrar el RegIndice en la posicion dada.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    private void borrarEn(int posicion) throws IOException {

        RegIndice indice = new RegIndice();
        final int tamaño = (int) raf.length() / indice.length();;

        if( posicion >= 0 && posicion < tamaño ) {

            for( int i = posicion + 1; i < tamaño; i++ ) {

                //Posicionamiento de la posicion que se va a mantener
                raf.seek( i * indice.length() );
                indice.read(raf);

                //Posicionamiento en la posicion del elemento que se desea borrar
                raf.seek( (i - 1) * indice.length() );
                indice.write(raf);

            }

            raf.setLength( raf.length() - indice.length() );
        } else
            System.err.println("Posicion invalida");
    }

    private void dividir() {

        System.out.println("Dividiendo");

        boolean sinPadre = padre == null;
        if(sinPadre) {
            System.out.println("Creando nueva raiz");
            padre = new Raiz(arbol, null, raf);
            arbol.setRaiz(padre);
        }

        final int mitad = indiceMax / 2;
        System.out.println("Moviendo de " + mitad + " hasta " + N);

        Hoja hoja = new Hoja(arbol, padre, raf);
        hoja.setIzq(this);
        this.setDer(hoja);
        for( int i = mitad; i <= N; i++ ) {

            hoja.insertar(indices[i]);
            indices[i] = null;
        }
        indiceMax = mitad;
        if(sinPadre)
            padre.insertar(this, this.getClave());
        padre.insertar(hoja, hoja.getClave() );
    }

    private void unir() {

        if( izq != null && izq.tamaño() > (N - 1) / 2 )
            insertar(izq.mover( izq.tamaño() - 1 ));
        else if ( der != null && der.tamaño() > (N - 1) / 2 )
            insertar(der.mover(0));
        else {

            boolean hojaIzq = izq != null;
            Hoja hoja = hojaIzq ? izq : der;
            final int tamaño = hoja != null ? hoja.tamaño() : 0;

            for(int i = 0; i < tamaño; i++)
                insertar(hoja.mover(i));

            if(hojaIzq) {

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

    private String getClave() {
        return indices[0].getClave();
    }

    private void insertar(RegIndice indice) {
        if( indice == null )
            return;

        final int claveBusq = Integer.parseInt( indice.getClave().replaceAll( "\\D+","" ) );
        int i = -1;
        boolean mayor = true;
        while( mayor && ++i < indiceMax ) {

            int claveInd = Integer.parseInt(
                    indices[i].getClave().replaceAll( "\\D+","" ) );
            mayor = claveInd < claveBusq;
            //System.out.println("claveInd " + claveInd + (mayor ? " <" : " >=" ) + " claveBusq " + claveBusq);
        }

        if( i < indiceMax && indices[i].getClave().equals(indice.getClave()) )
            return;

        System.out.println("Insertando " + indice.getClave() + " en posicion" + i);
        insertarEn(indice, i);
        indiceMax++;
        if( indiceMax > N )
            dividir();
    }

    private RegIndice mover(int posicion) {

        if( posicion == 0) {

            final RegIndice indice = indices[posicion];
            int i = 0;
            for( ; i < indiceMax - 1; i++ )
                indices[i] = indices[i + 1];

            indices[i] = null;
            padre.cambiarClave( indice.getClave(), this.getClave() );
            indiceMax--;
            return indice;
        } else if( posicion > 0 && posicion < indiceMax ) {

            final RegIndice indice = indices[posicion];
            indices[posicion] = null;
            indiceMax--;
            return indice;
        }

        return null;
    }

    private int offset(Hoja hoja) {

        int offset = 0;
        while( hoja != null ) {

            offset += hoja.tamaño();
            hoja = hoja.izq;
        }
        return offset;
    }

    private void setDer(Hoja hoja) {
        der = hoja;
    }

    private void setIzq(Hoja hoja) {
        izq = hoja;
    }

    private int tamaño() {
        return indiceMax;
    }
}
