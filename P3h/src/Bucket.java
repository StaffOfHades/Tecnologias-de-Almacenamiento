
import java.util.ArrayList;
import java.util.List;

public class Bucket {

	public DiskEntry[] entryList;
	public int size = 0;
	public int counter = 0;
	public int bitDepth = 0;

	public List<Hash> hashList = new ArrayList<Hash>();

    public Bucket(int size) {

        this.size = size;
        entryList = new DiskEntry[size];
    }
	
	/*
	 * Returns false if the bucket is full, otherwise inserts at the end and returns true.
	 */
	public boolean add(DiskEntry target) {

		if( counter >= size )
			return false;
		else {

			entryList[counter++] = target;
			return true;
		}
	}
	
	/*
	 * Returns false if the bucket is empty, otherwise deletes target and returns true.
	 */
	public boolean remove(DiskEntry target) {

		if( counter <= 0 )
			return false;
		else {

            int i = -1;
            int max = entryList.length;
            boolean seguir = false;
            while( !seguir && ++i < max)
                seguir = entryList[i] == target;

            if (seguir) {
                entryList[i] = null;
                return true;
            }
            return false;
		}
	}
	
	/*
	 * It receives a binary, and will attempt to find something similar in itself.
	 * If it finds it, it will return it. Otherwise it will just return null.
	 */
	public DiskEntry find(int key) {

        int i = -1;
        int max = entryList.length;
        boolean seguir = false;
        while( !seguir && ++i < max)
            seguir = entryList[i].hashedKey == key;

        return seguir ? entryList[i] : null;
	}
	
	public void add(Hash hash) {
		hashList.add(hash);
	}
	
	
	public void remove(Hash hash) {
		hashList.remove(hash);
	}
	
	public void flushCounter() {
		counter = 0;
	}
}
