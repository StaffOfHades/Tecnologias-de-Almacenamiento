
// Faltan los import

public class Raiz implements Constants, Nodo {

    private Nodo[] nodos = new Nodo[N + 1];
    private String[] claves = new String[N];
    private int indiceMax = 0; // (N - 1) / 2 < indiceMax < N
    private Raiz padre;

    public Raiz( Raiz padre ) {
        this.padre = padre;
    }

    /**
     * Busca linealmente en cual hoja se podria encontrar la clave indicada,
     * y le pida a la hoja que lo busque.
     * @param clave Clave bajo la cual esta el RegIndice.
     * @return El RegIndice si fue encontrado exitosamente, o nulo si no.
     */
    @Override
    public RegIndice buscar(String clave) throws IOException {
        // TODO
        int posicion;

        return nodos[posicion].buscar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser insertado la clave indicada 
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Liga a la posicion del primer Registro bajo la clave.
     */
    @Override
    public void insertar( String clave, int liga )
        throws IOException
    {   
        // TODO
        int posicion;

        return nodos[posicion].insertar( clave, liga );            
    }
    
    /** 
     * Borrar el RegIndice que exista bajo la clave.
     * @param posicion Posicion global del RedIndice dentro del archivo.
     */
    @Override
    public void borrar(String clave) throws IOException
    {   
        // TODO
        int posicion;

        return nodos[posicion].borrar(clave);
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

}

