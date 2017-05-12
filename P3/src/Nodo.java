import java.io.IOException;

public interface Nodo {

    void setPadre(Raiz raiz);

    void borrar() throws IOException;
    void borrar(String clave) throws IOException;
    RegIndice buscar(String clave) throws IOException;
    int insertar(String clave) throws IOException;
    void insertar(RegIndice indice) throws IOException;
    void modificar(String clave, int liga) throws IOException;
    void mostrar(int nivel) throws IOException;

}
