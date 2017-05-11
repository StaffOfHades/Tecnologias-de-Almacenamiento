import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;


public class MainClassOld
{
	public static RandomAccessFile raf;
	public static void main(String args[])
	{
		Random rand = new Random();
		try{
			File file = new File( "Hash.Info" );
			raf = new RandomAccessFile( file, "rw" );
		}catch(IOException e){e.printStackTrace();} 
		
		HashTableOld hTable = new HashTableOld();
		for(int s = 0; s <= 100; s++)
		{
			int max = 9000;
			int min = 1000;
			int randomNum = rand.nextInt((max - min) + 1) + min;
			hTable.insert(("Sucursal " + s), randomNum);
		}
		hTable.printHashTable();
		
		
		
	}
}
