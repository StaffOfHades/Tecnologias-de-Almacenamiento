import java.io.IOException;

public interface Nodo {

    RegIndice buscar(String clave) throws IOException;
    RegIndice buscar(int posicion) throws IOException;
    int insertar(String clave) throws IOException;
    void modificar(String clave, int liga) throws IOException;
    void modificar(int posicion, int liga) throws IOException;
    void borrar(String clave) throws IOException;
    void borrar(int posicion) throws IOException;
    void mostrar() throws IOException;
    void cerrar() throws IOException;
    Nodo getPadre();
    void setPadre(Raiz raiz);
}
