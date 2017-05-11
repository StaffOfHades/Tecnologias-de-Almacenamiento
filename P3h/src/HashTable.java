import java.util.ArrayList;
import java.util.List;

public class HashTable implements Constants {

	private Hash[] hashTable = new Hash[1];
    private int bitDepth = 0;
	
	public HashTable() {

		Bucket firstBucket = new Bucket(bucketSize);
		hashTable[0] = new Hash(firstBucket);
	}
	
	/*
	 * Main algorithm for seeking:
	 */
	public long search(String rawQuery) {

		//Hashing: String to int
		long query = hashFunction(rawQuery);

		//Finding hash:
		Hash owner = find(query);
		Bucket bucket = owner.bucket;

		//Linear search on bucket
        int i = -1;
        int max = bucket.size;
        boolean seguir = true;
        while( seguir && ++i < max)
            seguir = bucket.entryList[i].hashedKey == query;

        if (!seguir) {
            System.out.println( "Data found at disk " + bucket.entryList[i].pointer );
            return bucket.entryList[i].pointer;
        }

        System.out.println("Data not found");
        return SIN_ASIGNAR;
	}
	
	//Develop for disk application directly:
	public void delete(int query) {
		// TODO
	}
	
	/*
	 * Main algorithm for insertion.
	 */
	public void insert( String rawKey, long diskAddress ) {
		insert(new DiskEntry( hashFunction(rawKey), diskAddress ));
	}

	private void insert(DiskEntry target) {

        Hash closest = find(target.hashedKey);

        if(checkHashBucketForDoppelgangers( closest, target ))
            return;
        else if (cleanInsertAtHash( closest, target ))
            return;
        else if (expandHashTable(closest))
            insert(target);
        else {
            reorderContents( closest, activateAndConnectHash(closest) );
            insert(target);
        }
	}
	
