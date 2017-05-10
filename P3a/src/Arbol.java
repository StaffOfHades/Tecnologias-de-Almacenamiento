import java.io.IOException;
import java.io.RandomAccessFile;

public class Arbol implements Constants {

    private Nodo raiz;
    private RandomAccessFile raf = null;

    public Arbol(RandomAccessFile raf) {
        this.raf = raf;
    }

    public int tamaño() throws IOException {
        final RegIndice registro = new RegIndice();
        return (int) raf.length() / registro.length();
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
     * Buscar desde la raiz
     * @param posicion Posicion donde se encuentra el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(int posicion) throws IOException {
        return raiz == null ? null : raiz.buscar(posicion);
    }

    /**
     * Inserta desde la raiz.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    public int insertar( String clave )
        throws IOException
    {
        if( raiz == null )
            raiz = new Hoja(this, null, raf);

        // Recuperar la posicion,
        int posicion = raiz.insertar( clave );

        // Y regresar la posicion.
        return posicion;
    }
 
    /** 
     * Borrar el RegIndice desde la raiz.
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    public void borrar(String clave) throws IOException
    {   
        if( raiz == null ) return;

        // Si la raiz existe, intenta borra
        raiz.borrar( clave );
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
     * Modificar la liga desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    public void modificar(int posicion, int liga) throws IOException {

        if( raiz == null ) return;

        // Si la raiz existe, intenta modificar
        raiz.modificar(posicion, liga);
    }

    /**
     * Borrar todos los RegIndice desde la raiz.
     */
    public void borrar() throws IOException {

        if( raiz == null ) return;
        // TODO

        raf.setLength(0);
    }

    /**
     * Borrar el RegIndice desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    public void borrar(int posicion) throws IOException
    {
        if( raiz == null ) return;

        // Si raiz existe, intenta borrar
        raiz.borrar(posicion);
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

    /**
     * Cerrar el acceso de archivo para todas las hojas
     */
    public void cerrar() throws IOException {
        if (raiz != null)
            raiz.cerrar();
    }

    public void setRaiz(Nodo nodo) {
        raiz = nodo;
    }
}
