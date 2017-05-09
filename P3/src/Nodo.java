
public interface Nodo {

    public RegIndice buscar(String clave) throws IOException;
    public void insertar( String clave, int liga ) throws IOException;
    public void borrar(String clave) throws IOException;
    public void mostrar() throws IOException;

}
