import java.util.ArrayList;
import java.util.List;


public class BucketOld {
	/*
	 * --------------------------------------------------------
	 * Infrastructure------------------------------------------
	 * --------------------------------------------------------
	 */
	
	public DEntryOld[] entryList;
	public int size = 0;
	
	public int topCounter = 0;
	public int headerBitDepth = 0;
	
	public List<HashOld> dependencies = new ArrayList<HashOld>();
	
	/*
	 * Returns false if the bucket is full, otherwise inserts at the end and returns true.
	 */
	public boolean addToBucket(DEntryOld target)
	{
		//Size overflow
		if(topCounter >= size)
		{
			return false;
		}
		//Regular insert
		else
		{
			entryList[topCounter] = target;
			topCounter++;
			return true;
		}
	}
	
	/*
	 * Returns false if the bucket is empty, otherwise deletes target and returns true.
	 */
	public boolean removeFromBucket(DEntryOld target)
	{
		if(topCounter <= 0)
		{
			return false;
		}
		else
		{
			for(int c = 0; c < entryList.length; c++)
			{
				if(entryList[c] == target)
				{
					entryList[c] = null;
					return true;
				}
			}
			return false;
		}
	}
	
	/*
	 * It receives a binary, and will attempt to find something similar in itself.
	 * If it finds it, it will return it. Otherwise it will just return null.
	 */
	public DEntryOld BinaryFindInBucket(int target)
	{
		for(int c = 0; c < entryList.length; c++)
		{
			//Found
			if(entryList[c].hashedKey == target)
			{
				return entryList[c];
			}
			
		}
		//Not found
		return null;
	}
	
	public void addDependency(HashOld hash)
	{
		dependencies.add(hash);
	}
	
	
	public void removeDependency(HashOld hash)
	{
		dependencies.remove(hash);
	}
	
	public void flushTopCounter()
	{
		topCounter = 0;
	}
	
	public BucketOld(int size)
	{
		this.size = size;
		entryList = new DEntryOld[size];
	}
}
