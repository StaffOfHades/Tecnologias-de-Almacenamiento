/******************************************************************
/  clase: Indice
/
/  autor: Dr. JosŽ Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class RegIndice implements Constants {
    
	private byte[] clave = new byte[STR_SIZE];
	private int liga;
    
    /*-----------------------------------------------------------------
    / mŽtodos getters/setters
    /-----------------------------------------------------------------*/
    
	public String getClave() {

        return new String( clave ).replace("\0","");
	}
    
	public void setClave( String valor ) {

        if( valor.length() > clave.length ) {
            System.err.println( "ATENCION: Clave con mas de " + STR_SIZE + " caracteres" );
        }
        
		byte[] v = valor.getBytes();
        
		for( int i = 0; i < clave.length && i < v.length; i++ )
            clave[i] = v[i];
	}
    
    public int getLiga() {

		return liga;
    }
    
	public void setLiga( int posicion ) {

		liga = posicion;
	}

    /*-----------------------------------------------------------------
    / longitud en bytes
    /-----------------------------------------------------------------*/
    
	public int length() {

		return clave.length + Integer.SIZE / 8;
	}
        
    /*-----------------------------------------------------------------
    / mŽtodos para escribir y leer una entrada en el ’ndice
    /-----------------------------------------------------------------*/
    
	public void read( RandomAccessFile raf ) throws IOException {
        
		raf.read( clave );
		liga = raf.readInt();
	}
    
	public void write( RandomAccessFile raf ) throws IOException {
        
		raf.write( clave );
		raf.writeInt( liga );
	}
}
