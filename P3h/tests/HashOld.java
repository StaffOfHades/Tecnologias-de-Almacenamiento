
public class HashOld
{
	/*
	 * --------------------------------------------------------
	 * Infrastructure------------------------------------------
	 * --------------------------------------------------------
	 */
	
	public BucketOld bucket = null;
	public int binaryId = 0;
	public boolean active = false;
	public int bitDepth = 0;
	
	public static int binaryCounter = 0;
	
	private void determineBitDepth()
	{
		int value = binaryId;
		int count = 0;
		while (value > 0) {
		    count++;
		    value = value >> 1;
		}
		
		bitDepth = count;

		if(binaryId == 0)
		{
			bitDepth = 1;
		}
	}
	
	public HashOld(BucketOld bucket)
	{
		binaryId = binaryCounter;
		if(binaryCounter == 0)
			active = true;
		determineBitDepth();
		this.bucket = bucket;
		binaryCounter ++;
	}
}
