
// Faltan los import

public class Hoja implements Constants, Nodo {

    private Hoja der
    private Hoja izq;
    private Raiz padre;
    private int indiceMax = 0; // (N - 1) / 2 < indiceMax < N
    private int inicio; // Posicion donde iniciar la busqueda dentro del archivo
    private int fin; // Posicion donde finaliza la busqueda dentro del archivo

    private RandomAccessFile raf;

    public Hoja(Raiz padre, RandomAccessFile raf) {

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
        // TODO

        // Busqueda va de inicio a fin
        return null;
    }

    /**
     * Busca linealmente dentro de los limites si existe un registro bajo 
     * la clave indicada, y lo crea si no.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Liga a la posicion del primer Registro bajo la clave.
     */
    @Override
    public void insertar( String clave, int liga )
        throws IOException
    {
        // TODO

        // Busqueda va de inicio a fin
        int posicion;

        insertarEn( clave, posicion, liga );
    }

    /**
     * Inserte la clave en la posicion dada, agregando la liga,
     * y moviendo los registro para crear espacio.
     * @param clave Clave bajo la cual esta el RegIndice
     * @param liga Liga a la posicion del primer Registro bajo la clave.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    private void insertarEn( String clave, int liga,
                            int posicion ) throws IOException
    {
        // TODO 
        
        // Se debe actualizae inicio y fin
    }

    /**
     * Borrar el RegIndice si existe bajo una clave.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    public void borrar(String clave) throws IOException
    {
        // TODO
        int posicion;

        borrarEn(posicion);
    }

    /**
     * Borrar el RegIndice en la posicion dada.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    private void borrarEn(int posicion) throws IOException
    {
        // TODO

        // Se debe actualizar inicio y fin
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
    public void cerrar() throws IOException {
        raf.close();
    }
}
