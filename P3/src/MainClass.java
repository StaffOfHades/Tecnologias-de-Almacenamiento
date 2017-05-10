import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class MainClass {

    public static HashTable create() throws IOException {
        Random rand = new Random();

        /*
        File file = new File( "Hash.Info" );
        RandomAccessFile raf = new RandomAccessFile( file, "rw" );
        */

        HashTable table = new HashTable();
        final int max = 9000;
        final int min = 1000;
        for(int i = 0; i <= 8; i++) {
            int randomNum = rand.nextInt( (max - min) + 1) + min;
            System.out.println("Inserseting  sucursal " + i);
            table.insert( ( "Sucursal " + i ), randomNum );
        }
        System.out.println("Finished creation");

        return table;

    }

    public static void show(HashTable table) throws IOException {
        /*
        File file = new File( "Hash.Info" );
        RandomAccessFile raf = new RandomAccessFile( file, "rw" );
        */

        table.printHashTable();
    }

	public static void main(String args[]) throws IOException {


		show( create() );
	}
}