	public void printHashTable() {

        final int max = hashTable.length;
		System.out.println("Max: " + max);
		for(int i = 0; i < max; i++) {

			Hash activeHash = hashTable[i];
			System.out.print("Hash " + activeHash.id + ": " );

            final int size = activeHash.bucket.size;

			for(int j = 0; j < size; j++)
				if( activeHash.bucket.entryList[j] != null )
					System.out.print( activeHash.bucket.entryList[j].hashedKey + ", " );
			System.out.println();
		}
	}

	
	private long hashFunction(String subject) {

        long hash = 0;
        final int max = subject.length();

        for (int i = 0; i < max; i++) {

            long character = (long) subject.charAt(i);
            hash += subject.charAt(i) + ( character * i );
        }
        return hash;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private Hash find(long key) {

		List<Hash> activeList = new ArrayList<>();
        for( Hash table : hashTable )
            if( table.active )
                activeList.add(table);
		
		//Finding max bit bitDepth
		int depth = 0;
        for (Hash hash : activeList)
            if( hash.depth > depth )
                depth = hash.depth;
		
		//Getting binary targets of "binary"
        int size = depth;
	    boolean[] binaryBits = new boolean[size];
	    for( int i = size - 1; i >= 0; i-- )
	        binaryBits[i] = ( key & ( 1 << i ) ) != 0;
	    
	    //Comparing with elements of the activeList to find equal on ascendant order.
	    int[] correctness = new int[activeList.size()];
	    for(int i = 0; i < activeList.size(); i++) {

	    	correctness[i] = 0;
	    	Hash active = activeList.get(i);

	    	//Getting bit chain from active
	    	int input = active.id;
		    size = active.depth;
		    boolean[] bits = new boolean[size];

		    for (int j = size - 1; j >= 0; j--)
		        bits[j] = (input & (1 << j)) != 0;
		    
		    //Comparing for not wrong check of this level.
            /*
		    for( int j = 0; j < max; j++ )
		    	if( bits[j] == binaryBits[j] )
                    correctness[j]++;

             for(int j = 0; j < bits.length; j++) {

                if(bits[j] != binaryBits[j])
                    break;
                correctness[i]++;
            }
             */
            //Comparing for not wrong check of this level.
            final int max = bits.length;
            boolean igual = true;
            int j = -1;
            while( igual && ++j < max) {
                igual = bits[j] == binaryBits[j];
                if(igual)
                    correctness[i]++;
            }
	    }
	    
	    //Chosing the largest active that is not wrong
	    int topSize = -1;
	    Hash winner = null;
	    int max = correctness.length;

	    for( int i = 0; i < max; i++ ) {

            if( correctness[i] > topSize ) {

	    		topSize = correctness[i];
	    		winner = activeList.get(i);
	    	}
	    }
	    
		return winner;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private boolean checkHashBucketForDoppelgangers(Hash hash,
                                                    DiskEntry target)
	{
        //Checking if there is content jump between buckets:
        Bucket bucket = hash.bucket;
        boolean proceed = false;
        int i = -1;
        final int max = bucket.entryList.length;
        while( !proceed && ++i < max ) {

            if( bucket.entryList[i] == null )
                i = max;
            else
                proceed = bucket.entryList[i].hashedKey == target.hashedKey;
        }

        if( proceed )
            bucket.entryList[i].child = target;

        return proceed;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private boolean cleanInsertAtHash( Hash hash, DiskEntry target ) {

		Bucket bucket = hash.bucket;

		if(bucket.counter >= bucket.size)
			return false;
		else
			bucket.add(target);

        return true;
	}
	
	private boolean expandHashTable(Hash originator) {

		if(originator.bucket.bitDepth >= bitDepth) {

			//Left shifting:
			final int limiter = hashTable.length << 1;

			//Creating new vessel array for expanded hash table:
			final Hash[] table = new Hash[limiter];
            System.arraycopy(hashTable, 0, table, 0, hashTable.length);

			for(int i = hashTable.length; i < limiter; i++) {
                final int index = i - ( limiter / 2);
				table[i] = new Hash(table[index].bucket);
				//Registering dependency:
				table[index].bucket.add(table[i]);
			}
			hashTable = table;

			//Raising hash table header bit bitDepth by +1
			bitDepth++;

			//returning true
			return true;
		}
        return false;
	}
	
	private Hash activateAndConnectHash(Hash originator) {

		//Creating schism and activating it with its own new bucket:
		Hash schism = originator.bucket.hashList.get(0);
		originator.bucket.remove(schism);
		schism.bucket = new Bucket(bucketSize);
		schism.active = true;
		//Updating originator and schism buckets by +1:
		schism.bucket.bitDepth = ++originator.bucket.bitDepth;
		//Updating originator hashList and assigning new master to dependency:
		for(int i = 0; i < originator.bucket.hashList.size(); i++) {
			Hash hash = originator.bucket.hashList.get(0);
			Hash parent = find(hash.id);
			hash.bucket = parent.bucket;
			originator.bucket.remove(hash);
			parent.bucket.add(hash);
		}
		
		return schism;
	}
	
	private boolean reorderContents( Hash originator, Hash child ) {

		//Checking if there is content jump between buckets:
		boolean proceed = false;
        int i = -1;
        final int max = originator.bucket.size;
        while( !proceed && ++i < max ) {

            if( originator.bucket.entryList[i] == null )
                i = max;
            else
                proceed = find(originator.bucket.entryList[i].hashedKey) == child;
        }


		if(proceed) {

			//creating buffer:
			DiskEntry[] buffer = new DiskEntry[originator.bucket.size];

			//filling buffer:
			for( i = 0; i < originator.bucket.size; i++ ) {

				//passing data to buffer:
				buffer[i] = originator.bucket.entryList[i];

				//flushing originator bucket:
				originator.bucket.entryList[i] = null;
			}

			originator.bucket.flushCounter();
			//Distributing buffer to originator bucket and child bucket
			for( i = 0; i < buffer.length; i++ ) {

				Hash container = find(buffer[i].hashedKey);
				container.bucket.add(buffer[i]);
			}
			return true;
		}

		return false;
	}
	
}
