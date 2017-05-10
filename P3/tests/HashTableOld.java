import java.util.ArrayList;
import java.util.List;


public class HashTableOld
{
	/*
	 * --------------------------------------------------------
	 * Infrastructure------------------------------------------
	 * --------------------------------------------------------
	 */
	private int bucketSize = 8;
	private HashOld[] hashTable = new HashOld[1];
	private int headerBitDepth = 0;
	
	public HashTableOld()
	{
		//All in memory phase.
		if(hashTable[0] == null)
		{
			hashTable = new HashOld[1];
			BucketOld firstBucket = new BucketOld(bucketSize);
			hashTable[0] = new HashOld(firstBucket);
		}
		else
		{
			hashTable = new HashOld[1];
			BucketOld firstBucket = new BucketOld(bucketSize);
			hashTable[0] = new HashOld(firstBucket);
		}
	}
	
	/*
	 * --------------------------------------------------------
	 * Handlers------------------------------------------------
	 * --------------------------------------------------------
	 */
	
	/*
	 * Main algorithm for seeking:
	 */
	public long seek(String rawQuery)
	{
		//Hashing: String to int
		long query = hashFunction(rawQuery);
		//Finding hash:
		HashOld owner = findClosestMatchFromActiveHash(query);
		BucketOld bucky = owner.bucket;
		//Linear search on bucket
		for(int c = 0; c < bucky.size; c++)
		{
			if(bucky.entryList[c].hashedKey == query)
			{
				System.out.println("Data found at disk " + bucky.entryList[c].pointerToDisk);
				return bucky.entryList[c].pointerToDisk;
			}
		}
		System.out.println("Data not found");
		return -1;
	}
	
	//Develop for disk application directly:
	public void deleteIgnore(int query)
	{
		
	}
	
	/*
	 * Main algorithm for insertion.
	 */
	public void insert(String rawKey, long diskAddress)
	{
		long key = hashFunction(rawKey);
		DEntryOld target = new DEntryOld(key, diskAddress);
		HashOld closest = findClosestMatchFromActiveHash(target.hashedKey);
		if (checkHashBucketForDoppelgangers(closest, target))
		{
			return;
		}
		else
		{
			if (cleanInsertAtHash(closest, target))
			{
				return;
			}
			else
			{
				if(expandHashTable(closest))
				{
					insert(target);
				}
				else
				{
					HashOld child = activateAndConnectHash(closest);
					if(reorderContents(closest, child))
					{
						insert(target);
					}
					else
					{
						insert(target);
					}
				}
			}
		}
	}
	
	private void insert(DEntryOld target)
	{
		
		HashOld closest = findClosestMatchFromActiveHash(target.hashedKey);
		if (checkHashBucketForDoppelgangers(closest, target))
		{
			return;
		}
		else
		{
			if (cleanInsertAtHash(closest, target))
			{
				return;
			}
			else
			{
				if(expandHashTable(closest))
				{
					insert(target);
				}
				else
				{
					HashOld child = activateAndConnectHash(closest);
					if(reorderContents(closest, child))
					{
						insert(target);
					}
					else
					{
						insert(target);
					}
				}
			}
		}
	}
	
	public void printHashTable()
	{
		System.out.println(hashTable.length);
		for(int h = 0; h < hashTable.length; h++)
		{
			HashOld activeHash = hashTable[h];
			System.out.print("Hash " + activeHash.binaryId +": ");
			for(int c = 0; c < activeHash.bucket.size; c++)
			{
				//Sentry
				if(activeHash.bucket.entryList[c] == null)
				{
					continue;
				}
				else
				{
					System.out.print(activeHash.bucket.entryList[c].hashedKey +", ");
				}
			}
			System.out.println();
		}
	}
	
