
public class DEntryOld
{
	/*
	 * --------------------------------------------------------
	 * Infrastructure------------------------------------------
	 * --------------------------------------------------------
	 */
	
	public long pointerToDisk = 0;
	public DEntryOld child = null;
	public long hashedKey = 0;
	
	public void addChild(DEntryOld target)
	{
		this.child = target;
	}
	
	public void removeChild()
	{
		child = null;
	}
	
	public DEntryOld(long hashedKey, long pointerToDisk)
	{
		this.hashedKey = hashedKey;
		this.pointerToDisk = pointerToDisk;
	}
	
}
