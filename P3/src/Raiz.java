
// Faltan los import

import java.io.IOException;

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
        int posicion = SIN_ASIGNAR;

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
        // TODO
        int posicion = SIN_ASIGNAR;

        return nodos[posicion].buscar(posicion_indice);
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
        // TODO
        int posicion = SIN_ASIGNAR;

        return nodos[posicion].insertar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param clave Clave bajo la cual esta el RegIndice.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(String clave, int liga) throws IOException {
        // TODO
        int posicion = SIN_ASIGNAR;

        nodos[posicion].modificar(clave, liga);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     * @param liga Direccion a donde apunta la nueva liga.
     */
    @Override
    public void modificar(int posicion_indice, int liga) throws IOException {
        // TODO
        int posicion = SIN_ASIGNAR;

        nodos[posicion].modificar(posicion_indice, liga);
    }

    /** 
     * Busca linealmente en que hoja debe ser borrado
     * @param clave Clave bajo la cual se debe encontrar el registro.
     */
    @Override
    public void borrar(String clave) throws IOException
    {   
        // TODO
        int posicion = SIN_ASIGNAR;

        nodos[posicion].borrar(clave);
    }

    /**
     * Busca linealmente en que hoja debe ser modificada la liga
     * @param posicion_indice Posicion global del RedIndice dentro del archivo.
     */
    @Override
    public void borrar(int posicion_indice) throws IOException {
        // TODO
        int posicion = SIN_ASIGNAR;

        nodos[posicion].borrar(posicion_indice);
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
}

