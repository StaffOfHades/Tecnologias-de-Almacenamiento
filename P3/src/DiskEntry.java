
public class DiskEntry {
	
	public long pointer = 0;
	public DiskEntry child = null;
	public long hashedKey = 0;

	public DiskEntry( long hashedKey, long pointer ) {

		this.hashedKey = hashedKey;
		this.pointer = pointer;
	}
	
	public void addChild(DiskEntry target) {
		this.child = target;
	}
	
	public void removeChild() {
		child = null;
	}
	
}