	public void simulateTest()
	{
		//Testing creation
		int[] testingValues = {2, 4, 1, 5, 3};
		HashOld.binaryCounter = 0;
		hashTable = new HashOld[4];
		int iCounter= 1;
		for(int c = 0; c <  4; c++)
		{
			
			BucketOld bucky = new BucketOld(2);
			hashTable[c] = new HashOld(bucky);
			hashTable[c].active = true;
			if(c == 2)
			{
				hashTable[c].active = false;
				continue;
			}
			for(int n = 0; n < hashTable[0].bucket.size; n++)
			{
				
				DEntryOld t1 = new DEntryOld(testingValues[iCounter-1], 0);
				iCounter++;
				hashTable[c].bucket.addToBucket(t1);
				System.out.println("c " + c + ", n " + n + ", t1 " + t1.hashedKey);
				
				if(c == 3)
				{
					break;
				}
			}
			
		}
		
		
		//Testing
		//System.out.print(findClosestMatchFromActiveHash(2).binaryId);
		
		//DEntry test = new DEntry(11, 0);
		//System.out.println(checkHashBucketForDoppelgangers(findClosestMatchFromActiveHash(test.hashedKey), test));
		
		//System.out.println(cleanInsertAtHash(findClosestMatchFromActiveHash(test.hashedKey), test));
		
		headerBitDepth = 2;
		hashTable[0].bucket.headerBitDepth = 1;
		hashTable[1].bucket.headerBitDepth = 2;
		hashTable[2].bucket.headerBitDepth = 1;
		hashTable[3].bucket.headerBitDepth = 2;
		System.out.println(
			expandHashTable(hashTable[1])
				);
		for(int c = 0; c < hashTable.length; c++)
		{
			System.out.print("Hash num " + hashTable[c].binaryId +": ");
			for(int j = 0; j < hashTable[c].bucket.size; j++)
			{
				if(hashTable[c].bucket.entryList[j] != null)
					System.out.print(hashTable[c].bucket.entryList[j].hashedKey + ", ");
			}
			System.out.println();
		}
		activateAndConnectHash(hashTable[1]);
		
		//Checking all dependencies.
		for(int c = 0; c < hashTable.length; c++)
		{
			if(!hashTable[c].active)
			{
				System.out.println(hashTable[c].binaryId +" is a dependency");
			}
		}
		
		System.out.println(hashTable[5].bucket.topCounter);
		
	}
	
	
	/*
	 * --------------------------------------------------------
	 * Internal------------------------------------------------
	 * --------------------------------------------------------
	 */
	
