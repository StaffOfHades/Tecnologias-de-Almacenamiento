
// Import

public class ArbolB+ {

    private Nodo raiz;

     /**
     * Buscar desde la raiz
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    public RegIndice buscar(String clave) throws IOException {
        // TODO

        // Si raiz existe
        raiz.buscar(clave);
    }

    /**
     * Inserta desde la raiz.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Liga a la posicion del primer Registro bajo la clave.
     */
    public void insertar( String clave, int liga )
        throws IOException
    {   
        // TODO

        // Si raiz existe
        raiz.insertar( clave, liga );

        // Balancear
        balancear();
    }
 
    /** 
     * Borrar el RegIndice desde la raiz.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    public void borrar(String clave) throws IOException
    {   
        // TODO

        // Si raiz existe
        raiz.borrar( clave, liga );

        // Balancear
        balancear();
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
    @Override
    public void mostrar() throws IOException {
        // TODO

        // Si la raiz existe,
        raiz.mostrar();
    } 
}
