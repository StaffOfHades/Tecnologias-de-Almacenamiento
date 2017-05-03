
import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Times {


    public static void main(String[] args) throws IOException {
        
        int num_suc = Integer.parseInt( args[1] );
        
        String path = new File(".").getAbsolutePath();
        String folder = args[0];

        path = path.substring(0, path.length() - 1);

        FileReader input = new FileReader( path + folder + "/time.log" );
        LineNumberReader counter = new LineNumberReader( input );
        counter.skip( Long.MAX_VALUE );
        final int size = counter.getLineNumber();
        counter.close();

        input = new FileReader( path + folder + "/time.log" );
        BufferedReader reader = new BufferedReader( input );
        String line;
        String[] parts;

        double[] times = new double[size];
        int i = 0;

        double[] totalTimes = new double[3];

        while( ( line = reader.readLine() ) != null ) {
            int j = 0;
            boolean found = false;
            parts = line.split( "(:)|(;)|(\\s+)" );
            while( !found ) {
                
                    try {
                    
                    String part = parts[j];
                    if( part.trim().length() > 0 ) {

                        double d = Double.parseDouble( part );
                        times[i] = d;
                        found = true;
                    } else {

                        j++;
                    }
                } catch( NumberFormatException e ) {

                    j++;
                }
            }
            j++;
            int pos = -1;
            if( i == (num_suc - 1) ) {
                pos = 0;
            } else if( i == ( (num_suc * 2) - 1 ) ) {
                pos = 1;
            } else if( i == ( (num_suc * 3) - 1 ) ) {
                pos = 2;
            }
            while( pos >= 0 ) {

                try {

                    String part = parts[j];
                    if( part.trim().length() > 0 ) {

                        double d = Double.parseDouble( part );
                        totalTimes[pos] = d;
                        pos = -1;
                    } else {

                        j++;
                    }
                } catch( NumberFormatException e ) {

                    j++;
                }
            }
            i++;
        }

        double sum = 0;
        for( double time : times ) {
            sum += time;
        }

        double average = sum / size;

        System.out.println( "Average time: " + average );
        System.out.println( "Average total time: " + ( sum / 3 ) );
        System.out.println( "Time try 1: " + totalTimes[0] );
        System.out.println( "Time try 2: " + totalTimes[1] );
        System.out.println( "Time try 3: " + totalTimes[2] );

        reader.close();
        input.close();
    }

}