	public long hashFunction(String subject)
	{
		long hash = 0;
		
			//Char counter
			for (int i=0; i < subject.length(); i++) 
			{
				long cha=(long)subject.charAt(i);
	    		hash =(hash + (subject.charAt(i)) + (cha*i));
			}
			return hash;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private HashOld findClosestMatchFromActiveHash(long binary)
	{
		List<HashOld> activeList = new ArrayList<HashOld>();
		for(int c = 0; c < hashTable.length; c++)
		{
			if(hashTable[c].active == true)
			{
				activeList.add(hashTable[c]);
			}
		}
		
		//Finding max bit bitDepth
		int bitDepth = 0;
		for(int c = 0; c < activeList.size(); c++)
		{
			if(activeList.get(c).bitDepth > bitDepth)
			{
				bitDepth = activeList.get(c).bitDepth;
			}
		}
		
		//Getting binary targets of "binary"
		long input = binary;
	    int sSize = bitDepth;
	    boolean[] binaryBits = new boolean[sSize];
	    for (int i = sSize - 1; i >= 0; i--) {
	        binaryBits[i] = (input & (1 << i)) != 0;
	    }
	    
	    //Comparing with elements of the activeList to find equal on ascendant order.
	    int[] correctness = new int[activeList.size()];
	    for(int c = 0; c < activeList.size(); c++)
	    {
	    	correctness[c] = 0;
	    	HashOld active = activeList.get(c);
	    	//Getting bit chain from active
	    	int input2 = active.binaryId;
		    int sSize2 = active.bitDepth;
		    boolean[] bits = new boolean[sSize2];
		    for (int i = sSize2 - 1; i >= 0; i--) {
		        bits[i] = (input2 & (1 << i)) != 0;
		    }
		    
		    //Comparing for not wrong check of this level.
		    for(int i = 0; i < bits.length; i++)
		    {
		    	
		    	if(bits[i] != binaryBits[i])
		    	{
		    		break;
		    	}
		    	correctness[c]++;
		    }
	    }
	    
	    //Chosing the largest active that is not wrong
	    
	    int topSize = -1;
	    HashOld winner = null;
	    
	    for(int g = 0; g < correctness.length; g++)
	    {
	    	if(correctness[g] > topSize)
	    	{
	    		topSize = correctness[g];
	    		winner = activeList.get(g);
	    	}
	    }
	    
		return winner;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private boolean checkHashBucketForDoppelgangers(HashOld hash, DEntryOld insertTarget)
	{
		BucketOld target = hash.bucket;
		
		
		
		for(int c = 0; c < target.entryList.length; c++)
		{
			//Sentry:
			if(target.entryList[c] == null)
			{
				break;
			}
			//Continuing:
			if(target.entryList[c].hashedKey == insertTarget.hashedKey)
			{
				target.entryList[c].child = insertTarget;
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Completion status: COMPLETE
	 * Verification status: VERIFIED
	 */
	private boolean cleanInsertAtHash(HashOld hash, DEntryOld entryTarget)
	{
		BucketOld bucky = hash.bucket;
		if(bucky.topCounter >= bucky.size)
		{
			return false;
		}
		else
		{
			bucky.addToBucket(entryTarget);
			return true;
		}
	}
	
	private boolean expandHashTable(HashOld originator)
	{
		if(originator.bucket.headerBitDepth >= this.headerBitDepth)
		{
			//Left shifting:
			int limiter = hashTable.length << 1;
			//Creating new vessel array for expanded hash table:
			HashOld[] newVessel = new HashOld[limiter];
			for(int c = 0; c < hashTable.length; c++)
			{
				newVessel[c] = hashTable[c];
			}
			for(int c = hashTable.length; c < limiter; c++)
			{
				newVessel[c] = new HashOld(newVessel[c - (limiter/2)].bucket);
				//Registering dependency:
				newVessel[c-(limiter/2)].bucket.addDependency(newVessel[c]);
			}
			hashTable = newVessel;
			//Raising hash table header bit bitDepth by +1
			headerBitDepth++;
			//returning true
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private HashOld activateAndConnectHash(HashOld originator)
	{
		//Creating schism and activating it with its own new bucket:
		HashOld schism = originator.bucket.dependencies.get(0);
		originator.bucket.removeDependency(schism);
		schism.bucket = new BucketOld(bucketSize);
		schism.active = true;
		//Updating originator and schism buckets by +1:
		originator.bucket.headerBitDepth++;
		schism.bucket.headerBitDepth = originator.bucket.headerBitDepth;
		//Updating originator dependencies and assigning new master to dependency:
		for(int c = 0; c < originator.bucket.dependencies.size(); c++)
		{
			HashOld dependency = originator.bucket.dependencies.get(0);
			HashOld newMaster =
					findClosestMatchFromActiveHash(dependency.binaryId);
			dependency.bucket = newMaster.bucket;
			originator.bucket.removeDependency(dependency);
			newMaster.bucket.addDependency(dependency);
		}
		
		return schism;
	}
	
	private boolean reorderContents(HashOld originator, HashOld child)
	{
		//Checking if there is content jump between buckets:
		boolean proceed = false;
		for(int c = 0; c < originator.bucket.size; c++)
		{
			//Sentry:
			if(originator.bucket.entryList[c] == null)
			{
				break;
			}
			else if(
					findClosestMatchFromActiveHash(originator.bucket.entryList[c].hashedKey) == child
							)
			{
				proceed = true;
				break;
			}
		}
		//Proceeding ?: bucket jump
		if(proceed)
		{
			//creating buffer:
			DEntryOld[] buffer = new DEntryOld[originator.bucket.size];
			//filling buffer:
			for(int c = 0; c < originator.bucket.size; c++)
			{
				//passing data to buffer:
				buffer[c] = originator.bucket.entryList[c];
				//flushing originator bucket:
				originator.bucket.entryList[c] = null;
			}
			originator.bucket.flushTopCounter();
			//Distributing buffer to originator bucket and child bucket
			for(int c = 0; c < buffer.length; c++)
			{
				HashOld newContainer = findClosestMatchFromActiveHash(buffer[c].hashedKey);
				newContainer.bucket.addToBucket(buffer[c]);
			}
			return true;
		}
		//No bucket jump: returning false
		return false;
	}
	
}
