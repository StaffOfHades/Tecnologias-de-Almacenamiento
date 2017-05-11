
public class Hash {

	public Bucket bucket = null;

    public boolean active = false;
	public int id = 0;
	public int depth = 0;
	
	public static int binaryCounter = 0;

    public Hash(Bucket bucket) {

        this.bucket = bucket;

        if( binaryCounter == 0 )
            active = true;

        id = binaryCounter++;
        depth = calculateDepth();
    }
	
	private int calculateDepth() {

        int depth;

        if(id == 0)
            depth = 1;
        else {

            int value = id;
            int count = 0;
            while( value > 0 ) {

                count++;
                value = value >> 1;
            }
            depth = count;
        }

        return depth;
	}

}
