import java.io.IOException;
import java.io.RandomAccessFile;

public class Arbol implements Constants {

    private Nodo raiz;
    private RandomAccessFile raf = null;

    public Arbol(RandomAccessFile raf) throws IOException {

        this.raf = raf;
        final int tamaño = tamaño();

        Raiz.setArbol(this);
        Hoja.setArbol(this);
        Raiz.setRaf(raf);
        Hoja.setRaf(raf);

        raf.seek(0);
        for( int i = 0; i < tamaño; i++ ) {

            RegIndice indice = new RegIndice();
            indice.read(raf);
            insertar(indice);
        }
    }

    /**
     * Borrar todos los RegIndice desde la raiz.
     */
    public void borrar() throws IOException {

        if( raiz == null ) return;

        raiz.borrar();
        raiz = null;
        raf.setLength(0);
    }

    /**
     * Borrar el RegIndice desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    public void borrar(int posicion) throws IOException {

        if( raiz == null ) return;

        // Si raiz existe, intenta borrar
        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion * indice.length() );
        indice.read(raf);

        System.out.println( "Leyendo indice en posicion global " + posicion );

        raiz.borrar(indice.getClave());
    }

    /**
     * Borrar el RegIndice desde la raiz.
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    public void borrar(String clave) throws IOException {

        if( raiz == null ) return;

        // Si la raiz existe, intenta borra
        raiz.borrar(clave);
    }

    /**
     * Buscar del RandomAccessFile el RegIndice.
     * @param posicion Posicion global donde se encuentra el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(int posicion) throws IOException {

        if( posicion < 0 || posicion >= tamaño() )
            return null;

        RegIndice indice = new RegIndice();
        raf.seek( posicion * indice.length() );
        indice.read( raf );

        return indice;
    }

    /**
     * Buscar desde la raiz
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(String clave) throws IOException {
        return raiz == null ? null : raiz.buscar(clave);
    }

    /**
     * Cerrar el acceso de archivo para todas las hojas
     */
    public void cerrar() throws IOException {
        raf.close();
    }

    /**
     * Inserta desde la raiz.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    public int insertar( String clave ) throws IOException {
        if( raiz == null )
            raiz = new Hoja();

        // Regresar la posicion.
        return raiz.insertar( clave );
    }


    private void insertar(RegIndice indice) throws IOException {

        if( raiz == null )
            raiz = new Hoja();

        // Regresar la posicion.
        raiz.insertar( indice );
    }

    /**
     * Modificar la liga desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    public void modificar(int posicion, int liga) throws IOException {

        if( raiz == null ) return;

        RegIndice indice = new RegIndice();

        //Se posiciona el puntero enla posicion del registro y se lee
        raf.seek( posicion * indice.length() );
        indice.read(raf);

        System.out.println( "Leyendo indice en posicion global " + posicion );

        // Si la raiz existe, intenta modificar
        raiz.modificar(indice.getClave(), liga);
    }

    /**
     * Modificar la liga desde la raiz.
     *  @param clave Clave bajo la cual se debe encontrar el registro.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    public void modificar(String clave, int liga) throws IOException {

        if( raiz == null ) return;

        // Si la raiz existe, intente modificar
        raiz.modificar(clave, liga);
    }

    /** 
     * Mostrar el arbol empezando por la raiz.
     */
    public void mostrar() throws IOException {

        RegIndice indice = new RegIndice();
        final int tamaño = tamaño();

        System.out.println( "Numero de entradas: " + tamaño() );
        raf.seek( 0 );

        for( int i = 0; i < tamaño; i++ ) {

            indice.read(raf);
            System.out.println( "( " + indice.getClave() + ", "
                    + String.format( "%" + NUM_DIGITS + "d", indice.getLiga() )+ " )" );
        }
    }

    public void mostrarArbol () throws IOException {

        if (raiz == null) return;

        System.out.println("\nImprimiendo Arbol\n");

        raiz.mostrar(0);
    }

    public void setRaiz(Nodo nodo) {

        System.out.println( "Raiz es ahora " + nodo.toString() );
        raiz = nodo;
    }


    public int tamaño() throws IOException {
        final RegIndice registro = new RegIndice();
        return (int) raf.length() / registro.length();
    }
}
