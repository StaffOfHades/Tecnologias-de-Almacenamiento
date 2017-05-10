

import java.io.IOException;
import java.io.RandomAccessFile;

public class Hoja implements Constants, Nodo {

    private Hoja der;
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
     *  Si la posicion existe, regresa el registro.
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     * @return El RegIndice si existe.
     */
    @Override
    public RegIndice buscar(int posicion_indice) throws IOException {
        // TODO

        return null;
    }

    /**
     * Busca linealmente dentro de los limites si existe un registro bajo 
     * la clave indicada, y lo crea si no.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    @Override
    public int insertar( String clave )
        throws IOException
    {
        // TODO

        // Busqueda va de inicio a fin
        int posicion = SIN_ASIGNAR;

        insertarEn( clave, posicion );

        return posicion;
    }


    /**
     * Busca linealmente dentro de los limites si existe un registro bajo
     * la clave indicada, y lo modifica si si.
     * @param clave Clave bajo la cual esta el RegIndice
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(String clave, int liga) throws IOException {
        // TODO

        // Busqueda va de inicio a fin
        int posicion = SIN_ASIGNAR;

        modificar(posicion, liga);
    }

    /**
     * Si la posicion existe, modifica la liga.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(int posicion, int liga) throws IOException {
        // TODO
    }

    /**
     * Borrar el RegIndice si existe bajo una clave.
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    public void borrar(String clave) throws IOException
    {
        // TODO
        int posicion = SIN_ASIGNAR;

        borrarEn(posicion);
    }

    /**
     * Borrar el RegIndice en la posicion especificada
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    @Override
    public void borrar(int posicion) throws IOException {
        borrarEn(posicion);
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

    /**
     * Inserte la clave en la posicion dada, agregando la liga,
     * y moviendo los registro para crear espacio.
     * @param clave Clave bajo la cual esta el RegIndice
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    private void insertarEn( String clave, int posicion )
            throws IOException
    {
        // TODO

        // Se debe actualizar inicio y fin
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
}
