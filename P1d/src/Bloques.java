import java.io.*;

public class Bloques {

    private final static int BLOCK_SIZE = 4096;

    private RandomAccessFile raf;

    private byte[] bloque;
    private int usedSize; // Tamaño ocupado
    private int[] indices;
    
    /*---------------------------------------------------------
    /   Constructor
    /---------------------------------------------------------*/

    public Bloques(RandomAccessFile raf) {
        this.raf = raf;
        this.bloque = new byte[BLOCK_SIZE];
        this.usedSize = 0;
        this.indices = new int[0];
    }

    /*---------------------------------------------------------
    /   Getter
    /---------------------------------------------------------*/

    public int length() {
        return indices.size;
    }

    public int getUsedSize() {
        return usedSize;
    }

    public boolean hasSpace(int length) {
        return registro.length + usedSize < BLOCK_SIZE;
    }

    /*---------------------------------------------------------
    /   Setter
    /---------------------------------------------------------*/

    public boolean add(Registro registro) {
        if (registro.length + usedSize < BLOCK_SIZE) {
            add(registro.getBytes());
            addIndex(usedSize);
            usedSize += registro.length;
            return true;
        }
        return false;
    }

    private void add(byte[] registro) {
        registro.addIndex(indices.length);
        for (int i = 0
    }

    private void addIndex(int length) {
        int[] temp = new int[indices.size + 1];
        int i;
        for (i = 0; i < indices.size; i++) {
            temp[i] = indices[i];       
        }
        System.out.println("i: " + i);
        temp[i] = length;
    }

    public void remove 
    
}
