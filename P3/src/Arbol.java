import java.io.IOException;
import java.io.RandomAccessFile;

public class Arbol {

    private Nodo raiz;
    private RandomAccessFile raf = null;

    public Arbol(RandomAccessFile raf) {
        this.raf = raf;
    }

    public int tamaño() throws IOException {
        final Registro registro = new Registro();
        return (int) raf.length() / registro.length();
    }

    /**
     * Buscar desde la raiz
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(String clave) throws IOException {
        // TODO

        // Si raiz existe
        return raiz.buscar(clave);
    }


    /**
     * Buscar desde la raiz
     * @param posicion Posicion donde se encuentra el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(int posicion) throws IOException {
        // TODO

        // Si raiz existe
        return raiz.buscar(posicion);
    }

    /**
     * Inserta desde la raiz.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return Posicion global del RegIndice
     */
    public int insertar( String clave )
        throws IOException
    {   
        // TODO

        // Si raiz existe
        int posicion = raiz.insertar( clave );

        // Balancear
        balancear();

        return posicion;
    }
 
    /** 
     * Borrar el RegIndice desde la raiz.
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    public void borrar(String clave) throws IOException
    {   
        // TODO

        // Si raiz existe
        raiz.borrar( clave );

        // Balancear
        balancear();
    }

    /**
     * Modificar la liga desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    public void modificar(int posicion, int liga) throws IOException {
        // TODO

        // Si raiz existe
        raiz.modificar(posicion, liga);
    }

    /**
     * Balancear el arbol en base a N.
     */
    private void balancear() {

        // Se usan los indices para determinar el tamaño ocupado.

        // Si es una raiz, se modifican las claves
    }

    /** 
     * Mostrar el arbol empezando por la raiz.
     */
    public void mostrar() throws IOException {
        // TODO

        // Si la raiz existe,
        raiz.mostrar();
    } 
}
