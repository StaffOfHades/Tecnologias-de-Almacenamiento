import java.io.IOException;

public interface Nodo {

    public RegIndice buscar(String clave) throws IOException;
    public RegIndice buscar(int posicion) throws IOException;
    public int insertar(String clave) throws IOException;
    public void modificar( int posicion, int liga ) throws IOException;
    public void borrar(String clave) throws IOException;
    public void mostrar() throws IOException;

}
